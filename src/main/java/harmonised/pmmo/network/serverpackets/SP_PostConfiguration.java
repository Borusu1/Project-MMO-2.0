package harmonised.pmmo.network.serverpackets;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SP_PostConfiguration {

	public SP_PostConfiguration() {}
	public SP_PostConfiguration(FriendlyByteBuf buf) {}
	public void encode(FriendlyByteBuf buf) {}
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			
		});
		ctx.get().setPacketHandled(true);
	}
}
