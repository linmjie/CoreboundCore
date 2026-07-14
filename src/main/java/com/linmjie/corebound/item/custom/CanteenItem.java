package com.linmjie.corebound.item.custom;

import com.linmjie.corebound.Corebound;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;

public class CanteenItem extends Item {
    /*
    * Probably refactor these two into one data component later
    * DataComponents.PotionContent
    * ModDataComponentTypes.CANTEEN_POTION_COUNT
    * PotionContents.class
    */

    private static final int DRINK_DURATION = 32;

    public CanteenItem(Properties properties) {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            Corebound.LOGGER.info("Drank canteen placeholder");
        }

        return stack;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return DRINK_DURATION;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Later we'll check for if the canteen is filled before using
        // Also check if canteen could be placed onto depot (and maybe others like basin or cauldron?), probably only for shift clicking though
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
