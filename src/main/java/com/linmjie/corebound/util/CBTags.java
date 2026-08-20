package com.linmjie.corebound.util;

import com.linmjie.corebound.Corebound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CBTags {
    public static class Blocks {
        public static final TagKey<Block>

            DROPS_ROCKS = createTag("drops_rocks"),
            INCORRECT_FOR_BRONZE_TOOL = createTag("incorrect_for_bronze_tool"),

            //COMMON

            TIN_ORES = createCommonTag("ores/tin"),

            RAW_TIN_STORAGE = createCommonTag("storage_blocks/raw_tin"),

            STORAGE_BLOCKS_TIN = createCommonTag("storage_blocks/tin"),
            STORAGE_BLOCKS_BRONZE = createCommonTag("storage_blocks/bronze"),
            STORAGE_BLOCKS_STEEL = createCommonTag("storage_blocks/steel");

        private static net.minecraft.tags.TagKey<Block> createTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Corebound.MODID, name));
        }

        private static net.minecraft.tags.TagKey<Block> createCommonTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class Items {
        public static final TagKey<Item>

            ROCKS_REPLACE = createTag("rocks_replace"),
            NON_STRIPPED_LOGS_THAT_BURN = createTag("non_stripped_logs_that_burn"),
            STRIPPED_LOGS_THAT_BURN = createTag("stripped_logs_that_burn"),
            ROCK_ADJACENT = createTag("rock_adjacent"),
            WOODEN_TOOLS = createTag("wooden_tools"),
            STONE_TOOLS = createTag("stone_tools"),
            UNFIRED_CRAFTING_TOOLS = createTag("unfired_crafting_tools"),
            CRAFTING_TOOLS = createTag("crafting_tools"),

            //COMMON

            RAW_MATERIALS_TIN = createCommonTag("raw_materials/tin"),

            INGOTS_TIN = createCommonTag("ingots/tin"),
            INGOTS_BRONZE = createCommonTag("ingots/bronze"),
            INGOTS_STEEL = createCommonTag("ingots/steel"),
            INGOTS_BRASS = createCommonTag("ingots/brass"),

            NUGGETS_TIN = createCommonTag("nuggets/tin"),
            NUGGETS_BRONZE = createCommonTag("nuggets/bronze"),
            NUGGETS_STEEL = createCommonTag("nuggets/steel"),

            PLATES_STEEL = createCommonTag("plates/steel"),

            STORAGE_BLOCKS_RAW_TIN = createCommonTag("storage_blocks/raw_tin"),

            STORAGE_BLOCKS_TIN = createCommonTag("storage_blocks/tin"),
            STORAGE_BLOCKS_BRONZE = createCommonTag("storage_blocks/bronze"),
            STORAGE_BLOCKS_STEEL = createCommonTag("storage_blocks/steel");

        private static net.minecraft.tags.TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Corebound.MODID, name));
        }

        private static net.minecraft.tags.TagKey<Item> createCommonTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }
}
