package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class S2CMessageProjectileHitBlock
{
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageProjectileHitBlock> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> {
                buf.writeDouble(msg.x);
                buf.writeDouble(msg.y);
                buf.writeDouble(msg.z);
                buf.writeBlockPos(msg.pos);
                buf.writeEnum(msg.face);
            },
            buf -> new S2CMessageProjectileHitBlock(
                    buf.readDouble(), buf.readDouble(), buf.readDouble(),
                    buf.readBlockPos(), buf.readEnum(Direction.class)
            )
    );

    private final double x;
    private final double y;
    private final double z;
    private final BlockPos pos;
    private final Direction face;

    public S2CMessageProjectileHitBlock(double x, double y, double z, BlockPos pos, Direction face)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pos = pos;
        this.face = face;
    }

    public static void handle(S2CMessageProjectileHitBlock message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleProjectileHitBlock(message));
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

    public BlockPos getPos()
    {
        return this.pos;
    }

    public Direction getFace()
    {
        return this.face;
    }
}
