package me.praenyth.mods.minecartcrashes.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;

public class MinecartDamageSource {

    public static final RegistryEntry<DamageType> MINECART_NO_PASSENGER = RegistryEntry.of(new DamageType("minecart_no_passenger", 0.0f));

    public static final RegistryEntry<DamageType> MINECART_PASSENGER = RegistryEntry.of(new DamageType("minecart_passenger", 0.1f));

    //public static final RegistryKey<DamageType> MINECART_DAMAGE_SOURCE =
    //        RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("minecartcrashes", "minecart"));

    public static DamageSource minecartDamageSource() {
        return new DamageSource(MINECART_NO_PASSENGER);
    }

    public static DamageSource minecartPassengerDamageSource(Entity source) {
        return new DamageSource(MINECART_PASSENGER, source);
    }

}
