package harmonised.pmmo.client.gui.configure;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.client.gui.SelectionWidget;
import harmonised.pmmo.client.gui.SelectionWidget.SelectionEntry;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.setup.datagen.LangProvider;
import harmonised.pmmo.setup.datagen.LangProvider.Translation;
import harmonised.pmmo.util.CodecSpec;
import harmonised.pmmo.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistries;

public class ConfigObjectSelectScreen extends Screen{
	private static final ResourceLocation GUI_BG = new ResourceLocation(Reference.MOD_ID, "textures/gui/screenboxy.png");
	
	private SelectionWidget<SelectionEntry<TYPES>> typeSelect;
	private SelectionWidget<SelectionEntry<ResourceLocation>> objectSelect;
	private EditBox filterBox;
	private Button selectButton;
	private TYPES typeSelection;
	private ResourceLocation objectSelection;
	
	public static enum TYPES {
		ITEMS(CodecSpec.ITEM_SPEC, LangProvider.CONFIG_TYPESELECT_ITEMS, 
				filter -> ForgeRegistries.ITEMS.getKeys().stream()
							.filter(rl -> rl.toString().contains(filter))
							.map(val -> new SelectionEntry<>(Component.literal(val.toString()), val))
							.toList()), 
		BLOCKS(CodecSpec.BLOCK_SPEC, LangProvider.CONFIG_TYPESELECT_BLOCKS,
				filter -> ForgeRegistries.BLOCKS.getKeys().stream()
							.filter(rl -> rl.toString().contains(filter))
							.map(val -> new SelectionEntry<>(Component.literal(val.toString()), val))
							.toList()), 
		ENTITIES(CodecSpec.ENTITY_SPEC, LangProvider.CONFIG_TYPESELECT_ENTITIES,
				filter -> ForgeRegistries.ENTITY_TYPES.getKeys().stream()
							.filter(rl -> rl.toString().contains(filter))
							.map(val -> new SelectionEntry<>(Component.literal(val.toString()), val))
							.toList()),  
		DIMENSIONS(CodecSpec.DIMENSION_SPEC, LangProvider.CONFIG_TYPESELECT_DIMENSIONS,
				filter -> Minecraft.getInstance().player.connection.levels().stream()
							.filter(key -> key.location().toString().contains(filter))
							.map(val -> new SelectionEntry<>(Component.literal(val.location().toString()), val.location()))
							.toList()), 
		BIOMES(CodecSpec.BIOME_SPEC, LangProvider.CONFIG_TYPESELECT_BIOMES,
				filter -> ForgeRegistries.BIOMES.getKeys().stream()
							.filter(rl -> rl.toString().contains(filter))
							.map(val -> new SelectionEntry<>(Component.literal(val.toString()), val))
							.toList()), 
		PLAYERS(CodecSpec.PLAYER_SPEC, LangProvider.CONFIG_TYPESELECT_PLAYERS,
				filter -> Minecraft.getInstance().player.connection.getOnlinePlayers().stream()
							.filter(player -> player.getProfile().getName().contains(filter))
							.map(val -> new SelectionEntry<>(Component.literal(val.getProfile().getName())
											, new ResourceLocation(val.getProfile().getId().toString())))
							.toList()), 
		ENCHANTMENTS(CodecSpec.ENCHANTMENT_SPEC, LangProvider.CONFIG_TYPESELECT_ENCHANTMENTS,
				filter -> ForgeRegistries.ENCHANTMENTS.getKeys().stream()
							.filter(rl -> rl.toString().contains(filter))
							.map(val -> new SelectionEntry<>(Component.literal(val.toString()), val))
							.toList());
	
		private CodecSpec spec;
		public Translation translation;
		private Function<String, List<SelectionEntry<ResourceLocation>>> filteredObjects;
		TYPES(CodecSpec spec, Translation translation, Function<String, List<SelectionEntry<ResourceLocation>>> filteredObjects) {
			this.spec = spec;
			this.translation = translation;
			this.filteredObjects = filteredObjects;
		}
		
		public ConfigEditScreen getScreen(ResourceLocation objectID, JsonObject defaults) {
			return new ConfigEditScreen(spec, objectID, defaults);
		}
		
		public List<SelectionEntry<ResourceLocation>> getObjectList(String filter) {
			return filteredObjects.apply(filter);
		}
	}

	public ConfigObjectSelectScreen() {
		super(Component.literal("Object Select"));
		init();
	}
	
	protected void init() {
		filterBox = new EditBox(font, this.width/2 - 100, 60, 200, 20, Component.literal(""));
		objectSelect = new SelectionWidget<>(this.width/2 - 100, 95, 200, LangProvider.CONFIG_OBJECTSELECT_DEFAULT.asComponent(), sel -> objectSelection = sel.reference);
		objectSelect.setEntries(TYPES.ITEMS.getObjectList(""));
		typeSelect = new SelectionWidget<>(this.width/2 - 100, 25, 200, LangProvider.CONFIG_TYPESELECT_DEFAULT.asComponent(), (sel) -> {
			typeSelection = sel.reference;
			objectSelect.setEntries(typeSelection.getObjectList(filterBox.getValue()));
		});
		typeSelect.setEntries(Arrays.stream(TYPES.values()).map(type -> new SelectionEntry<>(type.translation.asComponent(), type)).toList());
		selectButton = new Button(this.width/2 - 32, 120, 64, 20, LangProvider.CONFIG_CONFIGURE.asComponent()
				, button -> Minecraft.getInstance().setScreen(typeSelection.getScreen(objectSelection, getDefaultSetting())));
		
		this.addWidget(typeSelect);
		this.addWidget(objectSelect);
		this.addWidget(filterBox);
		this.addWidget(selectButton);
		super.init();
	}

	@Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(stack, 1);
		GuiComponent.drawString(stack, font, LangProvider.CONFIG_PROMPT_TYPE.asComponent(), this.width/2-100, 10, 0xFFFFFF);
		GuiComponent.drawString(stack, font, LangProvider.CONFIG_FILTER_OBJECTS.asComponent(), this.width/2-100, 47, 0xFFFFFF);
		GuiComponent.drawString(stack, font, LangProvider.CONFIG_PROMPT_OBJECT.asComponent(), this.width/2-100, 82, 0xFFFFFF);
		filterBox.render(stack, mouseX, mouseY, partialTicks);
		selectButton.render(stack, mouseX, mouseY, partialTicks);
		objectSelect.render(stack, mouseX, mouseY, partialTicks);
		typeSelect.render(stack, mouseX, mouseY, partialTicks);
		super.render(stack, mouseX, mouseY, partialTicks);
	}
	
	@Override
    public void renderBackground(PoseStack stack, int p_renderBackground_1_) {
		RenderSystem.setShaderTexture(0, GUI_BG);
        this.blit(stack,  this.width/2-128, this.height/2-128, 0, 0,  256, 256);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
		if (objectSelect.isMouseOver(mouseX, mouseY))
			objectSelect.setEntries(typeSelection.getObjectList(filterBox.getValue()));
		return super.mouseClicked(mouseX, mouseY, partialTicks);
	}
	
	private static final Type mapType = new TypeToken<HashMap<?, ?>>(){}.getType();
	private JsonObject getDefaultSetting() {
		JsonObject root = new JsonObject();
		Core core = Core.get(LogicalSide.CLIENT);
		final Gson gson = new Gson(); 
		switch(typeSelection) {
		case ITEMS: {
			root.add("xp_values", Arrays.stream(EventType.ITEM_APPLICABLE_EVENTS)
					.map(event -> core.getXpUtils().getObjectExperienceMap(event, objectSelection))
					.map(map -> gson.toJsonTree(map, mapType))
					.collect(Collectors.toMap(null, null)));
			return root;
		}
		case BLOCKS: {
			return root;
		}
		case ENTITIES: {
			return root;
		}
		case DIMENSIONS: {
			return root;
		}
		case BIOMES: {
			return root;
		}
		case PLAYERS: {
			return root;
		}
		case ENCHANTMENTS: {
			return root;
		}
		default:
			return root;
		}
		
		
	}
}
