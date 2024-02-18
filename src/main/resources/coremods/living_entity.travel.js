function initializeCoreMod() {
    ASMAPI = Java.type('net.neoforged.coremod.api.ASMAPI');

    Opcodes = Java.type('org.objectweb.asm.Opcodes');

    InsnList = Java.type('org.objectweb.asm.tree.InsnList');

    InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');

    return {
        'LivingEntity#travel': {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.entity.LivingEntity',
                methodName: 'travel',
                methodDesc: '(Lnet/minecraft/world/phys/Vec3;)V'
            },
            transformer: function (methodNode) {
                var toInject = new InsnList();
                {
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 7));
                    toInject.add(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'org/auioc/mcmod/harmonicench/common/mobeffect/impl/WeightlessnessMobEffect',
                            'adjustFallFlySpeed',
                            '(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;',
                            false
                        )
                    );
                    toInject.add(new VarInsnNode(Opcodes.ASTORE, 7));
                }

                var at = methodNode.instructions.get(
                    methodNode.instructions.indexOf(
                        ASMAPI.findFirstMethodCallAfter(
                            methodNode,
                            ASMAPI.MethodType.VIRTUAL,
                            'net/minecraft/world/entity/LivingEntity',
                            'setDeltaMovement',
                            '(Lnet/minecraft/world/phys/Vec3;)V',
                            methodNode.instructions.indexOf(
                                ASMAPI.findFirstMethodCall(
                                    methodNode,
                                    ASMAPI.MethodType.VIRTUAL,
                                    'net/minecraft/world/entity/LivingEntity',
                                    'isFallFlying',
                                    '()Z'
                                )
                            )
                        )
                    ) - 6
                );
                methodNode.instructions.insertBefore(at, toInject);

                // print(ASMAPI.methodNodeToString(methodNode));
                return methodNode;
            }
        }
    };
}

//! LocalVariableTable
/*
    Slot    Name          Signature
    7       vec3          Lnet/minecraft/world/phys/Vec3;
    0       this          Lnet/minecraft/world/entity/LivingEntity;
*/

//! Code
/*
    public void travel(Vec3 pTravelVector) {
        if (this.isControlledByLocalInstance()) { //_ ...
            if ( ... && ... ) { //_ ...
            } else if ( ... ) { //_ ...
            } else if (this.isFallFlying()) {
                //_ ...
                Vec3 vec3 = this.getDeltaMovement();
                //_ ...
+               vec3 = WeightlessnessMobEffect.adjustFallFlySpeed(this, vec3);
                this.setDeltaMovement(vec3.multiply((double) 0.99F, (double) 0.98F, (double) 0.99F));
                //_ ...
            } else { //_ ...
            }
        } //_ ...
    }
*   ========== ByteCode ==========   *
    //_ ...
+       ALOAD 0
+       ALOAD 7
+       INVOKESTATIC org/auioc/mcmod/harmonicench/common/mobeffect/impl/WeightlessnessMobEffect.adjustFallFlySpeed (Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
+       ASTORE 7
        ALOAD 0
        ALOAD 7
        LDC 0.9900000095367432
        LDC 0.9800000190734863
        LDC 0.9900000095367432
        INVOKEVIRTUAL net/minecraft/world/phys/Vec3.multiply (DDD)Lnet/minecraft/world/phys/Vec3;
        INVOKEVIRTUAL net/minecraft/world/entity/LivingEntity.setDeltaMovement (Lnet/minecraft/world/phys/Vec3;)V
    //_ ...
*/
