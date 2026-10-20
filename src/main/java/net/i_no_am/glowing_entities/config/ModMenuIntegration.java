package net.i_no_am.glowing_entities.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (Screen screen) -> AutoConfigClient.getConfigScreen(ModConfig.class, screen).get();
    }
}