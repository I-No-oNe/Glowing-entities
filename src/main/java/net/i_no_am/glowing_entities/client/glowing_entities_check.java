package net.i_no_am.glowing_entities.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class glowing_entities_check implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("glowing_entities_check");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Loaded!");
	}
}
