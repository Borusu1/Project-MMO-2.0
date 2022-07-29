package harmonised.pmmo.client.gui.configure;

import com.mojang.blaze3d.vertex.PoseStack;

import harmonised.pmmo.util.CodecSpec;
import net.minecraft.client.gui.components.Widget;

public class ConfigPropertySelectorWidget implements Widget{
	private final CodecSpec spec;
	
	public ConfigPropertySelectorWidget(CodecSpec srcSpec) {
		this.spec = srcSpec;
	}

	@Override
	public void render(PoseStack p_94669_, int p_94670_, int p_94671_, float p_94672_) {
		// TODO Auto-generated method stub
		
	}

}
