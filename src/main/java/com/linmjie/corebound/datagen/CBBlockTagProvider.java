package com.linmjie.corebound.datagen;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.CBBlocks;
import com.linmjie.corebound.util.CBTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class CBBlockTagProvider extends BlockTagsProvider {
    public CBBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Corebound.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(CBBlocks.INCOMPLETE_CRAFTING_TABLE.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CBBlocks.RAW_TIN_BLOCK.get())
                .add(CBBlocks.TIN_ORE.get())
                .add(CBBlocks.DEEPSLATE_TIN_ORE.get())
                .add(CBBlocks.DARK_PORTAL.get());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(CBBlocks.RAW_TIN_BLOCK.get())
                .add(CBBlocks.TIN_ORE.get())
                .add(CBBlocks.DEEPSLATE_TIN_ORE.get());
        tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL)
                .add(CBBlocks.RAW_TIN_BLOCK.get())
                .add(CBBlocks.TIN_ORE.get())
                .add(CBBlocks.DEEPSLATE_TIN_ORE.get());
        tag(BlockTags.INCORRECT_FOR_GOLD_TOOL)
                .add(CBBlocks.RAW_TIN_BLOCK.get());
        tag(CBTags.Blocks.INCORRECT_FOR_BRONZE_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .addTag(BlockTags.DIAMOND_ORES);

        tag(CBTags.Blocks.DROPS_ROCKS)
                .add(Blocks.COBBLESTONE)
                .add(Blocks.COBBLED_DEEPSLATE)
                .add(Blocks.STONE)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.BLACKSTONE);

        // COMMON

        tag(Tags.Blocks.ORES)
                .addTag(CBTags.Blocks.TIN_ORES);
        tag(Tags.Blocks.ORES_IN_GROUND_STONE)
                .add(CBBlocks.TIN_ORE.get());
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
                .add(CBBlocks.DEEPSLATE_TIN_ORE.get());
        tag(CBTags.Blocks.TIN_ORES)
                .add(CBBlocks.TIN_ORE.get())
                .add(CBBlocks.DEEPSLATE_TIN_ORE.get());

        tag(Tags.Blocks.STORAGE_BLOCKS)
                .add(CBBlocks.RAW_TIN_BLOCK.get())
                .add(CBBlocks.TIN_BLOCK.get())
                .add(CBBlocks.BRONZE_BLOCK.get());
        tag(CBTags.Blocks.RAW_TIN_STORAGE)
                .add(CBBlocks.RAW_TIN_BLOCK.get());
        tag(CBTags.Blocks.STORAGE_BLOCKS_TIN)
                .add(CBBlocks.TIN_BLOCK.get());
        tag(CBTags.Blocks.STORAGE_BLOCKS_BRONZE)
                .add(CBBlocks.BRONZE_BLOCK.get());
    }
}
