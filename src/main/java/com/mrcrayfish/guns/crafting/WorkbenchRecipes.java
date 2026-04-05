package com.mrcrayfish.guns.crafting;

import com.mrcrayfish.guns.init.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class WorkbenchRecipes
{
    public static boolean isEmpty(Level world)
    {
        return world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.WORKBENCH.get()).isEmpty();
    }

    public static NonNullList<WorkbenchRecipe> getAll(Level world)
    {
        List<RecipeHolder<WorkbenchRecipe>> holders = world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.WORKBENCH.get());
        NonNullList<WorkbenchRecipe> result = NonNullList.create();
        for (RecipeHolder<WorkbenchRecipe> holder : holders)
        {
            result.add(holder.value());
        }
        return result;
    }

    @Nullable
    public static WorkbenchRecipe getRecipeById(Level world, ResourceLocation id)
    {
        List<RecipeHolder<WorkbenchRecipe>> holders = world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.WORKBENCH.get());
        for (RecipeHolder<WorkbenchRecipe> holder : holders)
        {
            if (holder.id().equals(id))
            {
                return holder.value();
            }
        }
        return null;
    }
}
