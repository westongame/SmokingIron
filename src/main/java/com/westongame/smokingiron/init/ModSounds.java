package com.westongame.smokingiron.init;

import com.westongame.smokingiron.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds 
{
	public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(Registries.SOUND_EVENT, Reference.MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_GRENADE_PIN = register("item.grenade.pin");
	public static final DeferredHolder<SoundEvent, SoundEvent> UI_WEAPON_ATTACH = register("ui.weapon.attach");

	private static DeferredHolder<SoundEvent, SoundEvent> register(String key)
	{
		return REGISTER.register(key, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, key)));
	}
}
