package harmonised.pmmo.client.gui.configure;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import harmonised.pmmo.util.CodecSpec;
import harmonised.pmmo.util.Reference;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ConfigEditScreen extends Screen{
	private static final ResourceLocation GUI_BG = new ResourceLocation(Reference.MOD_ID, "textures/gui/screenboxy.png");
	private final CodecSpec objectSpec;
	private final ResourceLocation baseObject;
	private JsonObject configuration;

	protected ConfigEditScreen(CodecSpec objectSpec, ResourceLocation baseObject, JsonObject configuration) {
		super(Component.literal("Config Edit Screen"));
		this.objectSpec = objectSpec;
		this.baseObject = baseObject;
		this.configuration = configuration;
	}

	@Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(stack, 1);
		
	}
	
	@Override
    public void renderBackground(PoseStack stack, int p_renderBackground_1_) {
		RenderSystem.setShaderTexture(0, GUI_BG);
        this.blit(stack,  this.width/2-128, this.height/2-128, 0, 0,  256, 256);
	}
}
