package net.i_no_am.glowing_entities.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class GlowingEntitiesMixin {

    private static boolean glowingEnabled = true; // Default to true
    private static int customLightLevel = 15; // Default light level

    @Inject(method = "getBlockLight", at = @At("RETURN"), cancellable = true)
    public <T extends Entity> void getLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (glowingEnabled) {
            cir.setReturnValue(customLightLevel);
        }
    }

    // Command to toggle glowing state
    public static void toggleGlowing() {
        glowingEnabled = !glowingEnabled;
        MinecraftClient.getInstance().player.sendMessage(
                glowingEnabled ? "Glowing is now enabled." : "Glowing is now disabled."
        );
    }

    // Command to set custom light level
    public static void setCustomLightLevel(String level) {
        try {
            customLightLevel = Integer.parseInt(level);
            MinecraftClient.getInstance().player.sendMessage("Custom light level set to " + customLightLevel);
        } catch (NumberFormatException e) {
            MinecraftClient.getInstance().player.sendMessage("Invalid light level. Please provide a valid integer.");
        }
    }
}
