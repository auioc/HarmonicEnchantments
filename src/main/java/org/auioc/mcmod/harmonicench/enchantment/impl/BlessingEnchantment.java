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
 * <b>祝福 Blessing</b>
 * <p>
 * 根据物品的所有魔咒等级之和，提供魔法抗性。
 * <ul>
 *     <li>增加 <code>∑(N,i=1)(6/i)×∑(n,j=1)[1/(5j-4)]</code> 点魔法抗性。（N：该物品所有魔咒等级之和）</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class BlessingEnchantment extends HEEnchantment {

}
