package bigjango.babies.mixin;

import net.minecraft.client.render.model.ModelBase;

import bigjango.babies.IModelBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModelBase.class, remap = false)
public abstract class ModelBaseMixin implements IModelBase {
	@Unique
	boolean baby = false;
    @Override
    public boolean babies$getIsBaby() { return baby; };
    @Override
    public void babies$setIsBaby(boolean baby) { this.baby = baby; };

    @Inject(method = "render", at = @At("HEAD"))
    public void render(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci) {}
}
