package org.auioc.mcmod.harmonicench.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.CrossbowItem;

@Mixin(value = CrossbowItem.class)
public class MixinCrossbowItem {

    //~ => harmonicench.cross_bow_item.js
    // @Inject(
    //     method = "Lnet/minecraft/world/item/CrossbowItem;shootProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;FZFFF)V",
    //     at = @At(
    //         value = "INVOKE",
    //         target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",
    //         shift = At.Shift.BEFORE
    //     ),
    //     locals = LocalCapture.CAPTURE_FAILHARD,
    //     require = 1,
    //     allow = 1
    // )
    // private static void shootProjectile(
    //     Level p_40895_, LivingEntity p_40896_, InteractionHand p_40897_, ItemStack p_40898_, ItemStack p_40899_,
    //     float p_40900_, boolean p_40901_, float p_40902_, float p_40903_, float p_40904_,
    //     CallbackInfo ci,
    //     boolean flag, Projectile projectile, CrossbowAttackMob var12, Quaternion var13, Vec3 var14, Vector3f var15
    // ) {
    //     // EnchantmentHelper.copyItemEnchantmentsToEntity(p_40899_, projectile, (ench, lvl) -> ench instanceof IProjectileEnchantment.HurtLiving);
    //     // EnchantmentHelper.handleProjectile(p_40898_, projectile);
    // }

    //? https://github.com/SpongePowered/Mixin/issues/405
    // Caused by: java.lang.RuntimeException: java.lang.VerifyError: Bad local variable type
    // Exception Details:
    //   Location:
    //     net/minecraft/world/item/CrossbowItem.shootProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;FZFFF)V
    // @258: aload
    //   Reason:
    //     Type top (current frame, locals[13]) is not assignable to reference type
    //   Current Frame:
    //     bci: @258
    //     flags: { }
    //     locals: { 'net/minecraft/world/level/Level', 'net/minecraft/world/entity/LivingEntity', 'net/minecraft/world/InteractionHand', 'net/minecraft/world/item/ItemStack', 'net/minecraft/world/item/ItemStack', float, integer, float, float, float, integer, 'net/minecraft/world/entity/projectile/Projectile', 'java/lang/Object' }
    //     stack: { 'net/minecraft/world/level/Level', 'net/minecraft/world/level/Level', 'net/minecraft/world/entity/LivingEntity', 'net/minecraft/world/InteractionHand', 'net/minecraft/world/item/ItemStack', 'net/minecraft/world/item/ItemStack', float, integer, float, float, float, 'org/spongepowered/asm/mixin/injection/callback/CallbackInfo', integer, 'net/minecraft/world/entity/projectile/Projectile', 'java/lang/Object' }
    //   Bytecode:
    //     0000000: 2ab4 018e 9a01 2a19 04b2 0084 b601 9236
    //     0000010: 0a15 0a99 0024 bb01 9459 2a19 042b 2bb6
    //     0000020: 00a6 2bb6 0197 1401 9867 2bb6 00ac 04b7
    //     0000030: 019c 3a0b a700 242a 2b2d 1904 b801 a03a
    //     0000040: 0b15 069a 000a 1709 0b95 9900 0e19 0bc0
    //     0000050: 0011 b201 a4b5 01a7 2bc1 01a9 9900 1f2b
    //     0000060: c001 a93a 0c19 0c19 0cb9 01ad 0100 2d19
    //     0000070: 0b17 09b9 01b1 0500 a700 532b 0cb6 01b5
    //     0000080: 3a0c bb01 b759 bb01 b959 190c b701 bc17
    //     0000090: 0904 b701 bf3a 0d2b 0cb6 01c2 3a0e bb01
    //     00000a0: b959 190e b701 bc3a 0f19 0f19 0db6 01c6
    //     00000b0: 190b 190f b601 c98d 190f b601 cc8d 190f
    //     00000c0: b601 cf8d 1707 1708 b601 d52d 150a 9900
    //     00000d0: 0706 a700 0404 2b2c ba01 e200 00b6 01e6
    //     00000e0: 2a2a 2b2c 2d19 0417 0515 0617 0717 0817
    //     00000f0: 09bb 01e8 5913 01e9 03b7 01eb 150a 190b
    //     0000100: 190c 190d 190e 190f b801 ef19 0bb6 01f3
    //     0000110: 572a 01c0 004b 2bb6 00a6 2bb6 00a9 2bb6
    //     0000120: 00ac b201 f6b2 009d 0c17 05b6 00c2 b1
    //   Stackmap Table:
    //     append_frame(@55,Integer)
    //     append_frame(@77,Object[#17])
    //     full_frame(@88,{Object[#180],Object[#162],Object[#525],Object[#107],Object[#107],Float,Integer,Float,Float,Float,Integer,Object[#465]},{})
    //     same_frame(@123)
    //     append_frame(@203,Object[#527])
    //     same_locals_1_stack_item_frame(@213,Object[#107])
    //     full_frame(@214,{Object[#180],Object[#162],Object[#525],Object[#107],Object[#107],Float,Integer,Float,Float,Float,Integer,Object[#465],Object[#527]},{Object[#107],Integer})
    //     chop_frame(@302,3)

}
