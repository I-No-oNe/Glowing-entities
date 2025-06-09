package net.i_no_am.glowing_entities.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "glowing_entities")
public class ModConfig implements ConfigData {

    @Comment("Enable or disable glowing entities feature")
    @ConfigEntry.Gui.Tooltip
    public boolean enable = true;

    @Comment("Set the glow level for entities (1-15)")
    @ConfigEntry.Gui.Tooltip()
    @ConfigEntry.BoundedDiscrete(min = 1, max = 15)
    public int glowLevel = 15;

    @Comment("Toggle the mod checker, THIS WILL NOT BE SHOWN IN THE CONFIG GUI!")
    @ConfigEntry.Gui.Excluded
    public boolean shouldCheck = true;
}