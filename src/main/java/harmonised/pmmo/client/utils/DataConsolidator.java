package harmonised.pmmo.client.utils;

import java.util.List;
import java.util.Map;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.client.gui.configure.ConfigObjectSelectScreen;
import harmonised.pmmo.config.codecs.CodecTypes.SalvageData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.veinmining.VeinMiningLogic;
import harmonised.pmmo.util.TagUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class DataConsolidator {
	public static enum ObjectType {ITEM, BLOCK, ENTITY, DIMENSION, BIOME, PLAYER, ENCHANTMENT;
		public static ObjectType fromConfigSelectType(ConfigObjectSelectScreen.TYPES inType) {
			switch (inType) {
			case ITEMS: return ITEM;
			case BLOCKS: return BLOCK;
			case ENTITIES: return ENTITY;
			case DIMENSIONS: return DIMENSION;
			case BIOMES: return BIOME;
			case PLAYERS: return PLAYER;
			case ENCHANTMENTS: default: {return ENCHANTMENT;}}
		}
	}
	
	public static CompoundTag getExistingData(Core core, ResourceLocation objectID, ObjectType type) {
		CompoundTag nbt = new CompoundTag();
		switch (type) {
		case ITEM: {
			CompoundTag internal = new CompoundTag();
			for (EventType event : EventType.ITEM_APPLICABLE_EVENTS) {
				internal.put(event.name().toUpperCase(), TagUtils.longMapToCompound(core.getXpUtils().getObjectExperienceMap(event, objectID)));
			}
			nbt.put("xp_values", internal);
			internal = new CompoundTag();
			for (ReqType req : ReqType.ITEM_APPLICABLE_EVENTS) {
				internal.put(req.name().toUpperCase(), TagUtils.intMapToCompound(core.getSkillGates().getObjectSkillMap(req, objectID)));
			}
			nbt.put("requirements", internal);
			internal = new CompoundTag();
			for (ModifierDataType mod : List.of(ModifierDataType.WORN, ModifierDataType.HELD)) {
				internal.put(mod.name().toUpperCase(), TagUtils.dblMapToCompound(core.getXpUtils().getObjectModifierMap(mod, objectID)));
			}
			nbt.put("bonuses", internal);
			nbt.put("negative_effect", TagUtils.effectMapToCompound(core.getDataConfig().getItemEffects(objectID)));
			internal = new CompoundTag();
			for (Map.Entry<ResourceLocation, SalvageData> entry : core.getSalvageLogic().getSalvageData(objectID).entrySet()) {
				internal.put(entry.getKey().toString(), entry.getValue().asTag());
			}
			nbt.put("salvage", internal);
			nbt.put(VeinMiningLogic.VEIN_DATA, core.getVeinData().getData(objectID).asTag());
			return nbt;
		}
		//TODO case BLOCK:{}
		default: return nbt;}
	}
}
