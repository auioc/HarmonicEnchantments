package org.auioc.mcmod.harmonicench.server.advancement.criterion;

import java.util.function.Predicate;
import org.auioc.mcmod.arnicalib.game.data.GsonHelper;
import org.auioc.mcmod.arnicalib.game.enchantment.EnchantmentRegistry;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.api.advancement.IEnchantmentPerformancePredicate;
import org.auioc.mcmod.harmonicench.api.enchantment.IAdvancementTriggerableEnchantment;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentPerformedTrigger extends SimpleCriterionTrigger<EnchantmentPerformedTrigger.TriggerInstance<? extends IEnchantmentPerformancePredicate>> {

    private static final ResourceLocation ID = HarmonicEnchantments.id("enchantment_performed");

    @Override
    public ResourceLocation getId() { return ID; }

    @Override

    protected EnchantmentPerformedTrigger.TriggerInstance<? extends IEnchantmentPerformancePredicate> createInstance(JsonObject json, EntityPredicate.Composite playerPredicate, DeserializationContext conditionParser) {
        var enchantment = EnchantmentRegistry.getOrElseThrow(GsonHelper.getAsString(json, "enchantment"));
        var itemPredicate = ItemPredicate.fromJson(json.get("item"));

        IEnchantmentPerformancePredicate performancePredicate;
        if (enchantment instanceof IAdvancementTriggerableEnchantment<?> e) {
            performancePredicate = e.deserializePerformancePredicate(json.getAsJsonObject("performance"), conditionParser);
        } else {
            throw new JsonSyntaxException("Not a advancement triggerable enchantment: " + enchantment.getRegistryName().toString());
        }
        return new TriggerInstance<>(playerPredicate, enchantment, itemPredicate, performancePredicate);
    }

    @SuppressWarnings("unchecked")
    public <P extends IEnchantmentPerformancePredicate> void trigger(ServerPlayer player, Enchantment enchantment, ItemStack itemStack, Predicate<P> performancePredicate) {
        this.trigger(player, (instance) -> ((TriggerInstance<P>) instance).matches(enchantment, itemStack, performancePredicate));
    }

    // ============================================================================================================== //

    public static class TriggerInstance<P extends IEnchantmentPerformancePredicate> extends AbstractCriterionTriggerInstance {

        private final Enchantment enchantment;
        private final ItemPredicate itemPredicate;
        private final P performancePredicate;

        public TriggerInstance(EntityPredicate.Composite playerPredicate, Enchantment enchantment, ItemPredicate itemPredicate, P performancePredicate) {
            super(EnchantmentPerformedTrigger.ID, playerPredicate);
            this.enchantment = enchantment;
            this.itemPredicate = itemPredicate;
            this.performancePredicate = performancePredicate;
        }

        public TriggerInstance(EntityPredicate playerPredicate, Enchantment enchantment, ItemPredicate itemPredicate, P performancePredicate) {
            this(EntityPredicate.Composite.wrap(playerPredicate), enchantment, itemPredicate, performancePredicate);
        }

        public boolean matches(Enchantment enchantment, ItemStack itemStack, Predicate<P> performancePredicate) {
            return this.enchantment.equals(enchantment)
                && this.itemPredicate.matches(itemStack)
                && performancePredicate.test(this.performancePredicate);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditionSerializer) {
            var json = super.serializeToJson(conditionSerializer);
            json.addProperty("enchantment", this.enchantment.getRegistryName().toString());
            json.add("item", this.itemPredicate.serializeToJson());
            json.add("performance", this.performancePredicate.serializeToJson(conditionSerializer));
            return json;
        }

    }

}
