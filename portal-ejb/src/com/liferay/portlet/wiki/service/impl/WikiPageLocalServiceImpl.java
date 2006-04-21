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

package com.liferay.portlet.wiki.service.impl;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Resource;
import com.liferay.portal.model.User;
import com.liferay.portal.service.persistence.UserUtil;
import com.liferay.portal.service.spring.ResourceLocalServiceUtil;
import com.liferay.portlet.wiki.NoSuchPageException;
import com.liferay.portlet.wiki.PageContentException;
import com.liferay.portlet.wiki.PageTitleException;
import com.liferay.portlet.wiki.model.WikiNode;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.persistence.WikiNodeUtil;
import com.liferay.portlet.wiki.service.persistence.WikiPageFinder;
import com.liferay.portlet.wiki.service.persistence.WikiPagePK;
import com.liferay.portlet.wiki.service.persistence.WikiPageUtil;
import com.liferay.portlet.wiki.service.spring.WikiPageLocalService;
import com.liferay.portlet.wiki.util.Indexer;
import com.liferay.portlet.wiki.util.NodeFilter;
import com.liferay.portlet.wiki.util.WikiUtil;
import com.liferay.portlet.wiki.util.comparator.PageCreateDateComparator;
import com.liferay.util.MathUtil;
import com.liferay.util.Validator;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="WikiPageLocalServiceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class WikiPageLocalServiceImpl implements WikiPageLocalService {

	public WikiPage addPage(String userId, String nodeId, String title)
		throws PortalException, SystemException {

		// Page

		User user = UserUtil.findByPrimaryKey(userId);
		Date now = new Date();

		validate(title);

		WikiPagePK pk = new WikiPagePK(
			nodeId, title, WikiPage.DEFAULT_VERSION);

		WikiPage page = WikiPageUtil.create(pk);

		page.setCompanyId(user.getCompanyId());
		page.setUserId(user.getUserId());
		page.setUserName(user.getFullName());
		page.setCreateDate(now);
		page.setFormat(WikiPage.DEFAULT_FORMAT);
		page.setHead(true);

		WikiPageUtil.update(page);

		// Resources

		addPageResources(page.getNode(), page, true, true);

		return page;
	}

	public void addPageResources(
			String nodeId, String title, boolean addCommunityPermissions,
			boolean addGuestPermissions)
		throws PortalException, SystemException {

		WikiNode node = WikiNodeUtil.findByPrimaryKey(nodeId);
		WikiPage page = getPage(nodeId, title);

		addPageResources(
			node, page, addCommunityPermissions, addGuestPermissions);
	}

	public void addPageResources(
			WikiNode node, WikiPage page, boolean addCommunityPermissions,
			boolean addGuestPermissions)
		throws PortalException, SystemException {

		ResourceLocalServiceUtil.addResources(
			page.getCompanyId(), node.getGroupId(), page.getUserId(),
			WikiPage.class.getName(), page.getResourcePK().toString(),
			false, addCommunityPermissions, addGuestPermissions);
	}

	public void deletePage(String nodeId, String title)
		throws PortalException, SystemException {

		List pages = WikiPageUtil.findByN_T_H(nodeId, title, true, 0, 1);

		if (pages.size() > 0) {
			WikiPage page = (WikiPage)pages.iterator().next();

			deletePage(page);
		}
	}

	public void deletePage(WikiPage page)
		throws PortalException, SystemException {

		// Lucene

		try {
			Indexer.deletePage(
				page.getCompanyId(), page.getNodeId(), page.getTitle());
		}
		catch (IOException ioe) {
			_log.error(ioe.getMessage());
		}

		// Resources

		ResourceLocalServiceUtil.deleteResource(
			page.getCompanyId(), WikiPage.class.getName(), Resource.TYPE_CLASS,
			Resource.SCOPE_INDIVIDUAL, page.getResourcePK().toString());

		// All versions

		WikiPageUtil.removeByN_T(page.getNodeId(), page.getTitle());
	}

	public void deletePages(String nodeId)
		throws PortalException, SystemException {

		Iterator itr = WikiPageUtil.findByN_H(nodeId, true).iterator();

		while (itr.hasNext()) {
			WikiPage page = (WikiPage)itr.next();

			deletePage(page);
		}
	}

	public List getLinks(String nodeId, String title) throws SystemException {
		List links = new ArrayList();

		List pages = WikiPageUtil.findByN_H(nodeId, true);

		for (int i = 0; i < pages.size(); i++) {
			WikiPage page = (WikiPage)pages.get(i);

			if (page.getFormat().equals(WikiPage.CLASSIC_WIKI_FORMAT)) {
				NodeFilter filter = WikiUtil.getFilter(nodeId);

				try {
					WikiUtil.convert(filter, page.getContent());

					if (filter.getTitles().get(title) != null) {
						links.add(page);
					}
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		Collections.sort(links);

		return links;
	}

	public List getOrphans(String nodeId) throws SystemException {
		List pageTitles = new ArrayList();

		List pages = WikiPageUtil.findByN_H(nodeId, true);

		for (int i = 0; i < pages.size(); i++) {
			WikiPage page = (WikiPage)pages.get(i);

			if (page.getFormat().equals(WikiPage.CLASSIC_WIKI_FORMAT)) {
				NodeFilter filter = WikiUtil.getFilter(nodeId);

				try {
					WikiUtil.convert(filter, page.getContent());

					pageTitles.add(filter.getTitles());
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		Set notOrphans = new HashSet();

		for (int i = 0; i < pages.size(); i++) {
			WikiPage page = (WikiPage)pages.get(i);

			for (int j = 0; j < pageTitles.size(); j++) {
				Map titles = (Map)pageTitles.get(j);

				if (titles.get(page.getTitle()) != null) {
					notOrphans.add(page);

					break;
				}
			}
		}

		List orphans = new ArrayList();

		for (int i = 0; i < pages.size(); i++) {
			WikiPage page = (WikiPage)pages.get(i);

			if (!notOrphans.contains(page)) {
				orphans.add(page);
			}
		}

		Collections.sort(orphans);

		return orphans;
	}

	public WikiPage getPage(String nodeId, String title)
		throws PortalException, SystemException {

		List pages = WikiPageUtil.findByN_T_H(nodeId, title, true, 0, 1);

		if (pages.size() > 0) {
			return (WikiPage)pages.iterator().next();
		}
		else {
			throw new NoSuchPageException();
		}
	}

	public WikiPage getPage(String nodeId, String title, double version)
		throws PortalException, SystemException {

		WikiPage page = null;

		if (version == 0) {
			page = getPage(nodeId, title);
		}
		else {
			page = WikiPageUtil.findByPrimaryKey(
				new WikiPagePK(nodeId, title, version));
		}

		return page;
	}

	public List getPages(String nodeId, int begin, int end)
		throws SystemException {

		return WikiPageUtil.findByNodeId(
			nodeId, begin, end, new PageCreateDateComparator(false));
	}

	public List getPages(String nodeId, String title, int begin, int end)
		throws SystemException {

		return WikiPageUtil.findByN_T(
			nodeId, title, begin, end, new PageCreateDateComparator(false));
	}

	public List getPages(String nodeId, boolean head, int begin, int end)
		throws SystemException {

		return WikiPageUtil.findByN_H(
			nodeId, head, begin, end, new PageCreateDateComparator(false));
	}

	public List getPages(
			String nodeId, String title, boolean head, int begin, int end)
		throws SystemException {

		return WikiPageUtil.findByN_T_H(
			nodeId, title, head, begin, end,
			new PageCreateDateComparator(false));
	}

	public int getPagesCount(String nodeId) throws SystemException {
		return WikiPageUtil.countByNodeId(nodeId);
	}

	public int getPagesCount(String nodeId, String title)
		throws SystemException {

		return WikiPageUtil.countByN_T(nodeId, title);
	}

	public int getPagesCount(String nodeId, boolean head)
		throws SystemException {

		return WikiPageUtil.countByN_H(nodeId, head);
	}

	public int getPagesCount(String nodeId, String title, boolean head)
		throws SystemException {

		return WikiPageUtil.countByN_T_H(nodeId, title, head);
	}

	public List getRecentChanges(String nodeId, int begin, int end)
		throws SystemException {

		Calendar cal = new GregorianCalendar();

		cal.add(Calendar.WEEK_OF_YEAR, -1);

		return WikiPageFinder.findByCreateDate(
			nodeId, cal.getTime(), false, begin, end);
	}

	public int getRecentChangesCount(String nodeId) throws SystemException {
		Calendar cal = new GregorianCalendar();

		cal.add(Calendar.WEEK_OF_YEAR, -1);

		return WikiPageFinder.countByCreateDate(nodeId, cal.getTime(), false);
	}

	public WikiPage revertPage(
			String userId, String nodeId, String title, double version)
		throws PortalException, SystemException {

		WikiPage oldPage = getPage(nodeId, title, version);

		return updatePage(
			userId, nodeId, title, oldPage.getContent(), oldPage.getFormat());
	}

	public WikiPage updatePage(
			String userId, String nodeId, String title, String content,
			String format)
		throws PortalException, SystemException {

		// Page

		User user = UserUtil.findByPrimaryKey(userId);
		Date now = new Date();

		validate(nodeId, content, format);

		WikiPage page = getPage(nodeId, title);

		page.setHead(false);

		WikiPageUtil.update(page);

		double oldVersion = page.getVersion();
		double newVersion = MathUtil.format(oldVersion + 0.1, 1, 1);

		WikiPagePK pk = new WikiPagePK(nodeId, title, newVersion);

		page = WikiPageUtil.create(pk);

		page.setCompanyId(user.getCompanyId());
		page.setUserId(user.getUserId());
		page.setUserName(user.getFullName());
		page.setCreateDate(now);
		page.setContent(content);
		page.setFormat(format);
		page.setHead(true);

		WikiPageUtil.update(page);

		// Node

		WikiNode node = WikiNodeUtil.findByPrimaryKey(nodeId);

		node.setLastPostDate(now);

		WikiNodeUtil.update(node);

		// Lucene

		try {
			Indexer.updatePage(
				node.getCompanyId(), node.getGroupId(), nodeId, title, content);
		}
		catch (IOException ioe) {
			_log.error(ioe.getMessage());
		}

		return page;
	}

	protected void validate(String title) throws PortalException {
		if (Validator.isNull(title)) {
			throw new PageTitleException();
		}

		Pattern pattern = Pattern.compile("(([A-Z][a-z]+){2,})");
		Matcher matcher = pattern.matcher(title);

		if (!matcher.matches()) {
			throw new PageTitleException();
		}
	}

	protected void validate(String nodeId, String content, String format)
		throws PortalException {

		if (format.equals(WikiPage.CLASSIC_WIKI_FORMAT)) {
			try {
				NodeFilter filter = WikiUtil.getFilter(nodeId);

				WikiUtil.convert(filter, content);
			}
			catch (Exception e) {
				throw new PageContentException();
			}
		}
	}

	private static Log _log = LogFactory.getLog(WikiPageLocalServiceImpl.class);

}