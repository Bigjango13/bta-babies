package bigjango.babies.mixin;

import bigjango.babies.IAgableMob;

import net.minecraft.core.entity.EntityLiving;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EntityLiving.class, remap = false)
public class EntityLivingMixin {
    // Makes the pitch higher
    private float get_pitch(float x) {
        if (this instanceof IAgableMob && ((IAgableMob) this).babies$isBaby()) {
            return 1.5f;
        }
        return 1.0f;
    }

    @ModifyConstant(method = "playLivingSound", constant = @Constant(floatValue = 1.0F))
    private float get_playLivingSound_pitch(float x) { return get_pitch(x); }
    @ModifyConstant(method = "playHurtSound",   constant = @Constant(floatValue = 1.0F))
    private float get_playHurtSound_pitch  (float x) { return get_pitch(x); }
    @ModifyConstant(method = "playDeathSound",  constant = @Constant(floatValue = 1.0F))
    private float get_playDeathSound_pitch (float x) { return get_pitch(x); }


    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void onLivingUpdate(CallbackInfo ci) {
        if (this instanceof IAgableMob) {
            IAgableMob e = (IAgableMob) this;
            int age = e.babies$getAge();
            if (age < 0) {
                e.babies$setAge(age + 1);
            } else if (age > 0) {
                e.babies$setAge(age - 1);
            }
        }
    }
}
