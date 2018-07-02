package com.ninelives.insurance.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	public boolean validate(final String hex) {
		matcher = pattern.matcher(hex);
		return matcher.matches();

	}
	
	public boolean validateLetters(String txt) {

	    String regx = "^[\\p{L} .'-]+$";
	    Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(txt);
	    return matcher.find();

	}
	
	
}