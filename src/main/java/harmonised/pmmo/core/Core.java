package harmonised.pmmo.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedListMultimap;

import harmonised.pmmo.api.APIUtils;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.client.utils.DataMirror;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.DataConfig;
import harmonised.pmmo.config.readers.ModifierDataType;
import harmonised.pmmo.core.nbt.LogicEntry;
import harmonised.pmmo.features.anticheese.CheeseTracker;
import harmonised.pmmo.features.autovalues.AutoValueConfig;
import harmonised.pmmo.features.autovalues.AutoValues;
import harmonised.pmmo.features.salvaging.SalvageLogic;
import harmonised.pmmo.network.Networking;
import harmonised.pmmo.network.clientpackets.CP_ClearData;
import harmonised.pmmo.registry.EventTriggerRegistry;
import harmonised.pmmo.registry.PerkRegistry;
import harmonised.pmmo.registry.PredicateRegistry;
import harmonised.pmmo.registry.TooltipRegistry;
import harmonised.pmmo.storage.PmmoSavedData;
import harmonised.pmmo.util.Functions;
import harmonised.pmmo.util.MsLoggy;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;

public class Core {
	private static final Map<LogicalSide, Function<LogicalSide, Core>> INSTANCES = Map.of(LogicalSide.CLIENT, Functions.memoize(Core::new), LogicalSide.SERVER, Functions.memoize(Core::new));
	private final XpUtils xp;
	private final SkillGates gates;
	private final DataConfig config;
	private final PredicateRegistry predicates;
	private final EventTriggerRegistry eventReg;
	private final TooltipRegistry tooltips;
	private final PerkRegistry perks;
	private final SalvageLogic salvageLogic;
	private final NBTUtils nbt;
	private final IDataStorage data;
	private final LogicalSide side;
	  
	private Core(LogicalSide side) {
		this.xp = new XpUtils();
	    this.gates = new SkillGates();
	    this.config = new DataConfig();
	    this.predicates = new PredicateRegistry();
	    this.eventReg = new EventTriggerRegistry();
	    this.tooltips = new TooltipRegistry();
	    this.perks = new PerkRegistry();
	    this.salvageLogic = new SalvageLogic();
	    this.nbt = new NBTUtils();
	    data = side.equals(LogicalSide.SERVER) ? new PmmoSavedData() : new DataMirror();
	    this.side = side;
	}
	  
	public static Core get(final LogicalSide side) {
	    return INSTANCES.get(side).apply(side);
	}
	public static Core get(final Level level) {
	    return get(level.isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER);
	}
	
	public void resetDataForReload() {
		xp.reset();
		gates.reset();
		config.reset();
		salvageLogic.reset();
		nbt.reset();
		if (side.equals(LogicalSide.SERVER)) {
			PmmoSavedData dataBackend = (PmmoSavedData) data;
			if (dataBackend.getServer() == null) return;
			for (ServerPlayer player : dataBackend.getServer().getPlayerList().getPlayers()) {
				Networking.sendToClient(new CP_ClearData(), player);
			}
		}
	}
	  
	public XpUtils getXpUtils() {return xp;}
	public SkillGates getSkillGates() {return gates;}
	public DataConfig getDataConfig() {return config;}
	public PredicateRegistry getPredicateRegistry() {return predicates;}
	public EventTriggerRegistry getEventTriggerRegistry() {return eventReg;}
	public TooltipRegistry getTooltipRegistry() {return tooltips;}
	public PerkRegistry getPerkRegistry() {return perks;}
	public SalvageLogic getSalvageLogic() {return salvageLogic;}
	public NBTUtils getNBTUtils() {return nbt;}
	public IDataStorage getData() {return data.get();}
	public IDataStorage getData(MinecraftServer server) {return data.get(server);}
	public LogicalSide getSide() {return side;}
	  
  	public boolean doesPlayerMeetReq(ReqType reqType, ResourceLocation objectID, UUID playerID) {
		Map<String, Integer> requirements = gates.getObjectSkillMap(reqType, objectID);
		return doesPlayerMeetReq(playerID, requirements);	
	}	
	public boolean doesPlayerMeetReq(UUID playerID, Map<String, Integer> requirements) {
		boolean meetsReq = true;
		for (Map.Entry<String, Integer> req : requirements.entrySet()) {
			int skillLevel = getData().getLevelFromXP(getData().getXpRaw(playerID, req.getKey()));
			if (req.getValue() > skillLevel)
				return false;
		}
		return meetsReq;
	}	
	public boolean doesPlayerMeetEnchantmentReq(ItemStack stack, UUID playerID) {
		ListTag enchantments = stack.getEnchantmentTags();
		for (int i = 0; i < enchantments.size(); i++) {
			CompoundTag enchantment = enchantments.getCompound(i);
			ResourceLocation enchantID = new ResourceLocation(enchantment.getString("id"));
			int enchantLvl = enchantment.getInt("lvl");
			if (!doesPlayerMeetReq(playerID, gates.getEnchantmentReqs(enchantID, enchantLvl)))
				return false;
		}	
		return true;
	}
	  
	public boolean isActionPermitted(ReqType type, ItemStack stack, Player player) {
		  if (!Config.reqEnabled(type).get()) return true;
		  ResourceLocation itemID = stack.getItem().getRegistryName();
		  	if (Config.reqEnabled(ReqType.USE_ENCHANTMENT).get())
		  		if (!doesPlayerMeetEnchantmentReq(stack, player.getUUID()))
		  			return false;
		  		if (predicates.predicateExists(itemID, type)) 
				return predicates.checkPredicateReq(player, stack, type);
			else if (gates.doesObjectReqExist(type, itemID))
				return doesPlayerMeetReq(type, itemID, player.getUUID());
			else if (AutoValueConfig.ENABLE_AUTO_VALUES.get()) {
				Map<String, Integer> requirements = AutoValues.getRequirements(type, itemID, ObjectType.ITEM);
				return doesPlayerMeetReq(player.getUUID(), requirements);
			}
		  return true;
	  }
	public boolean isBlockActionPermitted(ReqType type, BlockPos pos, Player player) {
		  if (!Config.reqEnabled(type).get()) return true;
		  BlockEntity tile = player.getLevel().getBlockEntity(pos);
		  ResourceLocation res = player.getLevel().getBlockState(pos).getBlock().getRegistryName();
		  return tile == null ?
				  isActionPermitted_BypassPredicates(type, res, player, ObjectType.BLOCK) :
				  isActionPermitted(type, tile, player);
	  }
	private boolean isActionPermitted_BypassPredicates(ReqType type, ResourceLocation res, Player player, ObjectType oType) {
		  if (gates.doesObjectReqExist(type, res))
				return doesPlayerMeetReq(type, res, player.getUUID());
		  else if (AutoValueConfig.ENABLE_AUTO_VALUES.get()) {
			  Map<String, Integer> requirements = AutoValues.getRequirements(type, res, oType);
			  return doesPlayerMeetReq(player.getUUID(), requirements);
		  }
		  return true;
	  }
	private boolean isActionPermitted(ReqType type, BlockEntity tile, Player player) {
		  Preconditions.checkNotNull(tile);
		  ResourceLocation blockID = tile.getBlockState().getBlock().getRegistryName();
			if (predicates.predicateExists(blockID, type)) {
				return predicates.checkPredicateReq(player, tile, type);
			}
			else if (gates.doesObjectReqExist(type, blockID))
				return doesPlayerMeetReq(type, blockID, player.getUUID());
			else if (AutoValueConfig.ENABLE_AUTO_VALUES.get()) {
				Map<String, Integer> requirements = AutoValues.getRequirements(type, blockID, ObjectType.BLOCK);
				return doesPlayerMeetReq(player.getUUID(), requirements);
			}
		  return true;
	  }
	private ResourceLocation playerID = new ResourceLocation("player");
	public boolean isActionPermitted(ReqType type, Entity entity, Player player) {
		  if (!Config.reqEnabled(type).get()) return true;
		  ResourceLocation entityID = entity.getType().equals(EntityType.PLAYER) ? playerID : new ResourceLocation(entity.getEncodeId());
			if (predicates.predicateExists(entityID, type)) 
				return predicates.checkPredicateReq(player, entity, type);
			else if (gates.doesObjectReqExist(type, entityID))
				return doesPlayerMeetReq(type, entityID, player.getUUID());
			else if (AutoValueConfig.ENABLE_AUTO_VALUES.get()) {
				Map<String, Integer> requirements = AutoValues.getRequirements(type, entityID, ObjectType.ENTITY);
				return doesPlayerMeetReq(player.getUUID(), requirements);
			}
		  return true;
	  }
	 
	public Map<String, Long> getExperienceAwards(EventType type, ItemStack stack, Player player, CompoundTag dataIn) {
		  Map<String, Long> xpGains = dataIn.contains(APIUtils.SERIALIZED_AWARD_MAP) 
					? xp.deserializeAwardMap(dataIn.getList(APIUtils.SERIALIZED_AWARD_MAP, Tag.TAG_COMPOUND))
					: new HashMap<>();
		  boolean tooltipsUsed = false;
		  ResourceLocation itemID = stack.getItem().getRegistryName();
		  if (tooltipsUsed = tooltips.xpGainTooltipExists(itemID, type))
			  xpGains = xp.mergeXpMapsWithSummateCondition(xpGains, tooltips.getItemXpGainTooltipData(itemID, type, stack));
		  return getCommonXpAwardData(xpGains, type, itemID, player, ObjectType.ITEM, tooltipsUsed);
	  }
	public Map<String, Long> getBlockExperienceAwards(EventType type, BlockPos pos, Level level, Player player, CompoundTag dataIn) {
		  Map<String, Long> xpGains = dataIn.contains(APIUtils.SERIALIZED_AWARD_MAP) 
					? xp.deserializeAwardMap(dataIn.getList(APIUtils.SERIALIZED_AWARD_MAP, Tag.TAG_COMPOUND))
					: new HashMap<>();
		  BlockEntity tile = level.getBlockEntity(pos);
		  ResourceLocation res = level.getBlockState(pos).getBlock().getRegistryName();
		  return tile == null ?
				  xp.mergeXpMapsWithSummateCondition(xpGains, getCommonXpAwardData(xpGains, type, res, player, ObjectType.BLOCK, false)) :
				  xp.mergeXpMapsWithSummateCondition(xpGains, getExperienceAwards(xpGains, type, tile, player));
	  }
	private Map<String, Long> getExperienceAwards(Map<String, Long> xpGains, EventType type, BlockEntity tile, Player player) {		  
			ResourceLocation blockID = tile.getBlockState().getBlock().getRegistryName();
			boolean tooltipsUsed = false;
			if (tooltipsUsed = tooltips.xpGainTooltipExists(blockID, type)) 
				xpGains = xp.mergeXpMapsWithSummateCondition(xpGains, tooltips.getBlockXpGainTooltipData(blockID, type, tile));
		  return getCommonXpAwardData(xpGains, type, blockID, player, ObjectType.BLOCK, tooltipsUsed);
	  }	  
	public Map<String, Long> getExperienceAwards(EventType type, Entity entity, Player player, CompoundTag dataIn) {
		  Map<String, Long> xpGains = dataIn.contains(APIUtils.SERIALIZED_AWARD_MAP) 
					? xp.deserializeAwardMap(dataIn.getList(APIUtils.SERIALIZED_AWARD_MAP, Tag.TAG_COMPOUND))
					: new HashMap<>();
		  boolean tooltipsUsed = false;
		  ResourceLocation entityID = entity.getType().equals(EntityType.PLAYER) ? playerID : new ResourceLocation(entity.getEncodeId());
		  if (tooltipsUsed = tooltips.xpGainTooltipExists(entityID, type))
			  xpGains = xp.mergeXpMapsWithSummateCondition(xpGains, tooltips.getEntityXpGainTooltipData(entityID, type, entity));
		  return getCommonXpAwardData(xpGains, type, entityID, player, ObjectType.ENTITY, tooltipsUsed);
	  }
	private Map<String, Long> getCommonXpAwardData(Map<String, Long> inMap, EventType type, ResourceLocation objectID, Player player, ObjectType oType, boolean predicateUsed) {
		if (!predicateUsed) {
			if (xp.hasXpGainObjectEntry(type, objectID)) 
				inMap = xp.mergeXpMapsWithSummateCondition(inMap, xp.getObjectExperienceMap(type, objectID));
			else if (AutoValueConfig.ENABLE_AUTO_VALUES.get()) 
				inMap = xp.mergeXpMapsWithSummateCondition(inMap, AutoValues.getExperienceAward(type, objectID, oType));
		}
		MsLoggy.info("XpGains: "+MsLoggy.mapToString(inMap));
		if (player != null)
			inMap = xp.applyXpModifiers(player, inMap);
		MsLoggy.info("XpGains (afterMod): "+MsLoggy.mapToString(inMap));
		inMap = CheeseTracker.applyAntiCheese(inMap);
		MsLoggy.info("XpGains (afterCheese): "+MsLoggy.mapToString(inMap));			
		return inMap;
	}

	public Map<String, Double> getConsolidatedModifierMap(Player player) {
			Map<String, Double> mapOut = new HashMap<>();
			for (ModifierDataType type : ModifierDataType.values()) {
				Map<String, Double> modifiers = new HashMap<>();
				switch (type) {
				case BIOME: {
					ResourceLocation biomeID = player.level.getBiome(player.blockPosition()).value().getRegistryName();
					modifiers = xp.getObjectModifierMap(type, biomeID);
					for (Map.Entry<String, Double> modMap : modifiers.entrySet()) {
						mapOut.merge(modMap.getKey(), modMap.getValue(), (n, o) -> {return n * o;});
					}
					break;
				}
				case HELD: {
					ItemStack offhandStack = player.getOffhandItem();
					ItemStack mainhandStack = player.getMainHandItem();
					ResourceLocation offhandID = offhandStack.getItem().getRegistryName();
					modifiers = tooltips.bonusTooltipExists(offhandID, type) ?
							tooltips.getBonusTooltipData(offhandID, type, offhandStack) :
							xp.getObjectModifierMap(type, offhandID);
					for (Map.Entry<String, Double> modMap : modifiers.entrySet()) {
						mapOut.merge(modMap.getKey(), modMap.getValue(), (n, o) -> {return n * o;});
					}				
					ResourceLocation mainhandID = mainhandStack.getItem().getRegistryName();				
					modifiers = tooltips.bonusTooltipExists(mainhandID, type) ?
							tooltips.getBonusTooltipData(mainhandID, null, mainhandStack) :
							xp.getObjectModifierMap(type, mainhandID);
					for (Map.Entry<String, Double> modMap : modifiers.entrySet()) {
						mapOut.merge(modMap.getKey(), modMap.getValue(), (n, o) -> {return n * o;});
					}				
					break;
				}
				case WORN: {
					player.getArmorSlots().forEach((stack) -> {
						ResourceLocation itemID = stack.getItem().getRegistryName();
						Map<String, Double> modifers = tooltips.bonusTooltipExists(itemID, type) ?
								tooltips.getBonusTooltipData(itemID, type, stack):
								xp.getObjectModifierMap(type, itemID);
						for (Map.Entry<String, Double> modMap : modifers.entrySet()) {
							mapOut.merge(modMap.getKey(), modMap.getValue(), (n, o) -> {return n * o;});
						}
					});
					break;
				}
				case DIMENSION: {
					ResourceLocation dimensionID = player.level.dimension().getRegistryName();
					modifiers = xp.getObjectModifierMap(type, dimensionID);
					for (Map.Entry<String, Double> modMap : modifiers.entrySet()) {
						mapOut.merge(modMap.getKey(), modMap.getValue(), (n, o) -> {return n * o;});
					}
					break;
				}
				default: {}
				}
				
			}
			return mapOut;
		}
	  
	public void awardXP(List<ServerPlayer> players, Map<String, Long> xpValues) {
			for (int i = 0; i < players.size(); i++) {
				for (Map.Entry<String, Long> award : xpValues.entrySet()) {
					if (getData().setXpDiff(players.get(i).getUUID(), award.getKey(), award.getValue())) {
						xp.sendXpAwardNotifications(players.get(i), award.getKey(), award.getValue());
					}
				}
			}
	  }
	  
	/** This method registers applies PMMO's NBT logic to the values that are 
	   *  configured.  This should be fired after data is loaded or else it will
	   *  register nothing.
	   */
	public void registerNBT() {			
		  //TODO maybe change the loops to use the enum applicablity arrays
			//==============REGISTER REQUIREMENT LOGIC=============================== 
			for (Map.Entry<ReqType, LinkedListMultimap<ResourceLocation, LogicEntry>> entry : nbt.itemReqLogic().entrySet()) {
				//bypass this req for items since it is not applicable
				if (entry.getKey().equals(ReqType.BREAK)) continue;
				//register remaining items and cases
				entry.getValue().forEach((rl, logic) -> {
					BiPredicate<Player, ItemStack> pred = (player, stack) -> doesPlayerMeetReq(player.getUUID(), nbt.getReqMap(entry.getKey(), stack));
					predicates.registerPredicate(rl, entry.getKey(), pred);
					Function<ItemStack, Map<String, Integer>> func = (stack) -> nbt.getReqMap(entry.getKey(), stack);
					tooltips.registerItemRequirementTooltipData(rl, entry.getKey(), func);
				});			
			}
			nbt.blockReqLogic().getOrDefault(ReqType.BREAK, LinkedListMultimap.create()).forEach((rl, logic) -> {
				BiPredicate<Player, BlockEntity> pred = (player, tile) -> doesPlayerMeetReq(player.getUUID(), nbt.getReqMap(ReqType.BREAK, tile));
				predicates.registerBreakPredicate(rl, ReqType.BREAK, pred);
				Function<BlockEntity, Map<String, Integer>> func = (tile) -> nbt.getReqMap(ReqType.BREAK, tile);
				tooltips.registerBlockRequirementTooltipData(rl, ReqType.BREAK, func);
			});
			for (Map.Entry<ReqType, LinkedListMultimap<ResourceLocation, LogicEntry>> entry : nbt.entityReqLogic().entrySet()) {
				//bypass this req for items since it is not applicable
				if (entry.getKey().equals(ReqType.BREAK)) continue;
				//register remaining items and cases
				entry.getValue().forEach((rl, logic) -> {
					BiPredicate<Player, Entity> pred = (player, entity) -> doesPlayerMeetReq(player.getUUID(), nbt.getReqMap(entry.getKey(), entity));
					predicates.registerEntityPredicate(rl, entry.getKey(), pred);
					Function<Entity, Map<String, Integer>> func = (entity) -> nbt.getReqMap(entry.getKey(), entity);
					tooltips.registerEntityRequirementTooltipData(rl, entry.getKey(), func);
				});
			}
			
			//==============REGISTER XP GAIN LOGIC=====================================
			for (Map.Entry<EventType, LinkedListMultimap<ResourceLocation, LogicEntry>> entry : nbt.itemXpGainLogic().entrySet()) {
				//bypass this req for items since it is not applicable
				if (entry.getKey().equals(EventType.BLOCK_BREAK)) continue;
				//register remaining items and cases
				entry.getValue().forEach((rl, logic) -> {
					Function<ItemStack, Map<String, Long>> func = (stack) -> nbt.getXpMap(entry.getKey(), stack);
					tooltips.registerItemXpGainTooltipData(rl, entry.getKey(), func);
				});
			}
			nbt.blockXpGainLogic().getOrDefault(ReqType.BREAK, LinkedListMultimap.create()).forEach((rl, logic) -> {
				Function<BlockEntity, Map<String, Long>> func = (tile) -> nbt.getXpMap(EventType.BLOCK_BREAK, tile);
				tooltips.registerBlockXpGainTooltipData(rl, EventType.BLOCK_BREAK, func);
			});
			for (Map.Entry<EventType, LinkedListMultimap<ResourceLocation, LogicEntry>> entry : nbt.entityXpGainLogic().entrySet()) {
				//bypass this req for items since it is not applicable
				if (entry.getKey().equals(EventType.BLOCK_BREAK)) continue;
				//register remaining items and cases
				entry.getValue().forEach((rl, logic) -> {
					Function<Entity, Map<String, Long>> func = (entity) -> nbt.getXpMap(entry.getKey(), entity);
					tooltips.registerEntityXpGainTooltipData(rl, entry.getKey(), func);
				});
			}
			
			//==============REGISTER BONUSES LOGIC=====================================
			MsLoggy.debug("Bonus Logic entrySet size: "+nbt.bonusLogic().size());
			for (Map.Entry<ModifierDataType, LinkedListMultimap<ResourceLocation, LogicEntry>> entry : nbt.bonusLogic().entrySet()) {
				MsLoggy.debug("Bonus Logic Element Size: "+entry.getKey().name()+" "+entry.getValue().size());
				entry.getValue().forEach((rl, logic) -> {
					MsLoggy.debug("Bonus Logic Detail: "+rl.toString()+" "+logic.toString());
					Function<ItemStack, Map<String, Double>> func = (stack) -> nbt.getBonusMap(entry.getKey(), stack);
					tooltips.registerItemBonusTooltipData(rl, entry.getKey(), func);
				});
			}
		}
}
