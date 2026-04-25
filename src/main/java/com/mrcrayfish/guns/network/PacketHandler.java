package com.mrcrayfish.guns.network;

import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.network.FrameworkNetwork;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.network.message.*;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;

public class PacketHandler
{
    private static FrameworkNetwork playChannel;

    public static void init()
    {
        playChannel = FrameworkAPI.createNetworkBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "play"), 1)
                .registerPlayMessage("aim", C2SMessageAim.class, C2SMessageAim.STREAM_CODEC, C2SMessageAim::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("reload", C2SMessageReload.class, C2SMessageReload.STREAM_CODEC, C2SMessageReload::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("shoot", C2SMessageShoot.class, C2SMessageShoot.STREAM_CODEC, C2SMessageShoot::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("unload", C2SMessageUnload.class, C2SMessageUnload.STREAM_CODEC, C2SMessageUnload::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("craft", C2SMessageCraft.class, C2SMessageCraft.STREAM_CODEC, C2SMessageCraft::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("bullet_trail", S2CMessageBulletTrail.class, S2CMessageBulletTrail.STREAM_CODEC, S2CMessageBulletTrail::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("attachments", C2SMessageAttachments.class, C2SMessageAttachments.STREAM_CODEC, C2SMessageAttachments::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("update_guns", S2CMessageUpdateGuns.class, S2CMessageUpdateGuns.STREAM_CODEC, S2CMessageUpdateGuns::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("blood", S2CMessageBlood.class, S2CMessageBlood.STREAM_CODEC, S2CMessageBlood::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("shooting", C2SMessageShooting.class, C2SMessageShooting.STREAM_CODEC, C2SMessageShooting::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("gun_sound", S2CMessageGunSound.class, S2CMessageGunSound.STREAM_CODEC, S2CMessageGunSound::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("projectile_hit_block", S2CMessageProjectileHitBlock.class, S2CMessageProjectileHitBlock.STREAM_CODEC, S2CMessageProjectileHitBlock::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("projectile_hit_entity", S2CMessageProjectileHitEntity.class, S2CMessageProjectileHitEntity.STREAM_CODEC, S2CMessageProjectileHitEntity::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("remove_projectile", S2CMessageRemoveProjectile.class, S2CMessageRemoveProjectile.STREAM_CODEC, S2CMessageRemoveProjectile::handle, PacketFlow.CLIENTBOUND)
                .build();
    }

    /**
     * Gets the play network channel for MrCrayfish's Gun Mod
     */
    public static FrameworkNetwork getPlayChannel()
    {
        return playChannel;
    }
}
