package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds 
{
	public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(Registries.SOUND_EVENT, Reference.MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_PISTOL_FIRE = register("item.pistol.fire");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_PISTOL_ENCHANTED_FIRE = register("item.pistol.enchanted_fire");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_PISTOL_RELOAD = register("item.pistol.reload");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_PISTOL_COCK = register("item.pistol.cock");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_SHOTGUN_FIRE = register("item.shotgun.fire");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_SHOTGUN_ENCHANTED_FIRE = register("item.shotgun.enchanted_fire");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_SHOTGUN_COCK = register("item.shotgun.cock");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_RIFLE_FIRE = register("item.rifle.fire");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_RIFLE_ENCHANTED_FIRE = register("item.rifle.enchanted_fire");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_RIFLE_COCK = register("item.rifle.cock");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_HEAVY_RIFLE_FIRE = register("item.heavy_rifle.fire");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_HEAVY_RIFLE_ENCHANTED_FIRE = register("item.heavy_rifle.enchanted_fire");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_HEAVY_RIFLE_COCK = register("item.heavy_rifle.cock");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_GRENADE_PIN = register("item.grenade.pin");
	public static final DeferredHolder<SoundEvent, SoundEvent> UI_WEAPON_ATTACH = register("ui.weapon.attach");

	private static DeferredHolder<SoundEvent, SoundEvent> register(String key)
	{
		return REGISTER.register(key, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, key)));
	}
}
