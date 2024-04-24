package net.i_no_am.glowing_entities;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class GlowingEntitiesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
				literal("glowing_effect")
						.then(argument("glowing", IntegerArgumentType.integer(-1, 15))
								.executes(context -> {
									int value = IntegerArgumentType.getInteger(context, "glowing");
									ClientPlayerEntity player = context.getSource().getPlayer();
									IEntityDataSaver playerData = (IEntityDataSaver) player;
									if (value == -1 || value == 0) {
										playerData.glowing_entities$getPersistentData().putInt("glow", 0); // Set glow level to 0
										context.getSource().sendFeedback(Text.literal("§l§6Glowing Entities: §cThe mod is turned off"));
										return 0;
									} else {
										playerData.glowing_entities$getPersistentData().putInt("glow", value);
										context.getSource().sendFeedback(Text.literal("§l§6Glowing Entities: §bThe Entity Glowing set to: " + value));
										return value;
									}
								})
						)
		));
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			ClientPlayerEntity player = client.player;
			if (player != null) {
				IEntityDataSaver playerData = (IEntityDataSaver) player;
				playerData.glowing_entities$getPersistentData().putInt("glow", 15);
			}
		});
	}
}