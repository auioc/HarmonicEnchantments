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
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
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
                            'org/auioc/mcmod/harmonicench/handler/HECoreModHandler',
                            'onApplyLootBonusCount',
                            '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/storage/loot/LootContext;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Holder;I)I',
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
    Slot    Name        Signature
    4       i           I
    5       j           I
    0       this        Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount;
    1       stack       Lnet/minecraft/world/item/ItemStack;
    2       context     Lnet/minecraft/world/level/storage/loot/LootContext;
    3       itemstack   Lnet/minecraft/world/item/ItemStack;
*/

//! Code
/*
    public ItemStack run(ItemStack stack, LootContext context) {
        ItemStack itemstack = context.getOptionalParameter(LootContextParams.TOOL);
        if (itemstack != null) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(stack, context, itemstack, this.enchantment, itemstack);
+           i = HECoreModHandler.onApplyLootBonusCount(this.enchantment, i);
            int j = this.formula.calculateNewCount(context.getRandom(), stack.getCount(), i);
            stack.setCount(j);
        }

        return stack;
    }
*/
