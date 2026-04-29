package com.westongame.smokingiron.util;

import com.westongame.smokingiron.common.Gun;
import com.westongame.smokingiron.init.ModEnchantments;
import com.westongame.smokingiron.particles.TrailData;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class GunEnchantmentHelper
{
    public static ParticleOptions getParticle(ItemStack weapon)
    {
        if(ModEnchantments.has(ModEnchantments.FIRE_STARTER, weapon))
        {
            return ParticleTypes.LAVA;
        }
        else if(ModEnchantments.has(ModEnchantments.PUNCTURING, weapon))
        {
            return ParticleTypes.ENCHANTED_HIT;
        }
        return new TrailData(weapon.isEnchanted());
    }

    public static int getReloadInterval(ItemStack weapon)
    {
        int interval = 14;
        if(weapon.getItem() instanceof com.westongame.smokingiron.item.GunItem gunItem)
        {
            int gunInterval = gunItem.getGun().getGeneral().getReloadInterval();
            if(gunInterval > 0) interval = gunInterval;
        }
        int level = ModEnchantments.getLevel(ModEnchantments.QUICK_HANDS, weapon);
        if(level > 0)
        {
            interval -= 3 * level;
        }
        return Math.max(interval, 1);
    }

    public static int getRate(ItemStack weapon, Gun modifiedGun)
    {
        int rate = modifiedGun.getGeneral().getRate();
        int level = ModEnchantments.getLevel(ModEnchantments.TRIGGER_FINGER, weapon);
        if(level > 0)
        {
            float newRate = rate * (0.25F * level);
            rate -= Mth.clamp(newRate, 0, rate);
        }
        return rate;
    }

    public static double getAimDownSightSpeed(ItemStack weapon)
    {
        double base = 1.0;
        if(weapon.getItem() instanceof com.westongame.smokingiron.item.GunItem gunItem)
        {
            base = gunItem.getGun().getGeneral().getAimSpeed();
        }
        int level = ModEnchantments.getLevel(ModEnchantments.LIGHTWEIGHT, weapon);
        return level > 0 ? base * 1.5 : base;
    }

    public static int getAmmoCapacity(ItemStack weapon, Gun modifiedGun)
    {
        int capacity = modifiedGun.getGeneral().getMaxAmmo();
        int level = ModEnchantments.getLevel(ModEnchantments.OVER_CAPACITY, weapon);
        if(level > 0)
        {
            capacity += Math.max(level, (capacity / 2) * level);
        }
        return capacity;
    }

    public static double getProjectileSpeedModifier(ItemStack weapon)
    {
        int level = ModEnchantments.getLevel(ModEnchantments.ACCELERATOR, weapon);
        if(level > 0)
        {
            return 1.0 + 0.5 * level;
        }
        return 1.0;
    }

    public static float getAcceleratorDamage(ItemStack weapon, float damage)
    {
        int level = ModEnchantments.getLevel(ModEnchantments.ACCELERATOR, weapon);
        if(level > 0)
        {
            return damage + damage * (0.1F * level);
        }
        return damage;
    }

    public static float getPuncturingChance(ItemStack weapon)
    {
        int level = ModEnchantments.getLevel(ModEnchantments.PUNCTURING, weapon);
        return level * 0.05F;
    }
}
