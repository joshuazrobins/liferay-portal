/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portalweb.portlet.documentsandmediadisplay.folder.viewdmdfoldersaddmultipledocuments;

import com.liferay.portalweb.portal.BaseTestSuite;
import com.liferay.portalweb.portal.util.TearDownPageTest;
import com.liferay.portalweb.portlet.documentsandmediadisplay.folder.adddmdfolder.AddDMDFolder1Test;
import com.liferay.portalweb.portlet.documentsandmediadisplay.folder.adddmdfolder.AddDMDFolder2Test;
import com.liferay.portalweb.portlet.documentsandmediadisplay.folder.adddmdfolder.AddDMDFolder3Test;
import com.liferay.portalweb.portlet.documentsandmediadisplay.folder.adddmdfolder.TearDownDMDFolderTest;
import com.liferay.portalweb.portlet.documentsandmediadisplay.portlet.addportletdmd.AddPageDMDTest;
import com.liferay.portalweb.portlet.documentsandmediadisplay.portlet.addportletdmd.AddPortletDMDTest;
import com.liferay.portalweb.portlet.documentsandmediadisplay.portlet.configureportletdmddisplaysettings.ConfigurePortletDMDDisplaySettingsTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brian Wing Shun Chan
 */
public class ViewDMDFoldersAddMultipleDocumentsTests extends BaseTestSuite {
	public static Test suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTestSuite(AddPageDMDTest.class);
		testSuite.addTestSuite(AddPortletDMDTest.class);
		testSuite.addTestSuite(ConfigurePortletDMDDisplaySettingsTest.class);
		testSuite.addTestSuite(AddDMDFolder1Test.class);
		testSuite.addTestSuite(AddDMDFolder2Test.class);
		testSuite.addTestSuite(AddDMDFolder3Test.class);
		testSuite.addTestSuite(ViewDMDFolder1AddMultipleDocumentsActionsTest.class);
		testSuite.addTestSuite(ViewDMDFolder2AddMultipleDocumentsActionsTest.class);
		testSuite.addTestSuite(ViewDMDFolder3AddMultipleDocumentsActionsTest.class);
		testSuite.addTestSuite(ViewDMDFolder1AddMultipleDocumentsDetailsTest.class);
		testSuite.addTestSuite(ViewDMDFolder2AddMultipleDocumentsDetailsTest.class);
		testSuite.addTestSuite(ViewDMDFolder3AddMultipleDocumentsDetailsTest.class);
		testSuite.addTestSuite(TearDownDMDFolderTest.class);
		testSuite.addTestSuite(TearDownPageTest.class);

		return testSuite;
	}
}