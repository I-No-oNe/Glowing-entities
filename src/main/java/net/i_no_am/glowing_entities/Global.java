package net.i_no_am.glowing_entities;

import me.shedaniel.autoconfig.AutoConfig;
import net.i_no_am.glowing_entities.config.ModConfig;
import net.minecraft.client.MinecraftClient;

public interface Global {
    MinecraftClient mc = MinecraftClient.getInstance();
    ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
}
