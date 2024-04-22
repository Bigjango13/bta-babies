package bigjango.babies.mixin;

import bigjango.babies.IAgableMob;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.animal.EntityAnimal;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = EntityAnimal.class, remap = false)
public abstract class EntityAnimalMixin extends EntityPathfinderMixin implements IAgableMob {
    public EntityAnimalMixin(World world) { super(world); }

    // Aging
    @Unique int age = 0;
    @Override
    public int babies$getAge() {
        return babies$agable() ? age : 0;
    }
    @Override public void babies$setAge(int age) { this.age = age; }
    @Override public boolean babies$agable() { return true; }
    @Override
    public boolean babies$isBaby() {
        return babies$agable() ? age < 0 : false;
    }
    @Override public boolean babies$dependsOnParent() { return babies$isBaby(); }

    // Breeding
    @Unique int loveTimer = 0;
    @Override
    public int babies$getLoveTimer() {
        return babies$breedable() ? loveTimer : 0;
    };
    @Override
    public boolean babies$breedable() {
        return babies$getAge() == 0;
    }
    @Override
    public void babies$setLoveTimer(int loveTimer) { this.loveTimer = loveTimer; }
    @Override
    public boolean babies$canFeed(EntityPlayer player, ItemStack is) {
        return is != null && is.getItem() == Item.wheat;
    }
    @Override
    public void babies$feed(EntityPlayer player, ItemStack is) {
        // The wording is ironic
        is.consumeItem(player);
    }
    @Override
    public boolean babies$canBreedWith(Entity e) { return e.getClass().isInstance(this); }
    @Override
    public Entity babies$giveBirth(Entity father) {
        Entity baby = null;
        try {
            baby = this.getClass().getConstructor(World.class).newInstance(world);
        } catch (Exception e) {
            System.out.println("[ERROR]: Failed to construct entity in babies$giveBirth!");
		}
        if (baby == null || father == null) return baby;
        ((EntityLiving)baby).setSkinVariant(((EntityLiving)father).getSkinVariant());
        if (random.nextInt(2) == 0) {
            ((EntityLiving)baby).setSkinVariant(this.getSkinVariant());
        }
        return baby;
    }
    @Override public boolean canDespawn() { return false; }
}
