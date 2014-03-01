package com.mcgill.locationfinder.measurement;

public interface MeasurementListener {
	
	void distanceMeasuredSucced(double distance);
	
	void failedToMeasure(int failedCode, String reason);
	
}
