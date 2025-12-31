package com.linmjie.corebound.datagen;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.ModBlocks;
import com.linmjie.corebound.block.custom.IncompleteCraftingTableBlock;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Corebound.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.RAW_TIN_BLOCK);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("corebound:block/" + deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("corebound:block/" + deferredBlock.getId().getPath() + appendix));
    }

    //String parameters are file names of the textures (excluding the .png)
    protected void incompleteCraftingTable(DeferredBlock<IncompleteCraftingTableBlock> deferredBlock,
                                           String top, String bottom, String pli, String ham, String saw,
                                           String saw_ham, String blank)
    {
        String name = "incomplete_crafting_table";

        /*
        9 different models, saw_pli_ham
        down, up, and particle are all constant
        east and south (side) -> pliers
        north and west (front) -> hammer and saw

        order of operations -> saw > pli > ham

        item model -> 0 tools
        */

        getVariantBuilder(deferredBlock.get())
                .forAllStates(state -> {
                    boolean hasSaw = state.getValue(IncompleteCraftingTableBlock.HAS_SAW);
                    boolean hasPli = state.getValue(IncompleteCraftingTableBlock.HAS_PLIERS);
                    boolean hasHam = state.getValue(IncompleteCraftingTableBlock.HAS_HAMMER);

                    return null;
                });
    }
}
