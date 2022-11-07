package me.praenyth.mods.minecartcollisions.mixin;

import me.praenyth.mods.minecartcollisions.MinecartCollisions;
import me.praenyth.mods.minecartcollisions.MinecartUtils;
import me.praenyth.mods.minecartcollisions.damagesource.MinecartDamageSource;
import me.praenyth.mods.minecartcollisions.gamerule.MinecartGamerules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {

    @Shadow public abstract Direction getMovementDirection();

    public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void minecartcollisions$tick(CallbackInfo ci) {

        Vec3d cartVelo = getVelocity();

        // damage mechanic
        if (cartVelo.length() > 1) {
            for (Entity entity : this.getWorld().getOtherEntities(this, new Box(
                    getPos().add(1.5, 1.5, 1.5), getPos().add(-1.5, -1, -1.5)
            ))) {
                try {

                    float damage = world.getGameRules().getInt(MinecartGamerules.MINECART_DAMAGE);

                    if (entity.getVehicle() != this) {
                        if (!entity.getType().getTranslationKey().contains("minecart")) {
                            if (getFirstPassenger() != null) {
                                entity.damage(MinecartDamageSource.minecartWithPassenger(getFirstPassenger()), damage);
                            } else {
                                entity.damage(MinecartDamageSource.minecartNoPassenger(), damage);
                            }
                        }
                    }

                    entity.getWorld().playSound(
                            getX(), getY(), getZ(),
                            new SoundEvent(new Identifier(
                                    "minecartcrashes", MinecartCollisions.config.sound
                            )),
                            SoundCategory.NEUTRAL,
                            1f,
                            1f,
                            true
                    );
                    entity.setVelocity(getVelocity().add(0, 0.5, 0));

                } catch (CrashException ignored) {

                }
            }
        }

    }

    @Inject(at = @At("RETURN"), method = "getMaxSpeed", cancellable = true)
    public void minecartcollisions$overrideMaxSpeed(CallbackInfoReturnable<Double> cir) {

        boolean useDefaultSpeed = MinecartUtils.shouldSlowDown((AbstractMinecartEntity) getWorld().getEntityById(getId()), getWorld());

        if (!useDefaultSpeed) {
            cir.setReturnValue(cir.getReturnValue() * world.getGameRules().getInt(MinecartGamerules.MAX_SPEED_MULTIPLIER));
        }

    }

    @Inject(method = "getVelocityMultiplier", at = @At("RETURN"), cancellable = true)
    private void minecartcollisions$getVelocityMultiplier(CallbackInfoReturnable<Float> cir) {

        Vec3d velocityVec = this.getVelocity();
        double velocity = Math.sqrt(Math.pow(velocityVec.x, 2) + Math.pow(velocityVec.y, 2) + Math.pow(velocityVec.z, 2));

        if (velocity > 0.4) {

            float friction = 1 - cir.getReturnValue();

            float velocityMultiplier = MinecartCollisions.velocityMultiplier(velocity);
            float velocitySubtractor = MinecartCollisions.velocitySubtractor(velocity);

            float veloMul = 1 + (friction * velocityMultiplier) - velocitySubtractor;

            cir.setReturnValue(veloMul);

        }

    }

}