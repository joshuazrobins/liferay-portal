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

import com.liferay.portlet.shopping.NoSuchOrderItemException;

import com.liferay.util.StringPool;
import com.liferay.util.dao.hibernate.OrderByComparator;
import com.liferay.util.dao.hibernate.QueryUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="ShoppingOrderItemPersistence.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ShoppingOrderItemPersistence extends BasePersistence {
	public com.liferay.portlet.shopping.model.ShoppingOrderItem create(
		ShoppingOrderItemPK shoppingOrderItemPK) {
		return new com.liferay.portlet.shopping.model.ShoppingOrderItem(shoppingOrderItemPK);
	}

	public com.liferay.portlet.shopping.model.ShoppingOrderItem remove(
		ShoppingOrderItemPK shoppingOrderItemPK)
		throws NoSuchOrderItemException, SystemException {
		Session session = null;

		try {
			session = openSession();

			ShoppingOrderItemHBM shoppingOrderItemHBM = (ShoppingOrderItemHBM)session.get(ShoppingOrderItemHBM.class,
					shoppingOrderItemPK);

			if (shoppingOrderItemHBM == null) {
				_log.warn("No ShoppingOrderItem exists with the primary key " +
					shoppingOrderItemPK.toString());
				throw new NoSuchOrderItemException(
					"No ShoppingOrderItem exists with the primary key " +
					shoppingOrderItemPK.toString());
			}

			com.liferay.portlet.shopping.model.ShoppingOrderItem shoppingOrderItem =
				ShoppingOrderItemHBMUtil.model(shoppingOrderItemHBM);
			session.delete(shoppingOrderItemHBM);
			session.flush();
			ShoppingOrderItemPool.remove(shoppingOrderItemPK);

			return shoppingOrderItem;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingOrderItem update(
		com.liferay.portlet.shopping.model.ShoppingOrderItem shoppingOrderItem)
		throws SystemException {
		Session session = null;

		try {
			if (shoppingOrderItem.isNew() || shoppingOrderItem.isModified()) {
				session = openSession();

				if (shoppingOrderItem.isNew()) {
					ShoppingOrderItemHBM shoppingOrderItemHBM = new ShoppingOrderItemHBM(shoppingOrderItem.getOrderId(),
							shoppingOrderItem.getItemId(),
							shoppingOrderItem.getSku(),
							shoppingOrderItem.getName(),
							shoppingOrderItem.getDescription(),
							shoppingOrderItem.getProperties(),
							shoppingOrderItem.getSupplierUserId(),
							shoppingOrderItem.getPrice(),
							shoppingOrderItem.getQuantity(),
							shoppingOrderItem.getShippedDate());
					session.save(shoppingOrderItemHBM);
					session.flush();
				}
				else {
					ShoppingOrderItemHBM shoppingOrderItemHBM = (ShoppingOrderItemHBM)session.get(ShoppingOrderItemHBM.class,
							shoppingOrderItem.getPrimaryKey());

					if (shoppingOrderItemHBM != null) {
						shoppingOrderItemHBM.setSku(shoppingOrderItem.getSku());
						shoppingOrderItemHBM.setName(shoppingOrderItem.getName());
						shoppingOrderItemHBM.setDescription(shoppingOrderItem.getDescription());
						shoppingOrderItemHBM.setProperties(shoppingOrderItem.getProperties());
						shoppingOrderItemHBM.setSupplierUserId(shoppingOrderItem.getSupplierUserId());
						shoppingOrderItemHBM.setPrice(shoppingOrderItem.getPrice());
						shoppingOrderItemHBM.setQuantity(shoppingOrderItem.getQuantity());
						shoppingOrderItemHBM.setShippedDate(shoppingOrderItem.getShippedDate());
						session.flush();
					}
					else {
						shoppingOrderItemHBM = new ShoppingOrderItemHBM(shoppingOrderItem.getOrderId(),
								shoppingOrderItem.getItemId(),
								shoppingOrderItem.getSku(),
								shoppingOrderItem.getName(),
								shoppingOrderItem.getDescription(),
								shoppingOrderItem.getProperties(),
								shoppingOrderItem.getSupplierUserId(),
								shoppingOrderItem.getPrice(),
								shoppingOrderItem.getQuantity(),
								shoppingOrderItem.getShippedDate());
						session.save(shoppingOrderItemHBM);
						session.flush();
					}
				}

				shoppingOrderItem.setNew(false);
				shoppingOrderItem.setModified(false);
				shoppingOrderItem.protect();
				ShoppingOrderItemPool.update(shoppingOrderItem.getPrimaryKey(),
					shoppingOrderItem);
			}

			return shoppingOrderItem;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingOrderItem findByPrimaryKey(
		ShoppingOrderItemPK shoppingOrderItemPK)
		throws NoSuchOrderItemException, SystemException {
		com.liferay.portlet.shopping.model.ShoppingOrderItem shoppingOrderItem = ShoppingOrderItemPool.get(shoppingOrderItemPK);
		Session session = null;

		try {
			if (shoppingOrderItem == null) {
				session = openSession();

				ShoppingOrderItemHBM shoppingOrderItemHBM = (ShoppingOrderItemHBM)session.get(ShoppingOrderItemHBM.class,
						shoppingOrderItemPK);

				if (shoppingOrderItemHBM == null) {
					_log.warn(
						"No ShoppingOrderItem exists with the primary key " +
						shoppingOrderItemPK.toString());
					throw new NoSuchOrderItemException(
						"No ShoppingOrderItem exists with the primary key " +
						shoppingOrderItemPK.toString());
				}

				shoppingOrderItem = ShoppingOrderItemHBMUtil.model(shoppingOrderItemHBM,
						false);
			}

			return shoppingOrderItem;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public List findByOrderId(String orderId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM WHERE ");
			query.append("orderId = ?");
			query.append(" ");
			query.append("ORDER BY ");
			query.append("supplierUserId ASC").append(", ");
			query.append("name ASC").append(", ");
			query.append("description ASC");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, orderId);

			Iterator itr = q.list().iterator();
			List list = new ArrayList();

			while (itr.hasNext()) {
				ShoppingOrderItemHBM shoppingOrderItemHBM = (ShoppingOrderItemHBM)itr.next();
				list.add(ShoppingOrderItemHBMUtil.model(shoppingOrderItemHBM));
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

	public List findByOrderId(String orderId, int begin, int end)
		throws SystemException {
		return findByOrderId(orderId, begin, end, null);
	}

	public List findByOrderId(String orderId, int begin, int end,
		OrderByComparator obc) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM WHERE ");
			query.append("orderId = ?");
			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY " + obc.getOrderBy());
			}
			else {
				query.append("ORDER BY ");
				query.append("supplierUserId ASC").append(", ");
				query.append("name ASC").append(", ");
				query.append("description ASC");
			}

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, orderId);

			List list = new ArrayList();
			Iterator itr = QueryUtil.iterate(q, getDialect(), begin, end);

			while (itr.hasNext()) {
				ShoppingOrderItemHBM shoppingOrderItemHBM = (ShoppingOrderItemHBM)itr.next();
				list.add(ShoppingOrderItemHBMUtil.model(shoppingOrderItemHBM));
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

	public com.liferay.portlet.shopping.model.ShoppingOrderItem findByOrderId_First(
		String orderId, OrderByComparator obc)
		throws NoSuchOrderItemException, SystemException {
		List list = findByOrderId(orderId, 0, 1, obc);

		if (list.size() == 0) {
			String msg = "No ShoppingOrderItem exists with the key ";
			msg += StringPool.OPEN_CURLY_BRACE;
			msg += "orderId=";
			msg += orderId;
			msg += StringPool.CLOSE_CURLY_BRACE;
			throw new NoSuchOrderItemException(msg);
		}
		else {
			return (com.liferay.portlet.shopping.model.ShoppingOrderItem)list.get(0);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingOrderItem findByOrderId_Last(
		String orderId, OrderByComparator obc)
		throws NoSuchOrderItemException, SystemException {
		int count = countByOrderId(orderId);
		List list = findByOrderId(orderId, count - 1, count, obc);

		if (list.size() == 0) {
			String msg = "No ShoppingOrderItem exists with the key ";
			msg += StringPool.OPEN_CURLY_BRACE;
			msg += "orderId=";
			msg += orderId;
			msg += StringPool.CLOSE_CURLY_BRACE;
			throw new NoSuchOrderItemException(msg);
		}
		else {
			return (com.liferay.portlet.shopping.model.ShoppingOrderItem)list.get(0);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingOrderItem[] findByOrderId_PrevAndNext(
		ShoppingOrderItemPK shoppingOrderItemPK, String orderId,
		OrderByComparator obc) throws NoSuchOrderItemException, SystemException {
		com.liferay.portlet.shopping.model.ShoppingOrderItem shoppingOrderItem = findByPrimaryKey(shoppingOrderItemPK);
		int count = countByOrderId(orderId);
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM WHERE ");
			query.append("orderId = ?");
			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY " + obc.getOrderBy());
			}
			else {
				query.append("ORDER BY ");
				query.append("supplierUserId ASC").append(", ");
				query.append("name ASC").append(", ");
				query.append("description ASC");
			}

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, orderId);

			Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
					shoppingOrderItem, ShoppingOrderItemHBMUtil.getInstance());
			com.liferay.portlet.shopping.model.ShoppingOrderItem[] array = new com.liferay.portlet.shopping.model.ShoppingOrderItem[3];
			array[0] = (com.liferay.portlet.shopping.model.ShoppingOrderItem)objArray[0];
			array[1] = (com.liferay.portlet.shopping.model.ShoppingOrderItem)objArray[1];
			array[2] = (com.liferay.portlet.shopping.model.ShoppingOrderItem)objArray[2];

			return array;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public List findByItemId(String itemId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM WHERE ");
			query.append("itemId = ?");
			query.append(" ");
			query.append("ORDER BY ");
			query.append("supplierUserId ASC").append(", ");
			query.append("name ASC").append(", ");
			query.append("description ASC");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, itemId);

			Iterator itr = q.list().iterator();
			List list = new ArrayList();

			while (itr.hasNext()) {
				ShoppingOrderItemHBM shoppingOrderItemHBM = (ShoppingOrderItemHBM)itr.next();
				list.add(ShoppingOrderItemHBMUtil.model(shoppingOrderItemHBM));
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

	public List findByItemId(String itemId, int begin, int end)
		throws SystemException {
		return findByItemId(itemId, begin, end, null);
	}

	public List findByItemId(String itemId, int begin, int end,
		OrderByComparator obc) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM WHERE ");
			query.append("itemId = ?");
			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY " + obc.getOrderBy());
			}
			else {
				query.append("ORDER BY ");
				query.append("supplierUserId ASC").append(", ");
				query.append("name ASC").append(", ");
				query.append("description ASC");
			}

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, itemId);

			List list = new ArrayList();
			Iterator itr = QueryUtil.iterate(q, getDialect(), begin, end);

			while (itr.hasNext()) {
				ShoppingOrderItemHBM shoppingOrderItemHBM = (ShoppingOrderItemHBM)itr.next();
				list.add(ShoppingOrderItemHBMUtil.model(shoppingOrderItemHBM));
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

	public com.liferay.portlet.shopping.model.ShoppingOrderItem findByItemId_First(
		String itemId, OrderByComparator obc)
		throws NoSuchOrderItemException, SystemException {
		List list = findByItemId(itemId, 0, 1, obc);

		if (list.size() == 0) {
			String msg = "No ShoppingOrderItem exists with the key ";
			msg += StringPool.OPEN_CURLY_BRACE;
			msg += "itemId=";
			msg += itemId;
			msg += StringPool.CLOSE_CURLY_BRACE;
			throw new NoSuchOrderItemException(msg);
		}
		else {
			return (com.liferay.portlet.shopping.model.ShoppingOrderItem)list.get(0);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingOrderItem findByItemId_Last(
		String itemId, OrderByComparator obc)
		throws NoSuchOrderItemException, SystemException {
		int count = countByItemId(itemId);
		List list = findByItemId(itemId, count - 1, count, obc);

		if (list.size() == 0) {
			String msg = "No ShoppingOrderItem exists with the key ";
			msg += StringPool.OPEN_CURLY_BRACE;
			msg += "itemId=";
			msg += itemId;
			msg += StringPool.CLOSE_CURLY_BRACE;
			throw new NoSuchOrderItemException(msg);
		}
		else {
			return (com.liferay.portlet.shopping.model.ShoppingOrderItem)list.get(0);
		}
	}

	public com.liferay.portlet.shopping.model.ShoppingOrderItem[] findByItemId_PrevAndNext(
		ShoppingOrderItemPK shoppingOrderItemPK, String itemId,
		OrderByComparator obc) throws NoSuchOrderItemException, SystemException {
		com.liferay.portlet.shopping.model.ShoppingOrderItem shoppingOrderItem = findByPrimaryKey(shoppingOrderItemPK);
		int count = countByItemId(itemId);
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM WHERE ");
			query.append("itemId = ?");
			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY " + obc.getOrderBy());
			}
			else {
				query.append("ORDER BY ");
				query.append("supplierUserId ASC").append(", ");
				query.append("name ASC").append(", ");
				query.append("description ASC");
			}

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, itemId);

			Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
					shoppingOrderItem, ShoppingOrderItemHBMUtil.getInstance());
			com.liferay.portlet.shopping.model.ShoppingOrderItem[] array = new com.liferay.portlet.shopping.model.ShoppingOrderItem[3];
			array[0] = (com.liferay.portlet.shopping.model.ShoppingOrderItem)objArray[0];
			array[1] = (com.liferay.portlet.shopping.model.ShoppingOrderItem)objArray[1];
			array[2] = (com.liferay.portlet.shopping.model.ShoppingOrderItem)objArray[2];

			return array;
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
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM ");
			query.append("ORDER BY ");
			query.append("supplierUserId ASC").append(", ");
			query.append("name ASC").append(", ");
			query.append("description ASC");

			Query q = session.createQuery(query.toString());
			Iterator itr = q.iterate();
			List list = new ArrayList();

			while (itr.hasNext()) {
				ShoppingOrderItemHBM shoppingOrderItemHBM = (ShoppingOrderItemHBM)itr.next();
				list.add(ShoppingOrderItemHBMUtil.model(shoppingOrderItemHBM));
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

	public void removeByOrderId(String orderId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM WHERE ");
			query.append("orderId = ?");
			query.append(" ");
			query.append("ORDER BY ");
			query.append("supplierUserId ASC").append(", ");
			query.append("name ASC").append(", ");
			query.append("description ASC");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, orderId);

			Iterator itr = q.list().iterator();

			while (itr.hasNext()) {
				ShoppingOrderItemHBM shoppingOrderItemHBM = (ShoppingOrderItemHBM)itr.next();
				ShoppingOrderItemPool.remove((ShoppingOrderItemPK)shoppingOrderItemHBM.getPrimaryKey());
				session.delete(shoppingOrderItemHBM);
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

	public void removeByItemId(String itemId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM WHERE ");
			query.append("itemId = ?");
			query.append(" ");
			query.append("ORDER BY ");
			query.append("supplierUserId ASC").append(", ");
			query.append("name ASC").append(", ");
			query.append("description ASC");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, itemId);

			Iterator itr = q.list().iterator();

			while (itr.hasNext()) {
				ShoppingOrderItemHBM shoppingOrderItemHBM = (ShoppingOrderItemHBM)itr.next();
				ShoppingOrderItemPool.remove((ShoppingOrderItemPK)shoppingOrderItemHBM.getPrimaryKey());
				session.delete(shoppingOrderItemHBM);
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

	public int countByOrderId(String orderId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append("SELECT COUNT(*) ");
			query.append(
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM WHERE ");
			query.append("orderId = ?");
			query.append(" ");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, orderId);

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

	public int countByItemId(String itemId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append("SELECT COUNT(*) ");
			query.append(
				"FROM ShoppingOrderItem IN CLASS com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemHBM WHERE ");
			query.append("itemId = ?");
			query.append(" ");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;
			q.setString(queryPos++, itemId);

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

	private static Log _log = LogFactory.getLog(ShoppingOrderItemPersistence.class);
}