package me.praenyth.mods.minecartcrashes.config;

import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigFile {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    public String sound = "this doesn't do anything currently, come back later when i figure this out";

    public static ConfigFile loadConfigFile(File file) {
        ConfigFile config = null;

        if (file.exists()) {

            try (BufferedReader fileReader = new BufferedReader(

                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)

            )) {

                config = gson.fromJson(fileReader, ConfigFile.class);

            } catch (IOException e) {

                throw new RuntimeException("[Minecraft Crashes] erm... awkward! ", e);

            }
        }

        if (config == null) {
            config = new ConfigFile();
        }

        config.saveConfigFile(file);
        return config;
    }

    public void saveConfigFile(File file) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
