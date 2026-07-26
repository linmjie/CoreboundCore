package com.linmjie.corebound.mixin;

import com.linmjie.corebound.fluid.CanteenFluidHandler;
import com.linmjie.corebound.item.ModItems;
import com.linmjie.corebound.item.custom.CanteenItem;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Using mixin feels very icky here but I just don't see another way to avoid it (no events or data driven ways)
@Mixin(GenericItemFilling.class)
public abstract class GenericItemFillingMixin {
    @Inject(method = "canItemBeFilled", at = @At("HEAD"), cancellable = true)
    private static void addCanteenToCanBeFilled(Level world, ItemStack stack,
                                               CallbackInfoReturnable<Boolean> cir)
    {
        if (stack.getItem() == ModItems.CANTEEN.get()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getRequiredAmountForItem", at = @At("HEAD"), cancellable = true)
    private static void addCanteenRequiredFluid(Level world, ItemStack stack, FluidStack availableFluid,
                                               CallbackInfoReturnable<Integer> cir)
    {
        if (stack.getItem() == ModItems.CANTEEN.get()
            && CanteenFluidHandler.canFillCanteen(stack, availableFluid))
        {
            cir.setReturnValue(CanteenItem.ONE_FILL_VOLUME);
        }
    }

    // This behavior may change to drain as many potions are left (max multiples of 250mb)
    // injected specifically after the constant behavior of draining the liquid by the required amount
    @Inject(method = "fillItem",
            at = @At(value = "INVOKE",
                     target = "Lnet/neoforged/neoforge/fluids/FluidStack;shrink(I)V",
                     shift = At.Shift.AFTER
            ),
            cancellable = true)
    private static void addCanteenFillBehavior(Level world, int requiredAmount, ItemStack stack, FluidStack availableFluid,
                                              CallbackInfoReturnable<ItemStack> cir)
    {
        if (stack.getItem() == ModItems.CANTEEN.get()
            && CanteenFluidHandler.canFillCanteen(stack, availableFluid))
        {
            ItemStack canteen = stack.copy();
            CanteenFluidHandler.fillCanteen(canteen, availableFluid.get(DataComponents.POTION_CONTENTS));
            stack.shrink(1);
            cir.setReturnValue(canteen);
        }
    }
}
