package com.lcrojano.trayectoria_gps_lcrojano;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
	                    	 if(!isUploaded(vi)){    
	                    		 
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
		private boolean isUploaded(View v){
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
		   		EditText server = (EditText)v.findViewById(R.id.editTextDestino);
			    String destino = edit.getText().toString();
		   	  /************* Php script path ****************/
		         upLoadServerUri  = args.getString(destino);

		        		 
		        		
		         new Thread(new Runnable() {
                     public void run() {
                    	 getActivity().runOnUiThread(new Runnable() {
                                 public void run() {
                                    // messageText.setText("uploading started.....");
                                 }
                             });                      
                        
                          uploadFile(uploadFilePath + "" + uploadFileName);
                                                   
                     }
                   }).start();        
		  
		   		
		   		 
				}else{
					go = false;
					toast.setText("Error al Ingresar Datos");
                  	toast.show();
                  	edit.setError(null);
				}
			
			return go;
		}
	    public int uploadFile(String sourceFileUri) {
	           
	           
	          String fileName = sourceFileUri;
	  
	          HttpURLConnection conn = null;
	          DataOutputStream dos = null;  
	          String lineEnd = "\r\n";
	          String twoHyphens = "--";
	          String boundary = "*****";
	          int bytesRead, bytesAvailable, bufferSize;
	          byte[] buffer;
	          int maxBufferSize = 1 * 1024 * 1024; 
	          File sourceFile = new File(sourceFileUri); 
	           
	          if (!sourceFile.isFile()) {
	               
	               //dialog.dismiss(); 
	                
	              // Log.e("uploadFile", "Source File not exist :"
	                //                   +uploadFilePath + "" + uploadFileName);
	                
	               getActivity().runOnUiThread(new Runnable() {
	                   public void run() {
	                       //messageText.setText("Source File not exist :"
	                         //      +uploadFilePath + "" + uploadFileName);
	                   }
	               }); 
	                
	               return 0;
	            
	          }
	          else
	          {
	               try { 
	                    
	                     // open a URL connection to the Servlet
	                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
	                   URL url = new URL(upLoadServerUri);
	                    
	                   // Open a HTTP  connection to  the URL
	                   conn = (HttpURLConnection) url.openConnection(); 
	                   conn.setDoInput(true); // Allow Inputs
	                   conn.setDoOutput(true); // Allow Outputs
	                   conn.setUseCaches(false); // Don't use a Cached Copy
	                   conn.setRequestMethod("POST");
	                   conn.setRequestProperty("Connection", "Keep-Alive");
	                   conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	                   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	                   conn.setRequestProperty("uploaded_file", fileName); 
	                    
	                   dos = new DataOutputStream(conn.getOutputStream());
	          
	                   dos.writeBytes(twoHyphens + boundary + lineEnd); 
	                   dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename="+ sourceFileUri + lineEnd);
	                    
	                   dos.writeBytes(lineEnd);
	          
	                   // create a buffer of  maximum size
	                   bytesAvailable = fileInputStream.available(); 
	          
	                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                   buffer = new byte[bufferSize];
	          
	                   // read file and write it into form...
	                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
	                      
	                   while (bytesRead > 0) {
	                        
	                     dos.write(buffer, 0, bufferSize);
	                     bytesAvailable = fileInputStream.available();
	                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
	                      
	                    }
	          
	                   // send multipart form data necesssary after file data...
	                   dos.writeBytes(lineEnd);
	                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	          
	                   // Responses from the server (code and message)
	                   serverResponseCode = conn.getResponseCode();
	                   String serverResponseMessage = conn.getResponseMessage();
	                     
	                  // Log.i("uploadFile", "HTTP Response is : "
	                    //       + serverResponseMessage + ": " + serverResponseCode);
	                    
	                   if(serverResponseCode == 200){
	                        
	                       getActivity().runOnUiThread(new Runnable() {
	                            public void run() {
	                                 
	                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
	                                              +" http://www.androidexample.com/media/uploads/"
	                                              +uploadFileName;
	                                 
	                                //messageText.setText(msg);
	                             toast.setText("File Upload Complete.");
	                                             toast.show();
	                            }
	                        });                
	                   }    
	                    
	                   //close the streams //
	                   fileInputStream.close();
	                   dos.flush();
	                   dos.close();
	                     
	              } catch (MalformedURLException ex) {
	                   
	                 // dialog.dismiss();  
	                  ex.printStackTrace();
	                   
	                  getActivity().runOnUiThread(new Runnable() {
	                      public void run() {
	                         // messageText.setText("MalformedURLException Exception : check script url.");
	                          toast.setText("MalformedURLException");
	                          toast.show();
	                      }
	                  });
	                   
	                 // Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
	              } catch (Exception e) {
	                   
	                 dismiss();  
	                  e.printStackTrace();
	                   
	                  getActivity().runOnUiThread(new Runnable() {
	                      public void run() {
	                          //messageText.setText("Got Exception : see logcat ");
	                          toast.setText("Got Exception : see logcat ");
	                          toast.show();
	                      }
	                  });
	                 // Log.e("Upload file to server Exception", "Exception : "
	                         //                          + e.getMessage(), e);  
	              }
	             dismiss();       
	              return serverResponseCode; 
	               
	           } // End else block 
	         } 
	   
}
