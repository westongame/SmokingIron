package com.westongame.smokingiron.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.westongame.smokingiron.client.network.ClientPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class S2CMessageProjectileHitEntity
{
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageProjectileHitEntity> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> {
                buf.writeDouble(msg.x);
                buf.writeDouble(msg.y);
                buf.writeDouble(msg.z);
                buf.writeByte(msg.type);
                buf.writeBoolean(msg.player);
            },
            buf -> new S2CMessageProjectileHitEntity(
                    buf.readDouble(), buf.readDouble(), buf.readDouble(),
                    buf.readByte(), buf.readBoolean()
            )
    );

    private final double x;
    private final double y;
    private final double z;
    private final int type;
    private final boolean player;

    public S2CMessageProjectileHitEntity(double x, double y, double z, int type, boolean player)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.player = player;
    }

    public static void handle(S2CMessageProjectileHitEntity message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleProjectileHitEntity(message));
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

    public boolean isHeadshot()
    {
        return this.type == HitType.HEADSHOT;
    }

    public boolean isCritical()
    {
        return this.type == HitType.CRITICAL;
    }

    public boolean isPlayer()
    {
        return this.player;
    }

    public static class HitType
    {
        public static final int NORMAL = 0;
        public static final int HEADSHOT = 1;
        public static final int CRITICAL = 2;
    }
}
