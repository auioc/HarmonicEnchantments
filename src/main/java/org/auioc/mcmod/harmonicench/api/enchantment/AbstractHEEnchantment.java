package org.auioc.mcmod.harmonicench.api.enchantment;

import java.util.function.Predicate;
import org.auioc.mcmod.arnicalib.game.enchantment.IValidSlotsVisibleEnchantment;
import org.auioc.mcmod.harmonicench.api.config.NullableBooleanValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractHEEnchantment extends Enchantment implements IValidSlotsVisibleEnchantment, IConfigurableEnchantment {

    protected final int maxLevel;
    protected final Predicate<Enchantment> compatibility;
    protected final NullableBooleanValue isEnabled = new NullableBooleanValue(() -> HEEnchantmentManager.getConfigEnabled(getRegistryName()));

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots, int maxLevel, Predicate<Enchantment> compatibility) {
        super(rarity, category, validSlots);
        this.maxLevel = maxLevel;
        this.compatibility = compatibility;
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot, int maxLevel, Predicate<Enchantment> compatibility) {
        this(rarity, category, new EquipmentSlot[] {validSlot}, maxLevel, compatibility);
    }

    public AbstractHEEnchantment() {
        this(Rarity.COMMON, EnchantmentCategory.VANISHABLE, EquipmentSlot.MAINHAND, 1, (o) -> true);
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    protected boolean checkCompatibility(Enchantment otherEnchantment) {
        return super.checkCompatibility(otherEnchantment) && compatibility.test(otherEnchantment);
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return isEnabled() && this.canApplyAtEnchantingTable(itemStack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemStack) {
        return isEnabled() && super.canApplyAtEnchantingTable(itemStack);
    }

    @Override
    public boolean isAllowedOnBooks() {
        return isEnabled() && super.isAllowedOnBooks();
    }

    @Override
    public boolean isDiscoverable() {
        return isEnabled();
    }

    @Override
    public boolean isTradeable() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled.getValue();
    }

}
