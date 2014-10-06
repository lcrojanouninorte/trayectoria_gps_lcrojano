package com.lcrojano.trayectoria_gps_lcrojano;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MenuPrincipal {
	private static final String DIALOG_FIRST = "FIRST";
	private String TAG = MenuPrincipalFragment.class.getSimpleName();
	private SemestreFragment semesterFragment;
	private SimuladorSemestralFragment simulador ;
	private AsignaturasListFragment asignaturas ;
	
	public MenuPrincipalFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
		
	private DatabaseHelper mHelper;
	private Student student;
	 Toast toast;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_menu_principal, container, false);
		//toast = Toast.makeText(getActivity().getApplicationContext(),"", Toast.LENGTH_SHORT);
		DatabaseHelper mHelper =  ((MainActivity)getActivity()).mHelper;
        student = mHelper.getStudent();
        //TODO: autenticar usuario, ahora solo coje al primero que encuentre
		  if(student == null){
			  //Dialog Fragment si es primera vez en la aplicacion
	        	FragmentManager fm = getActivity().getSupportFragmentManager(); 
	        	toast = Toast.makeText(getActivity().getApplicationContext(),"Bienvenido!", Toast.LENGTH_SHORT);
		    	FirstTimeFragment nAsig = new FirstTimeFragment();                
		    	nAsig.show(fm, "FirstTime"); 
	        }else{
	        	//actualizar vista 
				
	            ((TextView) rootView.findViewById(R.id.textViewNombreEstudiante))
	            	        .setText("Nombre: "+student.getNombre());
	            ((TextView) rootView.findViewById(R.id.textViewPromedioAcum))
            		        .setText("Prom. Acum: "+student.getPromAcum());
	            ((TextView) rootView.findViewById(R.id.textViewCreditosCursados))
	            			.setText("Creditos Cursados: "+student.getCredCursados());
	            double reqsem = ((MainActivity)getActivity()).roundTwoDecimals(student.getPromAcumDeseado());
	            ((TextView) rootView.findViewById(R.id.textViewPromDeseado))
            				.setText("Prom. Requerido: "+reqsem);
	            ((MainActivity)getActivity()).setPreferences("stud_id", student.getID()+"");
	            //TODO: investigar si es mejor enviar una instancia del estudiante al main
	            //O guardar su id como preferencial o enviarlo como argumento
	            toast.setText("Hola! Bienvenido");
	            toast.show();
	        }
		Button button = (Button) rootView.findViewById(R.id.buttonEditarSemestre);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				//Abrir fragment semestre, y cargar los datos requeridos
				Log.d(TAG, "Editar Semestre on click");
				int val;
				Bundle args = new Bundle();
		    	SemestreFragment semestre = new SemestreFragment();
			    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.container, semestre,"semestre")
				.addToBackStack(null)
				.commit();
			}
		});
		Button buttonSimuladorSemestral = ((Button) rootView.findViewById(R.id.buttonSimuladorPromSemestral));
		buttonSimuladorSemestral.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Editar Simulador Prom Semestral");
				int val;
				
				Bundle args = new Bundle();
				SimuladorPromedioAcumuladoFragment sim = new SimuladorPromedioAcumuladoFragment();
				sim.setArguments(args);
			    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.container, sim,"sim")
				.addToBackStack(null)
				.commit();
				
			}
		});
		Button buttonSimuladorAsignatura = ((Button) rootView.findViewById(R.id.buttonSimPromAsignaturas));
		buttonSimuladorAsignatura.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Bundle args = new Bundle();
		    	SemestreFragment semestre = new SemestreFragment();
			    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.container, semestre,"semestre")
				.addToBackStack(null)
				.commit();
				
			}
		});
		
	   mHelper.closeDB();
	    
		return rootView;
	}

}
