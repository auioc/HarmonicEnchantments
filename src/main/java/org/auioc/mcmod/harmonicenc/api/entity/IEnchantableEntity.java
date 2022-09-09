package org.auioc.mcmod.harmonicenc.api.entity;

import java.util.Map;
import net.minecraft.world.item.enchantment.Enchantment;

public interface IEnchantableEntity {

    default Map<Enchantment, Integer> getEnchantments() {
        return null;
    }

    default void addEnchantment(Enchantment ench, int lvl) {}

}
