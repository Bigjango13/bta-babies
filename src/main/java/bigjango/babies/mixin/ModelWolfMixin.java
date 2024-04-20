package bigjango.babies.mixin;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.render.model.ModelWolf;
import net.minecraft.client.render.model.Cube;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModelWolf.class, remap = false)
public abstract class ModelWolfMixin extends ModelBaseMixin {
    @Shadow
    Cube wolfHeadMain, wolfBody, wolfLeg1, wolfLeg2, wolfLeg3, wolfLeg4, wolfRightEar, wolfLeftEar, wolfSnout, wolfTail, wolfMane;

    @Shadow
    public abstract void setRotationAngles(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale);

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci) {
        if (baby) {
            ci.cancel();
            super.render(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale, ci);
            setRotationAngles(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, scale * 5, scale * 2);
            wolfHeadMain.renderWithRotation(scale);
            wolfRightEar.renderWithRotation(scale);
            wolfLeftEar.renderWithRotation(scale);
            wolfSnout.renderWithRotation(scale);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(.5f, .5f, .5f);
            GL11.glTranslatef(0, scale * 24, 0);
            wolfMane.render(scale);
            wolfBody.render(scale);
            wolfLeg1.render(scale);
            wolfLeg2.render(scale);
            wolfLeg3.render(scale);
            wolfLeg4.render(scale);
            wolfTail.renderWithRotation(scale);
            GL11.glPopMatrix();
        }
    }
}
