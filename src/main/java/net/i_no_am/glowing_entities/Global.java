package net.i_no_am.glowing_entities;

import me.shedaniel.autoconfig.AutoConfig;
import net.i_no_am.glowing_entities.config.ModConfig;
import net.minecraft.client.Minecraft;

public interface Global {
    Minecraft mc = Minecraft.getInstance();
    ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
}
