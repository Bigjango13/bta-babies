package bigjango.babies.mixin;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.render.model.ModelChicken;
import net.minecraft.client.render.model.Cube;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModelChicken.class, remap = false)
public abstract class ModelChickenMixin extends ModelBaseMixin {
    @Shadow
    public Cube head, body, rightLeg, leftLeg, rightWing, leftWing, bill, chin;

    @Shadow
    public abstract void setRotationAngles(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale);

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci) {
        if (baby) {
            ci.cancel();
            setRotationAngles(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, scale * 5, scale * 2);
            head.render(scale);
            bill.render(scale);
            chin.render(scale);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(.5f, .5f, .5f);
            GL11.glTranslatef(0, scale * 24, 0);
            body.render(scale);
            rightLeg.render(scale);
            leftLeg.render(scale);
            rightWing.render(scale);
            leftWing.render(scale);
            GL11.glPopMatrix();
        }
    }
}
