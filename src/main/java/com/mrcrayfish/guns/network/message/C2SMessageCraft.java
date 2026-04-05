package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.common.network.ServerPlayHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class C2SMessageCraft
{
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageCraft> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, msg -> msg.id,
            BlockPos.STREAM_CODEC, msg -> msg.pos,
            C2SMessageCraft::new
    );

    private final ResourceLocation id;
    private final BlockPos pos;

    public C2SMessageCraft(ResourceLocation id, BlockPos pos)
    {
        this.id = id;
        this.pos = pos;
    }

    public static void handle(C2SMessageCraft message, MessageContext context)
    {
        context.execute(() -> context.getPlayer().ifPresent(p ->
        {
            if(p instanceof ServerPlayer player)
            {
                ServerPlayHandler.handleCraft(player, message.id, message.pos);
            }
        }));
        context.setHandled(true);
    }
}
