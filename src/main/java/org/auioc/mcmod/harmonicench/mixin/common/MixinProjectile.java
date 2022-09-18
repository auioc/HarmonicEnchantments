package org.auioc.mcmod.harmonicench.mixin.common;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

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
                enchList.add(EnchantmentHelper.storeEnchantment(enchEntry.getKey().getRegistryName(), enchEntry.getValue()));
            }
            p_37265_.put("Enchantments", enchList);
        }
    }

}
