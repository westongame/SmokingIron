package com.westongame.smokingiron.crafting;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Author: MrCrayfish
 */
public class WorkbenchRecipeSerializer implements RecipeSerializer<WorkbenchRecipe>
{
    public static final MapCodec<WorkbenchRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ItemStack.CODEC.fieldOf("result").forGetter(WorkbenchRecipe::getItem),
            WorkbenchIngredient.CODEC.codec().listOf().fieldOf("materials").forGetter(r -> r.getMaterials())
        ).apply(instance, (result, materials) -> new WorkbenchRecipe(result, ImmutableList.copyOf(materials)))
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> STREAM_CODEC = new StreamCodec<>()
    {
        @Override
        public WorkbenchRecipe decode(RegistryFriendlyByteBuf buffer)
        {
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            ImmutableList.Builder<WorkbenchIngredient> builder = ImmutableList.builder();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
            {
                builder.add(WorkbenchIngredient.fromNetwork(buffer));
            }
            return new WorkbenchRecipe(result, builder.build());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, WorkbenchRecipe recipe)
        {
            ItemStack.STREAM_CODEC.encode(buffer, recipe.getItem());
            buffer.writeVarInt(recipe.getMaterials().size());
            for (WorkbenchIngredient ingredient : recipe.getMaterials())
            {
                ingredient.toNetwork(buffer);
            }
        }
    };

    @Override
    public MapCodec<WorkbenchRecipe> codec()
    {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> streamCodec()
    {
        return STREAM_CODEC;
    }
}
