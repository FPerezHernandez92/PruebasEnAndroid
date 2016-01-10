package com.example.herpefran92.appgestosfoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;

public class Gestos extends AppCompatActivity implements OnClickListener{

    Button btn;
    ImageView img;
    Intent i;
    Bitmap bmp;
    final static int cons = 0;

    TextView salida_x, salida_y, gesto;
    Boolean tocado1=false, tocado5=false, tocado9=false, tocado6=false, tocado3=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestos);
        salida_x = (TextView) findViewById(R.id.pos_x);
        salida_y = (TextView) findViewById(R.id.pos_y);
        gesto = (TextView) findViewById(R.id.gesto);
        init();
    }

    public void init(){
        btn = (Button)findViewById(R.id.btnCaptura);
        btn.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.imagen);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_MOVE){
            int aux_x = (int) event.getRawX();
            int aux_y = (int) event.getRawY();
            salida_x.setText("Pos_X: " + Integer.toString(aux_x));
            salida_y.setText("Pos_Y: " + Integer.toString(aux_y));
            int rango = 15;
            int x_1 = 143, y_1 = 330, x_5 = 400, y_5 = 470, x_9 = 620, y_9 = 615;
            int x_6 = 626, y_6 = 482, x_3 = 640, y_3 = 345;
            if(aux_x >= (x_1-rango) && aux_x <= (x_1+rango)){
                if (aux_y >= (y_1-rango) && aux_y <= (y_1+rango)){
                    tocado1 = true;
                    gesto.setText("Tocado1");
                }
            }
            else if(tocado1 && aux_x >= (x_5-rango) && aux_x <= (x_5+rango)){
                if (aux_y >= (y_5-rango) && aux_y <= (y_5+rango)){
                    tocado5 = true;
                    gesto.setText("Tocado1-5");
                }
            }
            else if(tocado5 && aux_x >= (x_9-rango) && aux_x <= (x_9+rango)){
                if (aux_y >= (y_9-rango) && aux_y <= (y_9+rango)){
                    tocado9 = true;
                    gesto.setText("Tocado1-5-9");
                }
            }
            else if(tocado9 && aux_x >= (x_6-rango) && aux_x <= (x_6+rango)){
                if (aux_y >= (y_6-rango) && aux_y <= (y_6+rango)){
                    tocado6 = true;
                    gesto.setText("Tocado1-5-9-6");
                }
            }
            else if(tocado6 && aux_x >= (x_3-rango) && aux_x <= (x_3+rango)){
                if (aux_y >= (y_3-rango) && aux_y <= (y_3+rango)){
                    tocado3 = true;
                    gesto.setText("Gesto aceptado");

                    i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, cons);
                }
            }
        }
        return true;

    }

    @Override
    public void onClick(View v) {
        /*int id;
        id = v.getId();
        switch (id){
            case R.id.btnCaptura:
                i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 1);
                break;
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            Bundle ext = data.getExtras();
            bmp = (Bitmap)ext.get("data");
            img.setImageBitmap(bmp);
        }
    }
}

// http://developer.android.com/intl/es/training/gestures/detector.html#detect
//http://stackoverflow.com/questions/6237200/motionevent-gety-and-getx-return-incorrect-values
// http://developer.android.com/intl/es/reference/android/view/MotionEvent.PointerCoords.html#x
// https://www.youtube.com/watch?v=l2vBKhhk10s