package com.linmjie.corebound.datagen;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.ModBlocks;
import com.linmjie.corebound.item.ModItems;
import com.linmjie.corebound.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Corebound.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.NON_STRIPPED_LOGS_THAT_BURN)
                .add(Items.OAK_LOG)
                .add(Items.ACACIA_LOG)
                .add(Items.BIRCH_LOG)
                .add(Items.CHERRY_LOG)
                .add(Items.JUNGLE_LOG)
                .add(Items.DARK_OAK_LOG)
                .add(Items.MANGROVE_LOG)
                .add(Items.CHERRY_LOG)
                .add(Items.SPRUCE_LOG)
                .add(Items.OAK_WOOD)
                .add(Items.ACACIA_WOOD)
                .add(Items.BIRCH_WOOD)
                .add(Items.CHERRY_WOOD)
                .add(Items.JUNGLE_WOOD)
                .add(Items.DARK_OAK_WOOD)
                .add(Items.MANGROVE_WOOD)
                .add(Items.CHERRY_WOOD)
                .add(Items.SPRUCE_WOOD);
        tag(ModTags.Items.STRIPPED_LOGS_THAT_BURN)
                .add(Items.STRIPPED_OAK_LOG)
                .add(Items.STRIPPED_ACACIA_LOG)
                .add(Items.STRIPPED_BIRCH_LOG)
                .add(Items.STRIPPED_CHERRY_LOG)
                .add(Items.STRIPPED_JUNGLE_LOG)
                .add(Items.STRIPPED_DARK_OAK_LOG)
                .add(Items.STRIPPED_MANGROVE_LOG)
                .add(Items.STRIPPED_CHERRY_LOG)
                .add(Items.STRIPPED_SPRUCE_LOG);
        tag(ModTags.Items.ROCK_ADJACENT)
                .add(Items.FLINT)
                .add(ModItems.ROCK.get());
        tag(ModTags.Items.ROCKS_REPLACE)
                .add(Items.COBBLESTONE)
                .add(Items.COBBLED_DEEPSLATE)
                .add(Items.STONE)
                .add(Items.DEEPSLATE)
                .add(Items.BLACKSTONE);
        tag(ModTags.Items.WOODEN_TOOLS)
                .add(Items.WOODEN_HOE)
                .add(Items.WOODEN_SHOVEL)
                .add(Items.WOODEN_PICKAXE)
                .add(Items.WOODEN_AXE)
                .add(Items.WOODEN_SWORD)
                .add(ModItems.WOODEN_SHEARS.get())
                .add(ModItems.SHARP_STICK.get());
        tag(ModTags.Items.STONE_TOOLS)
                .add(Items.STONE_HOE)
                .add(Items.STONE_SHOVEL)
                .add(Items.STONE_PICKAXE)
                .add(Items.STONE_AXE)
                .add(Items.STONE_SWORD);

        tag(Tags.Items.RAW_MATERIALS)
                .add(ModItems.RAW_TIN.get());
        tag(ModTags.Items.RAW_MATERIALS_TIN)
                .add(ModItems.RAW_TIN.get());
        tag(ModTags.Items.STORAGE_BLOCKS_RAW_TIN)
                .add(ModBlocks.RAW_TIN_BLOCK.asItem());
        tag(Tags.Items.STORAGE_BLOCKS)
                .add(ModBlocks.RAW_TIN_BLOCK.asItem());
    }
}
