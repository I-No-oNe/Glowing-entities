package net.i_no_am.glowing_entities.mixins;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(glowing_entities.class)
public abstract class glowing_entities {

@Inject(method = "getBlockLight", at = @At("RETURN"), cancellable = true)
public <T extends Entity> void getLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
    cir.setReturnValue(15);
}
