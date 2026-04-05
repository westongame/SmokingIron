package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public class C2SMessageShooting
{
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageShooting> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, msg -> msg.shooting,
            C2SMessageShooting::new
    );

    private final boolean shooting;

    public C2SMessageShooting(boolean shooting)
    {
        this.shooting = shooting;
    }

    public static void handle(C2SMessageShooting message, MessageContext context)
    {
        context.execute(() -> context.getPlayer().ifPresent(p ->
        {
            if(p instanceof ServerPlayer player)
            {
                ModSyncedDataKeys.SHOOTING.setValue(player, message.shooting);
            }
        }));
        context.setHandled(true);
    }
}
