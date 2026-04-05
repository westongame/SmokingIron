package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public class C2SMessageAim
{
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageAim> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, msg -> msg.aiming,
            C2SMessageAim::new
    );

    private final boolean aiming;

    public C2SMessageAim(boolean aiming)
    {
        this.aiming = aiming;
    }

    public static void handle(C2SMessageAim message, MessageContext context)
    {
        context.execute(() -> context.getPlayer().ifPresent(p ->
        {
            if(p instanceof ServerPlayer player && !player.isSpectator())
            {
                ModSyncedDataKeys.AIMING.setValue(player, message.aiming);
            }
        }));
        context.setHandled(true);
    }
}
