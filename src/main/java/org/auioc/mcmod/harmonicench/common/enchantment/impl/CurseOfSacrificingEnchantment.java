package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.auioc.mcmod.arnicalib.game.enchantment.EnchantmentTagUtils;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CurseOfSacrificingEnchantment extends HEEnchantment implements IItemEnchantment.Tick.Inventory, ILivingEnchantment.Death, IItemEnchantment.Tooltip {

    private static final String NBT_TIME = "SacrificingProcess";
    private static final int MAX_TIME = 20 * 60;

    public CurseOfSacrificingEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.VANISHABLE,
            EquipmentSlot.values(),
            1
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 15 + (lvl - 1) * 9;
    }

    @Override
    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 50;
    }

    @Override
    public boolean isTreasureOnly() { return true; }

    @Override
    public boolean isCurse() { return true; }

    @Override
    public void onInventoryTick(int lvl, ItemStack itemStack, Player player, Level level, int index, boolean selected) {
        if (player.tickCount % 5 == 0 && !player.getAbilities().instabuild && !player.isSpectator()) {
            var nbt = itemStack.getOrCreateTag();
            int time = nbt.getInt(NBT_TIME);
            if (time >= MAX_TIME && !level.isClientSide) {
                var enchTag = itemStack.getEnchantmentTags();
                player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, (EnchantmentTagUtils.calcTotalLevel(enchTag) * 60) * 20, (-1 * enchTag.size()) - 1));
                player.hurt(DamageSource.GENERIC, 0.1F);
                player.sendMessage(new TranslatableComponent(getDescriptionId() + ".vanished", itemStack.getDisplayName(), player.getDisplayName()), Util.NIL_UUID);
                itemStack.setCount(0);
            } else if (player.tickCount % 20 == 0) {
                nbt.putInt(NBT_TIME, time + lvl);
            }
        }
    }

    @Override
    public void onLivingDeath(int lvl, ItemStack itemStack, LivingEntity target, DamageSource source) {
        if (source.getEntity() instanceof Player) {
            resetSacrificingProcess(itemStack);
        }
    }

    public static int getSacrificingProcess(ItemStack itemStack) {
        return (itemStack.hasTag()) ? itemStack.getTag().getInt(NBT_TIME) : 0;
    }

    public static void resetSacrificingProcess(ItemStack itemStack) {
        if (itemStack.hasTag()) itemStack.getTag().putInt(NBT_TIME, 0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onItemTooltip(int lvl, @Nonnull ItemStack itemStack, @Nullable Player player, List<Component> lines, TooltipFlag flags) {
        EnchantmentHelper.getEnchantmentTooltip(lines, this.getDescriptionId()).ifPresent(
            (text) -> {
                int time = getSacrificingProcess(itemStack);
                if (time > 0 && time <= MAX_TIME) {
                    text.append(String.format(" (%s)", StringUtil.formatTickDuration((MAX_TIME - time) * 20)));
                }
            }
        );
    }

}
