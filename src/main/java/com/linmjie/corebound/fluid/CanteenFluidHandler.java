package com.linmjie.corebound.fluid;

import com.linmjie.corebound.component.CBDataComponentTypes;
import com.linmjie.corebound.item.custom.CanteenItem;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

// Utility Class
public class CanteenFluidHandler {
    public static FluidStack getFluidFromCanteenItem(ItemStack stack) {
        PotionContents potion = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        if (potion.is(Potions.WATER) && potion.customEffects().isEmpty())
            return new FluidStack(Fluids.WATER, 250);
        FluidStack fluid = PotionFluidHandler.getFluidFromPotion(potion, PotionFluid.BottleType.REGULAR, 250);
        fluid.set(AllDataComponents.POTION_FLUID_BOTTLE_TYPE, PotionFluid.BottleType.REGULAR); // not sure if this is necessary
        return fluid;
    }

    public static Pair<FluidStack, ItemStack> emptyCanteen(ItemStack stack, boolean simulate) {
        FluidStack fluid = getFluidFromCanteenItem(stack);
        ItemStack copy =  stack.copy();
        drainCanteen(copy);
        if (!simulate)
            stack.shrink(1);
        return Pair.of(fluid, copy);
    }

    // This assumes that the potion contents of the canteen stack (first arg) and the PotionContents (second arg) are the same
    public static void fillCanteen(ItemStack stack, PotionContents potionContents) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        assert contents != null;
        if (contents == PotionContents.EMPTY) {
            stack.set(DataComponents.POTION_CONTENTS, potionContents);
            stack.set(CBDataComponentTypes.CANTEEN_POTION_COUNT, 1);
        } else {
            assert contents.equals(potionContents);
            int canteenFill = stack.getOrDefault(CBDataComponentTypes.CANTEEN_POTION_COUNT, 0);
            if (canteenFill < CanteenItem.MAX_CAPACITY) {
                stack.set(CBDataComponentTypes.CANTEEN_POTION_COUNT, canteenFill + 1);
            }
        }
    }

    public static void drainCanteen(ItemStack stack) {
        int count = stack.getOrDefault(CBDataComponentTypes.CANTEEN_POTION_COUNT, 0);
        if (count <= 0) {
            stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            return;
        }
        if (count == 1) {
            stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        }
        stack.set(CBDataComponentTypes.CANTEEN_POTION_COUNT, count - 1);
    }

    public static boolean canFillCanteen(ItemStack canteen, FluidStack fluid) {
        int canteenFill = canteen.getOrDefault(CBDataComponentTypes.CANTEEN_POTION_COUNT, 0);
        boolean canteenNotFull = canteenFill < CanteenItem.MAX_CAPACITY;
        boolean potionContentsMatch;
        PotionContents availableFluidContents = fluid.get(DataComponents.POTION_CONTENTS);
        if (canteenFill > 0) {
            PotionContents canteenContents = canteen.get(DataComponents.POTION_CONTENTS);
            assert canteenContents != null;
            potionContentsMatch = canteenContents.equals(availableFluidContents);
        } else {
            assert canteenFill == 0;
            potionContentsMatch = availableFluidContents != null;
        }
        return canteenNotFull && potionContentsMatch;
    }
}
