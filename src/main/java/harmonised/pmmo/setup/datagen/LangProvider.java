package harmonised.pmmo.setup.datagen;

import java.lang.reflect.Field;
import java.util.HashMap;
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
	//=========ENUM VALUES=====================
	public static final Translation ENUM_ANVIL_REPAIR = Translation.Builder.start("pmmo.enum."+EventType.ANVIL_REPAIR.name())
			.addLocale(Locale.EN_US, "Anvil Repair").build();
	public static final Translation ENUM_BLOCK_BREAK = Translation.Builder.start("pmmo.enum."+EventType.BLOCK_BREAK.name())
			.addLocale(Locale.EN_US, "Break Block").build();
	/*
	"pmmo.enum.BREAK_SPEED":"Break Speed",
	"pmmo.enum.BLOCK_PLACE":"Place Block",
	"pmmo.enum.BREATH_CHANGE":"Breath Change",
	"pmmo.enum.BREED":"Breed",
	"pmmo.enum.BREW":"Brew",
	"pmmo.enum.CRAFT":"Craft",
	"pmmo.enum.CONSUME":"Eat/Drink",
	"pmmo.enum.RECEIVE_DAMAGE":"Receive Damage (Unspecified)",
	"pmmo.enum.FROM_MOBS":"Receive Mob Damage",
	"pmmo.enum.FROM_PLAYERS":"Receive Player Damage",
	"pmmo.enum.FROM_ANIMALS":"Receive Animal Damage",
	"pmmo.enum.FROM_PROJECTILES":"Receive Projectile Damage",
	"pmmo.enum.FROM_MAGIC":"Receive Magic Damage",
	"pmmo.enum.FROM_ENVIRONMENT":"Receive Environmental Damage",
	"pmmo.enum.FROM_IMPACT":"Receive Impact Damage",
	"pmmo.enum.DEAL_MELEE_DAMAGE":"Deal Melee Damage (Unspecified)",
	"pmmo.enum.MELEE_TO_MOBS":"Deal Melee Damage to Mobs",
	"pmmo.enum.MELEE_TO_PLAYERS":"Deal Melee Damage to Players",
	"pmmo.enum.MELEE_TO_ANIMALS":"Deal Melee Damage to Animals",
	"pmmo.enum.DEAL_RANGED_DAMAGE":"Deal Ranged Damage (Unspecified)",
	"pmmo.enum.RANGED_TO_MOBS":"Deal Ranged Damage to Mobs",
	"pmmo.enum.RANGED_TO_PLAYERS":"Deal Ranged Damage to Players",
	"pmmo.enum.RANGED_TO_ANIMALS":"Deal Ranged Damage to Animals",
	"pmmo.enum.DEATH":"Death",
	"pmmo.enum.ENCHANT":"Enchant",
	"pmmo.enum.FISH":"Fish",
	"pmmo.enum.SMELT":"Smelt/Cook",
	"pmmo.enum.GROW":"Grow",
	"pmmo.enum.HEALTH_CHANGE":"Health Change",
	"pmmo.enum.JUMP":"Jump",
	"pmmo.enum.SPRINT_JUMP":"Sprint Jump",
	"pmmo.enum.CROUCH_JUMP":"Crouch Jump",
	"pmmo.enum.WORLD_CONNECT":"World Connect",
	"pmmo.enum.WORLD_DISCONNECT":"World Disconnect",
	"pmmo.enum.HIT_BLOCK":"Hit Block",
	"pmmo.enum.ACTIVATE_BLOCK":"Activate Block",
	"pmmo.enum.ACTIVATE_ITEM":"Activate Item",
	"pmmo.enum.ENTITY":"Interact with Entity",
	"pmmo.enum.RESPAWN":"Respawn",
	"pmmo.enum.RIDING":"Riding",
	"pmmo.enum.SHIELD_BLOCK":"Block with Shield",
	"pmmo.enum.SKILL_UP":"Level Up",
	"pmmo.enum.SLEEP":"Sleep",
	"pmmo.enum.SPRINTING":"Sprinting",
	"pmmo.enum.SUBMERGED":"Submerged",
	"pmmo.enum.SWIMMING":"Swimming (above surface)",
	"pmmo.enum.DIVING":"Diving",
	"pmmo.enum.SURFACING":"Surfacing",
	"pmmo.enum.SWIM_SPRINTING":"Fast Swimming",
	"pmmo.enum.TAMING":"Taming",
	"pmmo.enum.VEIN_MINE":"Vein Mining",
	"pmmo.enum.DISABLE_PERK":"Disable Perk",
	 */
	
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
	}
	
	private void addTranslation(Translation translation) {
		add(translation.key(), translation.localeMap().getOrDefault(locale, ""));
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
