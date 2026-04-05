package com.mrcrayfish.guns.compat;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Method;

/**
 * Compatibility with tr7zw's First Person Model mod.
 * Disables the first-person body rendering when the player is holding a gun,
 * so that the Gun Mod's custom first-person arm/weapon rendering works correctly.
 */
public class FirstPersonModelCompat
{
    public static void init()
    {
        try
        {
            Class<?> apiClass = Class.forName("dev.tr7zw.firstperson.api.FirstPersonAPI");
            Class<?> handlerInterface = Class.forName("dev.tr7zw.firstperson.api.ActivationHandler");
            Method registerMethod = apiClass.getMethod("registerPlayerHandler", Object.class);

            Object handler = java.lang.reflect.Proxy.newProxyInstance(
                    handlerInterface.getClassLoader(),
                    new Class<?>[]{handlerInterface},
                    (proxy, method, args) ->
                    {
                        if(method.getName().equals("preventFirstperson"))
                        {
                            Player player = Minecraft.getInstance().player;
                            return player != null && player.getMainHandItem().getItem() instanceof GunItem;
                        }
                        return false;
                    }
            );

            registerMethod.invoke(null, handler);
            GunMod.LOGGER.info("First Person Model compatibility enabled");
        }
        catch(Exception e)
        {
            GunMod.LOGGER.error("Failed to register First Person Model compatibility", e);
        }
    }
}
