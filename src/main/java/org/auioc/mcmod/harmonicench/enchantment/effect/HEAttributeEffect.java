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

package org.auioc.mcmod.harmonicench.enchantment.effect;

import com.google.common.collect.HashMultimap;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import net.minecraft.world.phys.Vec3;
import org.auioc.mcmod.harmonicench.api.HEEnchantedValue;

/**
 * Variant of {@link EnchantmentAttributeEffect} that supports {@link HEEnchantedValue}
 */
public record HEAttributeEffect(
    ResourceLocation id,
    Holder<Attribute> attribute,
    HEEnchantedValue amount,
    AttributeModifier.Operation operation
) implements EnchantmentLocationBasedEffect {

    public static final MapCodec<HEAttributeEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(o -> o.id),
        Attribute.CODEC.fieldOf("attribute").forGetter(o -> o.attribute),
        HEEnchantedValue.CODEC.fieldOf("amount").forGetter(o -> o.amount),
        AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(o -> o.operation)
    ).apply(instance, HEAttributeEffect::new));

    public HEAttributeEffect(ResourceKey<Enchantment> enchantment, Holder<Attribute> attribute, HEEnchantedValue amount, AttributeModifier.Operation operation) {
        this(ResourceLocation.fromNamespaceAndPath(enchantment.location().getNamespace(), "enchantment." + enchantment.location().getPath()), attribute, amount, operation);
    }

    @Override
    public void onChangedBlock(ServerLevel level, int lvl, EnchantedItemInUse item, Entity entity, Vec3 pos, boolean applyTransientEffects) {
        if (applyTransientEffects && entity instanceof LivingEntity living) {
            living.getAttributes().addTransientAttributeModifiers(this.makeAttributeMap(lvl, item, item.inSlot()));
        }
    }

    @Override
    public void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 pos, int lvl) {
        if (entity instanceof LivingEntity living) {
            living.getAttributes().removeAttributeModifiers(this.makeAttributeMap(lvl, item, item.inSlot()));
        }
    }

    public AttributeModifier getModifier(int lvl, EnchantedItemInUse item, StringRepresentable slot) {
        return new AttributeModifier(
            id.withSuffix("/" + slot.getSerializedName()),
            amount.calculate(lvl, item),
            this.operation()
        );
    }

    private HashMultimap<Holder<Attribute>, AttributeModifier> makeAttributeMap(int lvl, EnchantedItemInUse item, EquipmentSlot slot) {
        var map = HashMultimap.<Holder<Attribute>, AttributeModifier>create();
        if (slot == null) { return map; }
        map.put(this.attribute, getModifier(lvl, item, slot));
        return map;
    }

    @Override
    public MapCodec<HEAttributeEffect> codec() { return CODEC; }

}
