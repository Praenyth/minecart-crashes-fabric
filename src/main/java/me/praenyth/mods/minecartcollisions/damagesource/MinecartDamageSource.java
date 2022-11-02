package me.praenyth.mods.minecartcollisions.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

public class MinecartDamageSource {

    public static DamageSource minecartNoPassenger() {
        return new DamageSource("minecart");
    }

    public static EntityDamageSource minecartWithPassenger(Entity source) {
        return new EntityDamageSource("minecart.passenger", source);
    }

}
