/*
 *  Copyright 2013 Zoraida Callejas and Michael McTear
 * 
 *  This file is part of the Sandra (Speech ANDroid Apps) Toolkit, from the book:
 *  Voice Application Development for Android, Michael McTear and Zoraida Callejas, 
 *  PACKT Publishing 2013 <http://www.packtpub.com/voice-application-development-for-android/book>,
 *  <http://lsi.ugr.es/zoraida/androidspeechbook>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>. 
 */

package sandra.examples.asr.asrwithintent;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import android.app.Activity;
import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * ASRWithIntent: Basic app with ASR using a RecognizerIntent
 * 
 * Simple demo in which the user speaks and the recognition results
 * are showed in a list along with their confidence values
 * 
 * The code for this app is self-contained: it uses an <code>Intent</code> 
 * for speech recognition. The rest of the apps in the book employ a special 
 * <code>ASR</code> library (<code>ASRLib</code>).
 * 
 * @author Zoraida Callejas
 * @author Michael McTear
 * @version 1.7, 01/22/14
 *
 */

public abstract class ASRWithIntent extends Activity implements SensorEventListener, LocationListener {

	// Default values for the language model and maximum number of recognition results
	// They are shown in the GUI when the app starts, and they are used when the user selection is not valid
	private final static int DEFAULT_NUMBER_RESULTS = 10;
	private final static String DEFAULT_LANG_MODEL = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM; 
	

	private int numberRecoResults = DEFAULT_NUMBER_RESULTS; 
	private String languageModel = DEFAULT_LANG_MODEL; 
	
	private static final String LOGTAG = "ASRBEGIN";
	private static int ASR_CODE = 123;

	ListView lv;
	Button hablar;
	String[] datos = {"h"};
	ArrayList<String> nBestView = new ArrayList<String>();
	//TextView seleccionado;


	public static final String NA = "N/A";
	public static final String FIXED = "FIXED";
	// location min time
	private static final int LOCATION_MIN_TIME = 30 * 1000;
	// location min distance
	private static final int LOCATION_MIN_DISTANCE = 10;
	// Gravity for accelerometer data
	private float[] gravity = new float[3];
	// magnetic data
	private float[] geomagnetic = new float[3];
	// Rotation data
	private float[] rotation = new float[9];
	// orientation (azimuth, pitch, roll)
	private float[] orientation = new float[3];
	// smoothed values
	private float[] smoothed = new float[3];
	// sensor manager
	private SensorManager sensorManager;
	// sensor gravity
	private Sensor sensorGravity;
	private Sensor sensorMagnetic;
	private LocationManager locationManager;
	private Location currentLocation;
	private GeomagneticField geomagneticField;
	private double bearing = 0;
	private TextView textDirection, textLat, textLong;
	private Brujula compassView;


	/**
	 * Sets up the activity initializing the GUI
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asrwithintent);

		//Shows in the GUI the default values for the language model and the maximum number of recognition results
		showDefaultValues(); 
		lv = (ListView) findViewById(R.id.nbest_listview);
		hablar = (Button) findViewById(R.id.speech_btn);
		//seleccionado = (TextView)findViewById(R.id.seleccionado);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos));

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				hablar.setText(nBestView.get(position));
				setListView(new ArrayList<String>());

			}
		});
		setSpeakButton();


		//textLat = (TextView) findViewById(R.id.latitude);
		//textLong = (TextView) findViewById(R.id.longitude);
		//textDirection = (TextView) findViewById(R.id.text);
		//compassView = (CompassView) findViewById(R.id.compass);
		// keep screen light on (wake lock light)
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onStart() {
		super.onStart();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		// listen to these sensors
		sensorManager.registerListener(this, sensorGravity,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, sensorMagnetic,
				SensorManager.SENSOR_DELAY_NORMAL);

		// I forgot to get location manager from system service ... Ooops <img src="http://www.ssaurel.com/blog/wp-includes/images/smilies/icon_biggrin.gif" alt=":D" class="wp-smiley">
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// request location data
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				LOCATION_MIN_TIME, LOCATION_MIN_DISTANCE, this);

		// get last known position
		Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (gpsLocation != null) {
			currentLocation = gpsLocation;
		} else {
			// try with network provider
			Location networkLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if (networkLocation != null) {
				currentLocation = networkLocation;
			} else {
				// Fix a position
				currentLocation = new Location(FIXED);
				currentLocation.setAltitude(1);
				currentLocation.setLatitude(43.296482);
				currentLocation.setLongitude(5.36978);
			}

			// set current location
			onLocationChanged(currentLocation);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// remove listeners
		sensorManager.unregisterListener(this, sensorGravity);
		sensorManager.unregisterListener(this, sensorMagnetic);
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLocation = location;
		// used to update location info on screen
		updateLocation(location);
		geomagneticField = new GeomagneticField(
				(float) currentLocation.getLatitude(),
				(float) currentLocation.getLongitude(),
				(float) currentLocation.getAltitude(),
				System.currentTimeMillis());
	}

	private void updateLocation(Location location) {
		if (FIXED.equals(location.getProvider())) {
			textLat.setText(NA);
			textLong.setText(NA);
		}

		// better => make this creation outside method
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		NumberFormat formatter = new DecimalFormat("#0.00", dfs);
		textLat.setText("Lat : " + formatter.format(location.getLatitude()));
		textLong.setText("Long : " + formatter.format(location.getLongitude()));
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		boolean accelOrMagnetic = false;

		// get accelerometer data
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// we need to use a low pass filter to make data smoothed
			smoothed = LowPassFilter.filter(event.values, gravity);
			gravity[0] = smoothed[0];
			gravity[1] = smoothed[1];
			gravity[2] = smoothed[2];
			accelOrMagnetic = true;

		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			smoothed = LowPassFilter.filter(event.values, geomagnetic);
			geomagnetic[0] = smoothed[0];
			geomagnetic[1] = smoothed[1];
			geomagnetic[2] = smoothed[2];
			accelOrMagnetic = true;

		}

		// get rotation matrix to get gravity and magnetic data
		SensorManager.getRotationMatrix(rotation, null, gravity, geomagnetic);
		// get bearing to target
		SensorManager.getOrientation(rotation, orientation);
		// east degrees of true North
		bearing = orientation[0];
		// convert from radians to degrees
		bearing = Math.toDegrees(bearing);

		// fix difference between true North and magnetical North
		if (geomagneticField != null) {
			bearing += geomagneticField.getDeclination();
		}

		// bearing must be in 0-360
		if (bearing < 0) {
			bearing += 360;
		}

		// update compass view
		compassView.setBearing((float) bearing);

		if (accelOrMagnetic) {
			compassView.postInvalidate();
		}

		updateTextDirection(bearing); // display text direction on screen
	}

	private void updateTextDirection(double bearing) {
		int range = (int) (bearing / (360f / 16f));
		String dirTxt = "";

		if (range == 15 || range == 0)
			dirTxt = "N";
		if (range == 1 || range == 2)
			dirTxt = "NE";
		if (range == 3 || range == 4)
			dirTxt = "E";
		if (range == 5 || range == 6)
			dirTxt = "SE";
		if (range == 7 || range == 8)
			dirTxt = "S";
		if (range == 9 || range == 10)
			dirTxt = "SW";
		if (range == 11 || range == 12)
			dirTxt = "W";
		if (range == 13 || range == 14)
			dirTxt = "NW";

		textDirection.setText("" + ((int) bearing) + ((char) 176) + " "
				+ dirTxt); // char 176 ) = degrees ...
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
				&& accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
			// manage fact that compass data are unreliable ...
			// toast ? display on screen ?
		}
	}
}


	/**
	 * Initializes the speech recognizer and starts listening to the user input
	 */
	private void listen()  {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		// Specify language model
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, languageModel);

		// Specify how many results to receive
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, numberRecoResults);  

		// Start listening
		startActivityForResult(intent, ASR_CODE);
    }
	
	
	/**
	 * Shows in the GUI the default values for the language model (checks radio button)
	 * and the maximum number of recognition results (shows the number in the text field)
	 */
	private void showDefaultValues() {
		//Show the default number of results in the corresponding EditText
		((EditText) findViewById(R.id.numResults_editText)).setText(""+DEFAULT_NUMBER_RESULTS);
		
		//Show the language model
		if(DEFAULT_LANG_MODEL.equals(RecognizerIntent.LANGUAGE_MODEL_FREE_FORM))
			((RadioButton) findViewById(R.id.langModelFree_radio)).setChecked(true);
		else
			((RadioButton) findViewById(R.id.langModelFree_radio)).setChecked(true);
	}
	
	/**
	 * Reads the values for the language model and the maximum number of recognition results
	 * from the GUI
	 */
	private void setRecognitionParams()  {
		String numResults = ((EditText) findViewById(R.id.numResults_editText)).getText().toString();
		
		//Converts String into int, if it is not possible, it uses the default value
		try{
			numberRecoResults = Integer.parseInt(numResults);
		} catch(Exception e) {	
			numberRecoResults = DEFAULT_NUMBER_RESULTS;	
		}
		//If the number is <= 0, it uses the default value
		if(numberRecoResults<=0)
			numberRecoResults = DEFAULT_NUMBER_RESULTS;
		
		
		RadioGroup radioG = (RadioGroup) findViewById(R.id.langModel_radioGroup);
		switch(radioG.getCheckedRadioButtonId()){
			case R.id.langModelFree_radio:
				languageModel = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
				break;
			case R.id.langModelWeb_radio:
				languageModel = RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH;
				break;
			default:
				languageModel = DEFAULT_LANG_MODEL;
				break;
		}
	}
	
	/**
	 * Sets up the listener for the button that the user
	 * must click to start talking
	 */
	@SuppressLint("DefaultLocale")
	private void setSpeakButton() {
		//Gain reference to speak button
		Button speak = (Button) findViewById(R.id.speech_btn);

		//Set up click listener
		speak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Speech recognition does not currently work on simulated devices,
				//it the user is attempting to run the app in a simulated device
				//they will get a Toast
				if ("generic".equals(Build.BRAND.toLowerCase())) {
					Toast toast = Toast.makeText(getApplicationContext(), "ASR is not supported on virtual devices", Toast.LENGTH_SHORT);
					toast.show();
					Log.d(LOGTAG, "ASR attempt on virtual device");
				} else {
					setRecognitionParams(); //Read speech recognition parameters from GUI
					listen();                //Set up the recognizer with the parameters and start listening
				}
			}
		});
	}

	/**
	 *  Shows the formatted best of N best recognition results (N-best list) from
	 *  best to worst in the <code>ListView</code>. 
	 *  For each match, it will render the recognized phrase and the confidence with 
	 *  which it was recognized.
	 */
	@SuppressLint("InlinedApi")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ASR_CODE)  {
            if (resultCode == RESULT_OK)  {            	
            	if(data!=null) {
	            	//Retrieves the N-best list and the confidences from the ASR result
	            	ArrayList<String> nBestList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	            	float[] nBestConfidences = null;
	            	
	            	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)  //Checks the API level because the confidence scores are supported only from API level 14
	            		nBestConfidences = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
	            	
					//Creates a collection of strings, each one with a recognition result and its confidence
	            	//following the structure "Phrase matched (conf: 0.5)"


					ArrayList<String> misMejores = new ArrayList<String>();

					//Voy a realizar un filtrado, es decir, 


					for(int i=0; i<nBestList.size(); i++){
						if(nBestConfidences!=null){
							if(nBestConfidences[i]>=0)
								misMejores.add(nBestList.get(i));
							else
								misMejores.add(nBestList.get(i));
						}
						else
							misMejores.add(nBestList.get(i) + " (no confidence value available)");
					}

					for (int i=0; i<misMejores.size(); i++){
						String aux;
						aux = misMejores.get(i);
						String [] campos = aux.split("\\s+");
						if (campos.length == 2) {
							nBestView.add(misMejores.get(i));
						}
					}

					//Includes the collection in the ListView of the GUI
					setListView(nBestView);
					
					Log.i(LOGTAG, "There were : "+ nBestView.size()+" recognition results");
            	}
            }
            else {       	
	    		//Reports error in recognition error in log
	    		Log.e(LOGTAG, "Recognition was not successful");
            }
        }
	}
	
	/**
	 * Includes the recognition results in the list view
	 * @param nBestView list of matches
	 */
	private void setListView(ArrayList<String> nBestView){
		
		// Instantiates the array adapter to populate the listView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nBestView);
    	ListView listView = (ListView) findViewById(R.id.nbest_listview);
    	listView.setAdapter(adapter);

	}
}