package net.i_no_am.glowing_entities;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.i_no_am.glowing_entities.config.ModConfig;
import net.i_no_am.glowing_entities.version.Version;

public class GlowingEntities implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        WorldRenderEvents.AFTER_SETUP.register((context) -> Version.create("I-No-oNe", "glowing_entities", Version.DownloadType.MODRINTH).notifyUpdate(true));
    }
}