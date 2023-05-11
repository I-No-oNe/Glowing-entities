package net.qlient.autototem.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class autototem implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("AutoTotem");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Loaded!");
	}
}
