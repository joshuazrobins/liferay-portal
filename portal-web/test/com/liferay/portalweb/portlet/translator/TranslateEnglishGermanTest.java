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

package com.liferay.portalweb.portlet.translator;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class TranslateEnglishGermanTest extends BaseTestCase {
	public void testTranslateEnglishGerman() throws Exception {
		selenium.open("/web/guest/home/");
		loadRequiredJavaScriptModules();

		for (int second = 0;; second++) {
			if (second >= 90) {
				fail("timeout");
			}

			try {
				if (selenium.isVisible("link=Translator Test Page")) {
					break;
				}
			}
			catch (Exception e) {
			}

			Thread.sleep(1000);
		}

		selenium.clickAt("link=Translator Test Page",
			RuntimeVariables.replace("Translator Test Page"));
		selenium.waitForPageToLoad("30000");
		loadRequiredJavaScriptModules();
		selenium.type("//textarea[@id='_26_text']",
			RuntimeVariables.replace(
				"My name is Liferay Translator, fluent in over 6 million forms of communication."));
		selenium.select("//select[@id='_26_id']",
			RuntimeVariables.replace("English to German"));
		selenium.clickAt("//input[@value='Translate']",
			RuntimeVariables.replace("Translate"));
		selenium.waitForPageToLoad("30000");
		loadRequiredJavaScriptModules();
		assertTrue(selenium.isTextPresent(
				"Mein Name ist der Liferay \u00dcbersetzer, der innen \u00fcber 6 Million Formen Kommunikation flie\u00dfend ist."));
	}
}