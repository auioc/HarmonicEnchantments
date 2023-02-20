package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.arnicalib.game.world.phys.RayTraceUtils;
import org.auioc.mcmod.harmonicench.api.advancement.IEnchantmentPerformancePredicate;
import org.auioc.mcmod.harmonicench.api.enchantment.IAdvancementTriggerableEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IPlayerEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import org.auioc.mcmod.harmonicench.server.advancement.HECriteriaTriggers;
import com.google.gson.JsonObject;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
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

public class AimEnchantment extends HEEnchantment implements ILivingEnchantment.Hurt, IPlayerEnchantment.Tick, IAdvancementTriggerableEnchantment {

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
            if (hit != null && hit.getEntity() instanceof LivingEntity living) {
                if (!living.hasEffect(MobEffects.GLOWING)) {
                    living.addEffect(new MobEffectInstance(MobEffects.GLOWING, MathUtil.sigma(lvl, 1, (int i) -> (20 / i)) * 20, 1));
                    player.sendMessage(
                        new TranslatableComponent(
                            getDescriptionId() + ".looking_at",
                            living.getDisplayName(), player.getDisplayName(), Math.round(living.position().distanceTo(player.position()))
                        ), Util.NIL_UUID
                    );
                    triggerAdvancement((ServerPlayer) player, itemStack, living);
                }
            }
        }
    }

    private void triggerAdvancement(ServerPlayer player, ItemStack itemStack, LivingEntity aimedEntity) {
        HECriteriaTriggers.ENCHANTMENT_PERFORMED.trigger(
            player, this, itemStack,
            AimEnchantmentPerformancePredicate.class,
            (p) -> p.matches(player, aimedEntity)
        );
    }

    @Override
    public AimEnchantmentPerformancePredicate deserializePerformancePredicate(JsonObject json, DeserializationContext conditionParser) {
        if (json == null) return AimEnchantmentPerformancePredicate.ANY;
        return new AimEnchantmentPerformancePredicate(
            EntityPredicate.Composite.fromJson(json, "aimed_entity", conditionParser),
            DistancePredicate.fromJson(json.get("distance"))
        );
    }

    // ============================================================================================================== //

    public static class AimEnchantmentPerformancePredicate implements IEnchantmentPerformancePredicate {

        public static final AimEnchantmentPerformancePredicate ANY = new AimEnchantmentPerformancePredicate(EntityPredicate.Composite.ANY, DistancePredicate.ANY);

        private final EntityPredicate.Composite aimedEntityPredicate;
        private final DistancePredicate distancePredicate;

        public AimEnchantmentPerformancePredicate(EntityPredicate.Composite aimedEntityPredicate, DistancePredicate distancePredicate) {
            this.aimedEntityPredicate = aimedEntityPredicate;
            this.distancePredicate = distancePredicate;
        }

        public AimEnchantmentPerformancePredicate(EntityPredicate aimedEntityPredicate, DistancePredicate distancePredicate) {
            this(EntityPredicate.Composite.wrap(aimedEntityPredicate), distancePredicate);
        }

        public boolean matches(ServerPlayer player, LivingEntity living) {
            if (this == ANY) return true;
            return this.aimedEntityPredicate.matches(EntityPredicate.createContext(player, living))
                && this.distancePredicate.matches(player.getX(), player.getY(), player.getZ(), living.getX(), living.getY(), living.getZ());
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditionSerializer) {
            var json = new JsonObject();
            json.add("aimed_entity", this.aimedEntityPredicate.toJson(conditionSerializer));
            json.add("distance", this.distancePredicate.serializeToJson());
            return json;
        }

    }


}
