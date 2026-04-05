package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.common.network.ServerPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public class C2SMessageAttachments
{
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageAttachments> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> {},
            buf -> new C2SMessageAttachments()
    );

    public static void handle(C2SMessageAttachments message, MessageContext context)
    {
        context.execute(() -> context.getPlayer().ifPresent(p ->
        {
            if(p instanceof ServerPlayer player)
            {
                ServerPlayHandler.handleAttachments(player);
            }
        }));
        context.setHandled(true);
    }
}
