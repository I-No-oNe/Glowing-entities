package net.i_no_am.glowing_entities;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class GlowingEntitiesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientCommandManager.DISPATCHER.register(
				literal("glowing_effect")
						.then(argument("glowing", IntegerArgumentType.integer())
								.executes(context -> {
									int value = IntegerArgumentType.getInteger(context, "glowing");
									IEntityDataSaver playerData = (IEntityDataSaver) context.getSource().getPlayer();
									playerData.getPersistentData().putInt("glow", value);
									if (value > 15)
										value = 15;
									String glow = value < 0 ? "Normal" : String.valueOf(value);
									context.getSource().sendFeedback(Text.of("§l§6Glowing Entities: §bThe Entity Glowing set to: " + glow));
									return value;
								})
						)
		);
	}
}
