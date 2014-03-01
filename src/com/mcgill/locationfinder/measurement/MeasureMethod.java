package com.mcgill.locationfinder.measurement;

import java.util.ArrayList;

public abstract class MeasureMethod implements Runnable {
	
	
	protected double distance;
	protected ArrayList<MeasurementListener> listeners;
	protected String name;
	
	public abstract void initialize();
	
	public abstract double measure() throws MeasurementException;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			initialize();
			double distance = measure();
			for(MeasurementListener l:listeners){
				l.distanceMeasuredSucced(distance);
			}
		}
		catch(MeasurementException e){
			for(MeasurementListener l:listeners){
				l.failedToMeasure(e.getFailCode(), e.getMessage());
			}
		}
	}
	
	//can be only accessed in the package
	void setMeasurementListener(ArrayList<MeasurementListener> listeners){
		this.listeners=listeners;
	}

}
