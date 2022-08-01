package harmonised.pmmo.setup.datagen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.util.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.data.LanguageProvider;

public class LangProvider extends LanguageProvider{
	private String locale;
	
	public enum Locale {
		EN_US("en_us");
		
		public String str;
		Locale(String locale) {str = locale;}
	}

	public LangProvider(DataGenerator gen, String locale) {
		super(gen, Reference.MOD_ID, locale);
		this.locale = locale;
	}

	static final List<Translation> impliedTranslations = new ArrayList<>();
	static {
		//=========PROVIDED SKILLS================
		addEN_US("pmmo.power", "Power");
		addEN_US("pmmo.mining", "Mining");
		addEN_US("pmmo.building", "Building");
		addEN_US("pmmo.excavation", "Excavation");
		addEN_US("pmmo.woodcutting", "Woodcutting");
		addEN_US("pmmo.farming", "Farming");
		addEN_US("pmmo.agility", "Agility");
		addEN_US("pmmo.endurance", "Endurance");
		addEN_US("pmmo.combat", "Combat");
		addEN_US("pmmo.archery", "Archery");
		addEN_US("pmmo.smithing", "Smithing");
		addEN_US("pmmo.flying", "Flying");
		addEN_US("pmmo.swimming", "Swimming");
		addEN_US("pmmo.fishing", "Fishing");
		addEN_US("pmmo.crafting", "Crafting");
		addEN_US("pmmo.magic", "Magic");
		addEN_US("pmmo.gunslinging", "Gunslinging");
		addEN_US("pmmo.slayer", "Slayer");
		addEN_US("pmmo.fletching", "Fletching");
		addEN_US("pmmo.taming", "Taming");
		addEN_US("pmmo.hunter", "Hunter");
		addEN_US("pmmo.engineering", "Engineering");
		addEN_US("pmmo.blood_magic", "Blood Magic");
		addEN_US("pmmo.astral_magic", "Astral Magic");
		addEN_US("pmmo.good_magic", "Good Magic");
		addEN_US("pmmo.evil_magic", "Evil Magic");
		addEN_US("pmmo.arcane_magic", "Arcane Magic");
		addEN_US("pmmo.elemental", "Elemental");
		addEN_US("pmmo.earth", "Earth");
		addEN_US("pmmo.water", "Water");
		addEN_US("pmmo.air", "Air");
		addEN_US("pmmo.fire", "Fire");
		addEN_US("pmmo.lightning", "Lightning");
		addEN_US("pmmo.void", "Void");
		addEN_US("pmmo.thaumatic", "Thaumatic");
		addEN_US("pmmo.summoning", "Summoning");
		addEN_US("pmmo.invention", "Invention");
		addEN_US("pmmo.runecrafting", "Runecrafting");
		addEN_US("pmmo.prayer", "Prayer");
		addEN_US("pmmo.cooking", "Cooking");
		addEN_US("pmmo.firemaking", "Firemaking");
		addEN_US("pmmo.afking", "Afking");
		addEN_US("pmmo.trading", "Trading");
		addEN_US("pmmo.sailing", "Sailing");
		addEN_US("pmmo.alchemy", "Alchemy");
		addEN_US("pmmo.construction", "Construction");
		addEN_US("pmmo.leatherworking", "Leatherworking");
		addEN_US("pmmo.exploration", "Exploration");
		addEN_US("pmmo.charisma","Charisma");
		//=========ENUM VALUES=====================
		addEN_US("pmmo.enum.ANVIL_REPAIR","Anvil Repair");
		addEN_US("pmmo.enum.BLOCK_BREAK","Break Block");
		addEN_US("pmmo.enum.BREAK_SPEED","Break Speed");
		addEN_US("pmmo.enum.BLOCK_PLACE","Place Block");
		addEN_US("pmmo.enum.BREATH_CHANGE","Breath Change");
		addEN_US("pmmo.enum.BREED","Breed");
		addEN_US("pmmo.enum.BREW","Brew");
		addEN_US("pmmo.enum.CRAFT","Craft");
		addEN_US("pmmo.enum.CONSUME","Eat/Drink");
		addEN_US("pmmo.enum.RECEIVE_DAMAGE","Receive Damage (Unspecified)");
		addEN_US("pmmo.enum.FROM_MOBS","Receive Mob Damage");
		addEN_US("pmmo.enum.FROM_PLAYERS","Receive Player Damage");
		addEN_US("pmmo.enum.FROM_ANIMALS","Receive Animal Damage");
		addEN_US("pmmo.enum.FROM_PROJECTILES","Receive Projectile Damage");
		addEN_US("pmmo.enum.FROM_MAGIC","Receive Magic Damage");
		addEN_US("pmmo.enum.FROM_ENVIRONMENT","Receive Environmental Damage");
		addEN_US("pmmo.enum.FROM_IMPACT","Receive Impact Damage");
		addEN_US("pmmo.enum.DEAL_MELEE_DAMAGE","Deal Melee Damage (Unspecified)");
		addEN_US("pmmo.enum.MELEE_TO_MOBS","Deal Melee Damage to Mobs");
		addEN_US("pmmo.enum.MELEE_TO_PLAYERS","Deal Melee Damage to Players");
		addEN_US("pmmo.enum.MELEE_TO_ANIMALS","Deal Melee Damage to Animals");
		addEN_US("pmmo.enum.DEAL_RANGED_DAMAGE","Deal Ranged Damage (Unspecified)");
		addEN_US("pmmo.enum.RANGED_TO_MOBS","Deal Ranged Damage to Mobs");
		addEN_US("pmmo.enum.RANGED_TO_PLAYERS","Deal Ranged Damage to Players");
		addEN_US("pmmo.enum.RANGED_TO_ANIMALS","Deal Ranged Damage to Animals");
		addEN_US("pmmo.enum.DEATH","Death");
		addEN_US("pmmo.enum.ENCHANT","Enchant");
		addEN_US("pmmo.enum.FISH","Fish");
		addEN_US("pmmo.enum.SMELT","Smelt/Cook");
		addEN_US("pmmo.enum.GROW","Grow");
		addEN_US("pmmo.enum.HEALTH_CHANGE","Health Change");
		addEN_US("pmmo.enum.JUMP","Jump");
		addEN_US("pmmo.enum.SPRINT_JUMP","Sprint Jump");
		addEN_US("pmmo.enum.CROUCH_JUMP","Crouch Jump");
		addEN_US("pmmo.enum.WORLD_CONNECT","World Connect");
		addEN_US("pmmo.enum.WORLD_DISCONNECT","World Disconnect");
		addEN_US("pmmo.enum.HIT_BLOCK","Hit Block");
		addEN_US("pmmo.enum.ACTIVATE_BLOCK","Activate Block");
		addEN_US("pmmo.enum.ACTIVATE_ITEM","Activate Item");
		addEN_US("pmmo.enum.ENTITY","Interact with Entity");
		addEN_US("pmmo.enum.RESPAWN","Respawn");
		addEN_US("pmmo.enum.RIDING","Riding");
		addEN_US("pmmo.enum.SHIELD_BLOCK","Block with Shield");
		addEN_US("pmmo.enum.SKILL_UP","Level Up");
		addEN_US("pmmo.enum.SLEEP","Sleep");
		addEN_US("pmmo.enum.SPRINTING","Sprinting");
		addEN_US("pmmo.enum.SUBMERGED","Submerged");
		addEN_US("pmmo.enum.SWIMMING","Swimming (above surface)");
		addEN_US("pmmo.enum.DIVING","Diving");
		addEN_US("pmmo.enum.SURFACING","Surfacing");
		addEN_US("pmmo.enum.SWIM_SPRINTING","Fast Swimming");
		addEN_US("pmmo.enum.TAMING","Taming");
		addEN_US("pmmo.enum.VEIN_MINE","Vein Mining");
		addEN_US("pmmo.enum.DISABLE_PERK","Disable Perk");
		
		addEN_US("pmmo.enum.WEAR","Wear Item");
		addEN_US("pmmo.enum.USE_ENCHANTMENT","Use Enchantment");
		addEN_US("pmmo.enum.TOOL","Use as Tool");
		addEN_US("pmmo.enum.WEAPON","Use as Weapon");
		addEN_US("pmmo.enum.USE","Activate Item Ability");
		addEN_US("pmmo.enum.PLACE","Place Block");
		addEN_US("pmmo.enum.BREAK","Break Block");
		addEN_US("pmmo.enum.BIOME","Enter Biome");
		addEN_US("pmmo.enum.KILL","Kill Entity");
		addEN_US("pmmo.enum.TRAVEL","Travel to");
		addEN_US("pmmo.enum.RIDE","Ride/Drive");
		addEN_US("pmmo.enum.TAME","Tame Animal");
		addEN_US("pmmo.enum.BREED","Breed Animal");
		addEN_US("pmmo.enum.INTERACT","Interact with Block");
		addEN_US("pmmo.enum.ENTITY_INTERACT","Interact with Entity");
		
		addEN_US("pmmo.enum.BIOME","Biome Modifier");
	    addEN_US("pmmo.enum.HELD","Held Modifier");
	    addEN_US("pmmo.enum.WORN","Worn Modifier");
	    addEN_US("pmmo.enum.DIMENSION","Dimension Modifier");
	}

	//=========KEY BINDINGS====================
	public static final Translation KEYBIND_CATEGORY = Translation.Builder.start("category.pmmo")
			.addLocale(Locale.EN_US, "Project MMO").build();
	public static final Translation KEYBIND_SHOWVEIN = Translation.Builder.start("key.pmmo.showVein")
			.addLocale(Locale.EN_US, "Toggle Vein Gauge").build();
	public static final Translation KEYBIND_ADDVEIN = Translation.Builder.start("key.pmmo.addVein")
			.addLocale(Locale.EN_US, "Increase Vein Size").build();
	public static final Translation KEYBIND_SUBVEIN = Translation.Builder.start("key.pmmo.subVein")
			.addLocale(Locale.EN_US, "Decrease Vein Size").build();
	public static final Translation KEYBIND_SHOWLIST = Translation.Builder.start("key.pmmo.showList")
			.addLocale(Locale.EN_US, "Toggle Skill List").build();
	public static final Translation KEYBIND_VEIN = Translation.Builder.start("key.pmmo.vein")
			.addLocale(Locale.EN_US, "Vein Mine Marker").build();
	public static final Translation KEYBIND_OPENMENU = Translation.Builder.start("key.pmmo.openMenu")
			.addLocale(Locale.EN_US, "Open Glossary").build();
			
	//=========GUI ELEMENTS====================
	public static final Translation CONFIG_TYPESELECT_ITEMS = Translation.Builder.start("pmmo.objectType.items")
			.addLocale(Locale.EN_US, "Items").build();
	public static final Translation CONFIG_TYPESELECT_BLOCKS = Translation.Builder.start("pmmo.objectType.blockss")
			.addLocale(Locale.EN_US, "Blocks").build();
	public static final Translation CONFIG_TYPESELECT_ENTITIES = Translation.Builder.start("pmmo.objectType.entities")
			.addLocale(Locale.EN_US, "Entities").build();
	public static final Translation CONFIG_TYPESELECT_DIMENSIONS = Translation.Builder.start("pmmo.objectType.dimensions")
			.addLocale(Locale.EN_US, "Dimensions").build();
	public static final Translation CONFIG_TYPESELECT_BIOMES = Translation.Builder.start("pmmo.objectType.biomes")
			.addLocale(Locale.EN_US, "Biomes").build();
	public static final Translation CONFIG_TYPESELECT_PLAYERS = Translation.Builder.start("pmmo.objectType.players")
			.addLocale(Locale.EN_US, "Players").build();
	public static final Translation CONFIG_TYPESELECT_ENCHANTMENTS = Translation.Builder.start("pmmo.objectType.enchantments")
			.addLocale(Locale.EN_US, "Enchantments").build();
	public static final Translation CONFIG_TYPESELECT_DEFAULT = Translation.Builder.start("pmmo.objectType.default")
			.addLocale(Locale.EN_US, "-- Select Object Type --").build();
	public static final Translation CONFIG_OBJECTSELECT_DEFAULT = Translation.Builder.start("pmmo.objectSelect.default")
			.addLocale(Locale.EN_US, "-- Select Object --").build();
	public static final Translation CONFIG_CONFIGURE = Translation.Builder.start("pmmo.gui.configure")
			.addLocale(Locale.EN_US, "Configure").build();
	public static final Translation CONFIG_PROMPT_TYPE = Translation.Builder.start("pmmo.gui.select_type")
			.addLocale(Locale.EN_US, "Select Type").build();
	public static final Translation CONFIG_FILTER_OBJECTS = Translation.Builder.start("pmmo.gui.filter_object")
			.addLocale(Locale.EN_US, "Filter Objects").build();
	public static final Translation CONFIG_PROMPT_OBJECT = Translation.Builder.start("pmmo.gui.select_object")
			.addLocale(Locale.EN_US, "Select Object").build();
	
	//=========LOGIN HANDLER===================
	public static final Translation WELCOME_TEXT = Translation.Builder.start("pmmo.welcomeText")
			.addLocale(Locale.EN_US, "Welcome! Project MMO is more fun with datapacks. download one %s").build();
	public static final Translation CLICK_ME = Translation.Builder.start("pmmo.clickMe")
			.addLocale(Locale.EN_US, "HERE").build();
	
	//=========KEY PRESS HANLDER===============
	public static final Translation VEIN_BLACKLIST = Translation.Builder.start("pmmo.veinBlacklist")
			.addLocale(Locale.EN_US, "Blacklisted blocks from Veining").build();
	
	//=========TOOLTIP HEADERS=================
	public static final Translation REQ_WEAR = Translation.Builder.start("pmmo.toWear")
			.addLocale(Locale.EN_US, "To Wear").build();
	public static final Translation REQ_TOOL = Translation.Builder.start("pmmo.tool")
			.addLocale(Locale.EN_US, "Tool").build();
	public static final Translation REQ_WEAPON = Translation.Builder.start("pmmo.weapon")
			.addLocale(Locale.EN_US, "Weapon").build();
	public static final Translation REQ_USE = Translation.Builder.start("pmmo.use")
			.addLocale(Locale.EN_US, "Use").build();
	public static final Translation REQ_PLACE = Translation.Builder.start("pmmo.place")
			.addLocale(Locale.EN_US, "To Place").build();
	public static final Translation REQ_BREAK = Translation.Builder.start("pmmo.break")
			.addLocale(Locale.EN_US, "To Break").build();
	public static final Translation XP_VALUE_BREAK = Translation.Builder.start("pmmo.xpValueBreak")
			.addLocale(Locale.EN_US, "Break Xp Value").build();
	public static final Translation XP_VALUE_CRAFT = Translation.Builder.start("pmmo.xpValueCraft")
			.addLocale(Locale.EN_US, "Craft Xp Value").build();
	public static final Translation XP_VALUE_SMELT = Translation.Builder.start("pmmo.xpValueSmelt")
			.addLocale(Locale.EN_US, "Smelt Xp Value").build();
	public static final Translation XP_VALUE_BREW = Translation.Builder.start("pmmo.xpValueBrew")
			.addLocale(Locale.EN_US, "Brew Xp Value").build();
	public static final Translation XP_VALUE_GROW = Translation.Builder.start("pmmo.xpValueGrow")
			.addLocale(Locale.EN_US, "Grow Xp Value").build();
	public static final Translation XP_VALUE_PLACE = Translation.Builder.start("pmmo.xpValuePlace")
			.addLocale(Locale.EN_US, "Place Xp Value").build();
	public static final Translation BOOST_HELD = Translation.Builder.start("pmmo.itemXpBoostHeld")
			.addLocale(Locale.EN_US, "Xp Boost In Hand").build();
	public static final Translation BOOST_WORN = Translation.Builder.start("pmmo.itemXpBoostWorn")
			.addLocale(Locale.EN_US, "Xp Boost Worn").build();
	public static final Translation VEIN_TOOLTIP = Translation.Builder.start("pmmo.veintooltip")
			.addLocale(Locale.EN_US, "Vein Mining").build();
	public static final Translation VEIN_DATA = Translation.Builder.start("pmmo.veindata")
			.addLocale(Locale.EN_US, "Charge Cap %1$s, recharges %3$s/s").build();
	public static final Translation VEIN_BREAK = Translation.Builder.start("pmmo.veinbreak")
			.addLocale(Locale.EN_US, "Cost to break as block: %s").build();
	
	//=========STAT SCROLL WIDGET=================
	
	//=========CODEC SPECS=====================
	public static final Translation CODECSPEC_ISTAGFOR = Translation.Builder.start("pmmo.codecspec.istagfor")
			.addLocale(Locale.EN_US, "Replicates configuration settings to all listed items").build();
	public static final Translation CODECSPEC_LONGMAP = Translation.Builder.start("pmmo.codecspec.longmap")
			.addLocale(Locale.EN_US, "Contains a map of skillnames and experience values").build();
	public static final Translation CODECSPEC_INTMAP = Translation.Builder.start("pmmo.codecspec.intmap")
			.addLocale(Locale.EN_US, "Contains a map of skillnames and level values").build();
	public static final Translation CODECSPEC_DBLMAP = Translation.Builder.start("pmmo.codecspec.dblmap")
			.addLocale(Locale.EN_US, "Contains a map of skillnames and modifier values").build();
	public static final Translation CODECSPEC_XP_VALUES = Translation.Builder.start("pmmo.codecspec.xp_values")
			.addLocale(Locale.EN_US, "Contains a map of XP award events and the xp values awarded").build();
	
	@Override
	protected void addTranslations() {
		for (Field entry : this.getClass().getDeclaredFields()) {
			if (entry.getType() == Translation.class) {
				try {
					addTranslation((Translation)entry.get(LangProvider.class));
				} catch (IllegalAccessException e) {e.printStackTrace();}
			}
		}
		for (Translation implied : impliedTranslations) {
			add(implied);
		}
	}
	
	private void addTranslation(Translation translation) {
		add(translation.key(), translation.localeMap().getOrDefault(locale, ""));
	}
	
	private static void addEN_US(String key, String translation) {
		impliedTranslations.add(Translation.Builder.start(key).addLocale(Locale.EN_US, translation).build());
	}
	
	public static record Translation(String key, Map<String, String> localeMap) {
		public static final Translation EMPTY = new Translation("", new HashMap<>());
		public MutableComponent asComponent() {
			return Component.translatable(key());
		}
		public MutableComponent asComponent(Object...obj) {
			return Component.translatable(key(), obj);
		}
		public static class Builder {
			String key;
			Map<String, String> localeMap;
			private Builder(String key) {this.key = key; localeMap = new HashMap<>();}
			
			public static Builder start(String key) {
				return new Builder(key);
			}
			public Builder addLocale(Locale locale, String translation) {
				this.localeMap.put(locale.str, translation);
				return this;
			}
			public Translation build() {
				return new Translation(key, localeMap);
			}
		}
	}
}
