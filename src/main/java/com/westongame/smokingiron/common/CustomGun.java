package com.westongame.smokingiron.common;

import com.westongame.smokingiron.annotation.Ignored;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Author: MrCrayfish
 */
public class CustomGun implements INBTSerializable<CompoundTag>
{
    @Ignored
    public ItemStack model;
    public Gun gun;

    public ItemStack getModel()
    {
        return this.model;
    }

    public Gun getGun()
    {
        return this.gun;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider)
    {
        CompoundTag compound = new CompoundTag();
        compound.put("Model", this.model.save(provider));
        compound.put("Gun", this.gun.serializeNBT(provider));
        return compound;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compound)
    {
        this.model = ItemStack.parseOptional(provider, compound.getCompound("Model"));
        this.gun = Gun.create(compound.getCompound("Gun"));
    }
}
