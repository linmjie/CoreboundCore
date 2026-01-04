package com.linmjie.corebound.datagen;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.ModBlocks;
import com.linmjie.corebound.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Corebound.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.INCOMPLETE_CRAFTING_TABLE.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.RAW_TIN_BLOCK.get());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.RAW_TIN_BLOCK.get());
        tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL)
                .add(ModBlocks.RAW_TIN_BLOCK.get());
        tag(BlockTags.INCORRECT_FOR_GOLD_TOOL)
                .add(ModBlocks.RAW_TIN_BLOCK.get());
        tag(ModTags.Blocks.DROPS_ROCKS)
                .add(Blocks.COBBLESTONE)
                .add(Blocks.COBBLED_DEEPSLATE)
                .add(Blocks.STONE)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.BLACKSTONE);

        tag(Tags.Blocks.STORAGE_BLOCKS)
                .add(ModBlocks.RAW_TIN_BLOCK.get());
        tag(ModTags.Blocks.RAW_TIN_STORAGE)
                .add(ModBlocks.RAW_TIN_BLOCK.get());
    }
}
