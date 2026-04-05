package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Author: MrCrayfish
 */
public class ModRecipeTypes
{
    public static final DeferredRegister<RecipeType<?>> REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<WorkbenchRecipe>> WORKBENCH = create("workbench");

    @SuppressWarnings("unchecked")
    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> create(String name)
    {
        return (DeferredHolder<RecipeType<?>, RecipeType<T>>) (DeferredHolder<?, ?>) REGISTER.register(name, () -> new RecipeType<T>()
        {
            @Override
            public String toString()
            {
                return name;
            }
        });
    }
}
