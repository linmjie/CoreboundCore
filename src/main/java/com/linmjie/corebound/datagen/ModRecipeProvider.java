package com.linmjie.corebound.datagen;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.ModBlocks;
import com.linmjie.corebound.item.ModItems;
import com.linmjie.corebound.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.WOODEN_SHEARS.get())
                .pattern(" P ")
                .pattern("LIP")
                .pattern("IL ")
                .define('P', ItemTags.PLANKS)
                .define('L', ModTags.Items.NON_STRIPPED_LOGS_THAT_BURN)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SAW.get())
                .pattern(" C")
                .pattern("CI")
                .define('C', Items.CLAY_BALL)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SCISSORS.get())
                .pattern("C C")
                .pattern(" C ")
                .pattern("I I")
                .define('C', Items.CLAY_BALL)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.HAMMER.get())
                .pattern("CCC")
                .pattern("CIC")
                .pattern(" I ")
                .define('C', Items.CLAY_BALL)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SHARP_STICK.get())
                .pattern(" TR")
                .pattern(" IT")
                .pattern("I  ")
                .define('T', ModItems.TWIG.get())
                .define('R', ModTags.Items.ROCK_ADJACENT)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(ModItems.ROCK.get()), has(ModItems.ROCK.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.COBBLESTONE)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.ROCK)
                .unlockedBy(getHasName(ModItems.ROCK), has(ModItems.ROCK))
                .save(recipeOutput, modPrefix() + "cobblestone");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.INCOMPLETE_CRAFTING_TABLE)
                .pattern("##")
                .pattern("##")
                .define('#', ItemTags.PLANKS)
                .unlockedBy(getHasName(ModItems.TWIG), has(ModItems.TWIG))
                .save(recipeOutput);
        ninePacker(recipeOutput, ModItems.RAW_TIN.get(), ModBlocks.RAW_TIN_BLOCK.get(), "tin");
    }

public static final ShapedRecipe CUSTOM_CAMPFIRE = new ShapedRecipe(
    "campfire",
    CraftingBookCategory.BUILDING,
    new ShapedRecipePattern(2, 2,
        NonNullList.of(Ingredient.EMPTY, //First ingredient has to be empty??
            Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Tags.Items.RODS_WOODEN),
            Ingredient.of(ItemTags.LOGS), Ingredient.of(ItemTags.LOGS)
        ),
    Optional.empty()),
    new ItemStack(Items.CAMPFIRE)
);

    //HELPER METHODS FOR STANDARDIZED RECIPE TYPES

    //ninePacker with default RecipeCategory.MISC and default "item_block" name for packed block
    protected static void ninePacker(RecipeOutput pRecipeOutput,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName) {
        ninePacker(pRecipeOutput, RecipeCategory.MISC, pUnpacked, pPacked,
                pUnpackedName, pUnpackedName+"_block");
    }
    //ninePacker with specified RecipeCategory and default "item_block" name for packed block
    protected static void ninePacker(RecipeOutput pRecipeOutput, RecipeCategory recipeCategory,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName) {
        ninePacker(pRecipeOutput, recipeCategory, pUnpacked, pPacked,
                pUnpackedName, pUnpackedName+"_block");
    }
    //ninePacker with default RecipeCategory.MISC & specified ids for unpacked and packed items
    protected static void ninePacker(RecipeOutput pRecipeOutput,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName, String pPackedName){
        ninePacker( pRecipeOutput, RecipeCategory.MISC, pUnpacked, pPacked,
                pUnpackedName, pPackedName);
    }
    protected static void ninePacker(RecipeOutput pRecipeOutput, RecipeCategory recipeCategory,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName, String pPackedName){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pPacked)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', pUnpacked)
                .unlockedBy(getHasName(pUnpacked), has(pUnpacked))
                .save(pRecipeOutput, modPrefix() + pPackedName+"_from_"+pUnpackedName);
        ShapelessRecipeBuilder.shapeless(recipeCategory, pUnpacked, 9)
                .requires(pPacked)
                .unlockedBy(getHasName(pPacked), has(pPacked))
                .save(pRecipeOutput, modPrefix() + pUnpackedName+"_from_"+pPackedName);
    }

    protected static void fourPacker(RecipeOutput pRecipeOutput,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName) {
        fourPacker(pRecipeOutput, RecipeCategory.MISC, pUnpacked, pPacked,
                pUnpackedName, pUnpackedName+"_block");
    }
    //ninePacker with specified RecipeCategory and default "item_block" name for packed block
    protected static void fourPacker(RecipeOutput pRecipeOutput, RecipeCategory recipeCategory,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName) {
        fourPacker(pRecipeOutput, recipeCategory, pUnpacked, pPacked,
                pUnpackedName, pUnpackedName+"_block");
    }
    //ninePacker with default RecipeCategory.MISC & specified ids for unpacked and packed items
    protected static void fourPacker(RecipeOutput pRecipeOutput,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName, String pPackedName){
        fourPacker( pRecipeOutput, RecipeCategory.MISC, pUnpacked, pPacked,
                pUnpackedName, pPackedName);
    }
    protected static void fourPacker(RecipeOutput pRecipeOutput, RecipeCategory recipeCategory,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName, String pPackedName){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pPacked)
                .pattern("##")
                .pattern("##")
                .define('#', pUnpacked)
                .unlockedBy(getHasName(pUnpacked), has(pUnpacked))
                .save(pRecipeOutput, modPrefix() + pPackedName + "_from_" + pUnpackedName);
        ShapelessRecipeBuilder.shapeless(recipeCategory, pUnpacked, 4)
                .requires(pPacked)
                .unlockedBy(getHasName(pPacked), has(pPacked))
                .save(pRecipeOutput, modPrefix() + pUnpackedName + "_from_" + pPackedName);
    }

    protected static String modPrefix(){
        return Corebound.MODID + ":";
    }
}
