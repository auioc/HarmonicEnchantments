/*
 * Copyright (C) 2022-2024 AUIOC.ORG
 *
 * This file is part of HarmonicLib, a mod made for Minecraft.
 *
 * HarmonicLib is free software: you can redistribute it and/or modify it under
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

package org.auioc.mcmod.harmoniclib.mixin.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.auioc.mcmod.arnicalib.game.registry.RegistryUtils;
import org.auioc.mcmod.harmoniclib.mixinapi.IMixinProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Mixin(value = Projectile.class)
public class MixinProjectile implements IMixinProjectile {

    @Nullable
    protected Map<Enchantment, Integer> enchantments;

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

    @Override
    public void addEnchantment(Enchantment ench, int lvl) {
        if (this.enchantments == null) this.enchantments = new HashMap<>();
        this.enchantments.put(ench, lvl);
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/projectile/Projectile;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1
    )
    protected void readAdditionalSaveData(CompoundTag p_37262_, CallbackInfo ci) {
        if (p_37262_.contains("Enchantments", 10)) {
            this.enchantments = EnchantmentHelper.deserializeEnchantments(p_37262_.getList("Enchantments", 10));
        }
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/projectile/Projectile;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1
    )
    protected void addAdditionalSaveData(CompoundTag p_37265_, CallbackInfo ci) {
        if (this.enchantments != null && !this.enchantments.isEmpty()) {
            var enchList = new ListTag();
            for (var enchEntry : this.enchantments.entrySet()) {
                enchList.add(EnchantmentHelper.storeEnchantment(RegistryUtils.id(enchEntry.getKey()), enchEntry.getValue()));
            }
            p_37265_.put("Enchantments", enchList);
        }
    }

}
