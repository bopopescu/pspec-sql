package edu.thu.ss.xml.parser;

public class ParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParsingException(String msg) {
		super(msg);
	}

	public ParsingException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
