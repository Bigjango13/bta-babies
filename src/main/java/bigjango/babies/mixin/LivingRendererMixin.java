package bigjango.babies.mixin;

import bigjango.babies.IAgableMob;
import bigjango.babies.IModelBase;

import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.client.render.model.ModelBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = LivingRenderer.class, remap = false)
public class LivingRendererMixin {
	@Unique
	EntityLiving entity = null;

    @Shadow
    protected ModelBase mainModel, renderPassModel, overlayModel;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(EntityLiving entity, double x, double y, double z, float yaw, float partialTick, CallbackInfo ci) {
        this.entity = entity;
    }

    @ModifyVariable(method = "render", at = @At("STORE"), name = "limbSwing")
    private float set_limbSwing(float x) {
        boolean baby = entity != null && entity instanceof IAgableMob && ((IAgableMob) (Object) entity).babies$isBaby();
        if (mainModel != null)       ((IModelBase) (Object) mainModel).babies$setIsBaby(baby);
        if (renderPassModel != null) ((IModelBase) (Object) renderPassModel).babies$setIsBaby(baby);
        if (overlayModel != null)    ((IModelBase) (Object) overlayModel).babies$setIsBaby(baby);
        if (baby) {
            return x * 3.0f;
        }
        return x;
    }
}
