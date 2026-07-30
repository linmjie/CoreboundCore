package com.linmjie.corebound.datagen;

import com.linmjie.corebound.block.CBBlocks;
import com.linmjie.corebound.item.CBItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class CBBlockLootTableProvider extends BlockLootSubProvider {
    protected CBBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(CBBlocks.RAW_TIN_BLOCK.get());
        dropSelf(CBBlocks.INCOMPLETE_CRAFTING_TABLE.get());

        this.add(CBBlocks.TIN_ORE.get(),
            block -> createOreDrop(CBBlocks.TIN_ORE.get(),
            CBItems.RAW_TIN.get()));

        this.add(CBBlocks.DEEPSLATE_TIN_ORE.get(),
                block -> createOreDrop(CBBlocks.DEEPSLATE_TIN_ORE.get(),
                        CBItems.RAW_TIN.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return CBBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
