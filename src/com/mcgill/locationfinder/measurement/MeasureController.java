package com.mcgill.locationfinder.measurement;

import java.util.ArrayList;

public class MeasureController {
	
	private MeasureController controller = new MeasureController();
	private ArrayList<MeasurementListener> listeners;
	private MeasureMethod method;
	
	private MeasureController(){
		method = null;
		listeners=new ArrayList<MeasurementListener>();
	}
	
	public MeasureController getInstance(){
		return controller;
	}
	
	public void setMeasurementMethod(MeasureMethod method){
		this.method=method;
	}
	
	public void measure() throws MeasurementException{
		if(method==null){
			throw new MeasurementException(MeasurementException.METHOD_NOT_SET, "Method is not set!");
		}
		method.setMeasurementListener(listeners);
		new Thread(method).start();
	}
	
	public void addMeasurementListener(MeasurementListener listener){
		listeners.add(listener);
	}
	
	
}
