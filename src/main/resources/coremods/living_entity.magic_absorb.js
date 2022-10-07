function initializeCoreMod() {
    ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

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
                methodName: ASMAPI.mapMethod('m_6515_'),
                methodDesc:
                    '(Lnet/minecraft/world/damagesource/DamageSource;F)F',
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
                            ASMAPI.mapMethod('m_6168_'),
                            '()Ljava/lang/Iterable;',
                            false
                        )
                    );
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    toInject.add(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'org/auioc/mcmod/harmonicench/utils/EnchantmentHelper',
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
            },
        },
    };
}

//! SRG <-> MCP
/*
    m_6515_    getDamageAfterMagicAbsorb
    m_6168_    getArmorSlots
*/

//! LocalVariableTable
/*
    Slot    Name        Signature
    3       i           I
    4       j           I
    5       f           F
    6       f1          F
    7       f2          F
    3       k           I
    0       this        Lnet/minecraft/world/entity/LivingEntity;
    1       p_21193_    Lnet/minecraft/world/damagesource/DamageSource;
    2       p_21194_    F
*/

//! Original method
/*
    if (p_21193_.isBypassMagic()) {
        return p_21194_;
    } else {
        //_ ...
        if (p_21194_ <= 0.0F) {
            return 0.0F;
        } else {
            int k = EnchantmentHelper.getDamageProtection(this.getArmorSlots(), p_21193_);
            if (k > 0) {
                p_21194_ = CombatRules.getDamageAfterMagicAbsorb(p_21194_, (float)k);
            }

            return p_21194_;
        }
    }
*   ========== ByteCode ==========   *
    //_ ...
    L15
        LINENUMBER 1533 L15
    FRAME SAME
        ALOAD 0
        INVOKEVIRTUAL net/minecraft/world/entity/LivingEntity.getArmorSlots ()Ljava/lang/Iterable;
        ALOAD 1
        INVOKESTATIC net/minecraft/world/item/enchantment/EnchantmentHelper.getDamageProtection (Ljava/lang/Iterable;Lnet/minecraft/world/damagesource/DamageSource;)I
        ISTORE 3
    L17
        LINENUMBER 1534 L17
    //_ ...
*/

//! Transformed method
/*
    if (p_21193_.isBypassMagic()) {
        return p_21194_;
    } else {
        //_ ...
        if (p_21194_ <= 0.0F) {
            return 0.0F;
        } else {
            int k = EnchantmentHelper.getDamageProtection(this.getArmorSlots(), p_21193_);
+           k += int org.auioc.mcmod.harmonicench.utils.EnchantmentHelper.getDamageProtectionWithItem(this.getArmorSlots(), p_21193_);
            if (k > 0) {
                p_21194_ = CombatRules.getDamageAfterMagicAbsorb(p_21194_, (float)k);
            }

            return p_21194_;
        }
    }
*   ========== ByteCode ==========   *
    //_ ...
    L15
        LINENUMBER 1533 L15
    FRAME SAME
        ALOAD 0
        INVOKEVIRTUAL net/minecraft/world/entity/LivingEntity.getArmorSlots ()Ljava/lang/Iterable;
        ALOAD 1
        INVOKESTATIC net/minecraft/world/item/enchantment/EnchantmentHelper.getDamageProtection (Ljava/lang/Iterable;Lnet/minecraft/world/damagesource/DamageSource;)I
        ISTORE 3
+       ILOAD 3
+       ALOAD 0
+       INVOKEVIRTUAL net/minecraft/world/entity/LivingEntity.getArmorSlots ()Ljava/lang/Iterable;
+       ALOAD 1
+       INVOKESTATIC org/auioc/mcmod/harmonicench/utils/EnchantmentHelper.getDamageProtectionWithItem (Ljava/lang/Iterable;Lnet/minecraft/world/damagesource/DamageSource;)I
+       IADD
+       ISTORE 3
    L17
        LINENUMBER 1534 L17
    //_ ...
*/
