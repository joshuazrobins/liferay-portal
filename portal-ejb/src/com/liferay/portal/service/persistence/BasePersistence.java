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

package com.liferay.portal.service.persistence;

import com.liferay.portal.spring.hibernate.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionFactoryImplementor;

/**
 * <a href="BasePersistence.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class BasePersistence {

	public SessionFactory getSessionFactory() {
		return _sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		_sessionFactory = (SessionFactoryImplementor)sessionFactory;
		_dialect = _sessionFactory.getDialect();

		_log.debug(
			"Connection provider " +
				_sessionFactory.getConnectionProvider().getClass().getName());

		_log.debug("Dialect " + _dialect.getClass().getName());
	}

	protected Dialect getDialect() {
		return _dialect;
	}

	public void closeSession(Session session) {
		HibernateUtil.closeSession(session);
	}

	protected Session openSession() throws HibernateException {
		return HibernateUtil.openSession(_sessionFactory);
	}

	protected Session openSession(SessionFactory sessionFactory)
		throws HibernateException {

		return HibernateUtil.openSession(sessionFactory);
	}

	private static Log _log = LogFactory.getLog(BasePersistence.class);

	private SessionFactoryImplementor _sessionFactory;
	private Dialect _dialect;

}