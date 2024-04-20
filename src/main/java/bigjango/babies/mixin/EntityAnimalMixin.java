package bigjango.babies.mixin;

import bigjango.babies.IAgableMob;

import net.minecraft.core.entity.animal.EntityAnimal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = EntityAnimal.class, remap = false)
public class EntityAnimalMixin implements IAgableMob {
    @Unique
    int age = -200;

    @Override
    public int babies$getAge() {
        return age;
    }

    @Override
    public void babies$setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean babies$isBaby() {
        return age < 0;
    }
}
