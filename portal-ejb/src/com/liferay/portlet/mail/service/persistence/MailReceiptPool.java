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

package com.liferay.portlet.mail.service.persistence;

import com.liferay.portal.util.ClusterPool;

import com.liferay.portlet.mail.model.MailReceipt;

import com.liferay.util.StringPool;
import com.liferay.util.Validator;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="MailReceiptPool.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class MailReceiptPool {
	public static final String GROUP_NAME = MailReceiptPool.class.getName();
	public static final String[] GROUP_NAME_ARRAY = new String[] { GROUP_NAME };

	public static void clear() {
		_log.debug("Clear");
		_instance._clear();
	}

	public static MailReceipt get(String receiptId) {
		MailReceipt mailReceipt = _instance._get(receiptId);
		_log.info("Get " + receiptId + " is " +
			((mailReceipt == null) ? "NOT " : "") + "in cache");

		return mailReceipt;
	}

	public static MailReceipt put(String receiptId, MailReceipt obj) {
		_log.info("Put " + receiptId);

		return _instance._put(receiptId, obj, false);
	}

	public static MailReceipt remove(String receiptId) {
		_log.info("Remove " + receiptId);

		return _instance._remove(receiptId);
	}

	public static MailReceipt update(String receiptId, MailReceipt obj) {
		_log.info("Update " + receiptId);

		return _instance._put(receiptId, obj, true);
	}

	private MailReceiptPool() {
		_cacheable = MailReceipt.CACHEABLE;
		_cache = ClusterPool.getCache();
		ClusterPool.registerPool(MailReceiptPool.class.getName());
	}

	private void _clear() {
		_cache.flushGroup(GROUP_NAME);
	}

	private MailReceipt _get(String receiptId) {
		if (!_cacheable) {
			return null;
		}
		else if (receiptId == null) {
			return null;
		}
		else {
			MailReceipt obj = null;
			String key = _encodeKey(receiptId);

			if (Validator.isNull(key)) {
				return null;
			}

			try {
				obj = (MailReceipt)_cache.getFromCache(key);
			}
			catch (NeedsRefreshException nfe) {
			}
			finally {
				if (obj == null) {
					_cache.cancelUpdate(key);
				}
			}

			return obj;
		}
	}

	private String _encodeKey(String receiptId) {
		String receiptIdString = String.valueOf(receiptId);

		if (Validator.isNull(receiptIdString)) {
			_log.debug("Key is null");

			return null;
		}
		else {
			String key = GROUP_NAME + StringPool.POUND + receiptIdString;
			_log.debug("Key " + key);

			return key;
		}
	}

	private MailReceipt _put(String receiptId, MailReceipt obj, boolean flush) {
		if (!_cacheable) {
			return obj;
		}
		else if (receiptId == null) {
			return obj;
		}
		else {
			String key = _encodeKey(receiptId);

			if (Validator.isNotNull(key)) {
				if (flush) {
					_cache.flushEntry(key);
				}

				_cache.putInCache(key, obj, GROUP_NAME_ARRAY);
			}

			return obj;
		}
	}

	private MailReceipt _remove(String receiptId) {
		if (!_cacheable) {
			return null;
		}
		else if (receiptId == null) {
			return null;
		}
		else {
			MailReceipt obj = null;
			String key = _encodeKey(receiptId);

			if (Validator.isNull(key)) {
				return null;
			}

			try {
				obj = (MailReceipt)_cache.getFromCache(key);
				_cache.flushEntry(key);
			}
			catch (NeedsRefreshException nfe) {
			}
			finally {
				if (obj == null) {
					_cache.cancelUpdate(key);
				}
			}

			return obj;
		}
	}

	private static Log _log = LogFactory.getLog(MailReceiptPool.class);
	private static MailReceiptPool _instance = new MailReceiptPool();
	private GeneralCacheAdministrator _cache;
	private boolean _cacheable;
}