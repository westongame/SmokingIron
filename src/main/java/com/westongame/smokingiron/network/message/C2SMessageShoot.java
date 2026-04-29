package com.westongame.smokingiron.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.westongame.smokingiron.common.network.ServerPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class C2SMessageShoot
{
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageShoot> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, C2SMessageShoot::getRotationYaw,
            ByteBufCodecs.FLOAT, C2SMessageShoot::getRotationPitch,
            C2SMessageShoot::new
    );

    private final float rotationYaw;
    private final float rotationPitch;

    public C2SMessageShoot(Player player)
    {
        this.rotationYaw = player.getYRot();
        this.rotationPitch = player.getXRot();
    }

    public C2SMessageShoot(float rotationYaw, float rotationPitch)
    {
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
    }

    public static void handle(C2SMessageShoot message, MessageContext context)
    {
        context.execute(() -> context.getPlayer().ifPresent(p ->
        {
            if(p instanceof ServerPlayer player)
            {
                ServerPlayHandler.handleShoot(message, player);
            }
        }));
        context.setHandled(true);
    }

    public float getRotationYaw()
    {
        return this.rotationYaw;
    }

    public float getRotationPitch()
    {
        return this.rotationPitch;
    }
}
