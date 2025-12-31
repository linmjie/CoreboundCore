package com.linmjie.corebound.event;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.datagen.ModRecipeProvider;
import com.linmjie.corebound.item.custom.LoggerItem;
import com.linmjie.corebound.item.custom.SpearItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.*;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = Corebound.MODID)
public class ModEvents {
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    private static final AttributeModifier SPEAR_ENTITY_RANGE_MODIFIER =
            new AttributeModifier(ResourceLocation.withDefaultNamespace("spear_entity_range_modifier"),
                    1.5, AttributeModifier.Operation.ADD_VALUE);

    @SubscribeEvent
    public static void onLoggerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getMainHandItem();

        if (mainHandItem.getItem() instanceof LoggerItem logger && player instanceof ServerPlayer serverPlayer) {
            BlockPos initialBlockPos = event.getPos();
            if (HARVESTED_BLOCKS.contains(initialBlockPos)) {
                return;
            }
            if (event.getLevel() instanceof Level level) {
                List<BlockPos> treeBlocks = LoggerItem.findTreeBlocks(level, initialBlockPos);
                if (treeBlocks.isEmpty()){
                    return;
                }
                else{
                    for (BlockPos pos : treeBlocks) {
                        if (pos == initialBlockPos || !logger.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                            continue;
                        }

                        HARVESTED_BLOCKS.add(pos);
                        serverPlayer.gameMode.destroyBlock(pos);
                        HARVESTED_BLOCKS.remove(pos);
                    }
                }
            }
        }
    }

    //CLAY-TILL MECHANIC VARS
    private static final HashMap<ServerPlayer, Integer> CLAY_SHOVEL_TICK_QUEUE = new HashMap<>();

    private static final int CLAY_GETTER_DELAY = 5;
    private static final double CLAY_DROP_PROB = 0.8;
    //END VARS

    @SubscribeEvent
    public static void onPlayerTickEvent(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        ItemStack mainHandItem = player.getMainHandItem();
        if (player instanceof ServerPlayer serverPlayer) {

            //For giving player increased range for crits when holding a SpearItem
            AttributeInstance attributeInstance = serverPlayer.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
            if (attributeInstance != null) {
                if (mainHandItem.getItem() instanceof SpearItem && !serverPlayer.onGround()) {
                    attributeInstance.addOrUpdateTransientModifier(SPEAR_ENTITY_RANGE_MODIFIER);
                } else {
                    attributeInstance.removeModifier(SPEAR_ENTITY_RANGE_MODIFIER);
                }
            }

            //Clay-shovel tick queue work
            //The tick counter decrementer for the clay-till mechanic event below
            if (CLAY_SHOVEL_TICK_QUEUE.containsKey(serverPlayer)) {
                int ticksLeft = CLAY_SHOVEL_TICK_QUEUE.get(serverPlayer);
                if (ticksLeft <= 0)
                    CLAY_SHOVEL_TICK_QUEUE.remove(serverPlayer);
                else
                    CLAY_SHOVEL_TICK_QUEUE.put(serverPlayer, ticksLeft - 1);
            }
        }
    }

    //Event for clay-till mechanic (dropping clay from shoveling tilled dirt)
    //Repeatedly right click with shovel on tilled dirt for a chance to get clayballs
    @SubscribeEvent
    public static void onShovelUsage(UseItemOnBlockEvent event) {
        Level level = event.getLevel();
        Player player = event.getPlayer();

        if(!level.isClientSide() && player != null) {
            BlockPos pos = event.getPos();
            ItemStack stack = event.getItemStack();
            
            if(event.getLevel().getBlockState(pos).is(Blocks.DIRT_PATH)
                    && stack.is(ItemTags.SHOVELS)
                    && !CLAY_SHOVEL_TICK_QUEUE.containsKey((ServerPlayer) player)
            ){
                CLAY_SHOVEL_TICK_QUEUE.put((ServerPlayer) player, CLAY_GETTER_DELAY); //Default tick delay of 5
                stack.hurtAndBreak(1, (ServerLevel) level, (ServerPlayer) player,
                        item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                if (Math.random() > CLAY_DROP_PROB) { //Succeed to drop clay
                    level.addFreshEntity(new ItemEntity(
    //Magic number to drop clay ball a little above the block |
                            level, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(Items.CLAY_BALL)));
                    level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 2.5F);
                } else { //Fail to drop clay
                    level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 0.1F, 0.5F);
                }
            }
        }
    }

    private static final Set<String> recipesToRemove = Set.of(
            "minecraft:campfire",
            "minecraft:crafting_table",
            "minecraft:wooden_sword",
            "minecraft:wooden_shovel",
            "minecraft:wooden_pickaxe",
            "minecraft:wooden_axe",
            "minecraft:wooden_hoe",
            "minecraft:stone_sword",
            "minecraft:stone_shovel",
            "minecraft:stone_pickaxe",
            "minecraft:stone_axe",
            "minecraft:stone_hoe"
    );

	@SubscribeEvent
    public static void onRecipeRegistrationEvent(RecipesUpdatedEvent event) {
        RecipeManager recipeManager = event.getRecipeManager();

        var recipes = event.getRecipeManager().getRecipes().stream()
            .filter(holder -> !recipesToRemove.contains(holder.id().toString()))
            .collect(Collectors.toCollection(ArrayList::new));

        recipeManager.replaceRecipes(recipes);
    }
}
