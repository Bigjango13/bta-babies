package bigjango.babies.mixin;

import bigjango.babies.IAgableMob;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.animal.EntityChicken;
import net.minecraft.core.world.World;
import net.minecraft.core.item.ItemSeeds;
import net.minecraft.core.item.ItemStack;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityChicken.class, remap = false)
public abstract class EntityChickenMixin extends EntityAnimalMixin {
    public EntityChickenMixin(World world) { super(world); }

    @Override
    public boolean babies$canFeed(EntityPlayer player, ItemStack is) {
        if (is == null) return false;
        // Piggys can eat a lot
        return is.getItem() instanceof ItemSeeds;
    }
}
