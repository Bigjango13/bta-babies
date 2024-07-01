package bigjango.babies.mixin;

import bigjango.babies.IAgableMob;

import java.util.Random;
import java.util.List;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.EntityPathfinder;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import com.mojang.nbt.CompoundTag;
import net.minecraft.core.world.pathfinder.Path;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EntityPathfinder.class, remap = false)
public abstract class EntityPathfinderMixin extends EntityLiving {
    public EntityPathfinderMixin(World world) { super(world); }

    @Unique
    boolean passiveTarget = false;

    // Breeding
    @Unique
    private void spawnHearts() {
        double xa = random.nextGaussian() * 0.02;
        double ya = random.nextGaussian() * 0.02;
        double za = random.nextGaussian() * 0.02;
        this.world.spawnParticle(
            "heart",
            x + random.nextFloat() * bbWidth * 2 - bbWidth,
            y + .5f + random.nextFloat() * bbHeight,
            z + random.nextFloat() * bbWidth * 2 - bbWidth,
            xa, ya, za, 0
        );
    }

    @Shadow
    protected Entity entityToAttack;
    @Shadow
    protected Path pathToEntity;
    @Shadow
    protected abstract Entity findPlayerToAttack();
    @Inject(method = "updatePlayerActionState", at = @At("HEAD"))
	protected void _updatePlayerActionState(CallbackInfo ci) {
		if (passiveTarget == true && entityToAttack != null) {
			pathToEntity = world.getPathToEntity(this, entityToAttack, 20);
		}
	}
    @Override
    public void onLivingUpdate() {
        // Check target
        if (this instanceof IAgableMob) {
            IAgableMob e = (IAgableMob) this;
            if (entityToAttack instanceof EntityPlayer && passiveTarget) {
                if (!e.babies$canFeed((EntityPlayer) entityToAttack, ((EntityPlayer) entityToAttack).getHeldItem())) {
                    entityToAttack = null;
                    passiveTarget = false;
                }
            }
            // Tick age
            if (e.babies$agable()) {
                int age = e.babies$getAge();
                if (age < 0 && e.babies$agable()) {
                    e.babies$setAge(age + 1);
                    e.babies$setLoveTimer(0);
                } else if (age > 0) {
                    e.babies$setAge(age - 1);
                    e.babies$setLoveTimer(0);
                }
            }
            // Tick love
            if (e.babies$breedable()) {
                int inLove = e.babies$getLoveTimer();
                if (inLove > 0) {
                    inLove--;
                    if (inLove % 10 == 0) spawnHearts();
                } else {
                    inLove = 0;
                }
                e.babies$setLoveTimer(inLove);
            }
            // Check pathfinding
            if (
                entityToAttack == null && (
                    (e.babies$breedable() && e.babies$getLoveTimer() > 0)
                    || e.babies$dependsOnParent()
                )
            ) {
                entityToAttack = findPlayerToAttack();
            }

        }

        super.onLivingUpdate();
    }

    @Unique
    public Entity real_findPlayerToAttack() {
        passiveTarget = false;
        if (this instanceof IAgableMob) {
            IAgableMob e = (IAgableMob) this;
            float r = 8.f;
            List<Entity> entites = world.getEntitiesWithinAABBExcludingEntity(this, bb.expand(r, r, r));
            if (e.babies$breedable() && e.babies$getLoveTimer() > 0) {
                // Find a willing partner
                for (Entity partner : entites) {
                    if (!(partner instanceof IAgableMob)) continue;
                    IAgableMob p = (IAgableMob) partner;
                    if (
                        p.babies$canBreedWith(this)
                        && p.babies$breedable()
                        && p.babies$getLoveTimer() > 0
                    ) {
                        passiveTarget = true;
                        return partner;
                    }
                }
            }
            if (e.babies$getAge() == 0) {
                // Find a player with food
                for (Entity p : entites) {
                    if (p instanceof EntityPlayer && e.babies$canFeed((EntityPlayer) p, ((EntityPlayer) p).getHeldItem())) {
                        passiveTarget = true;
                        return p;
                    }
                }
            } else if (e.babies$dependsOnParent()) {
                // Find your mother
                for (Entity parent : entites) {
                    if (!(parent instanceof IAgableMob)) continue;
                    IAgableMob p = (IAgableMob) parent;
                    if (p.babies$canBreedWith(this) && p.babies$getAge() >= 0) {
                        passiveTarget = true;
                        return parent;
                    }
                }
            }
        }
        return null;
    }

    @Shadow
    protected abstract boolean isMovementCeased();
    @Inject(method = "findPlayerToAttack", at = @At("HEAD"), cancellable = true)
    public void _findPlayerToAttack(CallbackInfoReturnable<Entity> cir) {
        Entity pta = real_findPlayerToAttack();
        if (pta != null && !isMovementCeased()) {
            cir.setReturnValue(pta);
        }
    }

    @Unique
    public boolean real_attackEntity(Entity entity, float distance) {
        // @todo: anti-instabreeding?
        if (world.isClientSide) return false;
        if (
            // Attacking checks
            distance < 2.0F && entity.bb.maxY > this.bb.minY
            && entity.bb.minY < this.bb.maxY
            // Breeding check
            && this instanceof IAgableMob
            && entity instanceof IAgableMob
            && entity == entityToAttack
            && passiveTarget
        ) {
            // More breeding checks
            IAgableMob e = (IAgableMob) this;
            if (
                e.babies$breedable()
                && e.babies$getLoveTimer() > 0
                && e.babies$canBreedWith(entity)
                && ((IAgableMob) entity).babies$getLoveTimer() > 0
            ) {
                // Spawn the baby
                Entity baby = e.babies$giveBirth(entity);
                if (baby != null) {
                    ((IAgableMob) baby).babies$setAge(-20 * 60 * 20);
                    baby.moveTo(x, y, z, yRot, xRot);
                    for (int i = 0; i < 7; i++) spawnHearts();
                    world.entityJoinedWorld(baby);
                }
                // Take a breaking from making kids
                e.babies$setAge(5 * 60 * 20);
                e.babies$setLoveTimer(0);
                entityToAttack = null;
                ((IAgableMob) entity).babies$setAge(5 * 60 * 20);
                ((IAgableMob) entity).babies$setLoveTimer(0);
                ((EntityPathfinder) entity).setTarget(null);
                return true;
            }
        } else if (passiveTarget) {
            return true;
        }
        return false;
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    void _attackEntity(Entity entity, float distance, CallbackInfo ci) {
        if (real_attackEntity(entity, distance)) {
            ci.cancel();
        }
    }

    // Saving/loading age
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this instanceof IAgableMob && ((IAgableMob) this).babies$agable()) {
            tag.putShort("Age_J", (short) ((IAgableMob) this).babies$getAge());
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (this instanceof IAgableMob && ((IAgableMob) this).babies$agable()) {
            if (!tag.containsKey("Age_J")) {
                ((IAgableMob) this).babies$setAge(0);
            } else {
                ((IAgableMob) this).babies$setAge(tag.getShort("Age_J"));
            }
        }
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack is = player.inventory.getCurrentItem();
        if (is != null && this instanceof IAgableMob && ((IAgableMob) this).babies$canFeed(player, is)) {
            IAgableMob e = (IAgableMob) this;
            if (e.babies$isBaby()) {
                // Age up by 30 secs
                e.babies$setAge(Math.min(0, e.babies$getAge() + (30 * 20)));
                e.babies$feed(player, is);
                spawnHearts();
            } else if (e.babies$breedable()) {
                // Fall in love for 30 secs
                e.babies$setLoveTimer(20 * 30);
                for (int i = 0; i < 7; i++) spawnHearts();
                entityToAttack = null;
                e.babies$feed(player, is);
            }
            return true;
        }
        return super.interact(player);
    }

    @Override
    public void handleEntityEvent(byte event, float a) {
        if (event == 47 && this instanceof IAgableMob) {
            ((IAgableMob) this).babies$setAge((int) a);
        } else {
            super.handleEntityEvent(event, a);
        }
    }
}
