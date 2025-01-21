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
 * <b>感电 Electrification</b>
 * <p>
 * 降雨或雷暴天气时，有概率产生闪电击中持有者并给予力量效果。
 * <ul>
 *     <li>降雨（雷暴）时，每一秒有 1%（5%）概率产生闪电击中持有者。</li>
 *     <li>持有者被任何闪电击中后，获得力量效果，等级为 <code>⌊∑(n,k=1)[5/(2k+1)]⌋</code> 级，持续 <code>10×∑(n,k=1)(1/k)</code> 秒。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class ElectrificationEnchantment extends HEEnchantment {

}
