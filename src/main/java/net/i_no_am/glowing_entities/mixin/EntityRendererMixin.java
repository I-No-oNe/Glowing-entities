package net.i_no_am.glowing_entities.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.glowing_entities.Global;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin implements Global {

    @ModifyReturnValue(method = "getBlockLightLevel", at = @At("RETURN"))
    public <T extends Entity> int modifyGetBlockLight(int original, T entity, BlockPos pos) {
        if (mc.player == null || !config.enable) return original;
        return config.glowLevel;
    }
}
