package com.linmjie.corebound.item;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.component.CBDataComponentTypes;
import com.linmjie.corebound.item.custom.CanteenItem;
import com.linmjie.corebound.item.custom.LoggerItem;
import com.linmjie.corebound.item.custom.SpearItem;
import com.linmjie.corebound.item.custom.TwigItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CBItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Corebound.MODID);

    public static final DeferredItem<Item>

        DEV_STEEL_CRAFTER = ITEMS.registerSimpleItem("dev_steel_crafter"),

        RAW_TIN = ITEMS.registerSimpleItem("raw_tin"),
        TIN_NUGGET = ITEMS.registerSimpleItem("tin_nugget"),
        TIN_INGOT = ITEMS.registerSimpleItem("tin_ingot"),

        BRONZE_NUGGET =  ITEMS.registerSimpleItem("bronze_nugget"),
        BRONZE_INGOT =  ITEMS.registerSimpleItem("bronze_ingot"),

        TWIG = ITEMS.register("twig",
            () -> new TwigItem(new Item.Properties())),

        ROCK = ITEMS.registerSimpleItem("rock"),

        UNFIRED_SAW = ITEMS.registerSimpleItem("unfired_saw",
            new Item.Properties().stacksTo(1)),
        UNFIRED_PLIERS = ITEMS.registerSimpleItem("unfired_pliers",
            new Item.Properties().stacksTo(1)),
        UNFIRED_HAMMER = ITEMS.registerSimpleItem("unfired_hammer",
            new Item.Properties().stacksTo(1)),

        SAW = ITEMS.registerSimpleItem("saw",
            new Item.Properties().stacksTo(1)),
        PLIERS = ITEMS.registerSimpleItem("pliers",
            new Item.Properties().stacksTo(1)),
        HAMMER = ITEMS.registerSimpleItem("hammer",
            new Item.Properties().stacksTo(1)),

        BRONZE_SWORD = ITEMS.register(
                "bronze_sword", () -> new SwordItem(CBTiers.BRONZE,
                new Item.Properties()
                .attributes(SwordItem.createAttributes(CBTiers.BRONZE, 3, -2.6F)))),
        BRONZE_SHOVEL = ITEMS.register(
                "bronze_shovel", () -> new ShovelItem(CBTiers.BRONZE,
                new Item.Properties()
                .attributes(ShovelItem.createAttributes(CBTiers.BRONZE, 1.5F, -3.2F)))),
        BRONZE_PICKAXE = ITEMS.register(
                "bronze_pickaxe", () -> new PickaxeItem(CBTiers.BRONZE,
                new Item.Properties()
                .attributes(PickaxeItem.createAttributes(CBTiers.BRONZE, 1.0F, -3.0F)))),
        BRONZE_AXE = ITEMS.register(
                "bronze_axe", () -> new AxeItem(CBTiers.BRONZE,
                new Item.Properties()
                .attributes(AxeItem.createAttributes(CBTiers.BRONZE, 6.0F, -3.4F)))),
        BRONZE_HOE = ITEMS.register(
                "bronze_hoe", () -> new HoeItem(CBTiers.BRONZE,
                new Item.Properties()
                .attributes(HoeItem.createAttributes(CBTiers.BRONZE, 0.0F, -3.2F)))),

        CANTEEN = ITEMS.register("canteen",
            () -> new CanteenItem(
                new Item.Properties()
                .stacksTo(1)
                .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .component(CBDataComponentTypes.CANTEEN_POTION_COUNT, 0)
            )),

        WOODEN_SHEARS = ITEMS.register("wooden_shears",
            () -> new Item(new Item.Properties()
            .durability(64)){
            public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
                if (!level.isClientSide && !state.is(BlockTags.FIRE)) {
                    stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);
                }
                return false;
            }}),

        SHARP_STICK = ITEMS.register("sharp_stick",
            () -> new SpearItem(CBTiers.BRONZE, new Item.Properties().
                attributes(SwordItem.createAttributes(CBTiers.BRONZE, 3, -3.2F))));

    public static final DeferredItem<LoggerItem> LOGGER_AXE = ITEMS.register("logger_axe",
        () -> new LoggerItem(Tiers.IRON, new Item.Properties()
            .attributes(AxeItem.createAttributes(Tiers.IRON,3F, 1F))));

    public static void register (IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
