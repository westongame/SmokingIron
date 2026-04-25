package com.mrcrayfish.guns.datagen;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.common.GripType;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.init.ModSounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

/**
 * Author: MrCrayfish
 */
public class GunGen extends GunProvider
{
    public GunGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void registerGuns()
    {
        this.addGun(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "heavy_rifle"), Gun.Builder.create()
                .setFireRate(40)
                .setGripType(GripType.TWO_HANDED)
                .setMaxAmmo(4)
                .setReloadAmount(1)
                .setRecoilAngle(10.0F)
                .setRecoilKick(1.0F)
                .setRecoilDurationOffset(0.5F)
                .setRecoilAdsReduction(0.4F)
                .setAlwaysSpread(true)
                .setSpread(1.0F)
                .setAmmo(ModItems.ADVANCED_AMMO.get())
                .setDamage(18.0F)
                .setProjectileAffectedByGravity(true)
                .setProjectileSize(0.0625F)
                .setProjectileSpeed(25.0F)
                .setProjectileLife(30)
                .setFireSound(ModSounds.ITEM_HEAVY_RIFLE_FIRE.get())
                .setReloadSound(ModSounds.ITEM_PISTOL_RELOAD.get())
                .setCockSound(ModSounds.ITEM_HEAVY_RIFLE_COCK.get())
                .setEnchantedFireSound(ModSounds.ITEM_HEAVY_RIFLE_ENCHANTED_FIRE.get())
                .setMuzzleFlash(1.0, 0.0, 3.6, -9.41)
                .setZoom(Gun.Modules.Zoom.builder()
                        .setFovModifier(0.6F)
                        .setOffset(0.0, 5.1, 2.0))
                .setScope(1.0F, 0.0, 4.4, 4.0)
                .setBarrel(0.5F, 0.0, 3.6, -9.4)
                .setUnderBarrel(1.0F, 0.0, 3.0, -0.5)
                .build());

        this.addGun(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "pistol"), Gun.Builder.create()
                .setFireRate(4)
                .setGripType(GripType.ONE_HANDED)
                .setMaxAmmo(16)
                .setReloadAmount(4)
                .setRecoilAngle(10.0F)
                .setRecoilAdsReduction(0.5F)
                .setAlwaysSpread(true)
                .setSpread(1.0F)
                .setAmmo(ModItems.BASIC_BULLET.get())
                .setDamage(9.0F)
                .setProjectileSize(0.2F)
                .setProjectileSpeed(10.0)
                .setProjectileLife(25)
                .setFireSound(ModSounds.ITEM_PISTOL_FIRE.get())
                .setReloadSound(ModSounds.ITEM_PISTOL_RELOAD.get())
                .setCockSound(ModSounds.ITEM_PISTOL_COCK.get())
                .setEnchantedFireSound(ModSounds.ITEM_PISTOL_ENCHANTED_FIRE.get())
                .setMuzzleFlash(0.5, 0.0, 3.3, 2.64)
                .setZoom(Gun.Modules.Zoom.builder()
                        .setFovModifier(0.7F)
                        .setOffset(0.0, 4.5, -1.0))
                .setScope(0.75F, 0.0, 3.7, 6.0)
                .setBarrel(0.5F, 0.0, 3.3, 2.65)
                .setStock(1.0F, 0.0, 3.3, 7.95)
                .build());

        this.addGun(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "rifle"), Gun.Builder.create()
                .setFireRate(8)
                .setGripType(GripType.TWO_HANDED)
                .setMaxAmmo(10)
                .setReloadAmount(2)
                .setRecoilAngle(10.0F)
                .setRecoilKick(0.5F)
                .setRecoilAdsReduction(0.5F)
                .setAlwaysSpread(true)
                .setSpread(1.0F)
                .setAmmo(ModItems.ADVANCED_AMMO.get())
                .setDamage(15.0F)
                .setProjectileAffectedByGravity(true)
                .setProjectileSize(0.0625F)
                .setProjectileSpeed(20.0F)
                .setProjectileLife(30)
                .setFireSound(ModSounds.ITEM_RIFLE_FIRE.get())
                .setReloadSound(ModSounds.ITEM_PISTOL_RELOAD.get())
                .setCockSound(ModSounds.ITEM_RIFLE_COCK.get())
                .setEnchantedFireSound(ModSounds.ITEM_RIFLE_ENCHANTED_FIRE.get())
                .setMuzzleFlash(0.5, 0.0, 3.8365, -10.21)
                .setZoom(Gun.Modules.Zoom.builder()
                        .setFovModifier(0.6F)
                        .setOffset(0.0, 4.6223, 6.0))
                .setScope(1.0F, 0.0, 4.3, 3.3)
                .setBarrel(0.45F, 0.0, 3.8365,-10.2)
                .setStock(1.0F, 0.0, 3.1294, 8.3)
                .setUnderBarrel(1.0F, 0.0, 2.63, -0.5)
                .build());

        this.addGun(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "shotgun"), Gun.Builder.create()
                .setFireRate(8)
                .setGripType(GripType.TWO_HANDED)
                .setMaxAmmo(8)
                .setReloadAmount(2)
                .setRecoilKick(0.5F)
                .setRecoilAngle(10.0F)
                .setRecoilAdsReduction(0.4F)
                .setProjectileAmount(5)
                .setAlwaysSpread(true)
                .setSpread(20.0F)
                .setAmmo(ModItems.SHELL.get())
                .setDamage(18.0F)
                .setProjectileSize(1.0F)
                .setProjectileSpeed(10.0)
                .setProjectileLife(5)
                .setFireSound(ModSounds.ITEM_SHOTGUN_FIRE.get())
                .setReloadSound(ModSounds.ITEM_PISTOL_RELOAD.get())
                .setCockSound(ModSounds.ITEM_SHOTGUN_COCK.get())
                .setEnchantedFireSound(ModSounds.ITEM_SHOTGUN_ENCHANTED_FIRE.get())
                .setMuzzleFlash(0.5, 0.0, 3.6505, -3.81)
                .setZoom(Gun.Modules.Zoom.builder()
                        .setFovModifier(0.7F)
                        .setOffset(0.0, 5.1, 3.2))
                .setScope(1.0F, 0.0, 4.4, 4.0)
                .setBarrel(0.5F, 0.0, 3.6506, -3.8)
                .setStock(1.0F, 0.0, 3.6506, 8.4)
                .build());
    }
}
