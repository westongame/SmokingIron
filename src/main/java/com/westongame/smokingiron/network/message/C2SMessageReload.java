package com.westongame.smokingiron.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.westongame.smokingiron.event.GunReloadEvent;
import com.westongame.smokingiron.init.ModSyncedDataKeys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;

public class C2SMessageReload
{
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageReload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, msg -> msg.reload,
            C2SMessageReload::new
    );

    private final boolean reload;

    public C2SMessageReload(boolean reload)
    {
        this.reload = reload;
    }

    public static void handle(C2SMessageReload message, MessageContext context)
    {
        context.execute(() -> context.getPlayer().ifPresent(p ->
        {
            if(p instanceof ServerPlayer player && !player.isSpectator())
            {
                ModSyncedDataKeys.RELOADING.setValue(player, message.reload);
                if(!message.reload)
                    return;

                ItemStack gun = player.getMainHandItem();
                if(NeoForge.EVENT_BUS.post(new GunReloadEvent.Pre(player, gun)).isCanceled())
                {
                    ModSyncedDataKeys.RELOADING.setValue(player, false);
                    return;
                }
                NeoForge.EVENT_BUS.post(new GunReloadEvent.Post(player, gun));
            }
        }));
        context.setHandled(true);
    }
}
