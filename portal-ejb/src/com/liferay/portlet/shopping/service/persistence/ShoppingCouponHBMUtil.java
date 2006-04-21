/**
 * Copyright (c) 2000-2006 Liferay, LLC. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portlet.shopping.service.persistence;

import com.liferay.util.dao.hibernate.Transformer;

/**
 * <a href="ShoppingCouponHBMUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ShoppingCouponHBMUtil implements Transformer {
	public static com.liferay.portlet.shopping.model.ShoppingCoupon model(
		ShoppingCouponHBM shoppingCouponHBM) {
		return model(shoppingCouponHBM, true);
	}

	public static com.liferay.portlet.shopping.model.ShoppingCoupon model(
		ShoppingCouponHBM shoppingCouponHBM, boolean checkPool) {
		com.liferay.portlet.shopping.model.ShoppingCoupon shoppingCoupon = null;

		if (checkPool) {
			shoppingCoupon = ShoppingCouponPool.get(shoppingCouponHBM.getPrimaryKey());
		}

		if (shoppingCoupon == null) {
			shoppingCoupon = new com.liferay.portlet.shopping.model.ShoppingCoupon(shoppingCouponHBM.getCouponId(),
					shoppingCouponHBM.getCompanyId(),
					shoppingCouponHBM.getCreateDate(),
					shoppingCouponHBM.getModifiedDate(),
					shoppingCouponHBM.getName(),
					shoppingCouponHBM.getDescription(),
					shoppingCouponHBM.getStartDate(),
					shoppingCouponHBM.getEndDate(),
					shoppingCouponHBM.getActive(),
					shoppingCouponHBM.getLimitCategories(),
					shoppingCouponHBM.getLimitSkus(),
					shoppingCouponHBM.getMinOrder(),
					shoppingCouponHBM.getDiscount(),
					shoppingCouponHBM.getDiscountType());
			ShoppingCouponPool.put(shoppingCoupon.getPrimaryKey(),
				shoppingCoupon);
		}

		return shoppingCoupon;
	}

	public static ShoppingCouponHBMUtil getInstance() {
		return _instance;
	}

	public Comparable transform(Object obj) {
		return model((ShoppingCouponHBM)obj);
	}

	private static ShoppingCouponHBMUtil _instance = new ShoppingCouponHBMUtil();
}