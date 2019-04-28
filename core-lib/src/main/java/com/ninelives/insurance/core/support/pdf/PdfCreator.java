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
	
	String templateFilePath;
	String templateFontFilePath;
	String templateFonttAppearance;
		
	public PdfCreator(String templateFilePath, String templateFontFilePath, String templateFonttAppearance){
		this.templateFilePath=templateFilePath;
		this.templateFontFilePath=templateFontFilePath;
		this.templateFonttAppearance=templateFonttAppearance;
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
		PDFont formFont = PDType0Font.load(pdf, new FileInputStream(templateFontFilePath), false);
		res.add(formFont);
		
		for(PDField field: acroForm.getFields()) {
			PDVariableText  textField = (PDVariableText) field;
			textField.setDefaultAppearance(templateFonttAppearance);
		}
		
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
        	acroForm.getField(entry.getKey()).setValue(entry.getValue());
        }
		acroForm.flatten(acroForm.getFields(),true);			
		pdf.save(outputFilePath);
		pdf.close();		
	}
}
