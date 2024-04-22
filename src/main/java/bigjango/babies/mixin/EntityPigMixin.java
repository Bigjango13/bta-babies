package bigjango.babies.mixin;

import bigjango.babies.IAgableMob;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.animal.EntityPig;
import net.minecraft.core.world.World;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityPig.class, remap = false)
public abstract class EntityPigMixin extends EntityAnimalMixin {
    public EntityPigMixin(World world) { super(world); }

    @Override
    public boolean babies$canFeed(EntityPlayer player, ItemStack is) {
        if (is == null) return false;
        // Piggys can eat a lot
        return is.itemID == Block.mushroomBrown.id
            || is.itemID == Block.mushroomRed.id
            || is.itemID == Block.pumpkin.id
            || is.itemID == Block.pumpkinCarvedIdle.id
            || is.itemID == Item.foodApple.id;
    }
}
