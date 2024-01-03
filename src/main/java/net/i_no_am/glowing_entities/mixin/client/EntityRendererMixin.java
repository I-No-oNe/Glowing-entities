package net.i_no_am.glowing_entities.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.i_no_am.glowing_entities.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

	@Inject(method = "getBlockLight", at = @At("RETURN"), cancellable = true)
	public <T extends Entity> void getLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir){
		int glow = 0;
		if (entity.getWorld().isClient() && MinecraftClient.getInstance().player != null) {
            PlayerEntity player = entity.getWorld().getPlayerByUuid(MinecraftClient.getInstance().player.getUuid());
			IEntityDataSaver playerData = (IEntityDataSaver) player;
			if(playerData != null && playerData.glowing_entities$getPersistentData().contains("glow"))
				glow = playerData.glowing_entities$getPersistentData().getInt("glow");
        }
		if (glow > 15)
			glow = 15;
		if (glow < 0)
			cir.setReturnValue(entity.isOnFire() ? 15 : entity.getWorld().getLightLevel(LightType.BLOCK, pos));
		else cir.setReturnValue(glow);
	}
}