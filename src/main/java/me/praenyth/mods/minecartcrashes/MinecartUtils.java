package me.praenyth.mods.minecartcrashes;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MinecartUtils {

    /**
     *
     * @author CammiePone
     * @param minecart
     * @param world
     * @return A boolean value for whether the minecart should slow down
     */
    public static boolean shouldSlowDown(AbstractMinecartEntity minecart, World world) {
        boolean slowEm = false;

        if(minecart != null) {
            int velocity = MathHelper.ceil(minecart.getVelocity().horizontalLength());
            Direction direction = Direction.getFacing(minecart.getVelocity().getX(), 0, minecart.getVelocity().getZ());
            BlockPos minecartPos = minecart.getBlockPos();
            Vec3i pain = new Vec3i(minecartPos.getX(), 0, minecartPos.getZ());
            BlockPos.Mutable pos = new BlockPos.Mutable();
            List<Vec3i> poses = new ArrayList<>();

            poses.add(minecartPos);

            for(int i = 0; i < poses.size(); i++) {
                pos.set(poses.get(i));
                int distance = pain.getManhattanDistance(new Vec3i(pos.getX(), 0, pos.getZ()));

                if(distance > velocity)
                    break;

                if(world.getBlockState(pos.down()).isIn(BlockTags.RAILS))
                    pos.move(0, -1, 0);

                BlockState state = world.getBlockState(pos);

                if(state.isIn(BlockTags.RAILS) && state.getBlock() instanceof AbstractRailBlock rails) {
                    RailShape shape = state.get(rails.getShapeProperty());

                    if((shape != RailShape.NORTH_SOUTH && shape != RailShape.EAST_WEST)) {
                        slowEm = true;
                        break;
                    }

                    Pair<Vec3i, Vec3i> pair = AbstractMinecartEntity.getAdjacentRailPositionsByShape(shape);
                    Vec3i first = pair.getFirst().add(pos);
                    Vec3i second = pair.getSecond().add(pos);

                    if(distance < 2) {
                        if(!poses.contains(first))
                            poses.add(first);
                        if(!poses.contains(second))
                            poses.add(second);

                        continue;
                    }

                    if((shape == RailShape.NORTH_SOUTH && direction == Direction.NORTH) || (shape == RailShape.EAST_WEST && direction == Direction.WEST)) {
                        if(!poses.contains(first))
                            poses.add(first);
                    }
                    else {
                        if(!poses.contains(second))
                            poses.add(second);
                    }
                }
            }
        }

        return slowEm;
    }

    /**
     * Checks if the entity is a minecart
     * (please let me know if there was an easier way of doing this)
     * @param entity
     * @return boolean for whether the entity is a minecart
     */
    public static boolean checkIfMinecart(Entity entity) {
        if (
                entity.getType().equals(EntityType.MINECART)
                || entity.getType().equals(EntityType.CHEST_MINECART)
                || entity.getType().equals(EntityType.TNT_MINECART)
                || entity.getType().equals(EntityType.HOPPER_MINECART)
                || entity.getType().equals(EntityType.SPAWNER_MINECART)
                || entity.getType().equals(EntityType.COMMAND_BLOCK_MINECART)
                || entity.getType().equals(EntityType.FURNACE_MINECART)
        ) {
            return true;
        } else {
            return false;
        }
    }

}
