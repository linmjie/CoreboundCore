package com.linmjie.corebound.datagen;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.item.CBItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class CBItemModelProvider extends ItemModelProvider {
    public CBItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Corebound.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(CBItems.RAW_TIN.get());
        basicItem(CBItems.TWIG.get());
        basicItem(CBItems.ROCK.get());
        basicItem(CBItems.CANTEEN.get());
        handheldItem(CBItems.WOODEN_SHEARS);
        handheldItem(CBItems.SHARP_STICK);
        handheldItem(CBItems.SAW);
        handheldItem(CBItems.UNFIRED_SAW);
        handheldItem(CBItems.PLIERS);
        handheldItem(CBItems.UNFIRED_PLIERS);
        handheldItem(CBItems.HAMMER);
        handheldItem(CBItems.UNFIRED_HAMMER);

        //handheldItem(ModItems.LOGGER_AXE);
    }

    private ItemModelBuilder handheldItem(DeferredItem<?> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Corebound.MODID,"item/" + item.getId().getPath()));
    }
}
