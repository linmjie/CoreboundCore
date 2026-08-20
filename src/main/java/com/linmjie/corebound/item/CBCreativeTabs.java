package com.linmjie.corebound.item;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.CBBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.Set;

public class CBCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Corebound.MODID);

    public static final Set<DeferredHolder<Item, ? extends Item>> CREATIVE_TAB_EXCLUDED_ITEMS = Set.of(CBItems.DEV_STEEL_CRAFTER);
    public static final Set<DeferredHolder<Block, ? extends Block>> CREATIVE_TAB_EXCLUDED_BLOCKS = Set.of();

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            insertAfter(event, Blocks.CHAIN, CBBlocks.TIN_BLOCK, CBBlocks.BRONZE_BLOCK);
        }

        if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {
        }

        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            insertAfter(event, Blocks.DEEPSLATE_IRON_ORE, CBBlocks.TIN_ORE, CBBlocks.DEEPSLATE_TIN_ORE);
            insertAfter(event, Blocks.RAW_IRON_BLOCK, CBBlocks.RAW_TIN_BLOCK);
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            insertBefore(event, Blocks.CRAFTING_TABLE, CBBlocks.INCOMPLETE_CRAFTING_TABLE);
        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            insertAfter(event, Items.STONE_HOE,
                    CBItems.BRONZE_SHOVEL, CBItems.BRONZE_PICKAXE, CBItems.BRONZE_AXE, CBItems.BRONZE_HOE);
            insertBefore(event, Items.BUCKET, CBItems.CANTEEN);
            insertBefore(event, Items.SHEARS, CBItems.WOODEN_SHEARS);
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            insertAfter(event, Items.STONE_SWORD, CBItems.BRONZE_SWORD);
            insertBefore(event, Items.SHIELD, CBItems.SHARP_STICK);
        }

        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
        }

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            insertAfter(event, Items.STICK, CBItems.TWIG, CBItems.ROCK);
            insertAfter(event, Items.RAW_IRON, CBItems.RAW_TIN);
            insertAfter(event, Items.IRON_NUGGET, CBItems.TIN_NUGGET, CBItems.BRONZE_NUGGET);
            insertAfter(event, Items.IRON_INGOT, CBItems.TIN_INGOT, CBItems.BRONZE_INGOT);
            insertAfter(event, Items.PHANTOM_MEMBRANE,
                    CBItems.UNFIRED_SAW, CBItems.UNFIRED_PLIERS, CBItems.UNFIRED_HAMMER,
                    CBItems.SAW, CBItems.PLIERS, CBItems.HAMMER);
        }

        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
        }

        if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COREBOUND = CREATIVE_MODE_TABS.register(
            "corebound", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.corebound")) //The language key for the title of your CreativeModeTab
                    .icon(() -> CBItems.SHARP_STICK.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        CBItems.ITEMS.getEntries().stream()
                            .filter(item -> !CREATIVE_TAB_EXCLUDED_ITEMS.contains(item))
                            .forEach(item -> output.accept(item.get().getDefaultInstance()));

                        //ItemStack testerCanteen = new ItemStack(ModItems.CANTEEN.get());
                        //testerCanteen.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.FIRE_RESISTANCE));
                        //testerCanteen.set(ModDataComponentTypes.CANTEEN_POTION_COUNT, 9);
                        //output.accept(testerCanteen);

                        CBBlocks.BLOCKS.getEntries().stream()
                                .filter(block -> !CREATIVE_TAB_EXCLUDED_BLOCKS.contains(block))
                                .forEach(block -> output.accept(block.get().asItem().getDefaultInstance()));
                    }).build());

    // Maybe I'll add more parameter combinations when I need them
    public static void insertAfter(BuildCreativeModeTabContentsEvent event,
                                   ItemLike existingEntry, ItemLike... newEntries)
    {
        insertAfter(event,
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                existingEntry.asItem().getDefaultInstance(),
                Arrays.stream(newEntries)
                        .map(entry -> entry.asItem().getDefaultInstance())
                        .toArray(ItemStack[]::new));
    }

    public static void insertAfter(BuildCreativeModeTabContentsEvent event,
                                   CreativeModeTab.TabVisibility tabVisibility,
                                   ItemStack existingEntry, ItemStack... newEntries)
    {
        ItemStack previousEntry = existingEntry;
        for (ItemStack newEntry : newEntries) {
            event.insertAfter(previousEntry, newEntry, tabVisibility);
            previousEntry = newEntry;
        }
    }

    public static void insertBefore(BuildCreativeModeTabContentsEvent event,
                                   ItemLike existingEntry, ItemLike... newEntries)
    {
        insertBefore(event,
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                existingEntry.asItem().getDefaultInstance(),
                Arrays.stream(newEntries)
                        .map(entry -> entry.asItem().getDefaultInstance())
                        .toArray(ItemStack[]::new));
    }

    public static void insertBefore(BuildCreativeModeTabContentsEvent event,
                                    CreativeModeTab.TabVisibility tabVisibility,
                                    ItemStack existingEntry, ItemStack... newEntries)
    {
        ItemStack previousEntry = existingEntry;
        for (ItemStack newEntry : newEntries) {
            event.insertBefore(previousEntry, newEntry, tabVisibility);
            previousEntry = newEntry;
        }
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}