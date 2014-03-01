package com.mcgill.locationfinder.measurement;

public class MeasurementException extends Exception {
	
	public static int BLUETOOTH_NOT_AVAILABLE=0;
	public static int METHOD_NOT_SET=1;
	
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
