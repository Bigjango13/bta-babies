package bigjango.babies;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;

public interface IAgableMob {
    // Aging
    public int babies$getAge();
    public void babies$setAge(int age);
    public boolean babies$isBaby();
    public boolean babies$agable();
    // Both
    public boolean babies$dependsOnParent();
    // Breeding
    public int babies$getLoveTimer();
    public void babies$setLoveTimer(int loveTimer);
    public boolean babies$canFeed(EntityPlayer player, ItemStack is);
    public void babies$feed(EntityPlayer player, ItemStack is);
    public boolean babies$breedable();
    public Entity babies$giveBirth(Entity father);
    public boolean babies$canBreedWith(Entity e);
}
