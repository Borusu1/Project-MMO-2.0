package harmonised.pmmo.client.gui.configure;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import harmonised.pmmo.util.CodecSpec;
import harmonised.pmmo.util.Reference;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ConfigEditScreen extends Screen{
	private static final ResourceLocation GUI_BG = new ResourceLocation(Reference.MOD_ID, "textures/gui/screenboxy.png");
	private final Screen parent;
	private ConfigScrollWidget scroll;
	
	private final CodecSpec objectSpec;
	private final List<ResourceLocation> applicableObjects = new ArrayList<>();
	private CompoundTag existingConfig, updatedValues, consolidatedValues;

	protected ConfigEditScreen(Screen parent, CodecSpec objectSpec, ResourceLocation baseObject, CompoundTag existingConfig) {
		super(Component.literal("Config Edit Screen"));
		this.parent = parent;
		this.objectSpec = objectSpec;
		this.applicableObjects.add(baseObject);
		this.existingConfig = existingConfig;
		this.consolidatedValues = existingConfig;
	}

	@Override
	protected void init() {
		scroll = new ConfigScrollWidget(this, 206, 200, (this.height/2 - 128) + 30, (this.width/2 - 128) + 25, objectSpec, consolidatedValues);
		this.addWidget(scroll);
		super.init();
	}
	
	@Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(stack, 1);
		scroll.render(stack, mouseX, mouseY, partialTicks);
		super.render(stack, mouseX, mouseY, partialTicks);
	}
	
	@Override
    public void renderBackground(PoseStack stack, int p_renderBackground_1_) {
		RenderSystem.setShaderTexture(0, GUI_BG);
        this.blit(stack,  this.width/2-128, this.height/2-128, 0, 0,  256, 256);
	}
	
	@Override
	public void onClose() {getMinecraft().setScreen(parent);}
}
