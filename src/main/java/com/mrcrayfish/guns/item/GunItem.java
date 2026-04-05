package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.debug.Debug;
import com.mrcrayfish.guns.init.ModDataComponents;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.registries.BuiltInRegistries;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.WeakHashMap;

public class GunItem extends Item implements IColored, IMeta
{
    private WeakHashMap<CompoundTag, Gun> modifiedGunCache = new WeakHashMap<>();

    private Gun gun = new Gun();

    public GunItem(Item.Properties properties)
    {
        super(properties);
    }

    public void setGun(NetworkGunManager.Supplier supplier)
    {
        this.gun = supplier.getGun();
    }

    public Gun getGun()
    {
        return this.gun;
    }

    private static final DecimalFormat ATTRIBUTE_FORMAT = new DecimalFormat("0.##");

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag)
    {
        Gun modifiedGun = this.getModifiedGun(stack);

        Item ammo = BuiltInRegistries.ITEM.get(modifiedGun.getProjectile().getItem());
        if(ammo != null)
        {
            tooltip.add(Component.translatable("info.cgm.ammo_type", Component.translatable(ammo.getDescriptionId()).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));
        }

        String additionalDamageText = "";
        float additionalDamage = stack.getOrDefault(ModDataComponents.ADDITIONAL_DAMAGE.get(), 0.0F);
        additionalDamage += GunModifierHelper.getAdditionalDamage(stack);
        if(additionalDamage > 0)
        {
            additionalDamageText = ChatFormatting.GREEN + " +" + ATTRIBUTE_FORMAT.format(additionalDamage);
        }
        else if(additionalDamage < 0)
        {
            additionalDamageText = ChatFormatting.RED + " " + ATTRIBUTE_FORMAT.format(additionalDamage);
        }

        float damage = modifiedGun.getProjectile().getDamage();
        damage = GunModifierHelper.getModifiedProjectileDamage(stack, damage);
        damage = GunEnchantmentHelper.getAcceleratorDamage(stack, damage);
        tooltip.add(Component.translatable("info.cgm.damage", ChatFormatting.WHITE + ATTRIBUTE_FORMAT.format(damage) + additionalDamageText).withStyle(ChatFormatting.GRAY));

        if(stack.has(ModDataComponents.IGNORE_AMMO.get()))
        {
            tooltip.add(Component.translatable("info.cgm.ignore_ammo").withStyle(ChatFormatting.AQUA));
        }
        else
        {
            int ammoCount = stack.getOrDefault(ModDataComponents.AMMO_COUNT.get(), 0);
            tooltip.add(Component.translatable("info.cgm.ammo", ChatFormatting.WHITE.toString() + ammoCount + "/" + GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun)).withStyle(ChatFormatting.GRAY));
        }

        tooltip.add(Component.translatable("info.cgm.attachment_help", KeyBinds.KEY_ATTACHMENTS.getTranslatedKeyMessage().getString().toUpperCase(Locale.ENGLISH)).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return slotChanged;
    }

    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        Gun modifiedGun = this.getModifiedGun(stack);
        return !stack.has(ModDataComponents.IGNORE_AMMO.get()) && stack.getOrDefault(ModDataComponents.AMMO_COUNT.get(), 0) != GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun);
    }

    @Override
    public int getBarWidth(ItemStack stack)
    {
        Gun modifiedGun = this.getModifiedGun(stack);
        int ammoCount = stack.getOrDefault(ModDataComponents.AMMO_COUNT.get(), 0);
        return (int) (13.0 * (ammoCount / (double) GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun)));
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        return Objects.requireNonNull(ChatFormatting.YELLOW.getColor());
    }

    public Gun getModifiedGun(ItemStack stack)
    {
        CompoundTag gunData = stack.get(ModDataComponents.GUN_DATA.get());
        if(gunData != null && gunData.contains("Gun", Tag.TAG_COMPOUND))
        {
            return this.modifiedGunCache.computeIfAbsent(gunData, item ->
            {
                if(gunData.getBoolean("Custom"))
                {
                    return Gun.create(gunData.getCompound("Gun"));
                }
                else
                {
                    Gun gunCopy = this.gun.copy();
                    gunCopy.deserializeNBT(net.minecraft.core.RegistryAccess.EMPTY, gunData.getCompound("Gun"));
                    return gunCopy;
                }
            });
        }
        if(GunMod.isDebugging())
        {
            return Debug.getGun(this);
        }
        return this.gun;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return this.getMaxStackSize(stack) == 1;
    }

    @Override
    public int getEnchantmentValue()
    {
        return 5;
    }

}
