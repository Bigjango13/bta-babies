package bigjango.babies.mixin;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.render.model.ModelCow;
import net.minecraft.client.render.model.Cube;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModelCow.class, remap = false)
public abstract class ModelCowMixin extends ModelQuadrupedMixin {
    @Shadow
    public Cube udders, horn1, horn2;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci) {
        head_z = 6.f;
        super.render(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale, ci);
        if (baby) {
            // Fixes utters and horns
            ci.cancel();
            setRotationAngles(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, scale * head_y, scale * head_z);
            this.horn1.render(scale);
            this.horn2.render(scale);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(.5f, .5f, .5f);
            GL11.glTranslatef(0, scale * 24, 0);
            this.udders.render(scale);
            GL11.glPopMatrix();
        }
    }
}
