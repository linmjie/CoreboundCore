package com.linmjie.corebound;

import com.linmjie.corebound.block.CBBlockEntities;
import com.linmjie.corebound.block.CBBlocks;
import com.linmjie.corebound.component.CBDataComponentTypes;
import com.linmjie.corebound.item.CBItems;
import com.linmjie.corebound.item.custom.CanteenItem;
import com.linmjie.corebound.loot.CBLootRegistries;
import com.linmjie.corebound.screen.CBMenuTypes;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Corebound.MODID)
public class Corebound {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "corebound";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "corebound" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Corebound(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        CBBlocks.register(modEventBus);
        CBBlockEntities.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        CBItems.register(modEventBus);
        CBDataComponentTypes.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (corebound) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register mod global loot modifiers and pass through event bus
        CBLootRegistries.register(modEventBus);

        //Register Menus
        CBMenuTypes.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(CBBlocks.RAW_TIN_BLOCK);
        }
    }
    // Creates a creative tab with the id "corebound:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register(
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

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
