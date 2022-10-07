function initializeCoreMod() {
    ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

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
                methodName: ASMAPI.mapMethod('m_7023_'),
                methodDesc: '(Lnet/minecraft/world/phys/Vec3;)V',
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
                            ASMAPI.mapMethod('m_20256_'),
                            '(Lnet/minecraft/world/phys/Vec3;)V',
                            methodNode.instructions.indexOf(
                                ASMAPI.findFirstMethodCall(
                                    methodNode,
                                    ASMAPI.MethodType.VIRTUAL,
                                    'net/minecraft/world/entity/LivingEntity',
                                    ASMAPI.mapMethod('m_21255_'),
                                    '()Z'
                                )
                            )
                        )
                    ) - 6
                );
                methodNode.instructions.insertBefore(at, toInject);

                // print(ASMAPI.methodNodeToString(methodNode));
                return methodNode;
            },
        },
    };
}

//! SRG <-> MCP
/*
    m_7023_     travel
    m_20256_    setDeltaMovement    (Lnet/minecraft/world/phys/Vec3;)V
    m_21255_    isFallFlying
*/

//! LocalVariableTable
/*
    Slot    Name          Signature
    7       d9            D
    9       f4            F
    10      f5            F
    11      f6            F
    12      vec36         Lnet/minecraft/world/phys/Vec3;
    13      vec32         Lnet/minecraft/world/phys/Vec3;
    9       vec33         Lnet/minecraft/world/phys/Vec3;
    7       d8            D
    9       vec34         Lnet/minecraft/world/phys/Vec3;
    18      d6            D
    18      d10           D
    18      d11           D
    20      d7            D
    22      f1            F
    7       vec3          Lnet/minecraft/world/phys/Vec3;
    8       vec31         Lnet/minecraft/world/phys/Vec3;
    9       f             F
    10      d1            D
    12      d3            D
    14      d4            D
    16      d5            D
    7       blockpos      Lnet/minecraft/core/BlockPos;
    8       f2            F
    9       f3            F
    10      vec35         Lnet/minecraft/world/phys/Vec3;
    11      d2            D
    2       d0            D
    4       gravity       Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;
    5       flag          Z
    6       fluidstate    Lnet/minecraft/world/level/material/FluidState;
    0       this          Lnet/minecraft/world/entity/LivingEntity;
    1       p_21280_      Lnet/minecraft/world/phys/Vec3;
*/

//! Code
/*
    public void travel(Vec3 p_21280_) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            //_ ...
            if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate)) {
                //_ ...
            } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate)) {
                //_ ...
            } else if (this.isFallFlying()) {
                Vec3 vec3 = this.getDeltaMovement();
                //_ ...
+               vec3 = org.auioc.mcmod.harmonicench.common.mobeffect.impl.WeightlessnessMobEffect.adjustFallFlySpeed(this, vec3);
                this.setDeltaMovement(vec3.multiply((double)0.99F, (double)0.98F, (double)0.99F));
                //_ ...
            } else {
                //_ ...
            }
        }
        //_ ...
    }
*   ========== ByteCode ==========   *
    //_ ...
    L78
        LINENUMBER 2093 L78
    FRAME SAME
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
    L80
        LINENUMBER 2094 L80
    //_ ...
*/
