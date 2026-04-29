package com.westongame.smokingiron.crafting;

import com.google.common.collect.ImmutableList;
import com.westongame.smokingiron.init.ModRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ocelot
 */
public class WorkbenchRecipeBuilder
{
    @Nullable
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final List<WorkbenchIngredient> ingredients;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    private WorkbenchRecipeBuilder(@Nullable RecipeCategory category, ItemLike item, int count)
    {
        this.category = category;
        this.result = item.asItem();
        this.count = count;
        this.ingredients = new ArrayList<>();
    }

    public static WorkbenchRecipeBuilder crafting(ItemLike item)
    {
        return new WorkbenchRecipeBuilder(null, item, 1);
    }

    public static WorkbenchRecipeBuilder crafting(ItemLike item, int count)
    {
        return new WorkbenchRecipeBuilder(null, item, count);
    }

    public static WorkbenchRecipeBuilder crafting(@Nullable RecipeCategory category, ItemLike item)
    {
        return new WorkbenchRecipeBuilder(category, item, 1);
    }

    public static WorkbenchRecipeBuilder crafting(@Nullable RecipeCategory category, ItemLike item, int count)
    {
        return new WorkbenchRecipeBuilder(category, item, count);
    }

    public WorkbenchRecipeBuilder addIngredient(ItemLike item, int count)
    {
        this.ingredients.add(WorkbenchIngredient.of(item, count));
        return this;
    }

    public WorkbenchRecipeBuilder addIngredient(WorkbenchIngredient ingredient)
    {
        this.ingredients.add(ingredient);
        return this;
    }

    public WorkbenchRecipeBuilder addCriterion(String name, Criterion<?> criterion)
    {
        this.criteria.put(name, criterion);
        return this;
    }

    public void build(RecipeOutput output)
    {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
        this.build(output, resourcelocation);
    }

    public void build(RecipeOutput output, ResourceLocation id)
    {
        this.validate(id);

        Advancement.Builder advancementBuilder = Advancement.Builder.advancement();
        this.criteria.forEach(advancementBuilder::addCriterion);
        advancementBuilder.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id));
        advancementBuilder.rewards(AdvancementRewards.Builder.recipe(id));
        advancementBuilder.requirements(AdvancementRequirements.Strategy.OR);

        ItemStack resultStack = new ItemStack(this.result, this.count);
        WorkbenchRecipe recipe = new WorkbenchRecipe(resultStack, ImmutableList.copyOf(this.ingredients));

        ResourceLocation advancementId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "recipes/" + (this.category != null ? this.category.getFolderName() + "/" : "") + id.getPath());
        output.accept(id, recipe, advancementBuilder.build(advancementId));
    }

    /**
     * Makes sure that this recipe is valid and obtainable.
     */
    private void validate(ResourceLocation id)
    {
        if(this.criteria.isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }
}
