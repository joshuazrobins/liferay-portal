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

package com.liferay.portlet.blogs.service.http;

import com.liferay.portal.security.auth.HttpPrincipal;
import com.liferay.portal.servlet.TunnelUtil;
import com.liferay.portal.shared.util.BooleanWrapper;
import com.liferay.portal.shared.util.MethodWrapper;
import com.liferay.portal.shared.util.NullWrapper;
import com.liferay.portal.shared.util.StackTraceUtil;

import com.liferay.portlet.blogs.service.spring.BlogsCategoryServiceUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="BlogsCategoryServiceHttp.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class BlogsCategoryServiceHttp {
	public static com.liferay.portlet.blogs.model.BlogsCategory addCategory(
		HttpPrincipal httpPrincipal, java.lang.String parentCategoryId,
		java.lang.String name, java.lang.String description,
		boolean addCommunityPermissions, boolean addGuestPermissions)
		throws com.liferay.portal.PortalException, 
			com.liferay.portal.SystemException {
		try {
			Object paramObj0 = parentCategoryId;

			if (parentCategoryId == null) {
				paramObj0 = new NullWrapper("java.lang.String");
			}

			Object paramObj1 = name;

			if (name == null) {
				paramObj1 = new NullWrapper("java.lang.String");
			}

			Object paramObj2 = description;

			if (description == null) {
				paramObj2 = new NullWrapper("java.lang.String");
			}

			Object paramObj3 = new BooleanWrapper(addCommunityPermissions);
			Object paramObj4 = new BooleanWrapper(addGuestPermissions);
			MethodWrapper methodWrapper = new MethodWrapper(BlogsCategoryServiceUtil.class.getName(),
					"addCategory",
					new Object[] {
						paramObj0, paramObj1, paramObj2, paramObj3, paramObj4
					});
			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodWrapper);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.PortalException) {
					throw (com.liferay.portal.PortalException)e;
				}

				if (e instanceof com.liferay.portal.SystemException) {
					throw (com.liferay.portal.SystemException)e;
				}

				throw e;
			}

			return (com.liferay.portlet.blogs.model.BlogsCategory)returnObj;
		}
		catch (com.liferay.portal.PortalException pe) {
			_log.error(StackTraceUtil.getStackTrace(pe));
			throw pe;
		}
		catch (com.liferay.portal.SystemException se) {
			_log.error(StackTraceUtil.getStackTrace(se));
			throw se;
		}
		catch (Exception e) {
			String stackTrace = StackTraceUtil.getStackTrace(e);
			_log.error(stackTrace);
			throw new com.liferay.portal.SystemException(stackTrace);
		}
	}

	public static void deleteCategory(HttpPrincipal httpPrincipal,
		java.lang.String categoryId)
		throws com.liferay.portal.PortalException, 
			com.liferay.portal.SystemException {
		try {
			Object paramObj0 = categoryId;

			if (categoryId == null) {
				paramObj0 = new NullWrapper("java.lang.String");
			}

			MethodWrapper methodWrapper = new MethodWrapper(BlogsCategoryServiceUtil.class.getName(),
					"deleteCategory", new Object[] { paramObj0 });
			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodWrapper);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.PortalException) {
					throw (com.liferay.portal.PortalException)e;
				}

				if (e instanceof com.liferay.portal.SystemException) {
					throw (com.liferay.portal.SystemException)e;
				}

				throw e;
			}
		}
		catch (com.liferay.portal.PortalException pe) {
			_log.error(StackTraceUtil.getStackTrace(pe));
			throw pe;
		}
		catch (com.liferay.portal.SystemException se) {
			_log.error(StackTraceUtil.getStackTrace(se));
			throw se;
		}
		catch (Exception e) {
			String stackTrace = StackTraceUtil.getStackTrace(e);
			_log.error(stackTrace);
			throw new com.liferay.portal.SystemException(stackTrace);
		}
	}

	public static com.liferay.portlet.blogs.model.BlogsCategory getCategory(
		HttpPrincipal httpPrincipal, java.lang.String categoryId)
		throws com.liferay.portal.PortalException, 
			com.liferay.portal.SystemException {
		try {
			Object paramObj0 = categoryId;

			if (categoryId == null) {
				paramObj0 = new NullWrapper("java.lang.String");
			}

			MethodWrapper methodWrapper = new MethodWrapper(BlogsCategoryServiceUtil.class.getName(),
					"getCategory", new Object[] { paramObj0 });
			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodWrapper);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.PortalException) {
					throw (com.liferay.portal.PortalException)e;
				}

				if (e instanceof com.liferay.portal.SystemException) {
					throw (com.liferay.portal.SystemException)e;
				}

				throw e;
			}

			return (com.liferay.portlet.blogs.model.BlogsCategory)returnObj;
		}
		catch (com.liferay.portal.PortalException pe) {
			_log.error(StackTraceUtil.getStackTrace(pe));
			throw pe;
		}
		catch (com.liferay.portal.SystemException se) {
			_log.error(StackTraceUtil.getStackTrace(se));
			throw se;
		}
		catch (Exception e) {
			String stackTrace = StackTraceUtil.getStackTrace(e);
			_log.error(stackTrace);
			throw new com.liferay.portal.SystemException(stackTrace);
		}
	}

	public static com.liferay.portlet.blogs.model.BlogsCategory updateCategory(
		HttpPrincipal httpPrincipal, java.lang.String categoryId,
		java.lang.String parentCategoryId, java.lang.String name,
		java.lang.String description)
		throws com.liferay.portal.PortalException, 
			com.liferay.portal.SystemException {
		try {
			Object paramObj0 = categoryId;

			if (categoryId == null) {
				paramObj0 = new NullWrapper("java.lang.String");
			}

			Object paramObj1 = parentCategoryId;

			if (parentCategoryId == null) {
				paramObj1 = new NullWrapper("java.lang.String");
			}

			Object paramObj2 = name;

			if (name == null) {
				paramObj2 = new NullWrapper("java.lang.String");
			}

			Object paramObj3 = description;

			if (description == null) {
				paramObj3 = new NullWrapper("java.lang.String");
			}

			MethodWrapper methodWrapper = new MethodWrapper(BlogsCategoryServiceUtil.class.getName(),
					"updateCategory",
					new Object[] { paramObj0, paramObj1, paramObj2, paramObj3 });
			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodWrapper);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.PortalException) {
					throw (com.liferay.portal.PortalException)e;
				}

				if (e instanceof com.liferay.portal.SystemException) {
					throw (com.liferay.portal.SystemException)e;
				}

				throw e;
			}

			return (com.liferay.portlet.blogs.model.BlogsCategory)returnObj;
		}
		catch (com.liferay.portal.PortalException pe) {
			_log.error(StackTraceUtil.getStackTrace(pe));
			throw pe;
		}
		catch (com.liferay.portal.SystemException se) {
			_log.error(StackTraceUtil.getStackTrace(se));
			throw se;
		}
		catch (Exception e) {
			String stackTrace = StackTraceUtil.getStackTrace(e);
			_log.error(stackTrace);
			throw new com.liferay.portal.SystemException(stackTrace);
		}
	}

	private static Log _log = LogFactory.getLog(BlogsCategoryServiceHttp.class);
}