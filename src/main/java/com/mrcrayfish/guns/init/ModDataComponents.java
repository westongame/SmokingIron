package com.mrcrayfish.guns.init;

import com.mojang.serialization.Codec;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.common.GunAttachments;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents
{
    public static final DeferredRegister.DataComponents REGISTER =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Reference.MOD_ID);

    // Ammo count in magazine
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> AMMO_COUNT =
            REGISTER.registerComponentType("ammo_count", builder -> builder
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT));

    // Infinite ammo flag
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> IGNORE_AMMO =
            REGISTER.registerComponentType("ignore_ammo", builder -> builder
                    .persistent(Unit.CODEC)
                    .networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));

    // Gun paint color
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> GUN_COLOR =
            REGISTER.registerComponentType("gun_color", builder -> builder
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT));

    // Additional damage modifier
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> ADDITIONAL_DAMAGE =
            REGISTER.registerComponentType("additional_damage", builder -> builder
                    .persistent(Codec.FLOAT)
                    .networkSynchronized(ByteBufCodecs.FLOAT));

    // Weapon attachments (scope, barrel, stock, under_barrel)
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GunAttachments>> ATTACHMENTS =
            REGISTER.registerComponentType("attachments", builder -> builder
                    .persistent(GunAttachments.CODEC)
                    .networkSynchronized(GunAttachments.STREAM_CODEC));

    // Gun properties (complex data, CompoundTag temporarily until full Codec migration)
    // TODO: Replace with typed Codec<Gun> record
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> GUN_DATA =
            REGISTER.registerComponentType("gun_data", builder -> builder
                    .persistent(CompoundTag.CODEC)
                    .networkSynchronized(ByteBufCodecs.COMPOUND_TAG));

    // Reticle color for scopes
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> RETICLE_COLOR =
            REGISTER.registerComponentType("reticle_color", builder -> builder
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT));
}
