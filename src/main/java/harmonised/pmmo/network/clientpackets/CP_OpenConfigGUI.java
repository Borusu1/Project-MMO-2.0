package harmonised.pmmo.network.clientpackets;

import java.util.function.Supplier;

import harmonised.pmmo.client.gui.configure.ConfigObjectSelectScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;

public class CP_OpenConfigGUI {
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Minecraft.getInstance().setScreen(new ConfigObjectSelectScreen());
		});
		ctx.get().setPacketHandled(true);
	}
}
