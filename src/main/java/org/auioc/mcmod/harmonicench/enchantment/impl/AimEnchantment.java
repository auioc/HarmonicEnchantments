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
 * <b>瞄准 Aim</b>
 * <p>
 * 使用望远镜时，标记敌对生物并提高弹射物伤害。
 * <ul>
 *     <li>每 5/3 秒，给予准心指向的敌对生物发光效果，持续 <code>∑(n,k=1)(20/k)</code> 秒，同时在对话框发送：“发现（生物），距离（玩家）X米。”</li>
 *     <li>使用或手持望远镜时，弹射物对带有发光效果的生物造成伤害增加 <code>[∑(n,k=1)(1/3k)]×100%</code>。</li>
 * </ul>
 *
 * @author WakelessSloth56
 * @author Libellule505
 */
public class AimEnchantment extends HEEnchantment {

}
