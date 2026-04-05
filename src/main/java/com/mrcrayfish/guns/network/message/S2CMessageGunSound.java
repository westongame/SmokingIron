package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public class S2CMessageGunSound
{
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageGunSound> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> {
                buf.writeResourceLocation(msg.id);
                buf.writeEnum(msg.category);
                buf.writeFloat(msg.x);
                buf.writeFloat(msg.y);
                buf.writeFloat(msg.z);
                buf.writeFloat(msg.volume);
                buf.writeFloat(msg.pitch);
                buf.writeInt(msg.shooterId);
                buf.writeBoolean(msg.muzzle);
                buf.writeBoolean(msg.reload);
            },
            buf -> new S2CMessageGunSound(
                    buf.readResourceLocation(),
                    buf.readEnum(SoundSource.class),
                    buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readFloat(), buf.readFloat(),
                    buf.readInt(),
                    buf.readBoolean(), buf.readBoolean()
            )
    );

    private final ResourceLocation id;
    private final SoundSource category;
    private final float x;
    private final float y;
    private final float z;
    private final float volume;
    private final float pitch;
    private final int shooterId;
    private final boolean muzzle;
    private final boolean reload;

    public S2CMessageGunSound(ResourceLocation id, SoundSource category, float x, float y, float z, float volume, float pitch, int shooterId, boolean muzzle, boolean reload)
    {
        this.id = id;
        this.category = category;
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
        this.shooterId = shooterId;
        this.muzzle = muzzle;
        this.reload = reload;
    }

    public static void handle(S2CMessageGunSound message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleMessageGunSound(message));
        context.setHandled(true);
    }

    public ResourceLocation getId()
    {
        return this.id;
    }

    public SoundSource getCategory()
    {
        return this.category;
    }

    public float getX()
    {
        return this.x;
    }

    public float getY()
    {
        return this.y;
    }

    public float getZ()
    {
        return this.z;
    }

    public float getVolume()
    {
        return this.volume;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public int getShooterId()
    {
        return this.shooterId;
    }

    public boolean showMuzzleFlash()
    {
        return this.muzzle;
    }

    public boolean isReload()
    {
        return this.reload;
    }
}
