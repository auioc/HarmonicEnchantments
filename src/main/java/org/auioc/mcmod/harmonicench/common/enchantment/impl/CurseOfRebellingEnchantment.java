package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.auioc.mcmod.arnicalib.game.random.GameRandomUtils;
import org.auioc.mcmod.harmonicench.api.advancement.EPPredicateType;
import org.auioc.mcmod.harmonicench.api.advancement.IEnchantmentPerformancePredicate;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.common.damagesource.HEDamageTypes;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.server.advancement.EnchantmentPerformancePredicates;
import org.auioc.mcmod.harmonicench.server.advancement.HECriteriaTriggers;

import java.util.Optional;

public class CurseOfRebellingEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Hurt {

    public CurseOfRebellingEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.BREAKABLE,
            EquipmentSlot.values(),
            (o) -> o != HEEnchantments.FREE_RIDING.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 10 + 20 * (lvl - 1);
    }

    @Override
    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 50;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public int onItemHurt(int lvl, ItemStack itemStack, int damage, RandomSource random, ServerPlayer player) {
        if (damage > 0) {
            int d = 0;
            for (int i = 0; i < damage; ++i) {
                if (GameRandomUtils.percentageChance(1, random)) {
                    d++;
                }
            }
            int value = d * 4;
            if (value > 0) {
                player.hurt(new DamageSource(player, itemStack), value);
                triggerAdvancement(player, itemStack, player.isDeadOrDying());
            }
        }
        return damage;
    }

    private void triggerAdvancement(ServerPlayer player, ItemStack itemStack, boolean isDead) {
        HECriteriaTriggers.ENCHANTMENT_PERFORMED.get().trigger(
            player, this, itemStack,
            (PerformancePredicate p) -> p.matches(isDead)
        );
    }

    // ============================================================================================================== //

    private static class DamageSource extends net.minecraft.world.damagesource.DamageSource {

        private final ItemStack betrayedItem;

        public DamageSource(Player player, ItemStack itemStack) {
            super(
                player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(HEDamageTypes.CURSE_OF_REBELLING),
                player
            );
            this.betrayedItem = itemStack;
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity living) {
            return Component.translatable("death.attack." + this.getMsgId(), living.getDisplayName(), this.betrayedItem.getDisplayName());
        }

    }

    // ============================================================================================================== //

    public record PerformancePredicate(Optional<Boolean> isDead) implements IEnchantmentPerformancePredicate {

        public boolean matches(boolean isDead) {
            return isDead().isEmpty() || isDead().get() == isDead;
        }

        // ========================================================================================================== //

        @Override
        public EPPredicateType getType() {
            return EnchantmentPerformancePredicates.CURSE_OF_REBELLING.get();
        }

        public static final Codec<PerformancePredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.strictOptionalField(Codec.BOOL, "is_dead").forGetter(PerformancePredicate::isDead)
            ).apply(instance, PerformancePredicate::new)
        );

    }

}
