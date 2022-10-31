package com.shoppeo.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shoppeo.common.entity.User;

public class UserExcelExporter extends AbstractExporter {
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setReponseHeader(response, "application/octet-stream", ".xlsx");
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("회원");
		
		ServletOutputStream outputStream = response.getOutputStream();
	}
}
