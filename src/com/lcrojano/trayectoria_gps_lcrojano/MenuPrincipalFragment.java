package com.lcrojano.trayectoria_gps_lcrojano;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.example.trayectoria_gps_lcrojano.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



public class MenuPrincipalFragment extends Fragment implements LocationListener {
	private static final String DIALOG_FIRST = "FIRST";
	private String TAG = MenuPrincipalFragment.class.getSimpleName();

	
	public MenuPrincipalFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
		

	 Toast toast;
	
	 boolean mStarted = false;
	private LocationManager mLocationManager;
	private TextView textViewLatitude;
	private TextView textViewLongitude;
	private TextView textViewAltitude;
	private TextView textViewSpeed;
	private MenuPrincipalFragment listener = this;
	private String posicion = "";
	private EditText editNombre;
	private EditText editLugar;
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_menu_principal, container, false);
		//toast = Toast.makeText(getActivity().getApplicationContext(),"", Toast.LENGTH_SHORT);
		mStarted = false;
		editNombre = (EditText) rootView.findViewById(R.id.editTextNombre);
		editLugar = (EditText) rootView.findViewById(R.id.editTextLugar);
		
		textViewAltitude = (TextView) rootView.findViewById(R.id.textViewAltitud);
		textViewSpeed = (TextView) rootView.findViewById(R.id.textViewVelocidad);
		textViewLongitude = (TextView) rootView.findViewById(R.id.textViewLongitud);
		textViewLatitude = (TextView) rootView.findViewById(R.id.textViewLatitud);

		
		ImageButton button = (ImageButton) rootView.findViewById(R.id.imageButtonIniciar);
		button.setOnClickListener(new OnClickListener() {
		

			@Override
			public void onClick(View v) {	
				//Abrir fragment semestre, y cargar los datos requeridos
				if (mStarted == false) {
					File file = new File("sdcard/" + "registros_de_rutas.txt");
					boolean deleted = file.delete();
					starCapturing();
				} else {
					Toast.makeText(getActivity(), "Stoped",
							Toast.LENGTH_LONG).show();
					stopCapturing();
					mStarted = !mStarted;
					
				}

			}
		});
		ImageButton button2 = (ImageButton) rootView.findViewById(R.id.imageButtonParar);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopCapturing();

				Toast.makeText(getActivity(), "Stoped",
						Toast.LENGTH_LONG).show();
				stopCapturing();
				mStarted = false;
			}
		});
		ImageButton button3 = (ImageButton) rootView.findViewById(R.id.imageButtonCargar);
		button3.setOnClickListener(new OnClickListener() {
			Bundle args;
			@Override
			public void onClick(View v) {
				if(posicion != ""){
					  //Dialog Fragment si es primera vez en la aplicacion
			    	FragmentManager fm = getActivity().getSupportFragmentManager(); 
			    	//toast = Toast.makeText(getActivity().getApplicationContext(),"Bienvenido!", Toast.LENGTH_SHORT);
			    	UploadToServer nAsig = new UploadToServer();   
			    	args = new Bundle();
			    	args.putString("msg", posicion);
					nAsig.setArguments(args);
			    	nAsig.show(fm, "upload"); 
				}else{
					Toast.makeText(getActivity(), "No hay datos aun intente mas tarde",
							Toast.LENGTH_LONG).show();
					
				}
			
	
				
			}
		});
		
		ImageButton button4 = (ImageButton) rootView.findViewById(R.id.imageButtonEnviar);
		button4.setOnClickListener(new OnClickListener() {
	    Bundle args;

			@Override
			public void onClick(View v) {
				
	if(posicion != ""){
		  //Dialog Fragment si es primera vez en la aplicacion
    	FragmentManager fm = getActivity().getSupportFragmentManager(); 
    	//toast = Toast.makeText(getActivity().getApplicationContext(),"Bienvenido!", Toast.LENGTH_SHORT);
    	SendMailFragmentDialog nAsig = new SendMailFragmentDialog();   
    	args = new Bundle();
    	args.putString("msg", posicion);
		nAsig.setArguments(args);
    	nAsig.show(fm, "sendMail"); 
	}else{
		Toast.makeText(getActivity(), "No hay datos aun intente mas tarde",
				Toast.LENGTH_LONG).show();
		
	}
				
			}
		});

		return rootView;
	}

	public void starCapturing() {
		boolean gps_enabled = true, network_enabled = true;
		mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

		try {
			gps_enabled = mLocationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {

		}
		try {
			network_enabled = mLocationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {

		}
		Log.d(TAG, "gps_enabled " + gps_enabled + " network_enabled "
				+ network_enabled);
		if (!gps_enabled || !network_enabled) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					getActivity());
			dialog.setMessage("GPS no esta habilitado!").setPositiveButton(
					"OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(
								DialogInterface paramDialogInterface,
								int paramInt) {
							// TODO Auto-generated method stub
							// Intent myIntent = new Intent(
							// Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							// getActivity().startActivity(myIntent);
							// // get gps
						}
					});
			dialog.show();

		} else {
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, listener);
			Toast.makeText(getActivity(), "Started", Toast.LENGTH_LONG)
					.show();
			mStarted = !mStarted;
			//mButton.setText("STOP");
		}
	}

	public void stopCapturing() {
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(this);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		double mLat = location.getLatitude();
		double mLong = location.getLongitude();
		double mAlt = location.getAltitude();
		double mSpeed = location.getSpeed();
		
		textViewLatitude.setText(String.valueOf(mLat));
		textViewLongitude.setText(String.valueOf(mLong));
		textViewAltitude.setText(String.valueOf(mAlt));
		textViewSpeed.setText(String.valueOf(mSpeed));
		String nombre = editNombre.getText().toString();
		String lugar = editLugar.getText().toString();
		
		posicion = posicion +
			    mLong + 
				"," + mLat + 
				"," + mAlt + 
				"," + mSpeed +
				"," + nombre + 
				"," + lugar + "\n";
		
		String pos2 = 
			    mLong + 
				"," + mLat + 
				"," + mAlt + 
				"," + mSpeed +
				"," + nombre + 
				"," + lugar;
		addTextToFile(pos2+"\n");
		//txt
		
				
				
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG,"onStop");
		stopCapturing();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
		if (mStarted){
			starCapturing();
		} else {
			
		}
	}
	public void addTextToFile(String text) {
        File logFile = new File("sdcard/" + "registros_de_rutas.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
                try {
                    BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                    buf.append("Longitud,Latitud,Altitud,Velocidad,Nombre,Lugar\n");
                    buf.newLine();
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}
}
