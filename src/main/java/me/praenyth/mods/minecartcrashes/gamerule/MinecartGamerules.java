package me.praenyth.mods.minecartcrashes.gamerule;

import me.praenyth.mods.minecartcrashes.MinecartCrashes;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class MinecartGamerules {

    public static final GameRules.Key<GameRules.IntRule> MINECART_DAMAGE =
            GameRuleRegistry.register("minecartDamage", GameRules.Category.MISC, GameRuleFactory.createIntRule(12));

    public static final GameRules.Key<GameRules.IntRule> MAX_SPEED_MULTIPLIER =
            GameRuleRegistry.register("minecartMaxSpeedMultiplier", GameRules.Category.MISC, GameRuleFactory.createIntRule(20));

    public static void registerGamerules() {
        MinecartCrashes.LOGGER.info("Registering the gamerules!!!");
    }

}
