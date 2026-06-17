package com.linmjie.corebound.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

public class CanteenItem extends PotionItem {
    /*
    * Probably refactor these two into one data component later
    * DataComponents.PotionContent
    * ModDataComponentTypes.CANTEEN_POTION_COUNT
    * PotionContents.class
    */
    public CanteenItem(Properties properties) {
        super(properties);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return this.getDescriptionId(); // Prevents the inherited PotionItem behavior of appending .effect.{whatever_effect}
    }
}
