function initializeCoreMod() {
    ASMAPI = Java.type('net.neoforged.coremod.api.ASMAPI');

    Opcodes = Java.type('org.objectweb.asm.Opcodes');

    InsnList = Java.type('org.objectweb.asm.tree.InsnList');

    VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');

    return {
        'ApplyBonusCount#run': {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.level.storage.loot.functions.ApplyBonusCount',
                methodName: 'run',
                methodDesc: '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/storage/loot/LootContext;)Lnet/minecraft/world/item/ItemStack;'
            },
            transformer: function (methodNode) {
                var toInject = new InsnList();
                {
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(
                        new FieldInsnNode(
                            Opcodes.GETFIELD,
                            'net/minecraft/world/level/storage/loot/functions/ApplyBonusCount',
                            'enchantment',
                            'Lnet/minecraft/core/Holder;'
                        )
                    );
                    toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
                    toInject.add(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'org/auioc/mcmod/harmoniclib/event/HLServerEventFactory',
                            'onApplyLootEnchantmentBonusCount',
                            '(Lnet/minecraft/world/level/storage/loot/LootContext;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Holder;I)I',
                            false
                        )
                    );
                    toInject.add(new VarInsnNode(Opcodes.ISTORE, 4));
                }

                var at = ASMAPI.findFirstInstructionBefore(
                    methodNode,
                    Opcodes.GETFIELD,
                    0
                );
                methodNode.instructions.insertBefore(
                    methodNode.instructions.get(
                        methodNode.instructions.indexOf(at) - 1
                    ),
                    toInject
                );

                // print(ASMAPI.methodNodeToString(methodNode));
                return methodNode;
            }
        }
    };
}

//! LocalVariableTable
/*
    Slot    Name         Signature
    4       i            I
    0       this         Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount;
    2       pContext     Lnet/minecraft/world/level/storage/loot/LootContext;
    3       itemstack    Lnet/minecraft/world/item/ItemStack;
*/

//! Code
/*
    public ItemStack run(ItemStack pStack, LootContext pContext) {
        //_ ...
        if (itemstack != null) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, itemstack);
+           i = HLServerEventFactory.onApplyLootEnchantmentBonusCount(pContext, itemstack, this.enchantment, i)
            int j = this.formula.calculateNewCount(p_79914_.getRandom(), p_79913_.getCount(), i);
            p_79913_.setCount(j);
        }
        //_ ...
    }
*   ========== ByteCode ==========   *
    //_ ...
+       ALOAD 2
+       ALOAD 3
+       ALOAD 0
+       GETFIELD net/minecraft/world/level/storage/loot/functions/ApplyBonusCount.enchantment : Lnet/minecraft/world/item/enchantment/Enchantment;
+       ILOAD 4
+       INVOKESTATIC org/auioc/mcmod/harmoniclib/event/HLServerEventFactory.onApplyLootEnchantmentBonusCount (Lnet/minecraft/world/level/storage/loot/LootContext;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Holder;I)I
+       ISTORE 4
        ALOAD 0
        GETFIELD net/minecraft/world/level/storage/loot/functions/ApplyBonusCount.formula : Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$Formula;
        ALOAD 2
        INVOKEVIRTUAL net/minecraft/world/level/storage/loot/LootContext.getRandom ()Ljava/util/Random;
        ALOAD 1
        INVOKEVIRTUAL net/minecraft/world/item/ItemStack.getCount ()I
        ILOAD 4
        INVOKEINTERFACE net/minecraft/world/level/storage/loot/functions/ApplyBonusCount$Formula.calculateNewCount (Ljava/util/Random;II)I (itf)
        ISTORE 5
    //_ ...
*/
