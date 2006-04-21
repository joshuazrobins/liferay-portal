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

import com.liferay.portal.SystemException;
import com.liferay.portal.service.persistence.BasePersistence;

import com.liferay.portlet.shopping.NoSuchCartException;

import com.liferay.util.StringPool;
import com.liferay.util.dao.hibernate.OrderByComparator;
import com.liferay.util.dao.hibernate.QueryUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="ShoppingCartPersistence.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ShoppingCartPersistence extends BasePersistence {
	public com.liferay.portlet.shopping.model.ShoppingCart create(String cartId) {
		return new com.liferay.portlet.shopping.model.ShoppingCart(cartId);
	}

	public com.liferay.portlet.shopping.model.ShoppingCart remove(String cartId)
		throws NoSuchCartException, SystemException {
		Session session = null;

		try {
			session = openSession();

			ShoppingCartHBM shoppingCartHBM = (ShoppingCartHBM)session.get(ShoppingCartHBM.class,
					cartId);

			if (shoppingCartHBM == null) {
				_log.warn("No ShoppingCart exists with the primary key " +
					cartId.toString());
				throw new NoSuchCartException(
					"No ShoppingCart exists with the primary key " +
					cartId.toString());
			}

			com.liferay.portlet.shopping.model.ShoppingCart shoppingCart = ShoppingCartHBMUtil.model(shoppingCartHBM);
			session.delete(shoppingCartHBM);
			session.flush();
			ShoppingCartPool.remove(cartId);

			return shoppingCart;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingCart update(
		com.liferay.portlet.shopping.model.ShoppingCart shoppingCart)
		throws SystemException {
		Session session = null;

		try {
			if (shoppingCart.isNew() || shoppingCart.isModified()) {
				session = openSession();

				if (shoppingCart.isNew()) {
					ShoppingCartHBM shoppingCartHBM = new ShoppingCartHBM(shoppingCart.getCartId(),
							shoppingCart.getCompanyId(),
							shoppingCart.getUserId(),
							shoppingCart.getCreateDate(),
							shoppingCart.getModifiedDate(),
							shoppingCart.getItemIds(),
							shoppingCart.getCouponIds(),
							shoppingCart.getAltShipping(),
							shoppingCart.getInsure());
					session.save(shoppingCartHBM);
					session.flush();
				}
				else {
					ShoppingCartHBM shoppingCartHBM = (ShoppingCartHBM)session.get(ShoppingCartHBM.class,
							shoppingCart.getPrimaryKey());

					if (shoppingCartHBM != null) {
						shoppingCartHBM.setCompanyId(shoppingCart.getCompanyId());
						shoppingCartHBM.setUserId(shoppingCart.getUserId());
						shoppingCartHBM.setCreateDate(shoppingCart.getCreateDate());
						shoppingCartHBM.setModifiedDate(shoppingCart.getModifiedDate());
						shoppingCartHBM.setItemIds(shoppingCart.getItemIds());
						shoppingCartHBM.setCouponIds(shoppingCart.getCouponIds());
						shoppingCartHBM.setAltShipping(shoppingCart.getAltShipping());
						shoppingCartHBM.setInsure(shoppingCart.getInsure());
						session.flush();
					}
					else {
						shoppingCartHBM = new ShoppingCartHBM(shoppingCart.getCartId(),
								shoppingCart.getCompanyId(),
								shoppingCart.getUserId(),
								shoppingCart.getCreateDate(),
								shoppingCart.getModifiedDate(),
								shoppingCart.getItemIds(),
								shoppingCart.getCouponIds(),
								shoppingCart.getAltShipping(),
								shoppingCart.getInsure());
						session.save(shoppingCartHBM);
						session.flush();
					}
				}

				shoppingCart.setNew(false);
				shoppingCart.setModified(false);
				shoppingCart.protect();
				ShoppingCartPool.update(shoppingCart.getPrimaryKey(),
					shoppingCart);
			}

			return shoppingCart;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingCart findByPrimaryKey(
		String cartId) throws NoSuchCartException, SystemException {
		com.liferay.portlet.shopping.model.ShoppingCart shoppingCart = ShoppingCartPool.get(cartId);
		Session session = null;

		try {
			if (shoppingCart == null) {
				session = openSession();

				ShoppingCartHBM shoppingCartHBM = (ShoppingCartHBM)session.get(ShoppingCartHBM.class,
						cartId);

				if (shoppingCartHBM == null) {
					_log.warn("No ShoppingCart exists with the primary key " +
						cartId.toString());
					throw new NoSuchCartException(
						"No ShoppingCart exists with the primary key " +
						cartId.toString());
				}

				shoppingCart = ShoppingCartHBMUtil.model(shoppingCartHBM, false);
			}

			return shoppingCart;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public List findByCompanyId(String companyId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingCart IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingCartHBM WHERE ");
			query.append("companyId = ?");
			query.append(" ");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, companyId);

			Iterator itr = q.list().iterator();
			List list = new ArrayList();

			while (itr.hasNext()) {
				ShoppingCartHBM shoppingCartHBM = (ShoppingCartHBM)itr.next();
				list.add(ShoppingCartHBMUtil.model(shoppingCartHBM));
			}

			return list;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public List findByCompanyId(String companyId, int begin, int end)
		throws SystemException {
		return findByCompanyId(companyId, begin, end, null);
	}

	public List findByCompanyId(String companyId, int begin, int end,
		OrderByComparator obc) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingCart IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingCartHBM WHERE ");
			query.append("companyId = ?");
			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY " + obc.getOrderBy());
			}

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, companyId);

			List list = new ArrayList();
			Iterator itr = QueryUtil.iterate(q, getDialect(), begin, end);

			while (itr.hasNext()) {
				ShoppingCartHBM shoppingCartHBM = (ShoppingCartHBM)itr.next();
				list.add(ShoppingCartHBMUtil.model(shoppingCartHBM));
			}

			return list;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingCart findByCompanyId_First(
		String companyId, OrderByComparator obc)
		throws NoSuchCartException, SystemException {
		List list = findByCompanyId(companyId, 0, 1, obc);

		if (list.size() == 0) {
			String msg = "No ShoppingCart exists with the key ";
			msg += StringPool.OPEN_CURLY_BRACE;
			msg += "companyId=";
			msg += companyId;
			msg += StringPool.CLOSE_CURLY_BRACE;
			throw new NoSuchCartException(msg);
		}
		else {
			return (com.liferay.portlet.shopping.model.ShoppingCart)list.get(0);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingCart findByCompanyId_Last(
		String companyId, OrderByComparator obc)
		throws NoSuchCartException, SystemException {
		int count = countByCompanyId(companyId);
		List list = findByCompanyId(companyId, count - 1, count, obc);

		if (list.size() == 0) {
			String msg = "No ShoppingCart exists with the key ";
			msg += StringPool.OPEN_CURLY_BRACE;
			msg += "companyId=";
			msg += companyId;
			msg += StringPool.CLOSE_CURLY_BRACE;
			throw new NoSuchCartException(msg);
		}
		else {
			return (com.liferay.portlet.shopping.model.ShoppingCart)list.get(0);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingCart[] findByCompanyId_PrevAndNext(
		String cartId, String companyId, OrderByComparator obc)
		throws NoSuchCartException, SystemException {
		com.liferay.portlet.shopping.model.ShoppingCart shoppingCart = findByPrimaryKey(cartId);
		int count = countByCompanyId(companyId);
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingCart IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingCartHBM WHERE ");
			query.append("companyId = ?");
			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY " + obc.getOrderBy());
			}

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, companyId);

			Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
					shoppingCart, ShoppingCartHBMUtil.getInstance());
			com.liferay.portlet.shopping.model.ShoppingCart[] array = new com.liferay.portlet.shopping.model.ShoppingCart[3];
			array[0] = (com.liferay.portlet.shopping.model.ShoppingCart)objArray[0];
			array[1] = (com.liferay.portlet.shopping.model.ShoppingCart)objArray[1];
			array[2] = (com.liferay.portlet.shopping.model.ShoppingCart)objArray[2];

			return array;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingCart findByUserId(
		String userId) throws NoSuchCartException, SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingCart IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingCartHBM WHERE ");
			query.append("userId = ?");
			query.append(" ");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, userId);

			Iterator itr = q.list().iterator();

			if (!itr.hasNext()) {
				String msg = "No ShoppingCart exists with the key ";
				msg += StringPool.OPEN_CURLY_BRACE;
				msg += "userId=";
				msg += userId;
				msg += StringPool.CLOSE_CURLY_BRACE;
				throw new NoSuchCartException(msg);
			}

			ShoppingCartHBM shoppingCartHBM = (ShoppingCartHBM)itr.next();

			return ShoppingCartHBMUtil.model(shoppingCartHBM);
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public List findAll() throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingCart IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingCartHBM ");

			Query q = session.createQuery(query.toString());
			Iterator itr = q.iterate();
			List list = new ArrayList();

			while (itr.hasNext()) {
				ShoppingCartHBM shoppingCartHBM = (ShoppingCartHBM)itr.next();
				list.add(ShoppingCartHBMUtil.model(shoppingCartHBM));
			}

			return list;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public void removeByCompanyId(String companyId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingCart IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingCartHBM WHERE ");
			query.append("companyId = ?");
			query.append(" ");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, companyId);

			Iterator itr = q.list().iterator();

			while (itr.hasNext()) {
				ShoppingCartHBM shoppingCartHBM = (ShoppingCartHBM)itr.next();
				ShoppingCartPool.remove((String)shoppingCartHBM.getPrimaryKey());
				session.delete(shoppingCartHBM);
			}

			session.flush();
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public void removeByUserId(String userId)
		throws NoSuchCartException, SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingCart IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingCartHBM WHERE ");
			query.append("userId = ?");
			query.append(" ");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, userId);

			Iterator itr = q.list().iterator();

			while (itr.hasNext()) {
				ShoppingCartHBM shoppingCartHBM = (ShoppingCartHBM)itr.next();
				ShoppingCartPool.remove((String)shoppingCartHBM.getPrimaryKey());
				session.delete(shoppingCartHBM);
			}

			session.flush();
		}
		catch (HibernateException he) {
			if (he instanceof ObjectNotFoundException) {
				String msg = "No ShoppingCart exists with the key ";
				msg += StringPool.OPEN_CURLY_BRACE;
				msg += "userId=";
				msg += userId;
				msg += StringPool.CLOSE_CURLY_BRACE;
				throw new NoSuchCartException(msg);
			}
			else {
				throw new SystemException(he);
			}
		}
		finally {
			closeSession(session);
		}
	}

	public int countByCompanyId(String companyId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append("SELECT COUNT(*) ");
			query.append(
				"FROM ShoppingCart IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingCartHBM WHERE ");
			query.append("companyId = ?");
			query.append(" ");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, companyId);

			Iterator itr = q.list().iterator();

			if (itr.hasNext()) {
				Integer count = (Integer)itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public int countByUserId(String userId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append("SELECT COUNT(*) ");
			query.append(
				"FROM ShoppingCart IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingCartHBM WHERE ");
			query.append("userId = ?");
			query.append(" ");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, userId);

			Iterator itr = q.list().iterator();

			if (itr.hasNext()) {
				Integer count = (Integer)itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	private static Log _log = LogFactory.getLog(ShoppingCartPersistence.class);
}