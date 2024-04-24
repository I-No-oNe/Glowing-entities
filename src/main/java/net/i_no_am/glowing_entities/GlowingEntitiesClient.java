package net.i_no_am.glowing_entities;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class GlowingEntitiesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientCommandManager.DISPATCHER.register(
				literal("glowing_effect")
						.then(argument("glowing", IntegerArgumentType.integer(-1, 15))
								.executes(context -> {
									int value = IntegerArgumentType.getInteger(context, "glowing");
									IEntityDataSaver playerData = (IEntityDataSaver) context.getSource().getPlayer();
									if (value == -1 || value == 0) {
										playerData.glowing_entities$getPersistentData().putInt("glow",0);
										context.getSource().sendFeedback(Text.of("§l§6Glowing Entities: §cThe mod is turned off"));
										return 0; // Return 0 to indicate success
									} else {
										playerData.glowing_entities$getPersistentData().putInt("glow", value);
										context.getSource().sendFeedback(Text.of("§l§6Glowing Entities: §bThe Entity Glowing set to: " + value));
										return value;
									}
								})
						)
						.executes(context -> {
							int value = 15;
							IEntityDataSaver playerData = (IEntityDataSaver) context.getSource().getPlayer();
							playerData.glowing_entities$getPersistentData().putInt("glow", value);
							context.getSource().sendFeedback(Text.of("§l§6Glowing Entities: §bThe Entity Glowing set to: " + value));
							return value;
						})
		);
	}
}
