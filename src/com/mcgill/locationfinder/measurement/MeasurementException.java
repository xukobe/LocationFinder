package com.mcgill.locationfinder.measurement;

public class MeasurementException extends Exception {
	
	public static final int BLUETOOTH_NOT_AVAILABLE=0;
	public static final int METHOD_NOT_SET=1;
	public static final int NO_SUCH_METHOD=2;
	public static final int BLUETOOTH_SOCKET_DOWN=3;
	public static final int ADDRESS_NULL = 4;
	public static final int NO_CONTEXT = 5;
	
	private int failCode;
	
	public MeasurementException(int code, String message){
		super(message);
		this.setFailCode(code);
	}

	public int getFailCode() {
		return failCode;
	}

	public void setFailCode(int failCode) {
		this.failCode = failCode;
	}
	
	
}
