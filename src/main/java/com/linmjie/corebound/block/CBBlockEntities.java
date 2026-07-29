package com.linmjie.corebound.block;

import com.linmjie.corebound.Corebound;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CBBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Corebound.MODID);

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}