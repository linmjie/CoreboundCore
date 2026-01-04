package com.linmjie.corebound.util;

import com.linmjie.corebound.Corebound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> DROPS_ROCKS = createTag("drops_rocks");

        private static net.minecraft.tags.TagKey<Block> createTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Corebound.MODID, name));
        }

        //COMMON

        public static final TagKey<Block> RAW_TIN_STORAGE = createCommonTag("storage_blocks/raw_tin");

        private static net.minecraft.tags.TagKey<Block> createCommonTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class Items {
        public static final TagKey<Item> ROCKS_REPLACE = createTag("rocks_replace");
        public static final TagKey<Item> NON_STRIPPED_LOGS_THAT_BURN = createTag("non_stripped_logs_that_burn");
        public static final TagKey<Item> STRIPPED_LOGS_THAT_BURN = createTag("stripped_logs_that_burn");
        public static final TagKey<Item> ROCK_ADJACENT = createTag("rock_adjacent");
        public static final TagKey<Item> WOODEN_TOOLS = createTag("wooden_tools");
        public static final TagKey<Item> STONE_TOOLS = createTag("stone_tools");
        public static final TagKey<Item> UNFIRED_CRAFTING_TOOLS = createTag("unfired_crafting_tools");
        public static final TagKey<Item> CRAFTING_TOOLS = createTag("crafting_tools");

        private static net.minecraft.tags.TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Corebound.MODID, name));
        }

        //COMMON

        public static final TagKey<Item> RAW_MATERIALS_TIN = createCommonTag("raw_materials/tin");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_TIN = createCommonTag("storage_blocks/raw_tin");

        private static net.minecraft.tags.TagKey<Item> createCommonTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }
}
