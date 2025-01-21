/*
 * Copyright (C) 2022-2025 AUIOC.ORG
 *
 * This file is part of HarmonicEnchantments, a mod made for Minecraft.
 *
 * HarmonicEnchantments is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.auioc.mcmod.harmonicench.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.HEEnchantment;

/**
 * <b>汲取 Siphoning</b>
 * <p>
 * 杀死生物后，根据其最大生命值恢复饥饿值和饱和度，优先回复饥饿值。
 * <ul>
 *     <li>恢复 <code>(x/15)∑(n,k=1)(1/k)</code> 点饥饿值或饱和度。（x：被击杀生物的最大生命值）</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class SiphoningEnchantment extends HEEnchantment {

}
