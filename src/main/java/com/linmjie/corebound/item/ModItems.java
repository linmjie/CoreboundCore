package com.linmjie.corebound.item;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.item.custom.LoggerItem;
import com.linmjie.corebound.item.custom.SpearItem;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Corebound.MODID);

    public static final DeferredItem<Item> RAW_TIN = ITEMS.registerSimpleItem("raw_tin");
    public static final DeferredItem<Item> TWIG = ITEMS.registerSimpleItem("twig");
    public static final DeferredItem<Item> ROCK = ITEMS.registerSimpleItem("rock");

    public static final DeferredItem<Item> UNFIRED_SAW = ITEMS.registerSimpleItem("unfired_saw");
    public static final DeferredItem<Item> UNFIRED_SCISSORS = ITEMS.registerSimpleItem("unfired_scissors");
    public static final DeferredItem<Item> UNFIRED_HAMMER = ITEMS.registerSimpleItem("unfirede_hammer");

    public static final DeferredItem<Item> SAW = ITEMS.registerSimpleItem("saw");
    public static final DeferredItem<Item> SCISSORS = ITEMS.registerSimpleItem("scissors");
    public static final DeferredItem<Item> HAMMER = ITEMS.registerSimpleItem("hammer");

    public static final DeferredItem<LoggerItem> LOGGER_AXE = ITEMS.register("logger_axe",
            () -> new LoggerItem(Tiers.IRON, new Item.Properties()
                    .attributes(AxeItem.createAttributes(Tiers.IRON,3F, 1F))));

    public static final DeferredItem<Item> WOODEN_SHEARS = ITEMS.register("wooden_shears",
            () -> new Item(new Item.Properties()
                    .durability(64)){
                public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
                    if (!level.isClientSide && !state.is(BlockTags.FIRE)) {
                        stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);
                    }
                    return false;
                }});

    public static final DeferredItem<Item> SHARP_STICK = ITEMS.register("sharp_stick",
            () -> new SpearItem(Tiers.WOOD, new Item.Properties().
                    attributes(SwordItem.createAttributes(Tiers.WOOD, 3, -3.2F))));

    public static void register (IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
