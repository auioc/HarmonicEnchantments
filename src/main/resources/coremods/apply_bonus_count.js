function initializeCoreMod() {
    ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

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
                methodName: ASMAPI.mapMethod('m_7372_'),
                methodDesc:
                    '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/storage/loot/LootContext;)Lnet/minecraft/world/item/ItemStack;',
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
                            ASMAPI.mapField('f_79899_'),
                            'Lnet/minecraft/world/item/enchantment/Enchantment;'
                        )
                    );
                    toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
                    toInject.add(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'org/auioc/mcmod/harmonicench/server/event/HEServerEventFactory',
                            'onApplyLootEnchantmentBonusCount',
                            '(Lnet/minecraft/world/level/storage/loot/LootContext;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/Enchantment;I)I',
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
            },
        },
    };
}

//! SRG <-> MCP
/*
    m_7372_     run
    f_79899_    enchantment
*/

//! LocalVariableTable
/*
    Slot    Name         Signature
    4       i            I
    5       j            I
    0       this         Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount;
    1       p_79913_     Lnet/minecraft/world/item/ItemStack;
    2       p_79914_     Lnet/minecraft/world/level/storage/loot/LootContext;
    3       itemstack    Lnet/minecraft/world/item/ItemStack;
*/

//! Code
/*
    public ItemStack run(ItemStack p_79913_, LootContext p_79914_) {
        //_ ...
        if (itemstack != null) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, itemstack);
+           i = int org.auioc.mcmod.harmonicench.server.event.HEServerEventFactory.onApplyLootEnchantmentBonusCount(p_79914_, itemstack, this.enchantment, i)
            int j = this.formula.calculateNewCount(p_79914_.getRandom(), p_79913_.getCount(), i);
            p_79913_.setCount(j);
        }
        //_ ...
    }
*   ========== ByteCode ==========   *
    //_ ...
    L3
        LINENUMBER 45 L3
        ALOAD 0
        GETFIELD net/minecraft/world/level/storage/loot/functions/ApplyBonusCount.enchantment : Lnet/minecraft/world/item/enchantment/Enchantment;
        ALOAD 3
        INVOKESTATIC net/minecraft/world/item/enchantment/EnchantmentHelper.getItemEnchantmentLevel (Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I
        ISTORE 4
    L4
        LINENUMBER 46 L4
+       ALOAD 2
+       ALOAD 3
+       ALOAD 0
+       GETFIELD net/minecraft/world/level/storage/loot/functions/ApplyBonusCount.enchantment : Lnet/minecraft/world/item/enchantment/Enchantment;
+       ILOAD 4
+       INVOKESTATIC org/auioc/mcmod/harmonicench/server/event/HEServerEventFactory.onApplyLootEnchantmentBonusCount (Lnet/minecraft/world/level/storage/loot/LootContext;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/Enchantment;I)I
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
    L5
        LINENUMBER 47 L5
    //_ ...
*/
