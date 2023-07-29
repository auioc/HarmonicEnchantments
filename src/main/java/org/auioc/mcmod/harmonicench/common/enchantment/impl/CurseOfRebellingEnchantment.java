package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.advancement.IEnchantmentPerformancePredicate;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IAdvancementTriggerableEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.common.damagesource.HEDamageTypes;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.CurseOfRebellingEnchantment.PerformancePredicate;
import org.auioc.mcmod.harmonicench.server.advancement.HECriteriaTriggers;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CurseOfRebellingEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Hurt, IAdvancementTriggerableEnchantment<PerformancePredicate> {

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
                if (random.nextInt(100) < 1) { // TODO ArnicaLib: GameRandomUtils percentageChance
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
        HECriteriaTriggers.ENCHANTMENT_PERFORMED.trigger(
            player, this, itemStack,
            (PerformancePredicate p) -> p.matches(isDead)
        );
    }

    @Override
    public PerformancePredicate deserializePerformancePredicate(JsonObject json, DeserializationContext conditionParser) {
        return new PerformancePredicate(
            GsonHelper.isBooleanValue(json, "dead") ? GsonHelper.getAsBoolean(json, "dead") : null
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

    public static class PerformancePredicate implements IEnchantmentPerformancePredicate {

        public static final PerformancePredicate ANY = new PerformancePredicate(null);

        private final Boolean isDead;

        public PerformancePredicate(Boolean isDead) {
            this.isDead = isDead;
        }

        public boolean matches(boolean isDead) {
            if (this == ANY) return true;
            if (this.isDead == null) return true;
            return this.isDead.booleanValue() == isDead;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditionSerializer) {
            var json = new JsonObject();
            if (this.isDead != null) json.addProperty("dead", this.isDead.booleanValue());
            return json;
        }

    }

}
