package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.arnicalib.game.chat.TextUtils;
import org.auioc.mcmod.harmonicench.api.enchantment.IBlockEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ProficiencyEnchantment extends HEEnchantment implements IBlockEnchantment.BreakSpeed, IBlockEnchantment.Break, IItemEnchantment.Tooltip {

    private static final String TAG_PROFICIENCY = "Proficiency";

    public ProficiencyEnchantment() {
        super(
            Enchantment.Rarity.UNCOMMON,
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

    private static int getProficiency(ItemStack itemStack) {
        return (itemStack.hasTag()) ? itemStack.getTag().getInt(TAG_PROFICIENCY) : 0;
    }

    @Override
    public void onBlockBreak(int lvl, ItemStack itemStack, Player player, BlockState blockState, BlockPos blockPos) {
        if (itemStack.isCorrectToolForDrops(blockState)) {
            if (player.getRandom().nextDouble() < (MathUtil.sigma(lvl, 1, (double i) -> 1.0D / i) / 200.0)) {
                var nbt = itemStack.getOrCreateTag();
                int proficiency = nbt.getInt(TAG_PROFICIENCY) + 1;
                nbt.putInt(TAG_PROFICIENCY, proficiency);
            }
        }
    }

    @Override
    public float getBreakSpeed(int lvl, ItemStack itemStack, Player player, BlockState blockState, BlockPos blockPos, float speed) {
        if (itemStack.isCorrectToolForDrops(blockState)) {
            int proficiency = getProficiency(itemStack);
            if (proficiency > 0) return speed + proficiency;
        }
        return speed;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onItemTooltip(int lvl, @Nonnull ItemStack itemStack, @Nullable Player player, List<Component> lines, TooltipFlag flags) {
        EnchantmentHelper.getEnchantmentTooltip(lines, this.getDescriptionId()).ifPresent((c) -> c.append(" ").append(TextUtils.translatable(getDescriptionId() + ".proficiency", getProficiency(itemStack))));
    }

}
