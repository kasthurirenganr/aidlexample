package com.aidlexample.aidlclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aidlexample.aidlserver.IAdd;
import com.aidlexample.aidlclient.R;
import com.aidlexample.aidlserver.Person;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private EditText num1, num2;
	private Button btnAdd, btnNonPremitive, btnCall;
	private TextView total;
	protected IAdd addService;
	private String Tag = "Client Application";
	private String serverAppUri = "com.aidlexample.aidlserver";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		num1 = (EditText) findViewById(R.id.num1);

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);

		total = (TextView) findViewById(R.id.total);

		initConnection();
	}

	private void initConnection() {
		if (addService == null) {
			Intent intent = new Intent(IAdd.class.getName());

			/*this is service name which has been declared in the server's manifest file in service's intent-filter*/
			intent.setAction("service.calc");

			/*From 5.0 annonymous intent calls are suspended so replacing with server app's package name*/
			intent.setPackage("com.aidlexample.aidlserver");

			// binding to remote service
			bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
		}
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			Log.d(Tag, "Service Connected");
			addService = IAdd.Stub.asInterface((IBinder) iBinder);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			Log.d(Tag, "Service Disconnected");
			addService = null;
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(serviceConnection);
	}

	@Override
	public void onClick(View view) {
		if (appInstalledOrNot(serverAppUri)) {
			switch (view.getId()) {

				case R.id.btnAdd:

					if ( num1.length() > 0 && addService != null) {
						try {
							total.setText("");
							total.setText("Result: " + addService.sendString( "sending log " + num1.getText().toString() ));
						} catch (RemoteException e) {
							e.printStackTrace();
							Log.d(Tag, "Connection cannot be establish");
						}
					}

					break;

			}
		} else {
			Toast.makeText(MainActivity.this, "Server App not installed", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (addService == null) {
			initConnection();
		}
	}
}