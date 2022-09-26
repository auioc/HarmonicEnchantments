function initializeCoreMod() {
    ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

    Opcodes = Java.type('org.objectweb.asm.Opcodes');

    InsnList = Java.type('org.objectweb.asm.tree.InsnList');

    VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');

    return {
        'CrossbowItem#shootProjectile': {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.item.CrossbowItem',
                methodName: ASMAPI.mapMethod('m_40894_'),
                methodDesc:
                    '(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;FZFFF)V',
            },
            transformer: function (methodNode) {
                var toInject = new InsnList();
                {
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 11));
                    toInject.add(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'org/auioc/mcmod/harmonicench/utils/EnchantmentHelper',
                            'copyItemEnchantmentsToEntity',
                            '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;)V',
                            false
                        )
                    );
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 11));
                    toInject.add(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'org/auioc/mcmod/harmonicench/utils/EnchantmentHelper',
                            'handleProjectile',
                            '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/projectile/Projectile;)V',
                            false
                        )
                    );
                }

                var at = ASMAPI.findFirstMethodCall(
                    methodNode,
                    ASMAPI.MethodType.VIRTUAL,
                    'net/minecraft/world/level/Level',
                    ASMAPI.mapMethod('m_7967_'),
                    '(Lnet/minecraft/world/entity/Entity;)Z'
                );
                methodNode.instructions.insertBefore(
                    methodNode.instructions.get(
                        methodNode.instructions.indexOf(at) - 2
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
    m_40894_    shootProjectile
    m_7967_     addFreshEntity
*/

//! LocalVariableTable
/*
    Slot    Name                 Signature
    11      projectile           Lnet/minecraft/world/entity/projectile/Projectile;
    12      crossbowattackmob    Lnet/minecraft/world/entity/monster/CrossbowAttackMob;
    12      vec31                Lnet/minecraft/world/phys/Vec3;
    13      quaternion           Lcom/mojang/math/Quaternion;
    14      vec3                 Lnet/minecraft/world/phys/Vec3;
    15      vector3f             Lcom/mojang/math/Vector3f;
    10      flag                 Z
    11      projectile           Lnet/minecraft/world/entity/projectile/Projectile;
    0       p_40895_             Lnet/minecraft/world/level/Level;
    1       p_40896_             Lnet/minecraft/world/entity/LivingEntity;
    2       p_40897_             Lnet/minecraft/world/InteractionHand;
    3       p_40898_             Lnet/minecraft/world/item/ItemStack;
    4       p_40899_             Lnet/minecraft/world/item/ItemStack;
    5       p_40900_             F
    6       p_40901_             Z
    7       p_40902_             F
    8       p_40903_             F
    9       p_40904_             F
*/

//! Code
/*
    public ItemStack run(ItemStack p_79913_, LootContext p_79914_) {
        if (!p_40895_.isClientSide) {
        //_...
+       EnchantmentHelper.copyItemEnchantmentsToEntity(p_40898_, projectile);
+       EnchantmentHelper.handleProjectile(p_40898_, projectile);
        p_40895_.addFreshEntity(projectile);
        //_...
        }
    }
*   ========== ByteCode ==========   *
    //_ ...
    L22
        LINENUMBER 223 L22
+       ALOAD 3
+       ALOAD 11
+       INVOKESTATIC org/auioc/mcmod/harmonicench/utils/EnchantmentHelper.copyItemEnchantmentsToEntity (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;)V
+       ALOAD 3
+       ALOAD 11
+       INVOKESTATIC org/auioc/mcmod/harmonicench/utils/EnchantmentHelper.handleProjectile (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/projectile/Projectile;)V
        ALOAD 0
        ALOAD 11
        INVOKEVIRTUAL net/minecraft/world/level/Level.addFreshEntity (Lnet/minecraft/world/entity/Entity;)Z
        POP
    L23
        LINENUMBER 224 L23
    //_ ...
*/
