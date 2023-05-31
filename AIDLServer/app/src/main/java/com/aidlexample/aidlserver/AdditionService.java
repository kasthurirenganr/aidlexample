package com.aidlexample.aidlserver;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;

public class AdditionService extends Service {

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		Log.i( "serviceonelog", "onBind" );
		return mBinder;
	}

	private final IAdd.Stub mBinder = new IAdd.Stub() {
		@Override
		public int sendString( String data ) throws RemoteException {
			Log.i( "serviceonelog", "Recieved in Server " +  data );
			return 10;
		}
	};
}