package org.auioc.mcmod.harmonicench.mixin.server;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.auioc.mcmod.arnicalib.game.entity.MobStance;
import org.auioc.mcmod.harmonicench.utils.EnchantmentPerformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PiglinAi.class)
public class MixinPiglinAi {

    @Inject(
        method = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isWearingGold(Lnet/minecraft/world/entity/LivingEntity;)Z",
        at = @At(value = "HEAD"),
        cancellable = true,
        require = 1,
        allow = 1
    )
    private static void isWearingGold(LivingEntity pLivingEntity, CallbackInfoReturnable<Boolean> cri) {
        var stance = EnchantmentPerformer.onPiglinCheckGoldArmor(pLivingEntity);
        if (stance == MobStance.NEUTRAL) {
            cri.setReturnValue(true);
        } else if (stance == MobStance.HOSTILE) {
            cri.setReturnValue(false);
        }
    }


}
