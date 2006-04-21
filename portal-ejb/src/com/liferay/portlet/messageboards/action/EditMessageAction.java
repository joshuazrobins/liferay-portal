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

package com.liferay.portlet.messageboards.action;

import com.liferay.documentlibrary.FileNameException;
import com.liferay.documentlibrary.FileSizeException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.Constants;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.messageboards.MessageBodyException;
import com.liferay.portlet.messageboards.MessageSubjectException;
import com.liferay.portlet.messageboards.NoSuchMessageException;
import com.liferay.portlet.messageboards.NoSuchTopicException;
import com.liferay.portlet.messageboards.RequiredMessageException;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.spring.MBMessageServiceUtil;
import com.liferay.util.FileUtil;
import com.liferay.util.ObjectValuePair;
import com.liferay.util.ParamUtil;
import com.liferay.util.Validator;
import com.liferay.util.servlet.SessionErrors;
import com.liferay.util.servlet.UploadPortletRequest;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="EditMessageAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class EditMessageAction extends PortletAction {

	public void processAction(
			ActionMapping mapping, ActionForm form, PortletConfig config,
			ActionRequest req, ActionResponse res)
		throws Exception {

		String cmd = ParamUtil.getString(req, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateMessage(req, res);
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteMessage(req);
			}
			else if (cmd.equals(Constants.SUBSCRIBE)) {
				subscribeMessage(req);
			}
			else if (cmd.equals(Constants.UNSUBSCRIBE)) {
				unsubscribeMessage(req);
			}

			sendRedirect(req, res);
		}
		catch (Exception e) {
			if (e != null &&
				e instanceof NoSuchMessageException ||
				e instanceof NoSuchTopicException ||
				e instanceof PrincipalException ||
				e instanceof RequiredMessageException) {

				SessionErrors.add(req, e.getClass().getName());

				setForward(req, "portlet.message_boards.error");
			}
			else if (e != null &&
					 e instanceof FileNameException ||
					 e instanceof FileSizeException ||
					 e instanceof MessageBodyException ||
					 e instanceof MessageSubjectException) {

				SessionErrors.add(req, e.getClass().getName());
			}
			else {
				throw e;
			}
		}
	}

	public ActionForward render(
			ActionMapping mapping, ActionForm form, PortletConfig config,
			RenderRequest req, RenderResponse res)
		throws Exception {

		try {
			ActionUtil.getMessage(req);
		}
		catch (Exception e) {
			if (e != null &&
				e instanceof NoSuchMessageException ||
				e instanceof PrincipalException) {

				SessionErrors.add(req, e.getClass().getName());

				return mapping.findForward("portlet.message_boards.error");
			}
			else {
				throw e;
			}
		}

		return mapping.findForward(
			getForward(req, "portlet.message_boards.edit_message"));
	}

	protected void deleteMessage(ActionRequest req) throws Exception {
		String topicId = ParamUtil.getString(req, "topicId");
		String messageId = ParamUtil.getString(req, "messageId");

		MBMessageServiceUtil.deleteMessage(topicId, messageId);
	}

	protected void subscribeMessage(ActionRequest req) throws Exception {
		String topicId = ParamUtil.getString(req, "topicId");
		String messageId = ParamUtil.getString(req, "messageId");

		MBMessageServiceUtil.subscribeMessage(topicId, messageId);
	}

	protected void unsubscribeMessage(ActionRequest req) throws Exception {
		String topicId = ParamUtil.getString(req, "topicId");
		String messageId = ParamUtil.getString(req, "messageId");

		MBMessageServiceUtil.unsubscribeMessage(topicId, messageId);
	}

	protected void updateMessage(ActionRequest req, ActionResponse res)
		throws Exception {

		String topicId = ParamUtil.getString(req, "topicId");
		String messageId = ParamUtil.getString(req, "messageId");

		String threadId = ParamUtil.getString(req, "threadId");
		String parentMessageId = ParamUtil.getString(req, "parentMessageId");
		String subject = ParamUtil.getString(req, "subject");
		String body = ParamUtil.getString(req, "body");
		boolean attachments = ParamUtil.getBoolean(req, "attachments");

		List files = new ArrayList();

		if (attachments) {
			UploadPortletRequest uploadReq =
				PortalUtil.getUploadPortletRequest(req);

			for (int i = 1; i <= 5; i++) {
				File file = uploadReq.getFile("msgFile" + i);
				String fileName = uploadReq.getFileName("msgFile" + i);
				byte[] bytes = FileUtil.getBytes(file);

				if (bytes != null && bytes.length > 0) {
					ObjectValuePair ovp = new ObjectValuePair(fileName, bytes);

					files.add(ovp);
				}
			}
		}

		boolean anonymous = ParamUtil.getBoolean(req, "anonymous");

		boolean addCommunityPermissions = ParamUtil.getBoolean(
			req, "addCommunityPermissions");
		boolean addGuestPermissions = ParamUtil.getBoolean(
			req, "addGuestPermissions");

		MBMessage message = null;

		if (Validator.isNull(messageId)) {
			if (Validator.isNull(threadId)) {

				// Post new thread

				message = MBMessageServiceUtil.addMessage(
					topicId, subject, body, files, anonymous,
					req.getPreferences(), addCommunityPermissions,
					addGuestPermissions);
			}
			else {

				// Post reply

				message = MBMessageServiceUtil.addMessage(
					topicId, threadId, parentMessageId, subject, body, files,
					anonymous, req.getPreferences(), addCommunityPermissions,
					addGuestPermissions);
			}
		}
		else {

			// Update message

			message = MBMessageServiceUtil.updateMessage(
				topicId, messageId, subject, body, files, req.getPreferences());
		}

		String redirect =
			ParamUtil.getString(req, "messageRedirect") +
				message.getMessageId();

		res.sendRedirect(redirect);
	}

}