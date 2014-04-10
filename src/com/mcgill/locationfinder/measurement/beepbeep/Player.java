package com.mcgill.locationfinder.measurement.beepbeep;

import java.io.IOException;

import com.mcgill.locationfinder.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.util.Log;

public class Player {
	
	private MediaPlayer mPlayer = null;
	
	private String LOG_TAG = "Beepbeep.Player";
	
	private Context context;
	
	public Player(Context context){
		this.context = context;
	}
	
	public void startPlaying(){
        mPlayer = MediaPlayer.create(context, R.raw.beep);
		mPlayer.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.stop();
				mp.release();
				mPlayer = null;
			}
			
		});
		mPlayer.start();
	}
	
	public void stopPlaying(){
		if(mPlayer!=null){
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}
	
}
