package com.mcgill.locationfinder.measurement;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

public class MeasureController {
	
	private static MeasureController controller = new MeasureController();
	private ArrayList<MeasurementListener> listeners;
	private String key;
	private MeasureMethod method;
	private HashMap<String,MeasureMethod> methods;
	
	private MeasureController(){
		key=null;
		method = null;
		listeners=new ArrayList<MeasurementListener>();
		methods=new HashMap<String,MeasureMethod>();
	}
	
	public static MeasureController getInstance(){
		return controller;
	}
	
	public void setMeasurementMethod(String key) throws MeasurementException{
		MeasureMethod m = methods.get(key);
		if(m==null)
			throw new MeasurementException(MeasurementException.NO_SUCH_METHOD,"No such method, please register a new method!");
		this.key=key;
		this.method=m;
	}
	
	public void startToWait(Context context) throws MeasurementException{
		if(method==null){
			throw new MeasurementException(MeasurementException.METHOD_NOT_SET, "Method is not set!");
		}
		method.setContext(context);
		method.waitForConnection();
	}
	
	public void measure() throws MeasurementException{
		if(method==null){
			throw new MeasurementException(MeasurementException.METHOD_NOT_SET, "Method is not set!");
		}
		new Thread(method).start();
	}
	
	public void connect(Context context, String address)throws MeasurementException{
		if(method==null){
			throw new MeasurementException(MeasurementException.METHOD_NOT_SET, "Method is not set!");
		}
		method.setServerAddress(address);
		method.setContext(context);
		method.connect();
	}
	
	public void addMeasurementListener(MeasurementListener listener){
		listeners.add(listener);
		for(String key:methods.keySet()){
			methods.get(key).addMeasurementListener(listener);
		}
	}
	
	public void registerMethod(String key, MeasureMethod method){
		methods.put(key, method);
	}
	
	public String getMethodKey(){
		return key;
	}
	
	public void clear(){
		listeners.clear();
		method.clear();
	}
	
	public MeasureMethod getMethod(){
		return method;
	}

}
