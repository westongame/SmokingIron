package com.westongame.smokingiron.init;

import com.westongame.smokingiron.Reference;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Author: MrCrayfish
 *
 * In 1.21.1, enchantments are data-driven and cannot be registered via code.
 * This class provides ResourceKeys for looking up enchantments from the registry
 * and a helper method for getting enchantment levels from items.
 */
public class ModEnchantments
{
    public static final ResourceKey<Enchantment> QUICK_HANDS = key("quick_hands");
    public static final ResourceKey<Enchantment> TRIGGER_FINGER = key("trigger_finger");
    public static final ResourceKey<Enchantment> LIGHTWEIGHT = key("lightweight");
    public static final ResourceKey<Enchantment> COLLATERAL = key("collateral");
    public static final ResourceKey<Enchantment> OVER_CAPACITY = key("over_capacity");
    public static final ResourceKey<Enchantment> RECLAIMED = key("reclaimed");
    public static final ResourceKey<Enchantment> ACCELERATOR = key("accelerator");
    public static final ResourceKey<Enchantment> PUNCTURING = key("puncturing");
    public static final ResourceKey<Enchantment> FIRE_STARTER = key("fire_starter");

    private static ResourceKey<Enchantment> key(String name)
    {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, name));
    }

    /**
     * Gets the level of an enchantment on an item stack by ResourceKey.
     * This searches through the item's enchantments for a matching key.
     */
    public static int getLevel(ResourceKey<Enchantment> enchantmentKey, ItemStack stack)
    {
        if (stack.isEmpty()) return 0;
        for (var entry : stack.getEnchantments().entrySet())
        {
            Holder<Enchantment> holder = entry.getKey();
            if (holder.is(enchantmentKey))
            {
                return entry.getIntValue();
            }
        }
        return 0;
    }

    /**
     * Checks if an item stack has a specific enchantment by ResourceKey.
     */
    public static boolean has(ResourceKey<Enchantment> enchantmentKey, ItemStack stack)
    {
        return getLevel(enchantmentKey, stack) > 0;
    }
}
