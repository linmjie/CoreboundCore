package com.linmjie.corebound.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class TwigItem extends Item {
    public TwigItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        BlockState modifiedState = blockstate.getToolModifiedState(context, ItemAbilities.FIRESTARTER_LIGHT, false);

        if (!level.isClientSide() && modifiedState != null){
            if (blockstate.getBlock() instanceof CampfireBlock c
                    || blockstate.getBlock() instanceof CandleBlock ca
                    || blockstate.getBlock() instanceof CandleCakeBlock cc
            ) {
                //Light thing
                if (Math.random() > 0.8) {
                    level.playSound(null, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS,
                            1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    level.setBlock(blockpos, modifiedState, 11);
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockpos);
                } else {
                    level.playSound(null, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS,
                            0.7F, level.getRandom().nextFloat() * 0.4F + 0.4F);
                }
                context.getItemInHand().consume(1, player);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_FLINT_ACTIONS.contains(itemAbility);
    }
}