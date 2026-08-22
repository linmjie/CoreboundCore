package com.linmjie.corebound.loot;

import com.linmjie.corebound.util.CoreboundUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AddItemModifier extends LootModifier {
    public static final MapCodec<AddItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).and(inst.group(
                        BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(e -> e.item),
                            Codec.INT.fieldOf("min").forGetter(e -> e.minDrop),
                            Codec.INT.fieldOf("max").forGetter(e -> e.maxDrop)))
                    .apply(inst, AddItemModifier::new));
    private final Item item;
    private final int minDrop;
    private final int maxDrop;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, int minDrop, int maxDrop) {
        super(conditionsIn);
        this.item = item;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item){
        super(conditionsIn);
        this.item = item;
        this.minDrop = 1;
        this.maxDrop = 1;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
        for (LootItemCondition condition : this.conditions) {
            if(!condition.test(lootContext)) {
                return generatedLoot;
            }
        }
        generatedLoot.add(new ItemStack(this.item, CoreboundUtils.randomInt(this.minDrop, this.maxDrop)));
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
