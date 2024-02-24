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
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
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

public class HEAdvancements {

    private static final AdvancementHolder PARENT = new AdvancementHolder(new ResourceLocation("minecraft:story/enchant_item"), null);

    // ============================================================================================================== //

    public static final DataGenAdvancementEntry HEADSHOT = create(
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
                    EntityPredicate.Builder.entity()
                        .of(EntityType.VEX)
                        .distance(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(30.0D))),
                    DamageSourcePredicate.Builder.damageType()
                        .tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))
                        .direct(
                            EntityPredicate.Builder.entity()
                                .of(EntityTypeTags.ARROWS)
                                .nbt(new NbtPredicate(parseNbt("{Enchantments:[{id:\"harmonicench:sniping\"}]}")))
                        )
                )
            )
    );

    // ====================================================================== //

    private static final List<ResourceKey<Biome>> JUNGLES = List.of(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE);

    public static final DataGenAdvancementEntry GUERRILLA_IN_THE_JUNGLE = create(
        "divergence/guerrilla_in_the_jungle", (id, b) -> killIllagerInJungle(b)
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

    private static Builder killIllagerInJungle(Builder b) {
        for (var biome : JUNGLES) {
            b.addCriterion(
                "kill_illager_in_" + biome.location().getPath(),
                CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(
                    new KilledTrigger.TriggerInstance(
                        Optional.of(EntityPredicate.wrap(
                            EntityPredicate.Builder.entity()
                                .located(LocationPredicate.Builder.inBiome(biome))
                                .build()
                        )),
                        Optional.of(EntityPredicate.wrap(
                            EntityPredicate.Builder.entity()
                                .of(HEntityTypeTags.ILLAGERS)
                                .build()
                        )),
                        Optional.of(
                            DamageSourcePredicate.Builder.damageType()
                                .tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))
                                .direct(
                                    EntityPredicate.Builder.entity()
                                        .of(EntityTypeTags.ARROWS)
                                        .nbt(new NbtPredicate(parseNbt("{Enchantments:[{id:\"harmonicench:handiness\"}]}")))
                                ).build()
                        )
                    )
                )
            );
        }
        return b;
    }

    // ====================================================================== //

    public static final DataGenAdvancementEntry SHOW_THE_BLADE = create(
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
                        Optional.of(
                            ContextAwarePredicate.create(
                                new EntityAttributeCondition(
                                    Attributes.MAX_HEALTH,
                                    EntityAttributeCondition.AttributeValueType.CURRENT_VALUE,
                                    MinMaxBounds.Doubles.atLeast(80.0),
                                    LootContext.EntityTarget.THIS
                                )
                            )
                        ),
                        Optional.of(
                            DamageSourcePredicate.Builder.damageType()
                                .source(
                                    EntityPredicate.Builder.entity()
                                        .equipment(
                                            EntityEquipmentPredicate.Builder.equipment()
                                                .mainhand(
                                                    ItemPredicate.Builder.item()
                                                        .hasEnchantment(new EnchantmentPredicate(HEEnchantments.BANE_OF_CHAMPIONS.get(), MinMaxBounds.Ints.ANY))
                                                ).build()
                                        )
                                ).build()
                        )
                    )
                )
            )
    );

    // ====================================================================== //

    public static final DataGenAdvancementEntry COME_WITH_PRACTICE = create(
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
                InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item()
                        .hasEnchantment(new EnchantmentPredicate(HEEnchantments.PROFICIENCY.get(), MinMaxBounds.Ints.ANY))
                        .hasNbt(parseNbt(String.format("{%s:%d}", ProficiencyEnchantment.NBT_PROFICIENCY, 26)))
                        .build()
                )
            )
    );

    // ====================================================================== //

    private static final List<ResourceKey<Biome>> COLD_BIOMES = List.of(
        Biomes.TAIGA, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.SNOWY_PLAINS, Biomes.SNOWY_BEACH,
        Biomes.SNOWY_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.GROVE, Biomes.SNOWY_SLOPES, Biomes.JAGGED_PEAKS,
        Biomes.FROZEN_PEAKS, Biomes.COLD_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.ICE_SPIKES,
        Biomes.THE_VOID
    );

    public static final DataGenAdvancementEntry FISH_IN_THE_SNOW = create(
        "divergence/fish_in_the_snow", (id, b) -> fishInColdBiomes(b)
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

    private static Builder fishInColdBiomes(Builder b) {
        for (var biome : COLD_BIOMES) {
            b.addCriterion(
                "fish_in_" + biome.location().getPath(),
                FishingRodHookedTrigger.TriggerInstance.fishedItem(
                    Optional.of(
                        ItemPredicate.Builder.item()
                            .hasEnchantment(new EnchantmentPredicate(HEEnchantments.LUCK_OF_THE_SNOW.get(), MinMaxBounds.Ints.ANY))
                            .build()
                    ),
                    Optional.of(
                        EntityPredicate.Builder.entity()
                            .located(LocationPredicate.Builder.inBiome(biome))
                            .build()
                    ),
                    Optional.of(
                        ItemPredicate.Builder.item()
                            .of(ItemTags.FISHES)
                            .build()
                    )
                )
            );
        }
        return b;
    }

    // ====================================================================== //

    public static final DataGenAdvancementEntry WHOS_THE_HAWK_EYE_NOW = create(
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
                    null,
                    HEEnchantments.AIM.get(),
                    null,
                    new AimEnchantment.PerformancePredicate(
                        EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.PHANTOM)).build()
                    )
                )

            )
    );

    // ====================================================================== //

    public static final DataGenAdvancementEntry BETRAYAL = create(
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
                    null,
                    HEEnchantments.REBELLING_CURSE.get(),
                    null,
                    new RebellingCurseEnchantment.PerformancePredicate(
                        Optional.of(true)
                    )
                )
            )
    );

    // ====================================================================== //

    //    public static final DataGenAdvancementEntry TEST = create(
    //        "divergence/test", (id, b) -> b
    //            .display(
    //                new DisplayInfoBuilder()
    //                    .icon(Items.GLASS)
    //                    .title(Component.literal("TEST"))
    //                    .challengeFrame().announceChat().showToast()
    //                    .build()
    //            )
    //            .parent(PARENT)
    //            .addCriterion(
    //                "test",
    //                LootEnchantmentAppliedTrigger.createCriterion(
    //                    Optional.empty(),
    //                    Optional.empty(),
    //                    MinMaxBounds.Ints.atLeast(3),
    //                    Optional.of(
    //                        HEnchantmentPredicate.builder()
    //                            .enchantments(Enchantments.ALL_DAMAGE_PROTECTION, Enchantments.AQUA_AFFINITY)
    //                            .level(MinMaxBounds.Ints.atLeast(2))
    //                            .properties(HEnchantmentPropertiesPredicate.builder().curse(true).build())
    //                            .build()
    //                    ),
    //                    Optional.of(LootEnchantmentAppliedTrigger.Requirement.ANY)
    //                )
    //            )
    //    );

    // ============================================================================================================== //

    public static void init() { }

    private static DataGenAdvancementEntry create(String _id, BiFunction<ResourceLocation, Builder, Builder> _builder) {
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

}
