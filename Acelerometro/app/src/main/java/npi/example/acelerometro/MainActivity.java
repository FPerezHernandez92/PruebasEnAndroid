package npi.example.acelerometro;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;

    TextView xCoor; // declare X axis object
    TextView yCoor; // declare Y axis object
    TextView zCoor; // declare Z axis object

    boolean arrancado = false;
    boolean llaveDentro = false;
    boolean cerrado = true;

    static final String STATE_ARRANCADO = "motorArrancado";
    static final String STATE_LLAVE = "llaveDentro";
    static final String STATE_PUERTA = "cocheCerrado";

    TextView instr;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            arrancado = savedInstanceState.getBoolean(STATE_ARRANCADO);
            llaveDentro = savedInstanceState.getBoolean(STATE_LLAVE);
            cerrado = savedInstanceState.getBoolean(STATE_PUERTA);
        }

        setContentView(R.layout.activity_main);

        xCoor=(TextView)findViewById(R.id.xcoor); // create X axis object
        yCoor=(TextView)findViewById(R.id.ycoor); // create Y axis object
        zCoor=(TextView)findViewById(R.id.zcoor); // create Z axis object

        instr=(TextView)findViewById(R.id.instrucciones);

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        // add listener. The listener will be HelloAndroid (this) class
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

		/*	More sensor speeds (taken from api docs)
		    SENSOR_DELAY_FASTEST get sensor data as fast as possible
		    SENSOR_DELAY_GAME	rate suitable for games
		 	SENSOR_DELAY_NORMAL	rate (default) suitable for screen orientation changes
		*/



    }

    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }

    public void onSensorChanged(SensorEvent event){
        // check sensor type
        if(event.sensor.getType()== Sensor.TYPE_ACCELEROMETER){

            // assign directions
            float x=event.values[0];
            float y=event.values[1];
            float z=event.values[2];

            if(cerrado)
            {
                instr.setText("Pon el móvil en vertical para abrir el coche");
            }

            if( y > 10 && cerrado)
            {
                cerrado = false;

                final MediaPlayer mp = MediaPlayer.create(this, R.raw.car_chirp_x);
                mp.start();
            }

            if(!cerrado && !llaveDentro)
            {
                instr.setText("Pon el movil en horizontal para meter la llave y girala para arrancar el motor");
            }
            if( x > 10 && !llaveDentro && !cerrado)
            {
                llaveDentro = true;
            }

            if(llaveDentro && !arrancado)
            {
                instr.setText(("Gira el móvil en sentido horario para arrancar"));
            }
            if( x < 0 && !arrancado && llaveDentro)
            {
                arrancado = true;

                final MediaPlayer mp = MediaPlayer.create(this, R.raw.car_x);
                mp.start();
            }

            if( arrancado)
            {
                instr.setText("Pon el móvil bocaabajo para tocar el claxon");
            }
            if( arrancado && z < -10)
            {
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.car_horn_x);
                mp.start();
            }

            xCoor.setText("X: "+x);
            yCoor.setText("Y: "+y);
            zCoor.setText("Z: "+z);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putBoolean(STATE_ARRANCADO, arrancado);
        savedInstanceState.putBoolean(STATE_LLAVE, llaveDentro);
        savedInstanceState.putBoolean(STATE_PUERTA, cerrado);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}