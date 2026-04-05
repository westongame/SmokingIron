package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class S2CMessageRemoveProjectile
{
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageRemoveProjectile> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, S2CMessageRemoveProjectile::getEntityId,
            S2CMessageRemoveProjectile::new
    );

    private final int entityId;

    public S2CMessageRemoveProjectile(int entityId)
    {
        this.entityId = entityId;
    }

    public static void handle(S2CMessageRemoveProjectile message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleRemoveProjectile(message));
        context.setHandled(true);
    }

    public int getEntityId()
    {
        return this.entityId;
    }
}
