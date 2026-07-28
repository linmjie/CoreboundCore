package com.linmjie.corebound.item.custom;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.component.ModDataComponentTypes;
import com.linmjie.corebound.fluid.CanteenFluidHandler;
import com.linmjie.corebound.item.ModItems;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.linmjie.corebound.mixin.acccessors.ItemDrainAccessor;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Objects;

public class CanteenItem extends Item {
    private static final int DRINK_DURATION = 32;
    public static final int MAX_CAPACITY = 16;
    public static final int ONE_FILL_VOLUME = 250; //mb

    public CanteenItem(Properties properties) {
        super(properties);
    }

    public static ItemStack getDefaultCanteen() {
        ItemStack canteen = new ItemStack(ModItems.CANTEEN.get());
        canteen.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        canteen.set(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0);
        return canteen;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        boolean hasInfiniteMaterials = entityLiving.hasInfiniteMaterials();
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;
        if (player instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            PotionContents potioncontents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            potioncontents.forEachEffect((effect) -> {
                if (effect.getEffect().value().isInstantenous()) {
                    effect.getEffect().value().applyInstantenousEffect(player, player, entityLiving, effect.getAmplifier(), 1.0);
                } else {
                    entityLiving.addEffect(effect);
                }
            });
            if (!hasInfiniteMaterials) {
                CanteenFluidHandler.drainCanteen(stack);
            }
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
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getOrDefault(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0) > 0) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            ItemStack stack = context.getItemInHand();
            assert !stack.isEmpty();
            Player player =  context.getPlayer();
            InteractionHand hand = context.getHand();
            BlockEntity be = level.getBlockEntity(context.getClickedPos());
            if (be instanceof BasinBlockEntity basin) {
                // can withdraw and deposit potion effects
                Corebound.LOGGER.info("enter basin interaction via item");
                if (CanteenFluidHandler.tryFillItemFromBE(level, player, hand, stack, basin))
                    return InteractionResult.SUCCESS;
                if (CanteenFluidHandler.tryEmptyItemIntoBE(level, player, hand, stack, basin))
                    return InteractionResult.SUCCESS;
            // getting an item drain from the blockpos of the item drain apparently does not work so......
            } else if (be instanceof ItemDrainBlockEntity drain) {
                // can only deposit potion effects
                // some mixin accessor because the tank is package-private
                SmartFluidTankBehaviour tank = ((ItemDrainAccessor) drain).getInternalTank();
                tank.allowInsertion();
                boolean canEmpty = CanteenFluidHandler.tryEmptyItemIntoBE(level, player, hand, stack, drain);
                tank.forbidInsertion();
                if (canEmpty) return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
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
