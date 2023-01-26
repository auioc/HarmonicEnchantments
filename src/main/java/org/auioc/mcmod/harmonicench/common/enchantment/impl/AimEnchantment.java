package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.arnicalib.game.world.phys.RayTraceUtils;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IPlayerEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fml.LogicalSide;

public class AimEnchantment extends HEEnchantment implements ILivingEnchantment.Hurt, IPlayerEnchantment.Tick {

    public AimEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            HEnchantmentCategory.SPYGLASS,
            new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND},
            2
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return lvl * 10;
    }

    @Override
    public int getMaxCost(int lvl) {
        return getMinCost(lvl) + 15;
    }

    @Override
    public boolean isTreasureOnly() { return true; }

    @Override
    public float onLivingHurt(int lvl, boolean isSource, EquipmentSlot slot, LivingEntity target, DamageSource source, float amount) {
        if (
            isSource
                && source.isProjectile()
                && target.hasEffect(MobEffects.GLOWING)
                && source.getEntity() instanceof Player player
                && (player.isScoping() || player.getOffhandItem().is(Items.SPYGLASS) || player.getMainHandItem().is(Items.SPYGLASS))
        ) {
            return amount + (float) MathUtil.sigma(lvl, 1, (double i) -> (1.0D / (3.0D * i)));
        }
        return amount;
    }

    @Override
    public void onPlayerTick(int lvl, ItemStack itemStack, EquipmentSlot slot, Player player, Phase phase, LogicalSide side) {
        if (phase == Phase.END && side == LogicalSide.SERVER && player.tickCount % 33 == 0 && player.isScoping()) {
            var hit = RayTraceUtils.getEntityHitResult(player, 100.0D);
            if (hit != null) {
                var living = (LivingEntity) hit.getEntity();
                if (!living.hasEffect(MobEffects.GLOWING)) {
                    living.addEffect(new MobEffectInstance(MobEffects.GLOWING, MathUtil.sigma(lvl, 1, (int i) -> (20 / i)) * 20, 1));
                    player.sendMessage(
                        new TranslatableComponent(
                            getDescriptionId() + ".looking_at",
                            living.getDisplayName(), player.getDisplayName(), Math.round(living.position().distanceTo(player.position()))
                        ), Util.NIL_UUID
                    );
                }
            }
        }
    }

}
