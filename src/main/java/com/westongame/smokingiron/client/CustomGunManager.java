package com.westongame.smokingiron.client;

import com.westongame.smokingiron.Reference;
import com.westongame.smokingiron.common.CustomGun;
import com.westongame.smokingiron.common.CustomGunLoader;
import com.westongame.smokingiron.init.ModDataComponents;
import com.westongame.smokingiron.init.ModItems;
import com.westongame.smokingiron.network.message.S2CMessageUpdateGuns;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Map;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class CustomGunManager
{
    private static Map<ResourceLocation, CustomGun> customGunMap;

    public static boolean updateCustomGuns(S2CMessageUpdateGuns message)
    {
        return updateCustomGuns(message.getCustomGuns());
    }

    private static boolean updateCustomGuns(Map<ResourceLocation, CustomGun> customGunMap)
    {
        CustomGunManager.customGunMap = customGunMap;
        return true;
    }

    public static void fill(CreativeModeTab.Output output)
    {
        if(customGunMap != null)
        {
            customGunMap.forEach((id, gun) ->
            {
                ItemStack stack = new ItemStack(ModItems.REVOLVER.get());
                stack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, Component.translatable("item." + id.getNamespace() + "." + id.getPath() + ".name"));
                CompoundTag gunData = new CompoundTag();
                gunData.put("Model", gun.getModel().save(net.minecraft.core.RegistryAccess.EMPTY));
                gunData.put("Gun", gun.getGun().serializeNBT(net.minecraft.core.RegistryAccess.EMPTY));
                gunData.putBoolean("Custom", true);
                stack.set(ModDataComponents.GUN_DATA.get(), gunData);
                stack.set(ModDataComponents.AMMO_COUNT.get(), gun.getGun().getGeneral().getMaxAmmo());
                output.accept(stack);
            });
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event)
    {
        customGunMap = null;
    }
}
