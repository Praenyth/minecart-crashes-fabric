package me.praenyth.mods.minecartcollisions;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecartCollisions implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("MinecartCollisions");

    @Override
    public void onInitialize() {
        LOGGER.info("Ok mod starting!!!!!!!");
    }

    public static float velocityMultiplier(double velocity) {
        return (float) ((velocity - 0.4) / 2);
    }

    public static float velocitySubtractor(double velocity) {
        return (float) (-(Math.pow(0.995, velocity)) + 1);
    }


}
