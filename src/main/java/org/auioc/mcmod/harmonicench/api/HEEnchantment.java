/*
 * Copyright (C) 2025 AUIOC.ORG
 *
 * This file is part of HarmonicEnchantments, a mod made for Minecraft.
 *
 * HarmonicEnchantments is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.auioc.mcmod.harmonicench.api;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.function.TriFunction;
import org.auioc.mcmod.arnicalib.game.data.TagRecord;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class HEEnchantment {

    protected static BuilderFunction define(TagKey<Item> supportedItems, Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots) {
        return (key, ctx) -> Enchantment.enchantment(Enchantment.definition(
                ctx.lookup(Registries.ITEM).getOrThrow(supportedItems),
                rarity.weight(), maxLevel, cost.min(), cost.max(), anvilCost, slots
            )
        );
    }

    protected static BuilderFunction define(ItemTagBuilder supportedItems, Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots) {
        return (key, ctx) -> define(supportedItems.apply(key).tag(), rarity, maxLevel, cost, anvilCost, slots).apply(key, ctx);
    }

    protected static BuilderFunction define(TagKey<Item> supportedItems, TagRecord<Enchantment> exclusiveWith, Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots) {
        return define(supportedItems, rarity, maxLevel, cost, anvilCost, slots).exclusiveWith(exclusiveWith);
    }

    protected static BuilderFunction define(TagKey<Item> supportedItems, EnchantmentTagBuilder exclusiveWith, Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots) {
        return define(supportedItems, rarity, maxLevel, cost, anvilCost, slots).exclusiveWith(exclusiveWith);
    }

    protected static BuilderFunction define(ItemTagBuilder supportedItems, EnchantmentTagBuilder exclusiveWith, Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots) {
        return define(supportedItems, rarity, maxLevel, cost, anvilCost, slots).exclusiveWith(exclusiveWith);
    }

    protected static BuilderFunction define(
        TagKey<Item> supportedItems, TagKey<Item> primaryItems,
        Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots
    ) {
        return (key, ctx) -> {
            var itemHolderGetter = ctx.lookup(Registries.ITEM);
            return Enchantment.enchantment(
                Enchantment.definition(
                    itemHolderGetter.getOrThrow(supportedItems), itemHolderGetter.getOrThrow(primaryItems),
                    rarity.weight(), maxLevel, cost.min(), cost.max(), anvilCost, slots
                )
            );
        };
    }

    protected static BuilderFunction define(ItemTagBuilder supportedItems, ItemTagBuilder primaryItems, Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots) {
        return (key, ctx) -> define(supportedItems.apply(key).tag(), primaryItems.apply(key).tag(), rarity, maxLevel, cost, anvilCost, slots).apply(key, ctx);
    }

    protected static BuilderFunction define(ItemTagBuilder supportedItems, TagKey<Item> primaryItems, Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots) {
        return (key, ctx) -> define(supportedItems.apply(key).tag(), primaryItems, rarity, maxLevel, cost, anvilCost, slots).apply(key, ctx);
    }

    protected static BuilderFunction define(TagKey<Item> supportedItems, ItemTagBuilder primaryItems, Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots) {
        return (key, ctx) -> define(supportedItems, primaryItems.apply(key).tag(), rarity, maxLevel, cost, anvilCost, slots).apply(key, ctx);
    }

    protected static BuilderFunction define(
        TagKey<Item> supportedItems, TagKey<Item> primaryItems, TagRecord<Enchantment> exclusiveWith,
        Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots
    ) {
        return define(supportedItems, primaryItems, rarity, maxLevel, cost, anvilCost, slots).exclusiveWith(exclusiveWith);
    }

    protected static BuilderFunction define(
        TagKey<Item> supportedItems, TagKey<Item> primaryItems, EnchantmentTagBuilder exclusiveWith,
        Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots
    ) {
        return define(supportedItems, primaryItems, rarity, maxLevel, cost, anvilCost, slots).exclusiveWith(exclusiveWith);
    }

    protected static BuilderFunction define(
        ItemTagBuilder supportedItems, TagKey<Item> primaryItems, EnchantmentTagBuilder exclusiveWith,
        Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots
    ) {
        return define(supportedItems, primaryItems, rarity, maxLevel, cost, anvilCost, slots).exclusiveWith(exclusiveWith);
    }

    protected static BuilderFunction define(
        TagKey<Item> supportedItems, ItemTagBuilder primaryItems, EnchantmentTagBuilder exclusiveWith,
        Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots
    ) {
        return define(supportedItems, primaryItems, rarity, maxLevel, cost, anvilCost, slots).exclusiveWith(exclusiveWith);
    }

    protected static BuilderFunction define(
        ItemTagBuilder supportedItems, ItemTagBuilder primaryItems, EnchantmentTagBuilder exclusiveWith,
        Rarity rarity, int maxLevel, Cost cost, int anvilCost, EquipmentSlotGroup... slots
    ) {
        return define(supportedItems, primaryItems, rarity, maxLevel, cost, anvilCost, slots).exclusiveWith(exclusiveWith);
    }

    // ============================================================================================================== //

    @FunctionalInterface
    public interface TagRecordBuilder<R> extends Function<ResourceKey<Enchantment>, TagRecord<R>> { }

    public interface EnchantmentTagBuilder extends TagRecordBuilder<Enchantment> { }

    public interface ItemTagBuilder extends TagRecordBuilder<Item> { }

    protected static EnchantmentTagBuilder exclusiveSet(Consumer<TagsProvider.TagAppender<Enchantment>> appender) {
        return (key) -> TagRecord.of(Registries.ENCHANTMENT, HarmonicEnchantments.id("exclusive_set/" + key.location().getPath()), appender);
    }

    @SafeVarargs
    protected static EnchantmentTagBuilder exclusiveSet(ResourceKey<Enchantment>... values) {
        return exclusiveSet(t -> t.add(values));
    }

    protected static ItemTagBuilder supportedItems(Consumer<IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> appender) {
        return (key) -> TagRecord.of(Registries.ITEM, HarmonicEnchantments.id("enchantable/" + key.location().getPath() + "_supported"),
            t -> appender.accept((IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>) t)
        );
    }

    protected static ItemTagBuilder primarySupportedItems(Consumer<IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> appender) {
        return (key) -> TagRecord.of(
            Registries.ITEM, HarmonicEnchantments.id("enchantable/" + key.location().getPath() + "_primary_supported"),
            t -> appender.accept((IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>) t)
        );
    }

    // ============================================================================================================== //

    @FunctionalInterface
    public interface BuilderFunction extends BiFunction<ResourceKey<Enchantment>, BootstrapContext<Enchantment>, Enchantment.Builder> {

        default BuilderFunction andThen(@NotNull TriFunction<ResourceKey<Enchantment>, BootstrapContext<Enchantment>, Enchantment.Builder, Enchantment.Builder> after) {
            Objects.requireNonNull(after);
            return (key, ctx) -> after.apply(key, ctx, apply(key, ctx));
        }

        default BuilderFunction exclusiveWith(TagKey<Enchantment> exclusiveTag) {
            return (key, ctx) -> apply(key, ctx).exclusiveWith(lookupEnchantment(ctx).getOrThrow(exclusiveTag));
        }

        default BuilderFunction exclusiveWith(TagRecord<Enchantment> exclusiveTag) {
            return exclusiveWith(exclusiveTag.tag());
        }

        default BuilderFunction exclusiveWith(TagRecordBuilder<Enchantment> exclusiveTag) {
            return (key, ctx) -> exclusiveWith(exclusiveTag.apply(key).tag()).apply(key, ctx);
        }

    }

    // ============================================================================================================== //

    public record Bootstrap(BuilderFunction enchantmentBuilder, List<TagRecordBuilder<?>> tagBuilders, List<TagRecord<?>> tagRecords) {

        public static Bootstrap.Builder of(BuilderFunction enchantmentBuilder) {
            return new Bootstrap.Builder(enchantmentBuilder);
        }

        public List<TagRecord<?>> tagsToGenerate(ResourceKey<Enchantment> key) {
            var tags = new ArrayList<>(tagRecords);
            tagBuilders.stream().map(b -> b.apply(key)).forEach(tags::add);
            return tags;
        }

        public static class Builder {

            private final BuilderFunction enchantmentBuilder;
            private final List<TagRecord<?>> tagRecords = new ArrayList<>();
            private final List<TagRecordBuilder<?>> tagBuilders = new ArrayList<>();
            private boolean curse = false;
            private boolean treasure = false;
            private boolean tradeable = false;

            private Builder(BuilderFunction enchantmentBuilder) { this.enchantmentBuilder = enchantmentBuilder; }

            public Builder tag(TagRecord<?>... tagsToGenerate) {
                tagRecords.addAll(List.of(tagsToGenerate));
                return this;
            }

            public Builder tag(TagRecordBuilder<?>... tagsToGenerate) {
                tagBuilders.addAll(List.of(tagsToGenerate));
                return this;
            }

            public Builder curse() {
                this.curse = true;
                return this;
            }

            public Builder treasure() {
                this.treasure = true;
                return this;
            }

            public Builder tradeable() {
                this.tradeable = true;
                return this;
            }

            @SuppressWarnings("unchecked")
            public Bootstrap build() {
                if (curse) {
                    tagBuilders.add(k -> (TagRecord) TagRecord.of(EnchantmentTags.CURSE, t -> t.add(k)));
                }
                if (tradeable) {
                    tagBuilders.add(k -> (TagRecord) TagRecord.of(EnchantmentTags.TRADEABLE, t -> t.add(k)));
                }
                tagBuilders.add(k -> (TagRecord) TagRecord.of(treasure ? EnchantmentTags.TREASURE : EnchantmentTags.NON_TREASURE, t -> t.add(k)));
                return new Bootstrap(enchantmentBuilder, Collections.unmodifiableList(tagBuilders), Collections.unmodifiableList(tagRecords));
            }

        }

    }

    // ============================================================================================================== //

    protected enum Rarity {

        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1);

        private final int weight;

        Rarity(int i) { this.weight = i; }

        public int weight() { return this.weight; }

    }

    // ============================================================================================================== //

    public record Cost(Enchantment.Cost min, Enchantment.Cost max) { }

    protected static Cost dynamicCost(int minBase, int minPreLevel, int maxBase, int maxPreLevel) {
        return new Cost(
            Enchantment.dynamicCost(minBase, minPreLevel),
            Enchantment.dynamicCost(maxBase, maxPreLevel)
        );
    }

    protected static Cost constantCost(int min, int max) {
        return new Cost(
            Enchantment.constantCost(min),
            Enchantment.constantCost(max)
        );
    }

    // ============================================================================================================== //

    protected static HolderGetter<Enchantment> lookupEnchantment(BootstrapContext<Enchantment> ctx) {
        return ctx.lookup(Registries.ENCHANTMENT);
    }

    protected static HolderGetter<Item> lookupItem(BootstrapContext<Enchantment> ctx) {
        return ctx.lookup(Registries.ITEM);
    }

    protected static HolderGetter<Block> lookupBlock(BootstrapContext<Enchantment> ctx) {
        return ctx.lookup(Registries.BLOCK);
    }

}
