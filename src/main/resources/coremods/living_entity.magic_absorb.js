function initializeCoreMod() {
    ASMAPI = Java.type('net.neoforged.coremod.api.ASMAPI');

    Opcodes = Java.type('org.objectweb.asm.Opcodes');

    InsnList = Java.type('org.objectweb.asm.tree.InsnList');

    InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');

    return {
        'LivingEntity#getDamageAfterMagicAbsorb': {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.entity.LivingEntity',
                methodName: 'getDamageAfterMagicAbsorb',
                methodDesc: '(Lnet/minecraft/world/damagesource/DamageSource;F)F'
            },
            transformer: function (methodNode) {
                var toInject = new InsnList();
                {
                    toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(
                        new MethodInsnNode(
                            Opcodes.INVOKEVIRTUAL,
                            'net/minecraft/world/entity/LivingEntity',
                            'getArmorSlots',
                            '()Ljava/lang/Iterable;',
                            false
                        )
                    );
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    toInject.add(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'org/auioc/mcmod/harmonicench/utils/EnchantmentPerformer',
                            'getDamageProtectionWithItem',
                            '(Ljava/lang/Iterable;Lnet/minecraft/world/damagesource/DamageSource;)I',
                            false
                        )
                    );
                    toInject.add(new InsnNode(Opcodes.IADD));
                    toInject.add(new VarInsnNode(Opcodes.ISTORE, 3));
                }

                var at = ASMAPI.findFirstInstructionBefore(
                    methodNode,
                    Opcodes.ISTORE,
                    0
                );
                methodNode.instructions.insert(at, toInject);

                // print(ASMAPI.methodNodeToString(methodNode));
                return methodNode;
            }
        }
    };
}

//! LocalVariableTable
/*
    Slot    Name            Signature
    3       k               I
    0       this            Lnet/minecraft/world/entity/LivingEntity;
    1       pDamageSource   Lnet/minecraft/world/damagesource/DamageSource;
*/

//! Code
/*
    protected float getDamageAfterMagicAbsorb(DamageSource pDamageSource, float pDamageAmount) {
        if (pDamageSource.is(DamageTypeTags.BYPASSES_EFFECTS)) { //_ ...
        } else {
            //_ ...
            if (pDamageAmount <= 0.0F) { //_ ...
            } else if (pDamageSource.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) { //_ ...
            } else {
                int k = EnchantmentHelper.getDamageProtection(this.getArmorSlots(), pDamageSource);
+               k += EnchantmentPerformer.getDamageProtectionWithItem(this.getArmorSlots(), pDamageSource);
                //_ ...
            }
        }
    }
*   ========== ByteCode ==========   *
    //_ ...
        ALOAD 0
        INVOKEVIRTUAL net/minecraft/world/entity/LivingEntity.getArmorSlots ()Ljava/lang/Iterable;
        ALOAD 1
        INVOKESTATIC net/minecraft/world/item/enchantment/EnchantmentHelper.getDamageProtection (Ljava/lang/Iterable;Lnet/minecraft/world/damagesource/DamageSource;)I
        ISTORE 3
+       ILOAD 3
+       ALOAD 0
+       INVOKEVIRTUAL net/minecraft/world/entity/LivingEntity.getArmorSlots ()Ljava/lang/Iterable;
+       ALOAD 1
+       INVOKESTATIC org/auioc/mcmod/harmonicench/utils/EnchantmentPerformer.getDamageProtectionWithItem (Ljava/lang/Iterable;Lnet/minecraft/world/damagesource/DamageSource;)I
+       IADD
+       ISTORE 3
    //_ ...
*/
