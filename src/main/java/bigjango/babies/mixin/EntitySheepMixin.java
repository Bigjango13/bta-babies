package bigjango.babies.mixin;

import bigjango.babies.IAgableMob;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.animal.EntitySheep;
import net.minecraft.core.world.World;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntitySheep.class, remap = false)
public abstract class EntitySheepMixin extends EntityAnimalMixin {
    public EntitySheepMixin(World world) { super(world); }

    @Shadow
    public abstract int getFleeceColor();
    @Override
    public Entity babies$giveBirth(Entity father) {
        Entity baby = super.babies$giveBirth(father);
        if (baby == null) return baby;
        // @todo: Color mixing?
        EntitySheep f = (EntitySheep) father;
        ((EntitySheep) baby).setFleeceColor(((EntitySheep) father).getFleeceColor());
        if (random.nextInt(2) == 0) {
            ((EntitySheep) baby).setFleeceColor(getFleeceColor());
        }
        return baby;
    }
}
