package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.harmonicench.api.advancement.IEnchantmentPerformancePredicate;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IAdvancementTriggerableEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.ILivingEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IPlayerEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.AimEnchantment.PerformancePredicate;
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
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fml.LogicalSide;

public class AimEnchantment extends AbstractHEEnchantment implements ILivingEnchantment.Hurt, IPlayerEnchantment.Tick, IAdvancementTriggerableEnchantment<PerformancePredicate> {

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
        if (phase == Phase.END && side == LogicalSide.SERVER && player.tickCount % 11 == 0 && player.isScoping()) {
            Vec3 from = player.getEyePosition();
            Vec3 view = player.getViewVector(1.0F);
            Vec3 to = from.add(view.x * 100.0D, view.y * 100.0D, view.z * 100.0D);
            var hit = ProjectileUtil.getEntityHitResult(player.level, player, from, to, (new AABB(from, to)).inflate(1.0D), EntitySelector.NO_SPECTATORS, 0.0F);
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
            (PerformancePredicate p) -> p.matches(player, aimedEntity)
        );
    }

    @Override
    public PerformancePredicate deserializePerformancePredicate(JsonObject json, DeserializationContext conditionParser) {
        if (json == null) return PerformancePredicate.ANY;
        return new PerformancePredicate(
            EntityPredicate.Composite.fromJson(json, "aimed_entity", conditionParser),
            DistancePredicate.fromJson(json.get("distance"))
        );
    }

    // ============================================================================================================== //

    public static class PerformancePredicate implements IEnchantmentPerformancePredicate {

        public static final PerformancePredicate ANY = new PerformancePredicate(EntityPredicate.Composite.ANY, DistancePredicate.ANY);

        private final EntityPredicate.Composite aimedEntityPredicate;
        private final DistancePredicate distancePredicate;

        public PerformancePredicate(EntityPredicate.Composite aimedEntityPredicate, DistancePredicate distancePredicate) {
            this.aimedEntityPredicate = aimedEntityPredicate;
            this.distancePredicate = distancePredicate;
        }

        public PerformancePredicate(EntityPredicate aimedEntityPredicate, DistancePredicate distancePredicate) {
            this(EntityPredicate.Composite.wrap(aimedEntityPredicate), distancePredicate);
        }

        public boolean matches(ServerPlayer player, LivingEntity aimedEntity) {
            if (this == ANY) return true;
            return this.aimedEntityPredicate.matches(EntityPredicate.createContext(player, aimedEntity))
                && this.distancePredicate.matches(player.getX(), player.getY(), player.getZ(), aimedEntity.getX(), aimedEntity.getY(), aimedEntity.getZ());
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
