package me.praenyth.mods.minecartcollisions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class MinecartUtils {

    public static Block checkRailInFront(World world, BlockPos checkPos) {

        BlockPos checkPosUnder = checkPos.down();

        if (world.getBlockState(checkPos).getBlock().equals(Blocks.RAIL)) {

            return world.getBlockState(checkPos).getBlock();

        } else if (world.getBlockState(checkPosUnder).getBlock().equals(Blocks.RAIL)) {

            return world.getBlockState(checkPosUnder).getBlock();

        } else if (world.getBlockState(checkPos).getBlock().equals(Blocks.POWERED_RAIL)) {

            return world.getBlockState(checkPos).getBlock();

        } else if (world.getBlockState(checkPosUnder).getBlock().equals(Blocks.POWERED_RAIL)) {

            return world.getBlockState(checkPosUnder).getBlock();

        }

        return null;

    }

    public static boolean onSlopedRail(BlockState state)
    {
        switch (state.get(Properties.RAIL_SHAPE)) {
            case ASCENDING_SOUTH:
            case ASCENDING_NORTH:
            case ASCENDING_WEST:
            case ASCENDING_EAST:
                return true;
        }
        return false;
    }

    public static boolean onFlatRail(BlockState state) {

        if (state.getBlock().equals(Blocks.RAIL)) {

            if (onSlopedRail(state)) {

                return true;

            }

        }

        return false;

    }

    public static boolean onParallelRail(BlockState myState, BlockState otherState) {

        if (otherState.getBlock().equals(Blocks.RAIL)) {

            if (myState.get(Properties.RAIL_SHAPE).equals(otherState.get(Properties.RAIL_SHAPE))) {
                return true;
            }

        }

        return false;

    }

    public static boolean onPerpendicularRail(BlockState myState, BlockState otherState) {

        if (otherState.getBlock().equals(Blocks.RAIL)) {

            if (!myState.get(Properties.RAIL_SHAPE).equals(otherState.get(Properties.RAIL_SHAPE))) {

                return true;

            }

        }

        return false;

    }

    public static boolean inIntersection(Entity entity, Vec3d movementDir, BlockState state) {

        Vec3i newMovementDir = normalizeVec3i(vec3dToVec3i(movementDir));

        BlockPos entityBlockPos = entity.getBlockPos();
        World entityWorld = entity.getEntityWorld();

        if (onFlatRail(state)) {

            BlockState front = entityWorld.getBlockState(entityBlockPos.add(newMovementDir));
            BlockState back = entityWorld.getBlockState(entityBlockPos.subtract(newMovementDir));
            BlockState left = entityWorld.getBlockState(entityBlockPos.add(newMovementDir.getZ(), 0, -newMovementDir.getX()));
            BlockState right = entityWorld.getBlockState(entityBlockPos.add(-newMovementDir.getZ(), 0, newMovementDir.getX()));

            if (onPerpendicularRail(state, left)
                    && onPerpendicularRail(state, right)
            ) {
                return true;
            }

        }

        return false;

    }

    public static Vec3i vec3dToVec3i(Vec3d vec3i) {
        return new Vec3i(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public static Vec3i normalizeVec3i(Vec3i vector) {
        double tempVector = Math.sqrt(
                vector.getX() * vector.getX()
                        + vector.getY() * vector.getY()
                        + vector.getZ() * vector.getZ()
        );

        return tempVector < 1.0E-4 ? new Vec3i(0, 0, 0) : new Vec3i(
                vector.getX() / tempVector, vector.getY() / tempVector, vector.getZ() / tempVector
        );
    }

}
