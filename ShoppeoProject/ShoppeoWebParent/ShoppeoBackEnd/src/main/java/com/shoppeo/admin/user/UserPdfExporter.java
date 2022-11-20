package com.shoppeo.admin.user;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shoppeo.common.entity.User;

public class UserPdfExporter extends AbstractExporter {

	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setReponseHeader(response, "application/pdf", ".pdf");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		String fontPath = "/static/webfonts/gullim.ttf";
		BaseFont objBastFont = BaseFont.createFont(fontPath,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font font = new Font(objBastFont, 15);
		Paragraph paragraph = new Paragraph("회원 목록", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.2f, 3.5f, 3.0f, 3.0f, 1.7f});
		
		writeTableHeader(table, font);
		writeTableData(table, listUsers);
		
		document.add(table);
		
		document.close();
		
	}

	private void writeTableData(PdfPTable table, List<User> listUsers) {
		for(User user : listUsers) {
			table.addCell(String.valueOf(user.getId()));
			table.addCell(String.valueOf(user.getEmail()));
			table.addCell(String.valueOf(user.getName()));
			table.addCell(String.valueOf(user.getRole().toString()));
			table.addCell(String.valueOf(user.isEnabled()));
		}
		
	}

	private void writeTableHeader(PdfPTable table, Font font) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.GRAY);
		cell.setPadding(5);
		
		cell.setPhrase(new Phrase("id",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("email",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("role",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Enabled",font));
		table.addCell(cell);
		
	}

}
