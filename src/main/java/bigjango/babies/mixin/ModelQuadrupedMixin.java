package bigjango.babies.mixin;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.render.model.ModelQuadruped;
import net.minecraft.client.render.model.Cube;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModelQuadruped.class, remap = false)
public abstract class ModelQuadrupedMixin extends ModelBaseMixin {
    @Unique
    float head_y = 8.f;
    @Unique
    float head_z = 4.f;

    @Shadow
    public Cube head, body, leg1, leg2, leg3, leg4;
    @Shadow
    public abstract void setRotationAngles(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale);

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci) {
        if (baby) {
            ci.cancel();
            setRotationAngles(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, scale * head_y, scale * head_z);
            this.head.render(scale);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(.5f, .5f, .5f);
            GL11.glTranslatef(0, scale * 24, 0);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
            GL11.glPopMatrix();
        }
    }
}