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

package com.liferay.portal.service.ejb;

import com.liferay.portal.service.impl.PrincipalSessionBean;
import com.liferay.portal.service.spring.OrgLaborService;
import com.liferay.portal.spring.util.SpringUtil;

import org.springframework.context.ApplicationContext;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <a href="OrgLaborServiceEJBImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class OrgLaborServiceEJBImpl implements OrgLaborService, SessionBean {
	public static final String CLASS_NAME = OrgLaborService.class.getName() +
		".transaction";

	public static OrgLaborService getService() {
		ApplicationContext ctx = SpringUtil.getContext();

		return (OrgLaborService)ctx.getBean(CLASS_NAME);
	}

	public com.liferay.portal.model.OrgLabor addOrgLabor(
		java.lang.String organizationId, java.lang.String typeId, int sunOpen,
		int sunClose, int monOpen, int monClose, int tueOpen, int tueClose,
		int wedOpen, int wedClose, int thuOpen, int thuClose, int friOpen,
		int friClose, int satOpen, int satClose)
		throws com.liferay.portal.PortalException, 
			com.liferay.portal.SystemException, java.rmi.RemoteException {
		PrincipalSessionBean.setThreadValues(_sc);

		return getService().addOrgLabor(organizationId, typeId, sunOpen,
			sunClose, monOpen, monClose, tueOpen, tueClose, wedOpen, wedClose,
			thuOpen, thuClose, friOpen, friClose, satOpen, satClose);
	}

	public void deleteOrgLabor(java.lang.String orgLaborId)
		throws com.liferay.portal.PortalException, 
			com.liferay.portal.SystemException, java.rmi.RemoteException {
		PrincipalSessionBean.setThreadValues(_sc);
		getService().deleteOrgLabor(orgLaborId);
	}

	public com.liferay.portal.model.OrgLabor getOrgLabor(
		java.lang.String orgLaborId)
		throws com.liferay.portal.PortalException, 
			com.liferay.portal.SystemException, java.rmi.RemoteException {
		PrincipalSessionBean.setThreadValues(_sc);

		return getService().getOrgLabor(orgLaborId);
	}

	public java.util.List getOrgLabors(java.lang.String organizationId)
		throws com.liferay.portal.SystemException, java.rmi.RemoteException {
		PrincipalSessionBean.setThreadValues(_sc);

		return getService().getOrgLabors(organizationId);
	}

	public com.liferay.portal.model.OrgLabor updateOrgLabor(
		java.lang.String orgLaborId, int sunOpen, int sunClose, int monOpen,
		int monClose, int tueOpen, int tueClose, int wedOpen, int wedClose,
		int thuOpen, int thuClose, int friOpen, int friClose, int satOpen,
		int satClose)
		throws com.liferay.portal.PortalException, 
			com.liferay.portal.SystemException, java.rmi.RemoteException {
		PrincipalSessionBean.setThreadValues(_sc);

		return getService().updateOrgLabor(orgLaborId, sunOpen, sunClose,
			monOpen, monClose, tueOpen, tueClose, wedOpen, wedClose, thuOpen,
			thuClose, friOpen, friClose, satOpen, satClose);
	}

	public void ejbCreate() throws CreateException {
	}

	public void ejbRemove() {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public SessionContext getSessionContext() {
		return _sc;
	}

	public void setSessionContext(SessionContext sc) {
		_sc = sc;
	}

	private SessionContext _sc;
}