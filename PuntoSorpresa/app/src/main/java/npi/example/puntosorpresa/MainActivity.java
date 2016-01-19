package npi.example.puntosorpresa;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;

   // TextView xCoor; // declare X axis object
    ImageView planta;
    TextView instr;
    int nivel;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

      //  xCoor=(TextView)findViewById(R.id.xcoor); // create X axis object
        planta=(ImageView)findViewById(R.id.plant);
        instr=(TextView)findViewById(R.id.instrucciones);

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        // add listener. The listener will be HelloAndroid (this) class
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);

        instr.setText("Dale luz a la planta para que crezca");
        planta.setImageResource(R.drawable.plant1);
        nivel = 1;
    }

    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }

    public void onSensorChanged(SensorEvent event){
        // check sensor type
        if(event.sensor.getType()== Sensor.TYPE_LIGHT){

            // assign directions
            float ligth=event.values[0];

          //  xCoor.setText("X: "+ ligth);

            if( ligth > 10 && nivel == 1)
            {
                planta.setImageResource(R.drawable.plant2);
                nivel++;
            }
            if( ligth > 20 && nivel == 2)
            {
                planta.setImageResource(R.drawable.plant3);
                nivel++;
            }
            if( ligth > 30 && nivel == 3)
            {
                planta.setImageResource(R.drawable.plant4);
                nivel++;
            }
            if( ligth > 40 && nivel == 4)
            {
                planta.setImageResource(R.drawable.plant5);
                nivel++;
            }
            if( ligth > 50 && nivel == 5)
            {
                planta.setImageResource(R.drawable.plant6);
                nivel++;
            }
            if( ligth > 60 && nivel == 6)
            {
                planta.setImageResource(R.drawable.plant7);
                nivel++;
            }
        }
    }
}