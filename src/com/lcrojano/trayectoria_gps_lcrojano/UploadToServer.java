package com.lcrojano.trayectoria_gps_lcrojano;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.trayectoria_gps_lcrojano.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class UploadToServer extends DialogFragment{
	 private View vi;
	 Toast toast;
	 @Override   
	 public void onCreate(Bundle savedInstanceState) {       
		 super.onCreate(savedInstanceState);    
		 setCancelable(false);
	 }
	
	 @Override  
	 public Dialog onCreateDialog(Bundle savedInstanceState) { 
		 vi = getActivity().getLayoutInflater().inflate(R.layout.fragment_upload, null); 
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 builder.setView(vi);
		 builder.setTitle("Cargar en Servidor");
		 builder.setPositiveButton(android.R.string.ok, 
				 new DialogInterface.OnClickListener() {
				 	@Override
		            public void onClick(DialogInterface dialog, int which)
		            {
		                //Do nothing here solo para compatibilidad
		            }
		         });
		    

		 return builder.create();
		
	}  
	 Bundle args;
	@Override
	 public void onStart()
	 {
		//Sobre escribimos el listener para que se realicen oopiones reuqeridas
	     super.onStart();    //super.onStart() is where dialog.show() is actually called 
	     AlertDialog d = (AlertDialog)getDialog();
	     toast = Toast.makeText(getActivity().getApplicationContext(),
	    		 "Datos agregados Correctamente", 
	    		 Toast.LENGTH_SHORT);
	     args = new Bundle();
	     args = getArguments();
	     if(d != null)
	     {
	         Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
	         positiveButton.setOnClickListener(
	        		 new View.OnClickListener() {
	                     @Override
	                     public void onClick(View v)
	                     {
	                    	 //Controlar si se debe salir del dialog
	                    	 if(!sendMail(vi)){    
	                    		 
	                    	 }else{
		                         toast.setText("Datos Ingresados Correctamente");
		                       	 toast.show();
		                       	 dismiss();
		                       	 //recargar layout menuprincipal
		                       
		                       	//((MainActivity)getActivity()).reloadFragment("menu_principal");
	                        }
	                     }
	                 });
	     }
	 }
	String upLoadServerUri = null;
	int serverResponseCode = 0;
	final String uploadFilePath = "/mnt/sdcard/";
	final String uploadFileName = "registros_de_rutas.txt";
		private boolean sendMail(View v){
			 //La primera vez que el usuario ingresa, se debe verificar, que el promedio deseado
			//sea alcanzable
			//Estudiante
			 boolean go = true;

		   	 EditText edit = (EditText)v.findViewById(R.id.editTextUrl);
		   	 String nombre = edit.getText().toString();
		   	 if(nombre == ""){
		   		 edit.setError("Olvidaste la url");
		   		 go =false;
		   	 }

		   	 
		   	 if(go){
			   	//enviar
		   		 
		   		
		  
		   		
		   		 
				}else{
					go = false;
					toast.setText("Error al Ingresar Datos");
                  	toast.show();
                  	edit.setError(null);
				}
			
			return go;
		}
	   
}
