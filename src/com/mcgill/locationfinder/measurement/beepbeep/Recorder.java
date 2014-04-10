package com.mcgill.locationfinder.measurement.beepbeep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class Recorder {
	
	private final String FileName=Environment.getExternalStorageDirectory().getPath()+"/beepbeep_recorded.wav";
	
	private String LOG_TAG = "Beepbeep.Recorder";
	
	private MediaRecorder mRecorder = null;
	
	private int audioSource = MediaRecorder.AudioSource.MIC;
	private static int sampleRateInHz = 44100; 
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	private static int channelConfig = AudioFormat.CHANNEL_IN_MONO; 
	private int bufferSizeInBytes = 0;
	private AudioRecord audioRecord; 
	private boolean isRecord = false;
	private Context context;
	
	public Recorder(Context context){
		this.context=context;
	}
	
	public void startRecording() {
		if(!isRecord){
			bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat); 
			audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
			audioRecord.startRecording(); 
			isRecord = true;
			Toast.makeText(context, "Start Recording", Toast.LENGTH_SHORT).show();
			Thread writerThread=new Thread(new RecorderFileWriter());
			writerThread.start();
		}
    }
	
	public void stopToRecord(){
		if(isRecord){
			isRecord=false;
			audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
		}
	}
	
	public class RecorderFileWriter implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			writeToFile();
			//playSound();
		}
		
		private void writeToFile(){
			byte[] audiodata = new byte[bufferSizeInBytes]; 
	        FileOutputStream fos = null; 
	        int readsize = 0;  
	        try{
	        	File file=new File(FileName);
	        	if (file.exists()) { 
	        		file.delete(); 
	            } 
	        	fos = new FileOutputStream(file);
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        }
	        
	        while(isRecord){
	        	readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes); 
	        	if (AudioRecord.ERROR_INVALID_OPERATION != readsize) { 
	        		try{ 
	        			fos.write(audiodata); 
	        		}catch (IOException e) { 
	        			e.printStackTrace(); 
	                } 
	             }
	        }
		}
		
		

	}
	
	public void playSound(){
		byte[] fileBuffer=new byte[2000000];
		FileInputStream fis=null;
		int readsize=0;
		int readin=0;
    	try {
    		File file=new File(FileName);
			fis = new FileInputStream(file);
			while((readin=fis.read(fileBuffer, readsize, 1000000-readsize))!=-1){
				readsize=readsize+readin;
			}
			final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
					sampleRateInHz, AudioFormat.CHANNEL_CONFIGURATION_MONO,
	                AudioFormat.ENCODING_PCM_16BIT, readsize,
	                AudioTrack.MODE_STATIC);
	        audioTrack.write(fileBuffer, 0, readsize);
	        audioTrack.play();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
