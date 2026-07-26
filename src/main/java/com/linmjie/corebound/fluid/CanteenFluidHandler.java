package com.linmjie.corebound.fluid;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.component.ModDataComponentTypes;
import com.linmjie.corebound.item.ModItems;
import com.linmjie.corebound.item.custom.CanteenItem;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

// Utility Class
// Consolidation of FluidHelper, GenericItemEmptying, and PotionFluidHandler utility classes for canteens specifically (+some static methods of PotionFluid)
public class CanteenFluidHandler {
    public static boolean tryEmptyItemIntoBE(Level worldIn,
                                             Player player,
                                             InteractionHand handIn,
                                             ItemStack heldItem,
                                             SmartBlockEntity be
    ) {
        assert heldItem.is(ModItems.CANTEEN);
        if (heldItem.getOrDefault(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0) <= 0) {
            return false;
        }

        Pair<FluidStack, ItemStack> emptyingResult = emptyItem(worldIn, heldItem, true);
        IFluidHandler capability = worldIn.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
        FluidStack fluidStack = emptyingResult.getFirst();

        if (capability == null || fluidStack.getAmount() != capability.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE)) {
            return false;
        }
        if (worldIn.isClientSide)
            return true;

        ItemStack copyOfHeld = heldItem.copy();
        emptyingResult = emptyItem(worldIn, copyOfHeld, false);
        capability.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

        if (!player.isCreative() && !(be instanceof CreativeFluidTankBlockEntity)) {
            player.setItemInHand(handIn, emptyingResult.getSecond());
        }
        return true;
    }

    public static Pair<FluidStack, ItemStack> emptyItem(Level level, ItemStack stack, boolean simulate) {
        FluidStack fluid = getFluidFromPotionItem(stack);
        ItemStack copy =  stack.copy();
        int count = stack.getOrDefault(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0);
        if (!simulate)
            drainCanteen(copy);
        return Pair.of(fluid, copy);
    }

    public static FluidStack getFluidFromPotionItem(ItemStack stack) {
        PotionContents potion = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        if (potion.is(Potions.WATER) && potion.customEffects().isEmpty())
            return new FluidStack(Fluids.WATER, 250);
        FluidStack fluid = getFluidFromPotion(potion,250);
        fluid.set(AllDataComponents.POTION_FLUID_BOTTLE_TYPE, PotionFluid.BottleType.REGULAR); // not sure if this is necessary
        return fluid;
    }

    public static FluidStack getFluidFromPotion(PotionContents potionContents, int amount) {
        if (potionContents.is(Potions.WATER))
            return new FluidStack(Fluids.WATER, amount);
        return addPotionToFluidStack(amount, potionContents);
    }

    public static FluidStack addPotionToFluidStack(int amount, PotionContents potionContents) {
        FluidStack fluidStack;
        fluidStack = new FluidStack(AllFluids.POTION.get().getSource(), amount);
        if (potionContents == PotionContents.EMPTY) {
            fluidStack.remove(DataComponents.POTION_CONTENTS);
            return fluidStack;
        }
        fluidStack.set(DataComponents.POTION_CONTENTS, potionContents);
        return fluidStack;
    }

    public static boolean tryFillItemFromBE(Level world,
                                            Player player,
                                            InteractionHand handIn,
                                            ItemStack heldItem,
                                            SmartBlockEntity be
    ) {
        assert heldItem.is(ModItems.CANTEEN);

        IFluidHandler capability = world.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);

        if (capability == null)
            return false;

        for (int i = 0; i < capability.getTanks(); i++) {
            FluidStack fluid = capability.getFluidInTank(i);
            if (fluid.isEmpty())
                continue;
            int requiredAmountForItem = CanteenItem.ONE_FILL_VOLUME;
            if (requiredAmountForItem > fluid.getAmount())
                continue;

            if (world.isClientSide)
                return true;

            if (player.isCreative() || be instanceof CreativeFluidTankBlockEntity)
                heldItem = heldItem.copy();
            Pair<ItemStack, Boolean> out = fillItem(world, requiredAmountForItem, heldItem, fluid.copy());

            FluidStack copy = fluid.copy();
            copy.setAmount(requiredAmountForItem);
            if (out.getSecond()) {
                capability.drain(copy, IFluidHandler.FluidAction.EXECUTE);
            }

            if (!player.isCreative())
                player.setItemInHand(handIn, out.getFirst());
            be.notifyUpdate();
            return true;
        }
        return false;
    }

    // fillItem not to be confused with fillCanteen lol
    // I'm copying some Create method names across different levels of abstraction so...
    public static Pair<ItemStack, Boolean> fillItem(Level world, int requiredAmount, ItemStack stack, FluidStack availableFluid) {
        FluidStack toFill = availableFluid.copy();
        PotionContents contents = availableFluid.get(DataComponents.POTION_CONTENTS);
        boolean didFill = true;
        toFill.setAmount(requiredAmount);
        availableFluid.shrink(requiredAmount);

        Fluid fluid = toFill.getFluid();
        if (FluidHelper.isWater(fluid)) {
            Corebound.LOGGER.info("attempted fill with water");
        }
        else if (contents != PotionContents.EMPTY) {
            didFill = attemptFill(stack, contents);
        }

        //Corebound.LOGGER.info("didFill: " + didFill);
        return Pair.of(stack, didFill);
    }

    public static boolean attemptFill(ItemStack canteen, PotionContents potionContents) {
        // Some funky stuff with pass by ref but it doesn't seem to break anything so I'm keeping it
        assert potionContents != PotionContents.EMPTY;
        if (canteen.get(DataComponents.POTION_CONTENTS).equals(PotionContents.EMPTY)) {
            // Corebound.LOGGER.info("attempted fill with empty");
            canteen.set(DataComponents.POTION_CONTENTS, potionContents);
            canteen.set(ModDataComponentTypes.CANTEEN_POTION_COUNT, 1);
            return true;
        } else if (canteen.get(DataComponents.POTION_CONTENTS).equals(potionContents)) {
            // Corebound.LOGGER.info("attempted fill with matching container and canteen potion contents");
            int currentCount = canteen.getOrDefault(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0);
            if (currentCount >= 16) {
                return false;
            }
            canteen.set(ModDataComponentTypes.CANTEEN_POTION_COUNT, currentCount + 1);
            return true;
        }
        // Corebound.LOGGER.info("attempted fill with different potion contents between canteen and container");
        return false;
    }

    // fillCanteen not to be confused with fillItem lol
    // I'm copying some Create method names across different levels of abstraction so...
    // This assumes that the potion contents of the canteen stack (first arg) and the PotionContents (second arg) are the same
    public static void fillCanteen(ItemStack stack, PotionContents potionContents) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        assert contents != null;
        if (contents == PotionContents.EMPTY) {
            stack.set(DataComponents.POTION_CONTENTS, potionContents);
            stack.set(ModDataComponentTypes.CANTEEN_POTION_COUNT, 1);
        } else {
            assert contents.equals(potionContents);
            int canteenFill = stack.getOrDefault(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0);
            if (canteenFill < CanteenItem.MAX_CAPACITY) {
                stack.set(ModDataComponentTypes.CANTEEN_POTION_COUNT, canteenFill + 1);
            }
        }
    }

    public static void drainCanteen(ItemStack stack) {
        int count = stack.getOrDefault(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0);
        if (count <= 0) {
            Corebound.LOGGER.warn("Decremented canteen contents even though they either don't exist or when empty");
            stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            return;
        }
        if (count == 1) {
            stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        }
        stack.set(ModDataComponentTypes.CANTEEN_POTION_COUNT, count - 1);
    }

    public static boolean canFillCanteen(ItemStack canteen, FluidStack fluid) {
        int canteenFill = canteen.getOrDefault(ModDataComponentTypes.CANTEEN_POTION_COUNT, 0);
        boolean canteenNotFull = canteenFill < CanteenItem.MAX_CAPACITY;
        boolean potionContentsMatch = false;
        if (canteenFill > 0) {
            PotionContents canteenContents = canteen.get(DataComponents.POTION_CONTENTS);
            PotionContents availableFluidContents = fluid.get(DataComponents.POTION_CONTENTS);
            assert canteenContents != null;
            potionContentsMatch = canteenContents.equals(availableFluidContents);
        } else {
            assert canteenFill == 0;
            potionContentsMatch = true;
        }
        return canteenNotFull && potionContentsMatch;
    }
}
