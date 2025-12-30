package com.linmjie.corebound.datagen;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.ModBlocks;
import com.linmjie.corebound.item.ModItems;
import com.linmjie.corebound.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        crudeSwordRecipe(recipeOutput, ItemTags.PLANKS, Items.WOODEN_SWORD, "wooden", ModItems.TWIG);
        crudeShovelRecipe(recipeOutput, ItemTags.PLANKS, Items.WOODEN_SHOVEL, "wooden", ModItems.TWIG);
        crudePickaxeRecipe(recipeOutput, ItemTags.PLANKS, Items.WOODEN_PICKAXE, "wooden", ModItems.TWIG);
        crudeAxeRecipe(recipeOutput, ItemTags.PLANKS, Items.WOODEN_AXE, "wooden", ModItems.TWIG);
        crudeHoeRecipe(recipeOutput, ItemTags.PLANKS, Items.WOODEN_HOE, "wooden", ModItems.TWIG);

        crudeSwordRecipe(recipeOutput, ItemTags.STONE_TOOL_MATERIALS, Items.STONE_SWORD, "stone", ModItems.ROCK);
        crudeShovelRecipe(recipeOutput, ItemTags.STONE_TOOL_MATERIALS, Items.STONE_SHOVEL, "stone", ModItems.ROCK);
        crudePickaxeRecipe(recipeOutput, ItemTags.STONE_TOOL_MATERIALS, Items.STONE_PICKAXE, "stone", ModItems.ROCK);
        crudeAxeRecipe(recipeOutput, ItemTags.STONE_TOOL_MATERIALS, Items.STONE_AXE, "stone", ModItems.ROCK);
        crudeHoeRecipe(recipeOutput, ItemTags.STONE_TOOL_MATERIALS, Items.STONE_HOE, "stone", ModItems.ROCK);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.WOODEN_SHEARS.get())
                .pattern(" P ")
                .pattern("LIP")
                .pattern("IL ")
                .define('P', ItemTags.PLANKS)
                .define('L', ModTags.Items.NON_STRIPPED_LOGS_THAT_BURN)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.UNFIRED_SAW.get())
                .pattern(" C")
                .pattern("CI")
                .define('C', Items.CLAY_BALL)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.UNFIRED_SCISSORS.get())
                .pattern("C C")
                .pattern(" C ")
                .pattern("I I")
                .define('C', Items.CLAY_BALL)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.UNFIRED_HAMMER.get())
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

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.CAMPFIRE)
                .pattern("II")
                .pattern("LL")
                .define('I', Tags.Items.RODS_WOODEN)
                .define('L', ItemTags.LOGS_THAT_BURN)
                .unlockedBy(getHasName(ModItems.TWIG), has(ModItems.TWIG))
                .save(recipeOutput, modPrefix() + "campfire");

        ninePacker(recipeOutput, ModItems.RAW_TIN.get(), ModBlocks.RAW_TIN_BLOCK.get(), "tin");

        coreboundToolsFiring(recipeOutput, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, 200);
        coreboundToolsFiring(recipeOutput, "blasting", RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, 1200);
    }


    //HELPER METHODS FOR STANDARDIZED RECIPE TYPES

    //COREBOUND CRAFTING TABLE TOOLS 
    protected static <T extends AbstractCookingRecipe> void coreboundToolsFiring(RecipeOutput recipeOutput, String cookingMethod,
                            RecipeSerializer<T> cookingSerializer, AbstractCookingRecipe.Factory<T> recipeFactory, int cookingTime) 
    {
        simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.UNFIRED_SAW, ModItems.SAW, 0.2F);
        simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.UNFIRED_SCISSORS, ModItems.SCISSORS, 0.2F);
        simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.UNFIRED_HAMMER, ModItems.HAMMER, 0.2F);
    }


    //PACKERS
    //ninePacker with default RecipeCategory.MISC and default "item_block" name for packed block
    protected static void ninePacker(RecipeOutput pRecipeOutput,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName) {
        ninePacker(pRecipeOutput, RecipeCategory.MISC, pUnpacked, pPacked,
                pUnpackedName, pUnpackedName + "_block");
    }
    //ninePacker with specified RecipeCategory and default "item_block" name for packed block
    protected static void ninePacker(RecipeOutput pRecipeOutput, RecipeCategory recipeCategory,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName) {
        ninePacker(pRecipeOutput, recipeCategory, pUnpacked, pPacked,
                pUnpackedName, pUnpackedName + "_block");
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
                .save(pRecipeOutput, modPrefix() + pPackedName + "_from_" + pUnpackedName);
        ShapelessRecipeBuilder.shapeless(recipeCategory, pUnpacked, 9)
                .requires(pPacked)
                .unlockedBy(getHasName(pPacked), has(pPacked))
                .save(pRecipeOutput, modPrefix() + pUnpackedName + "_from_" + pPackedName);
    }

    protected static void fourPacker(RecipeOutput pRecipeOutput,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName) {
        fourPacker(pRecipeOutput, RecipeCategory.MISC, pUnpacked, pPacked,
                pUnpackedName, pUnpackedName + "_block");
    }
    //ninePacker with specified RecipeCategory and default "item_block" name for packed block
    protected static void fourPacker(RecipeOutput pRecipeOutput, RecipeCategory recipeCategory,
                                     ItemLike pUnpacked, ItemLike pPacked,
                                     String pUnpackedName) {
        fourPacker(pRecipeOutput, recipeCategory, pUnpacked, pPacked,
                pUnpackedName, pUnpackedName + "_block");
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



    //CRUDE TOOLS
    protected static void crudeSwordRecipe (RecipeOutput pRecipeOutput,
                                       TagKey<Item> pMaterial, ItemLike tool,
                                       String name, ItemLike unlock)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, tool)
                .pattern(" # ")
                .pattern(" # ")
                .pattern("TIT")
                .define('#', pMaterial)
                .define('I', Items.STICK)
                .define('T', ModItems.TWIG.get())
                .unlockedBy(getHasName(unlock), has(pMaterial))
                .save(pRecipeOutput, modPrefix() + name + "_sword");
    }
    protected static void crudeShovelRecipe (RecipeOutput pRecipeOutput,
                                       TagKey<Item> pMaterial, ItemLike tool,
                                       String name, ItemLike unlock)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, tool)
                .pattern("T#T")
                .pattern(" I ")
                .pattern(" I ")
                .define('#', pMaterial)
                .define('I', Items.STICK)
                .define('T', ModItems.TWIG.get())
                .unlockedBy(getHasName(unlock), has(pMaterial))
                .save(pRecipeOutput, modPrefix() + name + "_shovel");
    }
    protected static void crudePickaxeRecipe (RecipeOutput pRecipeOutput,
                                       TagKey<Item> pMaterial, ItemLike tool,
                                       String name, ItemLike unlock)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, tool)
                .pattern("###")
                .pattern("TIT")
                .pattern(" I ")
                .define('#', pMaterial)
                .define('I', Items.STICK)
                .define('T', ModItems.TWIG.get())
                .unlockedBy(getHasName(unlock), has(pMaterial))
                .save(pRecipeOutput, modPrefix() + name + "_pickaxe");
    }
    protected static void crudeAxeRecipe (RecipeOutput pRecipeOutput,
                                      TagKey<Item> pMaterial, ItemLike tool,
                                      String name, ItemLike unlock)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, tool)
                .pattern("T##")
                .pattern("TI#")
                .pattern(" I ")
                .define('#', pMaterial)
                .define('I', Items.STICK)
                .define('T', ModItems.TWIG.get())
                .unlockedBy(getHasName(unlock), has(pMaterial))
                .save(pRecipeOutput, modPrefix() + name + "_axe");
    }
    protected static void crudeHoeRecipe (RecipeOutput pRecipeOutput,
                                       TagKey<Item> pMaterial, ItemLike tool,
                                       String name, ItemLike unlock)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, tool)
                .pattern("##T")
                .pattern("TI ")
                .pattern(" I ")
                .define('#', pMaterial)
                .define('I', Items.STICK)
                .define('T', ModItems.TWIG.get())
                .unlockedBy(getHasName(unlock), has(pMaterial))
                .save(pRecipeOutput, modPrefix() + name + "_hoe");
    }



    //STANDARD TOOLS
    protected static void swordRecipe (RecipeOutput pRecipeOutput,
                                       ItemLike pMaterial, ItemLike tool)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, tool)
                .pattern("#")
                .pattern("#")
                .pattern("I")
                .define('#', pMaterial)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pRecipeOutput);
    }
    protected static void shovelRecipe (RecipeOutput pRecipeOutput,
                                       ItemLike pMaterial, ItemLike tool)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, tool)
                .pattern("#")
                .pattern("I")
                .pattern("I")
                .define('#', pMaterial)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pRecipeOutput);
    }
    protected static void pickaxeRecipe (RecipeOutput pRecipeOutput,
                                       ItemLike pMaterial, ItemLike tool)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, tool)
                .pattern("###")
                .pattern(" I ")
                .pattern(" I ")
                .define('#', pMaterial)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pRecipeOutput);
    }
    protected static void axeRecipe (RecipeOutput pRecipeOutput,
                                      ItemLike pMaterial, ItemLike tool)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, tool)
                .pattern("##")
                .pattern("I#")
                .pattern("I ")
                .define('#', pMaterial)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pRecipeOutput);
    }
    protected static void hoeRecipe (RecipeOutput pRecipeOutput,
                                       ItemLike pMaterial, ItemLike tool)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, tool)
                .pattern("##")
                .pattern(" I")
                .pattern(" I")
                .define('#', pMaterial)
                .define('I', Items.STICK)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pRecipeOutput);
    }
    protected static void helmetRecipe(RecipeOutput pRecipeOutput,
                                       ItemLike pMaterial, ItemLike armor)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, armor)
                .pattern("###")
                .pattern("# #")
                .define('#', pMaterial)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pRecipeOutput);
    }
    protected static void chestplateRecipe(RecipeOutput pRecipeOutput,
                                       ItemLike pMaterial, ItemLike armor)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, armor)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', pMaterial)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pRecipeOutput);
    }
    protected static void leggingsRecipe(RecipeOutput pRecipeOutput,
                                       ItemLike pMaterial, ItemLike armor)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, armor)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', pMaterial)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pRecipeOutput);
    }
    protected static void bootsRecipe(RecipeOutput pRecipeOutput,
                                       ItemLike pMaterial, ItemLike armor)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, armor)
                .pattern("# #")
                .pattern("# #")
                .define('#', pMaterial)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pRecipeOutput);
    }


    protected static String modPrefix(){
        return Corebound.MODID + ":";
    }
}
