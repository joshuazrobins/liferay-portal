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

package com.liferay.portal.model;

import com.liferay.portal.model.BaseModel;
import com.liferay.portal.util.PropsUtil;

import com.liferay.util.GetterUtil;
import com.liferay.util.XSSUtil;

/**
 * <a href="PermissionModel.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class PermissionModel extends BaseModel {
	public static boolean XSS_ALLOW_BY_MODEL = GetterUtil.get(PropsUtil.get(
				"xss.allow.com.liferay.portal.model.Permission"), XSS_ALLOW);
	public static boolean XSS_ALLOW_PERMISSIONID = GetterUtil.get(PropsUtil.get(
				"xss.allow.com.liferay.portal.model.Permission.permissionId"),
			XSS_ALLOW_BY_MODEL);
	public static boolean XSS_ALLOW_COMPANYID = GetterUtil.get(PropsUtil.get(
				"xss.allow.com.liferay.portal.model.Permission.companyId"),
			XSS_ALLOW_BY_MODEL);
	public static boolean XSS_ALLOW_ACTIONID = GetterUtil.get(PropsUtil.get(
				"xss.allow.com.liferay.portal.model.Permission.actionId"),
			XSS_ALLOW_BY_MODEL);
	public static boolean XSS_ALLOW_RESOURCEID = GetterUtil.get(PropsUtil.get(
				"xss.allow.com.liferay.portal.model.Permission.resourceId"),
			XSS_ALLOW_BY_MODEL);
	public static long LOCK_EXPIRATION_TIME = GetterUtil.getLong(PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.PermissionModel"));

	public PermissionModel() {
	}

	public String getPrimaryKey() {
		return _permissionId;
	}

	public void setPrimaryKey(String pk) {
		setPermissionId(pk);
	}

	public String getPermissionId() {
		return GetterUtil.getString(_permissionId);
	}

	public void setPermissionId(String permissionId) {
		if (((permissionId == null) && (_permissionId != null)) ||
				((permissionId != null) && (_permissionId == null)) ||
				((permissionId != null) && (_permissionId != null) &&
				!permissionId.equals(_permissionId))) {
			if (!XSS_ALLOW_PERMISSIONID) {
				permissionId = XSSUtil.strip(permissionId);
			}

			_permissionId = permissionId;
			setModified(true);
		}
	}

	public String getCompanyId() {
		return GetterUtil.getString(_companyId);
	}

	public void setCompanyId(String companyId) {
		if (((companyId == null) && (_companyId != null)) ||
				((companyId != null) && (_companyId == null)) ||
				((companyId != null) && (_companyId != null) &&
				!companyId.equals(_companyId))) {
			if (!XSS_ALLOW_COMPANYID) {
				companyId = XSSUtil.strip(companyId);
			}

			_companyId = companyId;
			setModified(true);
		}
	}

	public String getActionId() {
		return GetterUtil.getString(_actionId);
	}

	public void setActionId(String actionId) {
		if (((actionId == null) && (_actionId != null)) ||
				((actionId != null) && (_actionId == null)) ||
				((actionId != null) && (_actionId != null) &&
				!actionId.equals(_actionId))) {
			if (!XSS_ALLOW_ACTIONID) {
				actionId = XSSUtil.strip(actionId);
			}

			_actionId = actionId;
			setModified(true);
		}
	}

	public String getResourceId() {
		return GetterUtil.getString(_resourceId);
	}

	public void setResourceId(String resourceId) {
		if (((resourceId == null) && (_resourceId != null)) ||
				((resourceId != null) && (_resourceId == null)) ||
				((resourceId != null) && (_resourceId != null) &&
				!resourceId.equals(_resourceId))) {
			if (!XSS_ALLOW_RESOURCEID) {
				resourceId = XSSUtil.strip(resourceId);
			}

			_resourceId = resourceId;
			setModified(true);
		}
	}

	public Object clone() {
		Permission clone = new Permission();
		clone.setPermissionId(getPermissionId());
		clone.setCompanyId(getCompanyId());
		clone.setActionId(getActionId());
		clone.setResourceId(getResourceId());

		return clone;
	}

	public int compareTo(Object obj) {
		if (obj == null) {
			return -1;
		}

		Permission permission = (Permission)obj;
		String pk = permission.getPrimaryKey();

		return getPrimaryKey().compareTo(pk);
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		Permission permission = null;

		try {
			permission = (Permission)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		String pk = permission.getPrimaryKey();

		if (getPrimaryKey().equals(pk)) {
			return true;
		}
		else {
			return false;
		}
	}

	public int hashCode() {
		return getPrimaryKey().hashCode();
	}

	private String _permissionId;
	private String _companyId;
	private String _actionId;
	private String _resourceId;
}