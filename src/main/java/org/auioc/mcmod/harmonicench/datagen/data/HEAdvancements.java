package org.auioc.mcmod.harmonicench.datagen.data;

import java.util.function.UnaryOperator;
import org.auioc.mcmod.arnicalib.game.chat.TextUtils;
import org.auioc.mcmod.arnicalib.game.item.ItemUtils;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.datagen.provider.HEAdvancementProvider.DataGenAdvancementEntry;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HEAdvancements {

    private static final ResourceLocation PARENT = new ResourceLocation("minecraft:story/enchant_item");

    // ====================================================================== //

    public static final DataGenAdvancementEntry HEADSHOT = create(
        "divergence/headshot", (b) -> b
            .display(
                glintIcon(Items.CROSSBOW),
                TextUtils.translatable("advancements.harmonicench.divergence.headshot"),
                TextUtils.translatable("advancements.harmonicench.divergence.headshot.description"),
                null, FrameType.GOAL,
                true, true, true // TODO arnicalib displayinfo builder
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

    // ============================================================================================================== //

    public static void init() {}

    private static DataGenAdvancementEntry create(String _id, UnaryOperator<Builder> _builder) {
        return new DataGenAdvancementEntry(HarmonicEnchantments.id(_id), _builder);
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
