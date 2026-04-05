package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class S2CMessageStunGrenade
{
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageStunGrenade> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, S2CMessageStunGrenade::getX,
            ByteBufCodecs.DOUBLE, S2CMessageStunGrenade::getY,
            ByteBufCodecs.DOUBLE, S2CMessageStunGrenade::getZ,
            S2CMessageStunGrenade::new
    );

    private final double x;
    private final double y;
    private final double z;

    public S2CMessageStunGrenade(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void handle(S2CMessageStunGrenade message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleExplosionStunGrenade(message));
        context.setHandled(true);
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }
}
