package com.linmjie.corebound.datagen;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.ModBlocks;
import com.linmjie.corebound.block.custom.IncompleteCraftingTableBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.logging.log4j.core.Core;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Corebound.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.RAW_TIN_BLOCK);
        incompleteCraftingTable(ModBlocks.INCOMPLETE_CRAFTING_TABLE,
                "top", "bottom", "side_pli",
                "front_ham", "front_saw", "front_saw_ham",
                "blank");
        blockItem(ModBlocks.INCOMPLETE_CRAFTING_TABLE);
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
    //I lowkey cobbled this method up under great frustration, maybe I'll refactor this later..
    protected void incompleteCraftingTable(DeferredBlock<IncompleteCraftingTableBlock> deferredBlock,
                                           String top, String bottom, String pli, String ham, String saw,
                                           String saw_ham, String blank)
    {
        /*
        8 different models, saw_pli_ham
        down, up, and particle are all constant
        east and south (side) -> pliers
        north and west (front) -> hammer and saw

        order of operations -> saw > pli > ham
        operations counted in binary:
        saw pli ham
        0/1 0/1 0/1

        item model -> 0 tools
        */
        String name = "block/incomplete_crafting_table";

        ModelFile[] models = new ModelFile[8];
        for (int i = 0; i < models.length; i++) {
            boolean hasSaw = ((i >> 2) & 1) == 1;
            boolean hasPli = ((i >> 1) & 1) == 1;
            boolean hasHam = (i & 1) == 1;
            var side = ResourceLocation.fromNamespaceAndPath(Corebound.MODID,
                    hasPli? name + "_" + pli : name + "_side_" + blank
            );

            //suffix used later
            StringBuilder suffix = new StringBuilder(13);

            String front_s = null;
            if (hasSaw) {
                suffix.append("_saw");
                front_s = saw;
            }
            if (hasPli) {
                suffix.append("_pli");
            }
            if (hasHam) {
                suffix.append("_ham");
                front_s = ham;
            }

            if (hasSaw && hasHam)
                front_s = saw_ham;

            var front = ResourceLocation.fromNamespaceAndPath(Corebound.MODID,
                    front_s != null? name + "_" + front_s : name + "_front_" + blank);


            models[i] = models().cube(name + suffix,
                    ResourceLocation.fromNamespaceAndPath(Corebound.MODID, name + "_" + bottom),
                    ResourceLocation.fromNamespaceAndPath(Corebound.MODID, name + "_" + top),
                    front, side, side, front)
                    .texture("particle", ResourceLocation.fromNamespaceAndPath(Corebound.MODID, name + "_front_" + blank));
        }

        getVariantBuilder(deferredBlock.get())
                .forAllStates(state -> {
                    boolean hasSaw = state.getValue(IncompleteCraftingTableBlock.HAS_SAW);
                    boolean hasPli = state.getValue(IncompleteCraftingTableBlock.HAS_PLIERS);
                    boolean hasHam = state.getValue(IncompleteCraftingTableBlock.HAS_HAMMER);

                    int mask = 0;
                    if (hasSaw)
                        mask |= 0b100;
                    if (hasPli)
                        mask |= 0b010;
                    if (hasHam)
                        mask |= 0b001;

                    return ConfiguredModel.builder().modelFile(models[mask]).build();
                });
    }
}
