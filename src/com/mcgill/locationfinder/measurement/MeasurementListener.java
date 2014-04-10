package com.mcgill.locationfinder.measurement;

public interface MeasurementListener {
	
	public void connected(Object obj);
	
	void distanceMeasuredSucced(double distance);
	
	void failedToMeasure(int failedCode, String reason);
	
}
