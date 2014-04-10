package com.mcgill.locationfinder.communication_wifi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class CommunicationService extends Service{

	private final ServiceBinder binder = new ServiceBinder();
	//private 
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}
	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	}



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}



	public class ServiceBinder extends Binder
	{
		public CommunicationService getService()
		{
			return CommunicationService.this;
		}
	}

}
