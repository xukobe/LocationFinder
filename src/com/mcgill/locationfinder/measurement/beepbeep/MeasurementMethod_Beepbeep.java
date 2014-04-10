package com.mcgill.locationfinder.measurement.beepbeep;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.mcgill.locationfinder.measurement.MeasureMethod;
import com.mcgill.locationfinder.measurement.MeasurementException;

public class MeasurementMethod_Beepbeep extends MeasureMethod{

	private Player player;
	private Recorder recorder;
	
	private Timer timer = null;
	private final int DURATION = 10000;
	
	public MeasurementMethod_Beepbeep(Context context){
		player = new Player(context);
		recorder = new Recorder(context);
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitForConnection() {
		// TODO Auto-generated method stub
		recorder.startRecording();
		timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				recorder.stopToRecord();
			}
			
		}, DURATION);
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		player.startPlaying();
	}

	@Override
	public void measure() throws MeasurementException {
		// TODO Auto-generated method stub
		//for test
		recorder.playSound();
	}

	@Override
	public double calculateDis(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
