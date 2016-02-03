
package ca.concordia.encs.diameter.exception;

public class IllegalAVPDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -644081660829624583L;

	/**
	 * 
	 */
	public IllegalAVPDataException() {
	}

	/**
	 * @param arg0
	 */
	public IllegalAVPDataException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public IllegalAVPDataException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public IllegalAVPDataException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
