package net.i_no_am.glowing_entities.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.i_no_am.glowing_entities.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;

    @Override
    public NbtCompound glowing_entities$getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    @Unique
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<Void> info) {
        if (persistentData != null) {
            nbt.put("glowing_entities.i_no_am_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    @Unique
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("glowing_entities.i_no_am_data", 10)) {
            persistentData = nbt.getCompound("glowing_entities.i_no_am_data");
        }
    }
}
