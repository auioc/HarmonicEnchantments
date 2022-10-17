package org.auioc.mcmod.harmonicench.api.enchantment;

import java.util.HashMap;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.auioc.mcmod.arnicalib.game.enchantment.IValidSlotsVisibleEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public abstract class AbstractHEEnchantment extends Enchantment implements IValidSlotsVisibleEnchantment {

    protected final int maxLevel;
    protected final Predicate<Enchantment> compatibility;
    @Nullable
    protected BooleanValue acquirability;
    @Nullable
    protected BooleanValue functionality;

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
        return isAcquirable() && this.canApplyAtEnchantingTable(itemStack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemStack) {
        return isAcquirable() && super.canApplyAtEnchantingTable(itemStack);
    }

    @Override
    public boolean isAllowedOnBooks() {
        return isAcquirable() && super.isAllowedOnBooks();
    }

    @Override
    public boolean isDiscoverable() {
        return isAcquirable();
    }

    @Override
    public boolean isTradeable() {
        return isAcquirable();
    }

    protected abstract HashMap<ResourceLocation, BooleanValue> getAcquirabilityMap();

    protected abstract HashMap<ResourceLocation, BooleanValue> getFunctionalityMap();

    public boolean isAcquirable() {
        if (this.acquirability == null) {
            this.acquirability = getAcquirabilityMap().get(getRegistryName());
            if (this.acquirability == null) return true;
        }
        return this.acquirability.get();
    }

    public boolean isFunctional() {
        if (this.functionality == null) {
            this.functionality = getFunctionalityMap().get(getRegistryName());
            if (this.functionality == null) return true;
        }
        return this.functionality.get();
    }

}
