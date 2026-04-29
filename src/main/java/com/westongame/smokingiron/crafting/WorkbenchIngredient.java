package com.westongame.smokingiron.crafting;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

/**
 * Author: MrCrayfish
 *
 * A wrapper around Ingredient that adds a count field.
 * In 1.21.1, Ingredient is final and cannot be extended.
 */
public class WorkbenchIngredient
{
    public static final MapCodec<WorkbenchIngredient> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(WorkbenchIngredient::getIngredient),
            Codec.INT.optionalFieldOf("count", 1).forGetter(WorkbenchIngredient::getCount)
        ).apply(instance, WorkbenchIngredient::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WorkbenchIngredient> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC, WorkbenchIngredient::getIngredient,
        ByteBufCodecs.VAR_INT, WorkbenchIngredient::getCount,
        WorkbenchIngredient::new
    );

    private final Ingredient ingredient;
    private final int count;

    public WorkbenchIngredient(Ingredient ingredient, int count)
    {
        this.ingredient = ingredient;
        this.count = count;
    }

    public Ingredient getIngredient()
    {
        return this.ingredient;
    }

    public int getCount()
    {
        return this.count;
    }

    /**
     * Tests if the given stack matches this ingredient (ignoring count).
     */
    public boolean test(ItemStack stack)
    {
        return this.ingredient.test(stack);
    }

    /**
     * Returns the matching item stacks for this ingredient.
     */
    public ItemStack[] getItems()
    {
        return this.ingredient.getItems();
    }

    public static WorkbenchIngredient of(ItemLike provider, int count)
    {
        return new WorkbenchIngredient(Ingredient.of(provider), count);
    }

    public static WorkbenchIngredient of(ItemStack stack, int count)
    {
        return new WorkbenchIngredient(Ingredient.of(stack.getItem()), count);
    }

    public static WorkbenchIngredient of(TagKey<Item> tag, int count)
    {
        return new WorkbenchIngredient(Ingredient.of(tag), count);
    }

    public static WorkbenchIngredient of(ResourceLocation id, int count)
    {
        // For data generation with unknown items - creates an empty ingredient
        // The actual item resolution happens at recipe load time
        return new WorkbenchIngredient(Ingredient.of(), count);
    }

    /**
     * Serialize to JSON for data generation.
     */
    public JsonObject toJson()
    {
        JsonObject object = new JsonObject();
        // Serialize the ingredient using Ingredient's own JSON serialization
        object.add("ingredient", Ingredient.CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE, this.ingredient).getOrThrow());
        object.addProperty("count", this.count);
        return object;
    }

    /**
     * Deserialize from JSON.
     */
    public static WorkbenchIngredient fromJson(JsonObject object)
    {
        Ingredient ingredient;
        if (object.has("ingredient"))
        {
            ingredient = Ingredient.CODEC.parse(com.mojang.serialization.JsonOps.INSTANCE, object.get("ingredient")).getOrThrow();
        }
        else
        {
            // Legacy format: the object itself is the ingredient (has "item" or "tag" key)
            ingredient = Ingredient.CODEC.parse(com.mojang.serialization.JsonOps.INSTANCE, object).getOrThrow();
        }
        int count = object.has("count") ? object.get("count").getAsInt() : 1;
        return new WorkbenchIngredient(ingredient, count);
    }

    public void toNetwork(RegistryFriendlyByteBuf buffer)
    {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, this.ingredient);
        buffer.writeVarInt(this.count);
    }

    public static WorkbenchIngredient fromNetwork(RegistryFriendlyByteBuf buffer)
    {
        Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        int count = buffer.readVarInt();
        return new WorkbenchIngredient(ingredient, count);
    }
}
