package com.shoppeo.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.shoppeo.common.entity.User;

public class AbstractExporter {
	public void setReponseHeader(HttpServletResponse response, String contentType, String extension) throws IOException {
		DateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dataFormatter.format(new Date());
		String fileName = "users_" + timestamp + extension;
		
		response.setContentType(contentType);
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename =" + fileName;
		response.setHeader(headerKey, headerValue);
		response.setCharacterEncoding("UTF-8");
	}
}
