package bigjango.babies.mixin;

import bigjango.babies.IAgableMob;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.EntityPathfinder;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.animal.EntityWolf;
import net.minecraft.core.world.World;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemStack;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EntityWolf.class, remap = false)
public abstract class EntityWolfMixin extends EntityAnimalMixin {
    public EntityWolfMixin(World world) { super(world); }

    @Shadow
    public abstract String getWolfOwner();
    @Override
    public boolean babies$canFeed(EntityPlayer player, ItemStack is) {
        return
            // Player must be the owner
            player.username.equalsIgnoreCase(this.getWolfOwner())
            // Item must be edible
            && is != null && is.getItem() instanceof ItemFood
            && ((ItemFood) is.getItem()).getIsWolfsFavoriteMeat()
            // Don't break healing
            && getHealth() >= getMaxHealth();
    }

    @Override
    public Entity babies$giveBirth(Entity father) {
        Entity baby = super.babies$giveBirth(father);
        if (baby == null) return baby;
        // Randomize owner, normally this will be the player itself
        EntityWolf f = (EntityWolf) father;
        ((EntityWolf) baby).setWolfOwner(((EntityWolf) father).getWolfOwner());
        if (random.nextInt(2) == 0) {
            ((EntityWolf) baby).setWolfOwner(getWolfOwner());
        }
        ((EntityWolf) baby).setWolfTamed(true);
        return baby;
    }

    @Inject(method = "findPlayerToAttack", at = @At("HEAD"), cancellable = true)
    protected void __findPlayerToAttack(CallbackInfoReturnable<Entity> cir) {
        if (babies$breedable() && babies$getLoveTimer() > 0) {
            Entity pta = real_findPlayerToAttack();
            if (pta != null && !isMovementCeased()) {
                cir.setReturnValue(pta);
            }
        }
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    void __attackEntity(Entity entity, float distance, CallbackInfo ci) {
        if (real_attackEntity(entity, distance)) {
            ci.cancel();
        }
    }

}
