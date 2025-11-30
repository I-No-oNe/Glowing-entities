package net.i_no_am.glowing_entities;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.i_no_am.glowing_entities.config.ModConfig;
import net.i_no_am.glowing_entities.version.Version;

public class GlowingEntities implements ClientModInitializer, Global {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        WorldRenderEvents.END_MAIN.register((context) ->
                Version.builder()
                        .name("Glowing-Entities")
                        .modId("glowing-entities")
                        .gitUsername("I-No-oNe")
                        .downloadSource("https://modrinth.com/mod/glowing-entities")
                        .printVersions(true)
                        .condition(() -> config.shouldCheck)
                        .build()
                        .notifyUpdates());
    }
}