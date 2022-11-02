package me.praenyth.mods.minecartcollisions.mixin;

import me.praenyth.mods.minecartcollisions.MinecartCollisions;
import me.praenyth.mods.minecartcollisions.damagesource.MinecartDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {

    public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void minecartcollisions$tick(CallbackInfo ci) {

        if (getVelocity().length() > 1) {
            for (Entity entity : this.getWorld().getOtherEntities(this, new Box(
                    getPos().add(2, 2, 2), getPos().add(-2, -2, -2)
            ))) {
                try {
                    if (getFirstPassenger() != null) {
                        entity.damage(MinecartDamageSource.minecartWithPassenger(getFirstPassenger()), 6f);
                    } else {
                        entity.damage(MinecartDamageSource.minecartNoPassenger(), 6f);
                    }
                } catch (CrashException ignored) {

                }
            }
        }

    }

    @Inject(at = @At("RETURN"), method = "getMaxSpeed", cancellable = true)
    public void minecartcollisions$overrideMaxSpeed(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(cir.getReturnValue() * 20);
    }

    @Inject(method = "getVelocityMultiplier", at = @At("RETURN"), cancellable = true)
    private void minecartcollisions$getVelocityMultiplier(CallbackInfoReturnable<Float> cir) {

        Vec3d velocityVec = this.getVelocity();
        double velocity = Math.sqrt(Math.pow(velocityVec.x, 2) + Math.pow(velocityVec.y, 2) + Math.pow(velocityVec.z, 2));

        if (velocity > 0.4) {

            cir.setReturnValue((float) velocity * 1.95f);

        }
    }

}