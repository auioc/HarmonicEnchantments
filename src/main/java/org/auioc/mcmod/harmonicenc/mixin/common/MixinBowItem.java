package org.auioc.mcmod.harmonicenc.mixin.common;

import org.auioc.mcmod.harmonicenc.api.enchantment.IProjectileEnchantment;
import org.auioc.mcmod.harmonicenc.utils.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(value = BowItem.class)
public class MixinBowItem {

    @Inject(
        method = "Lnet/minecraft/world/item/BowItem;releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",
            shift = At.Shift.BEFORE
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        require = 1,
        allow = 1
    )
    public void releaseUsing(
        ItemStack p_40667_, Level p_40668_, LivingEntity p_40669_, int p_40670_,
        CallbackInfo ci,
        Player player, boolean flag, ItemStack itemstack, int i, float f, boolean flag1, ArrowItem arrowitem, AbstractArrow abstractarrow, int j, int k
    ) {
        EnchantmentHelper.copyItemEnchantmentsToEntity(p_40667_, abstractarrow, (ench, lvl) -> ench instanceof IProjectileEnchantment);
    }

}
