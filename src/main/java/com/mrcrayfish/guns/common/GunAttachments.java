package com.mrcrayfish.guns.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Typed data component for gun attachments, replacing NBT compound "Attachments".
 */
public record GunAttachments(ItemStack scope, ItemStack barrel, ItemStack stock, ItemStack underBarrel)
{
    public static final GunAttachments EMPTY = new GunAttachments(
            ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);

    public static final Codec<GunAttachments> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.OPTIONAL_CODEC.fieldOf("scope").forGetter(GunAttachments::scope),
            ItemStack.OPTIONAL_CODEC.fieldOf("barrel").forGetter(GunAttachments::barrel),
            ItemStack.OPTIONAL_CODEC.fieldOf("stock").forGetter(GunAttachments::stock),
            ItemStack.OPTIONAL_CODEC.fieldOf("under_barrel").forGetter(GunAttachments::underBarrel)
    ).apply(instance, GunAttachments::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GunAttachments> STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.OPTIONAL_STREAM_CODEC, GunAttachments::scope,
                    ItemStack.OPTIONAL_STREAM_CODEC, GunAttachments::barrel,
                    ItemStack.OPTIONAL_STREAM_CODEC, GunAttachments::stock,
                    ItemStack.OPTIONAL_STREAM_CODEC, GunAttachments::underBarrel,
                    GunAttachments::new
            );

    /**
     * Gets the attachment for the given type.
     */
    public ItemStack getAttachment(IAttachment.Type type)
    {
        return switch(type)
        {
            case SCOPE -> scope;
            case BARREL -> barrel;
            case STOCK -> stock;
            case UNDER_BARREL -> underBarrel;
        };
    }

    /**
     * Returns a new GunAttachments with the given type set.
     */
    public GunAttachments withAttachment(IAttachment.Type type, ItemStack stack)
    {
        return switch(type)
        {
            case SCOPE -> new GunAttachments(stack, barrel, stock, underBarrel);
            case BARREL -> new GunAttachments(scope, stack, stock, underBarrel);
            case STOCK -> new GunAttachments(scope, barrel, stack, underBarrel);
            case UNDER_BARREL -> new GunAttachments(scope, barrel, stock, stack);
        };
    }

    /**
     * Checks if the given attachment type is equipped (non-empty).
     */
    public boolean hasAttachment(IAttachment.Type type)
    {
        return !getAttachment(type).isEmpty();
    }

    /**
     * Mutable builder for constructing GunAttachments.
     */
    public static class Builder
    {
        private ItemStack scope = ItemStack.EMPTY;
        private ItemStack barrel = ItemStack.EMPTY;
        private ItemStack stock = ItemStack.EMPTY;
        private ItemStack underBarrel = ItemStack.EMPTY;

        public Builder set(IAttachment.Type type, ItemStack stack)
        {
            switch(type)
            {
                case SCOPE -> this.scope = stack;
                case BARREL -> this.barrel = stack;
                case STOCK -> this.stock = stack;
                case UNDER_BARREL -> this.underBarrel = stack;
            }
            return this;
        }

        public GunAttachments build()
        {
            return new GunAttachments(scope, barrel, stock, underBarrel);
        }
    }
}
