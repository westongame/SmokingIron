package com.mrcrayfish.guns.client;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforgespi.locating.IModFile;

import java.nio.file.Path;
import java.util.Optional;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class PackHandler {
    @SubscribeEvent
    public static void onAddPackFindersEvent(AddPackFindersEvent event) {
        if (!event.getPackType().equals(PackType.CLIENT_RESOURCES)) return;
        final IModFile modFile = ModList.get().getModFileById("cgm").getFile();
        final Path resourcePath = modFile.findResource("packs/cgm_pbr");
        final String name = "feature/cgm_pbr_textures";
        final Pack.ResourcesSupplier sup = new PathPackResources.PathResourcesSupplier(resourcePath);
        event.addRepositorySource(c -> {
            Pack pack = Pack.readMetaAndCreate(
                    new PackLocationInfo(name, Component.translatable("pack.cgm.pbr.title"), PackSource.FEATURE, Optional.empty()),
                    sup,
                    PackType.CLIENT_RESOURCES,
                    new net.minecraft.server.packs.PackSelectionConfig(false, Pack.Position.TOP, false)
            );
            if (pack != null) {
                c.accept(pack);
            }
        });
    }
}
