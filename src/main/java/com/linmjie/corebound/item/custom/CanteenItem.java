package com.linmjie.corebound.item.custom;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.component.ModDataComponentTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CanteenItem extends Item {
    private static final int DRINK_DURATION = 32;
    public static final int MAX_CAPACITY = 16;

    public CanteenItem(Properties properties) {
        super(properties);
    }

    public static ItemStack decrementCanteenContents(ItemStack stack) {
        int count = stack.getOrDefault(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0);
        if (count <= 0) {
            Corebound.LOGGER.warn("Decremented canteen contents even though they either don't exist or when empty");
            stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            return stack;
        }
        if (count == 1) {
            stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        }
        stack.set(ModDataComponentTypes.CANTEEN_POTION_COUNT, count - 1);
        return stack;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;
        if (player instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            PotionContents potioncontents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            potioncontents.forEachEffect((effect) -> {
                if (effect.getEffect().value().isInstantenous()) {
                    effect.getEffect().value().applyInstantenousEffect(player, player, entityLiving, effect.getAmplifier(), (double)1.0F);
                } else {
                    entityLiving.addEffect(effect);
                }
            });
            decrementCanteenContents(stack);
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return DRINK_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // todo: check if canteen could be placed onto depot (and maybe others like basin or cauldron?), probably only for shift clicking though
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getOrDefault(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0) > 0) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
        Corebound.LOGGER.info("Can't use canteen");
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);

        if (potioncontents != null) {
            Objects.requireNonNull(tooltipComponents);
            potioncontents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
        }
        int potionCount = stack.getOrDefault(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0);
        if (potionCount > 0) {
            tooltipComponents.add(Component.translatable("tooltip.corebound.canteen_potion_count_tooltip", potionCount, MAX_CAPACITY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.corebound.canteen_empty_tooltip"));
        }
    }
}
