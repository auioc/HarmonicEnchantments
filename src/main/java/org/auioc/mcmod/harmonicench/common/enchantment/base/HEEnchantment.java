package org.auioc.mcmod.harmonicench.common.enchantment.base;

import java.util.HashMap;
import java.util.function.Predicate;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.common.config.HECommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class HEEnchantment extends AbstractHEEnchantment {

    public HEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots, int maxLevel, Predicate<Enchantment> compatibility) {
        super(rarity, category, validSlots, maxLevel, compatibility);
    }

    public HEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots, Predicate<Enchantment> compatibility) {
        super(rarity, category, validSlots, 1, compatibility);
    }

    public HEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, int maxLevel, Predicate<Enchantment> compatibility) {
        super(rarity, category, new EquipmentSlot[] {validSlot}, maxLevel, compatibility);
    }

    public HEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, Predicate<Enchantment> compatibility) {
        super(rarity, category, validSlot, 1, compatibility);
    }

    public HEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, int maxLevel) {
        super(rarity, category, validSlot, maxLevel, (o) -> true);
    }

    @Override
    protected HashMap<ResourceLocation, BooleanValue> getAcquirabilityMap() {
        return HECommonConfig.ACQUIRABILITY;
    }

    @Override
    protected HashMap<ResourceLocation, BooleanValue> getFunctionalityMap() {
        return HECommonConfig.FUNCTIONALITY;
    }

}
