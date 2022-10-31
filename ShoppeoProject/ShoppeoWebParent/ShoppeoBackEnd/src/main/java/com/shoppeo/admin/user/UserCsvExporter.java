package com.shoppeo.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shoppeo.common.entity.User;

public class UserCsvExporter extends AbstractExporter {
	
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setReponseHeader(response, "text/csv", ".csv");
		
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
