package net.i_no_am.glowing_entities.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.glowing_entities.Global;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin implements Global {

    @ModifyReturnValue(method = "getBlockLight", at = @At("RETURN"))
    public <T extends Entity> int modifyGetBlockLight(int original, T entity) {
        if (mc.player == null || entity.getEntityWorld() == null || !config.enable) return original;
        return config.glowLevel;
    }
}
