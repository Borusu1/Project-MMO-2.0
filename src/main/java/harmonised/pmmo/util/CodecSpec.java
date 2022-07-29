package harmonised.pmmo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.config.codecs.CodecTypes.SalvageData;
import net.minecraft.resources.ResourceLocation;

public interface CodecSpec {
	public String getField();
	public List<? extends CodecSpec> getMembers();
	
	public static record GroupSpec(String fieldName, List<CodecSpec> members) implements CodecSpec {
		@Override
		public String getField() {return fieldName();}
		@Override
		public List<? extends CodecSpec> getMembers() {return members();}
	}

	public static record ListSpec(String fieldName, Class<?> listType) implements CodecSpec {
		@Override
		public String getField() {return fieldName();}
		@Override
		public List<CodecSpec> getMembers() {return new ArrayList<>();}
	}
	
	public static record MapSpec(String fieldName, Class<?> keyTypes, Class<?> valueTypes) implements CodecSpec {
		@Override
		public String getField() {return fieldName();}
		@Override
		public List<CodecSpec> getMembers() {return new ArrayList<>();}
	}
	
	public static record NestedMapSpec(String fieldName, Class<?> keyTypes, CodecSpec valueTypes) implements CodecSpec {
		@Override
		public String getField() {return fieldName();}
		@Override
		public List<CodecSpec> getMembers() {return new ArrayList<>();}
	}
	
	public static record PrimativeSpec(String fieldName, Class<?> type) implements CodecSpec {
		@Override
		public String getField() {return fieldName();}
		@Override
		public List<CodecSpec> getMembers() {return new ArrayList<>();}
	}
	
	//===========================UTILITY METHODS======================================
	static List<MapSpec> getEventMap(EventType[] subset) {
		return Arrays.stream(subset)
			.map(event -> new MapSpec(event.name().toUpperCase(), String.class, Long.class))
			.toList();
	}
	static List<MapSpec> getReqMap(ReqType[] subset) {
		return Arrays.stream(subset)
			.map(req -> new MapSpec(req.name().toUpperCase(), String.class, Integer.class))
			.toList();
	}
	
	//===========================DECLARATIONS=========================================
	public static final GroupSpec ITEM_SPEC = new GroupSpec("items", List.of(
			new ListSpec("isTagFor", ResourceLocation.class),
			new GroupSpec("xp_values", new ArrayList<>(getEventMap(EventType.ITEM_APPLICABLE_EVENTS))),
			new GroupSpec("bonuses", List.of(new MapSpec(ModifierDataType.HELD.name().toUpperCase(), String.class, Double.class),
											new MapSpec(ModifierDataType.WORN.name().toUpperCase(), String.class, Double.class))),
			new GroupSpec("requirements", new ArrayList<>(getReqMap(ReqType.ITEM_APPLICABLE_EVENTS))),
			new MapSpec("negative_effect", ResourceLocation.class, Integer.class),
			SalvageData.GROUP_SPEC,
			new GroupSpec("vein_data", List.of(
					new PrimativeSpec("chargeCap", Integer.class),
					new PrimativeSpec("chargeRate", Double.class)))
	));
	public static final GroupSpec BLOCK_SPEC = new GroupSpec("blocks", List.of(
			new ListSpec("isTagFor", ResourceLocation.class),
			new GroupSpec("xp_values", new ArrayList<>(getEventMap(EventType.BLOCK_APPLICABLE_EVENTS))),
			new GroupSpec("requirements", new ArrayList<>(getReqMap(ReqType.BLOCK_APPLICABLE_EVENTS))),
			new GroupSpec("vein_data", List.of(
					new PrimativeSpec("consumeAmount", Integer.class)))
	));
	public static final GroupSpec ENTITY_SPEC = new GroupSpec("blocks", List.of(
			new ListSpec("isTagFor", ResourceLocation.class),
			new GroupSpec("xp_values", new ArrayList<>(getEventMap(EventType.ENTITY_APPLICABLE_EVENTS))),
			new GroupSpec("requirements", new ArrayList<>(getReqMap(ReqType.ENTITY_APPLICABLE_EVENTS)))
	));
	public static final GroupSpec DIMENSION_SPEC = new GroupSpec("dimensions", List.of(
			new ListSpec("isTagFor", ResourceLocation.class),
			new ListSpec("vein_blacklist", ResourceLocation.class),
			new MapSpec("travel_req", String.class, Integer.class),
			new NestedMapSpec("mob_multiplier", ResourceLocation.class
					, new MapSpec("", String.class, Double.class))
	));
	public static final GroupSpec BIOME_SPEC = new GroupSpec("dimensions", List.of(
			new ListSpec("isTagFor", ResourceLocation.class),
			new ListSpec("vein_blacklist", ResourceLocation.class),
			new MapSpec("travel_req", String.class, Integer.class),
			new NestedMapSpec("mob_multiplier", ResourceLocation.class
					, new MapSpec("", String.class, Double.class)),
			new MapSpec("positive_effect", ResourceLocation.class, Integer.class),
			new MapSpec("negative_effect", ResourceLocation.class, Integer.class)
	));
	public static final GroupSpec PLAYER_SPEC = new GroupSpec("players", List.of(
			new PrimativeSpec("ignoreReq", Boolean.class),
			new MapSpec("bonuses", String.class, Double.class)
	));
	public static final GroupSpec ENCHANTMENT_SPEC = new GroupSpec("enchantments", List.of(
			new NestedMapSpec("", Integer.class
					, new MapSpec("", String.class, Integer.class))			
	));
}