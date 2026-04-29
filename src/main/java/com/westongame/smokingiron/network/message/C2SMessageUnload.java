package com.westongame.smokingiron.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.westongame.smokingiron.common.network.ServerPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public class C2SMessageUnload
{
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageUnload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> {},
            buf -> new C2SMessageUnload()
    );

    public static void handle(C2SMessageUnload message, MessageContext context)
    {
        context.execute(() -> context.getPlayer().ifPresent(p ->
        {
            if(p instanceof ServerPlayer player && !player.isSpectator())
            {
                ServerPlayHandler.handleUnload(player);
            }
        }));
        context.setHandled(true);
    }
}
