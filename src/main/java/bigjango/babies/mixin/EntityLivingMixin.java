package bigjango.babies.mixin;

import bigjango.babies.IAgableMob;

import net.minecraft.core.entity.EntityLiving;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = EntityLiving.class, remap = false)
public abstract class EntityLivingMixin {
    // Makes the pitch higher
    @Unique
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
}
