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

package com.liferay.portlet.addressbook.util;

import com.liferay.portlet.addressbook.model.ABContact;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * <a href="OutlookExporter.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class OutlookExporter extends Exporter {

	public static final String FILE_NAME = "outlook.csv";

	public void setContacts(ABContact[] contacts) {
		StringBuffer sb = new StringBuffer();

		sb.append("\"First Name\",");
		sb.append("\"Last Name\",");
		sb.append("\"E-mail Address\",");

		sb.append("\"Home Street\",");
		sb.append("\"Home City\",");
		sb.append("\"Home State\",");
		sb.append("\"Home Postal Code\",");
		sb.append("\"Home Country\",");
		sb.append("\"Home Phone\",");
		sb.append("\"Home Fax\",");
		sb.append("\"Home Phone 2\",");

		sb.append("\"Company\",");
		sb.append("\"Business Street\",");
		sb.append("\"Business City\",");
		sb.append("\"Business State\",");
		sb.append("\"Business Postal Code\",");
		sb.append("\"Business Country\",");
		sb.append("\"Business Phone\",");
		sb.append("\"Business Fax\",");
		sb.append("\"Business Phone 2\",");

		sb.append("\"Birthday\",");
		sb.append("\"Web Page\"");

		sb.append("\n");

		DateFormat df = new SimpleDateFormat("mm/dd/yyyy");

		for (int i = 0; contacts != null && i < contacts.length; i++) {
			ABContact c = contacts[i];

			sb.append(getValue(c.getFirstName()));
			sb.append(getValue(c.getLastName()));
			sb.append(getValue(c.getEmailAddress()));

			sb.append(getValue(c.getHomeStreet()));
			sb.append(getValue(c.getHomeCity()));
			sb.append(getValue(c.getHomeState()));
			sb.append(getValue(c.getHomeZip()));
			sb.append(getValue(c.getHomeCountry()));
			sb.append(getValue(c.getHomePhone()));
			sb.append(getValue(c.getHomeFax()));
			sb.append(getValue(c.getHomeCell()));

			sb.append(getValue(c.getBusinessCompany()));
			sb.append(getValue(c.getBusinessStreet()));
			sb.append(getValue(c.getBusinessCity()));
			sb.append(getValue(c.getBusinessState()));
			sb.append(getValue(c.getBusinessZip()));
			sb.append(getValue(c.getBusinessCountry()));
			sb.append(getValue(c.getBusinessPhone()));
			sb.append(getValue(c.getBusinessFax()));
			sb.append(getValue(c.getBusinessCell()));

			sb.append(getValue(c.getBirthday(), df));
			sb.append(getValue(c.getWebsite()));

			// Get rid of extra ,

			sb.delete(sb.length() - 1, sb.length());

			sb.append("\n");
		}

		_bytes = sb.toString().getBytes();
	}

	public byte[] getData() {
		return _bytes;
	}

	public String getFileName() {
		return FILE_NAME;
	}

	private byte[] _bytes;

}