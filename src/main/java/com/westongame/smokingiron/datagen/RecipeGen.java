package com.westongame.smokingiron.datagen;

import com.westongame.smokingiron.Reference;
import com.westongame.smokingiron.crafting.WorkbenchIngredient;
import com.westongame.smokingiron.crafting.WorkbenchRecipeBuilder;
import com.westongame.smokingiron.init.ModBlocks;
import com.westongame.smokingiron.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import com.westongame.smokingiron.crafting.DyeItemRecipe;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class RecipeGen extends RecipeProvider
{
    public RecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer)
    {
        // Dye Item special recipe
        SpecialRecipeBuilder.special(DyeItemRecipe::new).save(consumer, Reference.MOD_ID + ":dye_item");

        // Gunsmith's Workbench — netherite gate (concept: INI/IBI/WWW)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WORKBENCH.get())
                .pattern("INI")
                .pattern("IBI")
                .pattern("WWW")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('N', Items.NETHERITE_INGOT)
                .define('B', Items.IRON_BLOCK)
                .define('W', ItemTags.PLANKS)
                .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(consumer);

        // Guns — Wild West (Smoking Iron)
        // Peacemaker — стартовый сайдарм
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.REVOLVER.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.INGOTS_IRON, 8))
                .addIngredient(WorkbenchIngredient.of(Items.COPPER_INGOT, 2))
                .addIngredient(WorkbenchIngredient.of(ItemTags.PLANKS, 2))
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .build(consumer);
        // Winchester — daily driver
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.LEVER_ACTION_RIFLE.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.INGOTS_IRON, 14))
                .addIngredient(WorkbenchIngredient.of(Items.COPPER_INGOT, 2))
                .addIngredient(WorkbenchIngredient.of(ItemTags.PLANKS, 4))
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .build(consumer);
        // Coach Gun — двустволка
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.DOUBLE_BARRELED_SHOTGUN.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.INGOTS_IRON, 16))
                .addIngredient(WorkbenchIngredient.of(Items.COPPER_INGOT, 2))
                .addIngredient(WorkbenchIngredient.of(ItemTags.PLANKS, 2))
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .build(consumer);
        // Sharps — endgame buffalo rifle (3 iron_block = 27 iron-eq)
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.HUNTING_RIFLE.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.INGOTS_IRON, 8))
                .addIngredient(WorkbenchIngredient.of(Items.IRON_BLOCK, 3))
                .addIngredient(WorkbenchIngredient.of(Items.COPPER_INGOT, 2))
                .addIngredient(WorkbenchIngredient.of(ItemTags.PLANKS, 3))
                .addCriterion("has_iron_block", has(Items.IRON_BLOCK))
                .build(consumer);

        // Ammo — concept: медь / золото / редстоун как активные расходники
        // .44-40 Winchester (Peacemaker + Winchester)
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.BASIC_BULLET.get(), 8)
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 4))
                .addIngredient(WorkbenchIngredient.of(Items.COPPER_INGOT, 1))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.GUNPOWDERS, 1))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.DUSTS_REDSTONE, 1))
                .addCriterion("has_copper_ingot", has(Items.COPPER_INGOT))
                .addCriterion("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .build(consumer);
        // .50-70 Sharps
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.ADVANCED_AMMO.get(), 8)
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 4))
                .addIngredient(WorkbenchIngredient.of(Items.GOLD_INGOT, 1))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.GUNPOWDERS, 1))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.DUSTS_REDSTONE, 1))
                .addCriterion("has_gold_ingot", has(Items.GOLD_INGOT))
                .addCriterion("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .build(consumer);
        // Buckshot (Coach Gun)
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.SHELL.get(), 8)
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 6))
                .addIngredient(WorkbenchIngredient.of(Items.COPPER_INGOT, 1))
                .addIngredient(WorkbenchIngredient.of(Items.GOLD_INGOT, 1))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.GUNPOWDERS, 1))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.DUSTS_REDSTONE, 1))
                .addCriterion("has_copper_ingot", has(Items.COPPER_INGOT))
                .addCriterion("has_gold_ingot", has(Items.GOLD_INGOT))
                .addCriterion("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .build(consumer);
        // Dynamite (concept: 4 sand + 4 gunpowder + 1 paper + 1 string)
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.GRENADE.get(), 1)
                .addIngredient(WorkbenchIngredient.of(ItemTags.SAND, 4))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.GUNPOWDERS, 4))
                .addIngredient(WorkbenchIngredient.of(Items.PAPER, 1))
                .addIngredient(WorkbenchIngredient.of(Items.STRING, 1))
                .addCriterion("has_gunpowder", has(Tags.Items.GUNPOWDERS))
                .build(consumer);

        // Scope Attachments
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.SHORT_SCOPE.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 2))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.GEMS_AMETHYST, 1))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.DUSTS_REDSTONE, 2))
                .addCriterion("has_iron_ingot", has(Tags.Items.NUGGETS_IRON))
                .addCriterion("has_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .addCriterion("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .build(consumer);
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.MEDIUM_SCOPE.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 4))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.GEMS_AMETHYST, 1))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.DUSTS_REDSTONE, 4))
                .addCriterion("has_iron_ingot", has(Tags.Items.NUGGETS_IRON))
                .addCriterion("has_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .addCriterion("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .build(consumer);
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.LONG_SCOPE.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 6))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.GEMS_AMETHYST, 2))
                .addIngredient(WorkbenchIngredient.of(Tags.Items.DYES_BLACK, 1))
                .addCriterion("has_iron_ingot", has(Tags.Items.NUGGETS_IRON))
                .addCriterion("has_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .addCriterion("has_black_dye", has(Tags.Items.DYES_BLACK))
                .build(consumer);

        // Stock Attachments
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.LIGHT_STOCK.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 6))
                .addIngredient(WorkbenchIngredient.of(Items.GRAY_WOOL, 1))
                .addCriterion("has_iron_ingot", has(Tags.Items.NUGGETS_IRON))
                .addCriterion("has_gray_wool", has(Items.GRAY_WOOL))
                .build(consumer);
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.TACTICAL_STOCK.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 8))
                .addIngredient(WorkbenchIngredient.of(Items.GRAY_WOOL, 1))
                .addCriterion("has_iron_ingot", has(Tags.Items.NUGGETS_IRON))
                .addCriterion("has_gray_wool", has(Items.GRAY_WOOL))
                .build(consumer);
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.WEIGHTED_STOCK.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 12))
                .addIngredient(WorkbenchIngredient.of(Items.GRAY_WOOL, 1))
                .addCriterion("has_iron_ingot", has(Tags.Items.NUGGETS_IRON))
                .addCriterion("has_gray_wool", has(Items.GRAY_WOOL))
                .build(consumer);

        // Under Barrel Attachments
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.LIGHT_GRIP.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 4))
                .addIngredient(WorkbenchIngredient.of(Items.GRAY_WOOL, 1))
                .addCriterion("has_iron_ingot", has(Tags.Items.NUGGETS_IRON))
                .addCriterion("has_gray_wool", has(Items.GRAY_WOOL))
                .build(consumer);
        WorkbenchRecipeBuilder.crafting(RecipeCategory.COMBAT, ModItems.SPECIALISED_GRIP.get())
                .addIngredient(WorkbenchIngredient.of(Tags.Items.NUGGETS_IRON, 8))
                .addIngredient(WorkbenchIngredient.of(Items.GRAY_WOOL, 1))
                .addCriterion("has_iron_ingot", has(Tags.Items.NUGGETS_IRON))
                .addCriterion("has_gray_wool", has(Items.GRAY_WOOL))
                .build(consumer);
    }
}
