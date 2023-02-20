package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import java.util.Random;
import org.auioc.mcmod.arnicalib.base.random.RandomUtils;
import org.auioc.mcmod.arnicalib.game.chat.TextUtils;
import org.auioc.mcmod.harmonicench.api.advancement.IEnchantmentPerformancePredicate;
import org.auioc.mcmod.harmonicench.api.enchantment.IAdvancementTriggerableEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import org.auioc.mcmod.harmonicench.server.advancement.HECriteriaTriggers;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CurseOfRebellingEnchantment extends HEEnchantment implements IItemEnchantment.Hurt, IAdvancementTriggerableEnchantment {

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
    public int onItemHurt(int lvl, ItemStack itemStack, int damage, Random random, ServerPlayer player) {
        if (damage > 0) {
            int d = 0;
            for (int i = 0; i < damage; ++i) if (RandomUtils.percentageChance(1, player.getRandom())) d++;
            int value = d * 4;
            if (value > 0) {
                player.hurt(new CurseOfRebellingDamageSource(itemStack), value);
                triggerAdvancement(player, itemStack, player.isDeadOrDying());
            }
        }
        return damage;
    }

    private void triggerAdvancement(ServerPlayer player, ItemStack itemStack, boolean isDead) {
        HECriteriaTriggers.ENCHANTMENT_PERFORMED.trigger(
            player, this, itemStack,
            CurseOfRebellingEnchantmentPerformancePredicate.class,
            (p) -> p.matches(isDead)
        );
    }

    @Override
    public CurseOfRebellingEnchantmentPerformancePredicate deserializePerformancePredicate(JsonObject json, DeserializationContext conditionParser) {
        return new CurseOfRebellingEnchantmentPerformancePredicate(
            GsonHelper.isBooleanValue(json, "dead") ? GsonHelper.getAsBoolean(json, "dead") : null
        );
    }

    // ============================================================================================================== //

    public static class CurseOfRebellingDamageSource extends DamageSource {

        private final ItemStack betrayedItem;

        public CurseOfRebellingDamageSource(ItemStack itemStack) {
            super("curseOfRebelling");
            this.bypassArmor().bypassMagic().setMagic();
            this.betrayedItem = itemStack;
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity living) {
            return TextUtils.translatable("death.attack.curseOfRebelling", living.getDisplayName(), this.betrayedItem.getDisplayName());
        }

    }

    // ============================================================================================================== //

    public static class CurseOfRebellingEnchantmentPerformancePredicate implements IEnchantmentPerformancePredicate {

        public static final CurseOfRebellingEnchantmentPerformancePredicate ANY = new CurseOfRebellingEnchantmentPerformancePredicate(null);

        private final Boolean isDead;

        public CurseOfRebellingEnchantmentPerformancePredicate(Boolean isDead) {
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
