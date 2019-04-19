package com.ninelives.insurance.core.support.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDVariableText;

public class PdfCreator {
	private static final String DEFAULT_APPREANCE = "0.18039 0.20392 0.21176 rg /F8 7.994 Tf";
	
	String templateFilePath;
	String fontFilePath;
	String templateFontDefaultAppearance = DEFAULT_APPREANCE;
	
	public PdfCreator(String templateFilePath, String fontFilePath){
		this(templateFilePath, fontFilePath, null);
	}
	
	public PdfCreator(String templateFilePath, String fontFilePath, String templateFontDefaultAppearance){
		this.templateFilePath=templateFilePath;
		this.fontFilePath=fontFilePath;
		if(templateFontDefaultAppearance!=null){
			this.templateFontDefaultAppearance=templateFontDefaultAppearance;
		}
		
	}
	
	public void printFieldMap(Map<String, String> fieldMap, String outputFilePath) throws IOException{
		PDDocument pdf= PDDocument.load(new File(templateFilePath));
		
		PDDocumentCatalog docCatalog = pdf.getDocumentCatalog();			
		PDAcroForm acroForm = docCatalog.getAcroForm();
		PDResources res = acroForm.getDefaultResources();
		if(res==null) {
			res = new PDResources();
		}

		//Set font
		PDFont formFont = PDType0Font.load(pdf, new FileInputStream(fontFilePath), false);
		res.add(formFont);
		
		for(PDField field: acroForm.getFields()) {
			PDVariableText  textField = (PDVariableText) field;
			textField.setDefaultAppearance(templateFontDefaultAppearance);
		}
		
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
        	acroForm.getField(entry.getKey()).setValue(entry.getValue());
        }
		acroForm.flatten(acroForm.getFields(),true);			
		pdf.save(outputFilePath);
		pdf.close();		
	}
}
