package org.auioc.mcmod.harmonicench.datagen.data;

import java.util.List;
import java.util.function.UnaryOperator;
import org.auioc.mcmod.arnicalib.game.advancement.DisplayInfoBuilder;
import org.auioc.mcmod.arnicalib.game.datagen.advancement.DataGenAdvancementEntry;
import org.auioc.mcmod.arnicalib.game.item.ItemUtils;
import org.auioc.mcmod.arnicalib.game.tag.HEntityTypeTags;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class HEAdvancements {

    private static final ResourceLocation PARENT = new ResourceLocation("minecraft:story/enchant_item");

    // ============================================================================================================== //

    public static final DataGenAdvancementEntry HEADSHOT = create(
        "divergence/headshot", (b) -> b
            .display(
                new DisplayInfoBuilder()
                    .icon(glintIcon(Items.CROSSBOW))
                    .titleAndDescription(titleKey("divergence/guerrilla_in_the_jungle"))
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
                        .isProjectile(true)
                        .direct(
                            EntityPredicate.Builder.entity()
                                .of(EntityTypeTags.ARROWS)
                                .nbt(new NbtPredicate(parseTag("{Enchantments:[{id:\"harmonicench:sniping\"}]}")))
                        )
                )
            )
    );

    // ====================================================================== //

    private static final List<ResourceKey<Biome>> JUNGLES = List.of(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE);

    public static final DataGenAdvancementEntry GUERRILLA_IN_THE_JUNGLE = create(
        "divergence/guerrilla_in_the_jungle", (b) -> killIllagerInJungle(b)
            .display(
                new DisplayInfoBuilder()
                    .icon(glintIcon(Items.BOW))
                    .titleAndDescription(titleKey("divergence/guerrilla_in_the_jungle"))
                    .goalFrame().announceChat().showToast().hidden()
                    .build()
            )
            .parent(PARENT)
            .requirements(RequirementsStrategy.OR)
    );

    private static Builder killIllagerInJungle(Builder b) {
        for (var biome : JUNGLES) {
            b.addCriterion(
                "kill_illager_in_" + biome.location().getPath(), new KilledTrigger.TriggerInstance(
                    CriteriaTriggers.PLAYER_KILLED_ENTITY.getId(),
                    EntityPredicate.Composite.wrap(
                        EntityPredicate.Builder.entity()
                            .located(LocationPredicate.inBiome(biome))
                            .build()
                    ),
                    EntityPredicate.Composite.wrap(
                        EntityPredicate.Builder.entity()
                            .of(HEntityTypeTags.ILLAGERS)
                            .build()
                    ),
                    DamageSourcePredicate.Builder.damageType()
                        .isProjectile(true)
                        .direct(
                            EntityPredicate.Builder.entity()
                                .of(EntityTypeTags.ARROWS)
                                .nbt(new NbtPredicate(parseTag("{Enchantments:[{id:\"harmonicench:handiness\"}]}")))
                        ).build()
                )
            );
        }
        return b;
    }

    // ============================================================================================================== //

    public static void init() {}

    private static DataGenAdvancementEntry create(String _id, UnaryOperator<Builder> _builder) {
        return new DataGenAdvancementEntry(HarmonicEnchantments.id(_id), _builder);
    }

    private static String titleKey(String id) {
        return "advancements." + HarmonicEnchantments.MOD_ID + "." + id.replace("/", ".");
    }

    private static CompoundTag parseTag(String nbt) {
        try {
            return TagParser.parseTag(nbt);
        } catch (CommandSyntaxException e) {
            return new CompoundTag();
        }
    }

    private static ItemStack glintIcon(Item item) {
        return ItemUtils.createItemStack(item, 1, parseTag("{Enchantments:[{}]}"));
    }

}
