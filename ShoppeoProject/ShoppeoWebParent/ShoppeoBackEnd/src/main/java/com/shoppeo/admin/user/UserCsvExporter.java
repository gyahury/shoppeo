package com.shoppeo.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shoppeo.common.entity.User;

public class UserCsvExporter {
	
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		DateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dataFormatter.format(new Date());
		String fileName = "users_" + timestamp + ".csv";
		
		response.setContentType("text/csv");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename =" + fileName;
		response.setHeader(headerKey, headerValue);
		response.setCharacterEncoding("UTF-8");
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"id","이메일","이름","권한","상태"};
		String[] fieldMapping = {"id","email","name","role","enabled"};
		
		csvWriter.writeHeader(csvHeader);
		
		for(User user : listUsers) {
			csvWriter.write(user, fieldMapping);
		}
		
		
		csvWriter.close();
	}
}
