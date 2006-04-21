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

package com.liferay.portlet.documentlibrary.service.spring;

/**
 * <a href="DLFileRankLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class DLFileRankLocalServiceUtil {
	public static void deleteFileRanks(java.lang.String userId)
		throws com.liferay.portal.SystemException {
		try {
			DLFileRankLocalService dlFileRankLocalService = DLFileRankLocalServiceFactory.getService();
			dlFileRankLocalService.deleteFileRanks(userId);
		}
		catch (com.liferay.portal.SystemException se) {
			throw se;
		}
		catch (Exception e) {
			throw new com.liferay.portal.SystemException(e);
		}
	}

	public static void deleteFileRanks(java.lang.String folderId,
		java.lang.String name) throws com.liferay.portal.SystemException {
		try {
			DLFileRankLocalService dlFileRankLocalService = DLFileRankLocalServiceFactory.getService();
			dlFileRankLocalService.deleteFileRanks(folderId, name);
		}
		catch (com.liferay.portal.SystemException se) {
			throw se;
		}
		catch (Exception e) {
			throw new com.liferay.portal.SystemException(e);
		}
	}

	public static java.util.List getFileRanks(java.lang.String groupId,
		java.lang.String userId) throws com.liferay.portal.SystemException {
		try {
			DLFileRankLocalService dlFileRankLocalService = DLFileRankLocalServiceFactory.getService();

			return dlFileRankLocalService.getFileRanks(groupId, userId);
		}
		catch (com.liferay.portal.SystemException se) {
			throw se;
		}
		catch (Exception e) {
			throw new com.liferay.portal.SystemException(e);
		}
	}

	public static com.liferay.portlet.documentlibrary.model.DLFileRank updateFileRank(
		java.lang.String groupId, java.lang.String companyId,
		java.lang.String userId, java.lang.String folderId,
		java.lang.String name)
		throws com.liferay.portal.PortalException, 
			com.liferay.portal.SystemException {
		try {
			DLFileRankLocalService dlFileRankLocalService = DLFileRankLocalServiceFactory.getService();

			return dlFileRankLocalService.updateFileRank(groupId, companyId,
				userId, folderId, name);
		}
		catch (com.liferay.portal.PortalException pe) {
			throw pe;
		}
		catch (com.liferay.portal.SystemException se) {
			throw se;
		}
		catch (Exception e) {
			throw new com.liferay.portal.SystemException(e);
		}
	}
}