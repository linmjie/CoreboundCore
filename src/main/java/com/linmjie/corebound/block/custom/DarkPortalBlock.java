package com.linmjie.corebound.block.custom;

import com.linmjie.corebound.worldgen.dimension.CBDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

//Place it, right click it, you're in the DarkWorld. Right click one over there to come back.
//Not a real multiblock frame portal, just the one block for now.
public class DarkPortalBlock extends Block {

    public DarkPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        //Client just swings and waits, the server does the actual moving
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        //Toggle: already in the dark world -> go home, anywhere else -> go to the dark world
        ResourceKey<Level> destinationKey = level.dimension() == CBDimensions.DARK_WORLD
                ? Level.OVERWORLD
                : CBDimensions.DARK_WORLD;

        ServerLevel destination = serverLevel.getServer().getLevel(destinationKey);
        if (destination == null) { //Datapack missing or broken, don't blow up over it
            return InteractionResult.FAIL;
        }

        //Ticket keeps the destination chunk loaded through the handoff, same as vanilla nether portals do
        player.changeDimension(new DimensionTransition(destination, findLandingSpot(destination, pos), Vec3.ZERO,
                player.getYRot(), player.getXRot(),
                DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)));
        return InteractionResult.CONSUME;
    }

    //Same x/z as the portal, dropped on top of whatever the destination has there.
    //In the DarkWorld that's just the deepslate floor.
    private static Vec3 findLandingSpot(ServerLevel destination, BlockPos pos) {
        //Generate the chunk FIRST. Level#getHeight quietly returns min_y for chunks it hasn't
        //loaded yet, which on a first trip meant getting stuffed into the bottom deepslate layer.
        destination.getChunkAt(pos);

        int y = destination.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
        //Empty column (nothing to stand on at all) - at least don't start below the world
        y = Math.max(y, destination.getMinBuildHeight() + 1);

        return new BlockPos(pos.getX(), y, pos.getZ()).getBottomCenter();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        //A little portal fuzz so it doesn't read as a plain black cube
        level.addParticle(ParticleTypes.PORTAL,
                pos.getX() + random.nextDouble(), pos.getY() + 1.0D, pos.getZ() + random.nextDouble(),
                random.nextGaussian() * 0.05D, random.nextDouble() * 0.1D, random.nextGaussian() * 0.05D);
    }
}
