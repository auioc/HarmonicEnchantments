/*
 * Copyright (C) 2022-2024 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.datagen.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.LootContext;
import org.auioc.mcmod.arnicalib.game.advancement.DisplayInfoBuilder;
import org.auioc.mcmod.arnicalib.game.datagen.advancement.DataGenAdvancementEntry;
import org.auioc.mcmod.arnicalib.game.item.ItemUtils;
import org.auioc.mcmod.arnicalib.game.loot.predicate.EntityAttributeCondition;
import org.auioc.mcmod.arnicalib.game.tag.HEntityTypeTags;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.enchantment.impl.AimEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.ProficiencyEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.RebellingCurseEnchantment;
import org.auioc.mcmod.harmoniclib.advancement.criterion.EnchantmentPerformedTrigger;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class HEAdvancements {

    private static final AdvancementHolder PARENT = new AdvancementHolder(new ResourceLocation("minecraft:story/enchant_item"), null);

    // ============================================================================================================== //

    public static final DataGenAdvancementEntry HEADSHOT = ((Supplier<DataGenAdvancementEntry>) () -> {
        var killed = entity()
            .of(EntityType.VEX)
            .distance(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(30.0D)));
        var damage = damageType()
            .tag(tag(DamageTypeTags.IS_PROJECTILE))
            .direct(entity()
                .of(EntityTypeTags.ARROWS)
                .nbt(nbt("{Enchantments:[{id:\"harmonicench:sniping\"}]}"))
            );

        return create(
            "divergence/headshot", (id, b) -> b
                .display(
                    new DisplayInfoBuilder()
                        .icon(glintIcon(Items.CROSSBOW))
                        .titleAndDescription(titleKey(id))
                        .goalFrame().announceChat().showToast().hidden()
                        .build()
                )
                .parent(PARENT)
                .addCriterion(
                    "snipe_vex",
                    KilledTrigger.TriggerInstance.playerKilledEntity(
                        killed,
                        damage
                    )
                )
        );
    }).get();

    // ====================================================================== //

    public static final DataGenAdvancementEntry GUERRILLA_IN_THE_JUNGLE = ((Supplier<DataGenAdvancementEntry>) () -> {
        var jungles = List.of(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE);
        var killed = entity(HEntityTypeTags.ILLAGERS);
        var damage = damageType()
            .tag(tag(DamageTypeTags.IS_PROJECTILE))
            .direct(
                entity()
                    .of(EntityTypeTags.ARROWS)
                    .nbt(nbt("{Enchantments:[{id:\"harmonicench:handiness\"}]}"))
            );
        var killIllagerInJungles = (UnaryOperator<Advancement.Builder>) (b) -> {
            for (var biome : jungles) {
                b.addCriterion(
                    "kill_illager_in_" + biome.location().getPath(),
                    CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(
                        new KilledTrigger.TriggerInstance(
                            Optional.of(EntityPredicate.wrap(
                                entity().located(LocationPredicate.Builder.inBiome(biome))
                            )),
                            Optional.of(EntityPredicate.wrap(killed)),
                            Optional.of(damage.build())
                        )
                    )
                );
            }
            return b;
        };

        return create(
            "divergence/guerrilla_in_the_jungle", (id, b) ->
                killIllagerInJungles.apply(b)
                    .display(
                        new DisplayInfoBuilder()
                            .icon(glintIcon(Items.BOW))
                            .titleAndDescription(titleKey(id))
                            .goalFrame().announceChat().showToast().hidden()
                            .build()
                    )
                    .parent(PARENT)
                    .requirements(AdvancementRequirements.Strategy.OR)
        );
    }).get();

    // ====================================================================== //

    public static final DataGenAdvancementEntry SHOW_THE_BLADE = ((Supplier<DataGenAdvancementEntry>) () -> {
        var killed = new EntityAttributeCondition(
            Attributes.MAX_HEALTH,
            EntityAttributeCondition.ValueType.BASE,
            MinMaxBounds.Doubles.atLeast(80.0),
            LootContext.EntityTarget.THIS
        );
        var damage = damageType()
            .source(entityMainHand(item(HEEnchantments.BANE_OF_CHAMPIONS.get())));

        return create(
            "divergence/show_the_blade", (id, b) -> b
                .display(
                    new DisplayInfoBuilder()
                        .icon(glintIcon(Items.NETHERITE_SWORD))
                        .titleAndDescription(titleKey(id))
                        .goalFrame().announceChat().showToast().hidden()
                        .build()
                )
                .parent(PARENT)
                .addCriterion(
                    "kill_boss",
                    CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(
                        new KilledTrigger.TriggerInstance(
                            Optional.empty(),
                            Optional.of(ContextAwarePredicate.create(killed)),
                            Optional.of(damage.build())
                        )
                    )
                )
        );
    }).get();

    // ====================================================================== //

    public static final DataGenAdvancementEntry COME_WITH_PRACTICE = ((Supplier<DataGenAdvancementEntry>) () -> {
        var item = item(HEEnchantments.PROFICIENCY.get())
            .hasNbt(parseNbt(String.format("{%s:%d}", ProficiencyEnchantment.NBT_PROFICIENCY, 26)));

        return create(
            "divergence/come_with_practice", (id, b) -> b
                .display(
                    new DisplayInfoBuilder()
                        .icon(glintIcon(Items.NETHERITE_PICKAXE))
                        .titleAndDescription(titleKey(id))
                        .goalFrame().announceChat().showToast().hidden()
                        .build()
                )
                .parent(PARENT)
                .addCriterion(
                    "proficient",
                    InventoryChangeTrigger.TriggerInstance.hasItems(item)
                )
        );
    }).get();

    // ====================================================================== //

    public static final DataGenAdvancementEntry FISH_IN_THE_SNOW = ((Supplier<DataGenAdvancementEntry>) () -> {
        var coldBiomes = List.of(
            Biomes.TAIGA, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.SNOWY_PLAINS, Biomes.SNOWY_BEACH,
            Biomes.SNOWY_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.GROVE, Biomes.SNOWY_SLOPES, Biomes.JAGGED_PEAKS,
            Biomes.FROZEN_PEAKS, Biomes.COLD_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.ICE_SPIKES,
            Biomes.THE_VOID
        );

        var fishingRod = item(HEEnchantments.LUCK_OF_THE_SNOW.get());
        var hookedItem = item(ItemTags.FISHES);

        var fishInColdBiome = (UnaryOperator<Advancement.Builder>) (b) -> {
            for (var biome : coldBiomes) {
                b.addCriterion(
                    "fish_in_" + biome.location().getPath(),
                    FishingRodHookedTrigger.TriggerInstance.fishedItem(
                        Optional.of(fishingRod.build()),
                        Optional.of(
                            entity()
                                .located(LocationPredicate.Builder.inBiome(biome))
                                .build()
                        ),
                        Optional.of(hookedItem.build())
                    )
                );
            }
            return b;
        };

        return create(
            "divergence/fish_in_the_snow", (id, b) ->
                fishInColdBiome.apply(b)
                    .display(
                        new DisplayInfoBuilder()
                            .icon(glintIcon(Items.FISHING_ROD))
                            .titleAndDescription(titleKey(id))
                            .goalFrame().announceChat().showToast().hidden()
                            .build()
                    )
                    .parent(PARENT)
                    .requirements(AdvancementRequirements.Strategy.OR)
        );
    }).get();

    // ====================================================================== //

    public static final DataGenAdvancementEntry WHOS_THE_HAWK_EYE_NOW = ((Supplier<DataGenAdvancementEntry>) () -> {
        var enchantment = HEEnchantments.AIM.get();
        var aimed = entity(EntityType.PHANTOM);

        return create(
            "divergence/whos_the_hawk_eye_now", (id, b) -> b
                .display(
                    new DisplayInfoBuilder()
                        .icon(glintIcon(Items.SPYGLASS))
                        .titleAndDescription(titleKey(id))
                        .goalFrame().announceChat().showToast().hidden()
                        .build()
                )
                .parent(PARENT)
                .addCriterion(
                    "aim_entity",
                    EnchantmentPerformedTrigger.createCriterion(
                        null, enchantment, null,
                        new AimEnchantment.PerformancePredicate(aimed)
                    )
                )
        );
    }).get();

    // ====================================================================== //

    public static final DataGenAdvancementEntry BETRAYAL = ((Supplier<DataGenAdvancementEntry>) () -> {
        var enchantment = HEEnchantments.REBELLING_CURSE.get();
        boolean isDead = true;

        return create(
            "divergence/betrayal", (id, b) -> b
                .display(
                    new DisplayInfoBuilder()
                        .icon(Items.GLASS)
                        .titleAndDescription(titleKey(id))
                        .challengeFrame().announceChat().showToast().hidden()
                        .build()
                )
                .parent(PARENT)
                .addCriterion(
                    "betrayal",
                    EnchantmentPerformedTrigger.createCriterion(
                        null, enchantment, null,
                        new RebellingCurseEnchantment.PerformancePredicate(isDead)
                    )
                )
        );
    }).get();

    // ====================================================================== //

    public static final DataGenAdvancementEntry WALKING_BATTERY = ((Supplier<DataGenAdvancementEntry>) () -> {
        var player = entityMainHand(item(Items.TRIDENT, HEEnchantments.ELECTRIFICATION.get()));
        var damage = damageInstance().type(damageType().tag(tag(DamageTypeTags.IS_LIGHTNING)));

        return create(
            "divergence/walking_battery", (id, b) -> b
                .display(
                    new DisplayInfoBuilder()
                        .icon(glintIcon(Items.TRIDENT))
                        .titleAndDescription(titleKey(id))
                        .goalFrame().announceChat().showToast().hidden()
                        .build()
                )
                .parent(PARENT)
                .addCriterion(
                    "hurt_by_lightning",
                    CriteriaTriggers.ENTITY_HURT_PLAYER.createCriterion(
                        new EntityHurtPlayerTrigger.TriggerInstance(
                            Optional.of(EntityPredicate.wrap(player)),
                            Optional.of(damage.build())
                        )
                    )
                )
        );
    }).get();

    // ============================================================================================================== //

    public static void init() { }

    private static DataGenAdvancementEntry create(String _id, BiFunction<ResourceLocation, Advancement.Builder, Advancement.Builder> _builder) {
        return new DataGenAdvancementEntry(HarmonicEnchantments.id(_id), _builder);
    }

    private static String titleKey(ResourceLocation id) {
        return "advancements." + id.getNamespace() + "." + id.getPath().replace("/", ".");
    }

    private static CompoundTag parseNbt(String nbt) {
        try {
            return TagParser.parseTag(nbt);
        } catch (CommandSyntaxException e) {
            return new CompoundTag();
        }
    }

    private static ItemStack glintIcon(Item item) {
        return ItemUtils.createItemStack(item, 1, parseNbt("{Enchantments:[{}]}"));
    }

    private static EntityPredicate.Builder entity() {
        return EntityPredicate.Builder.entity();
    }

    private static EntityPredicate entity(EntityType<?> entity) {
        return entity().of(entity).build();
    }

    private static EntityPredicate entity(TagKey<EntityType<?>> tag) {
        return entity().of(tag).build();
    }

    private static DamagePredicate.Builder damageInstance() {
        return DamagePredicate.Builder.damageInstance();
    }

    private static DamageSourcePredicate.Builder damageType() {
        return DamageSourcePredicate.Builder.damageType();
    }

    private static <T> TagPredicate<T> tag(TagKey<T> tag) {
        return TagPredicate.is(tag);
    }

    private static NbtPredicate nbt(String nbt) {
        return new NbtPredicate(parseNbt(nbt));
    }

    private static ItemPredicate.Builder item() {
        return ItemPredicate.Builder.item();
    }

    private static ItemPredicate.Builder item(TagKey<Item> tag) {
        return item().of(tag);
    }

    private static ItemPredicate.Builder item(Item item, Enchantment enchantment) {
        return item()
            .of(item)
            .hasEnchantment(new EnchantmentPredicate(enchantment, MinMaxBounds.Ints.ANY));
    }

    private static ItemPredicate.Builder item(Enchantment enchantment) {
        return item().hasEnchantment(new EnchantmentPredicate(enchantment, MinMaxBounds.Ints.ANY));
    }

    private static EntityPredicate.Builder entityMainHand(ItemPredicate.Builder itemPredicate) {
        return EntityPredicate.Builder.entity()
            .equipment(EntityEquipmentPredicate.Builder.equipment()
                .mainhand(itemPredicate).build()
            );
    }

}
