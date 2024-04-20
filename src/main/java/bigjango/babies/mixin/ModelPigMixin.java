package bigjango.babies.mixin;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.render.model.ModelPig;
import net.minecraft.client.render.model.Cube;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModelPig.class, remap = false)
public abstract class ModelPigMixin extends ModelQuadrupedMixin {
    @Shadow
    public Cube nose;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci) {
        head_y = 4.f;
        super.render(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale, ci);
        if (baby) {
            // Fixes nose
            ci.cancel();
            setRotationAngles(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, scale * head_y, scale * head_z);
            this.nose.render(scale);
            GL11.glPopMatrix();
        }
    }
}