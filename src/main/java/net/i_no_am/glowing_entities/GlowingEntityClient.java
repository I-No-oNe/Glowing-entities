package net.i_no_am.glowing_entities;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class GlowingEntityClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("Glowing effect")
				.then(argument("glowing", IntegerArgumentType.integer())
						.executes(context -> {
							int value = IntegerArgumentType.getInteger(context, "glowing");
							IEntityDataSaver playerData = (IEntityDataSaver) context.getSource().getPlayer();
							playerData.glowing_entities$getPersistentData().putInt("glow",value);
							if (value > 15)
								value = 15;
							String glow = value < 0 ? "Normal" : String.valueOf(value);
							context.getSource().sendFeedback(Text.literal("Glowing Entities:The Entity Glowing set to: " + glow).formatted(Formatting.GOLD));
							return value;
						}))));
	}
}