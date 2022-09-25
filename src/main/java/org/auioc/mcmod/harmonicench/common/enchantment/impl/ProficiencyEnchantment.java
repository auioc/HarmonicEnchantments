package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.utils.java.MathUtil;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IBlockEnchantment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

public class ProficiencyEnchantment extends AbstractHEEnchantment implements IBlockEnchantment.BreakSpeed, IBlockEnchantment.Break {

    public ProficiencyEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.DIGGER,
            EquipmentSlot.MAINHAND,
            5,
            (o) -> o != Enchantments.BLOCK_EFFICIENCY
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 1 + 10 * (lvl - 1);
    }

    @Override
    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 50;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof ShearsItem ? true : super.canEnchant(itemStack);
    }

    @Override
    public void onBlockBreak(int lvl, ItemStack itemStack, Player player, BlockState blockState, BlockPos blockPos) {
        if (itemStack.isCorrectToolForDrops(blockState)) {
            if (player.getRandom().nextDouble() < (MathUtil.sigma(lvl, 1, (double i) -> 1.0D / i) / 200.0)) {
                var nbt = itemStack.getOrCreateTag();
                int proficiency = nbt.getInt("Proficiency") + 1;
                nbt.putInt("Proficiency", proficiency);
            }
        }
    }

    @Override
    public float getBreakSpeed(int lvl, ItemStack itemStack, Player player, BlockState blockState, BlockPos blockPos, float speed) {
        if (itemStack.isCorrectToolForDrops(blockState)) {
            if (itemStack.hasTag()) {
                int proficiency = itemStack.getTag().getInt("Proficiency");
                if (proficiency > 0) return speed + proficiency;
            }
        }
        return speed;
    }

}
