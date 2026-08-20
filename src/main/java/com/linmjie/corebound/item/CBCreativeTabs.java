package com.linmjie.corebound.item;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.CBBlocks;
import com.linmjie.corebound.item.custom.CanteenItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CBCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Corebound.MODID);

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(CBBlocks.RAW_TIN_BLOCK);
        }
    }

    // Creates a creative tab with the id "corebound:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COREBOUND = CREATIVE_MODE_TABS.register(
            "corebound", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.corebound")) //The language key for the title of your CreativeModeTab
                    .icon(() -> CBItems.SHARP_STICK.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(CBItems.RAW_TIN.get());
                        output.accept(CBBlocks.TIN_ORE.get());
                        output.accept(CBBlocks.DEEPSLATE_TIN_ORE.get());
                        output.accept(CBBlocks.RAW_TIN_BLOCK.get());
                        output.accept(CBItems.ROCK.get());
                        output.accept(CBItems.LOGGER_AXE.get());
                        output.accept(CBItems.WOODEN_SHEARS);
                        output.accept(CBItems.TWIG);
                        output.accept(CBItems.SHARP_STICK);
                        output.accept(CBItems.UNFIRED_SAW);
                        output.accept(CBItems.UNFIRED_PLIERS);
                        output.accept(CBItems.UNFIRED_HAMMER);
                        output.accept(CBItems.SAW);
                        output.accept(CBItems.PLIERS);
                        output.accept(CBItems.HAMMER);

                        output.accept(CanteenItem.getDefaultCanteen());
                        //ItemStack testerCanteen = new ItemStack(ModItems.CANTEEN.get());
                        //testerCanteen.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.FIRE_RESISTANCE));
                        //testerCanteen.set(ModDataComponentTypes.CANTEEN_POTION_COUNT, 9);
                        //output.accept(testerCanteen);

                        output.accept(CBBlocks.INCOMPLETE_CRAFTING_TABLE.get());
                        output.accept(CBBlocks.DARK_PORTAL.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
