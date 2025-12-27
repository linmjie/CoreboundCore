package com.linmjie.corebound.block.custom;

import com.linmjie.corebound.gui.menu.IncompleteCraftingMenu;
import com.linmjie.corebound.item.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class IncompleteCraftingTableBlock extends CraftingTableBlock {
    public static final MapCodec<IncompleteCraftingTableBlock> CODEC = simpleCodec(IncompleteCraftingTableBlock::new);
    private static final Component CONTAINER_TITLE = Component.translatable("container.incomplete_crafting");
    public static final BooleanProperty HAS_SAW = BooleanProperty.create("has_saw");
    public static final BooleanProperty HAS_SCISSORS = BooleanProperty.create("has_scissors");
    public static final BooleanProperty HAS_HAMMER = BooleanProperty.create("has_hammer");

    public MapCodec<? extends IncompleteCraftingTableBlock> codec() {
        return CODEC;
    }

    public IncompleteCraftingTableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HAS_SAW, false));
        this.registerDefaultState(this.defaultBlockState().setValue(HAS_SCISSORS, false));
        this.registerDefaultState(this.defaultBlockState().setValue(HAS_HAMMER, false));
    }

    private Item getToolItem(BooleanProperty has){
        if (has == HAS_SAW)
            return ModItems.SAW.asItem();
        if (has == HAS_SCISSORS)
            return ModItems.SCISSORS.asItem();
        if (has == HAS_HAMMER)
            return ModItems.HAMMER.asItem();
        throw new IllegalArgumentException("Not saw, scissors, or hammer");
    }

    protected static int getToolsMask(BlockState state){
        int mask = 0;
        if (state.getValue(HAS_SAW))
            mask |= 0b001;
        if (state.getValue(HAS_SCISSORS))
            mask |= 0b010;
        if (state.getValue(HAS_HAMMER))
            mask |= 0b100;
        return mask;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            boolean consume = false;
            BooleanProperty toDo = null;
            if (stack.is(Items.AIR)) {
                player.openMenu(state.getMenuProvider(level, pos));
                player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                return ItemInteractionResult.CONSUME;
            }

            if (stack.is(ModItems.SAW.get())) {
                toDo = HAS_SAW;
                consume = true;
            } else if (stack.is(ModItems.SCISSORS.get())) {
                toDo = HAS_SCISSORS;
                consume = true;
            } else if (stack.is(ModItems.HAMMER.get())){
                toDo = HAS_HAMMER;
                consume = true;
            }

            if (consume && !state.getValue(toDo)){
                level.setBlockAndUpdate(pos, state.setValue(toDo, true));
                stack.consume(1, player);
                level.scheduleTick(pos, this, 1);
                return ItemInteractionResult.CONSUME;
            }

        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(HAS_SAW) && state.getValue(HAS_SCISSORS) && state.getValue(HAS_HAMMER)){
            level.setBlockAndUpdate(pos, Blocks.CRAFTING_TABLE.defaultBlockState());
        }
        super.tick(state, level, pos, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HAS_SAW)
                .add(HAS_SCISSORS)
                .add(HAS_HAMMER);
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (p_52229_, p_52230_, p_52231_) -> new IncompleteCraftingMenu(
                        p_52229_, p_52230_, ContainerLevelAccess.create(level, pos), getToolsMask(state)),
                CONTAINER_TITLE
        );
    }
}
