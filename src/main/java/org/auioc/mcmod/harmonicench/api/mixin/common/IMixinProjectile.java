package org.auioc.mcmod.harmonicench.api.mixin.common;

import org.auioc.mcmod.harmonicench.api.entity.IEnchantableEntity;
import net.minecraft.world.phys.Vec3;

public interface IMixinProjectile extends IEnchantableEntity {

    Vec3 getShootingPosition();

}
