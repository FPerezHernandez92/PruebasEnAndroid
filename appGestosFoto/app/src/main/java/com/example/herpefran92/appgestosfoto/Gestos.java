package com.example.herpefran92.appgestosfoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Gestos extends FragmentActivity implements OnClickListener{

    ImageView img;
    Intent i;
    Bitmap bmp;
    final static int cons = 0;
    Boolean tengoquelevantar=false;

    Boolean tocado1=false, tocado5=false, tocado9=false, tocado6=false, tocado3=false, fotolanzada = false;
    boolean tocado2 = false;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9;

    boolean isImageFitToScreen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestos);
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);
        t5 = (TextView) findViewById(R.id.t5);
        t6 = (TextView) findViewById(R.id.t6);
        t7 = (TextView) findViewById(R.id.t7);
        t8 = (TextView) findViewById(R.id.t8);
        t9 = (TextView) findViewById(R.id.t9);
        init();

    }

    public void init(){
        img = (ImageView) findViewById(R.id.imagen);
        Toast toast = Toast.makeText(this, "Realiza el patrón", Toast.LENGTH_LONG);
        toast.show();
        cambiaTodosColor(Color.BLACK);
    }

    public boolean compruebaToque(int aux_x, int aux_y, int x, int y, int rango){
        boolean res = false;
        if((aux_x >= (x-rango)) && (aux_x <= (x+rango))){
            if ((aux_y >= (y-rango)) && (aux_y <= (y+rango))){
                res = true;
            }
        }
        return res;
    }

    public void cambiaColor(TextView tecla, int color){
        tecla.setTextColor(color);
    }

    public void cambiaTodosColor(int color){
        t1.setTextColor(color);
        t5.setTextColor(color);
        t9.setTextColor(color);
        t6.setTextColor(color);
        t3.setTextColor(color);
        t2.setTextColor(color);
        t4.setTextColor(color);
        t7.setTextColor(color);
        t8.setTextColor(color);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_MOVE){
            int aux_x = (int) event.getRawX();
            int aux_y = (int) event.getRawY();
            int rango = 70;
            int x_1 = 148, y_1 = 160, x_5 = 393, y_5 = 310, x_9 = 620, y_9 = 460;
            int x_6 = 614, y_6 = 310, x_3 = 616, y_3 = 160;
            int rango2 = 50;
            int x_2 = 389, y_2 = 160, x_4 = 149, y_4 = 310, x_7 = 158, y_7 = 460, x_8 = 381, y_8 = 460;



            //Compruebo que se está tocando dentro del patrón
            if (aux_y < 95 || aux_y > 526 || aux_x < 40 || aux_x > 740){
                tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = tocado2 = false;
                cambiaTodosColor(Color.RED);
                tengoquelevantar = true;
            }
            else {
                if (!tocado1 && !tengoquelevantar){
                    tocado1 = compruebaToque(aux_x, aux_y, x_1, y_1, rango);
                    if (tocado1){
                        cambiaColor(t1,Color.GREEN);
                        fotolanzada = false;
                    }
                }
                else if (tocado1 && !tengoquelevantar) {
                    if (!tocado5) {
                        tocado5 = compruebaToque(aux_x, aux_y, x_5, y_5, rango);
                        if (tocado5) {
                            cambiaColor(t5,Color.GREEN);
                        }
                    } else if (tocado5) {
                        if (!tocado9) {
                            tocado9 = compruebaToque(aux_x, aux_y, x_9, y_9, rango);
                            if (tocado9) {
                                cambiaColor(t9, Color.GREEN);
                            }
                        } else if (tocado9) {
                            if (!tocado6) {
                                tocado6 = compruebaToque(aux_x, aux_y, x_6, y_6, rango);
                                if (tocado6) {
                                    cambiaColor(t6, Color.GREEN);
                                }
                            } else if (tocado6) {
                                if (!tocado3) {
                                    tocado3 = compruebaToque(aux_x, aux_y, x_3, y_3, rango);
                                    if (tocado3) {
                                        cambiaColor(t3, Color.GREEN);
                                    }
                                } else if (tocado3) {
                                    if (!tocado2) {
                                        tocado2 = compruebaToque(aux_x, aux_y, x_2, y_2, rango);
                                        if (tocado2) {
                                            cambiaColor(t2, Color.GREEN);
                                            if (fotolanzada == false) {
                                                fotolanzada = true;
                                                i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                startActivityForResult(i, cons);
                                                tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = tocado2 = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (tocado5 && (aux_x >= (x_1-rango)) && (aux_x <= (x_1+rango))){
                    if ((aux_y >= (y_1-rango)) && (aux_y <= (y_1+rango))){
                        tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                        tengoquelevantar = true;
                        cambiaTodosColor(Color.RED);
                    }
                }
                if (tocado9 && (aux_x >= (x_5-rango)) && (aux_x <= (x_5+rango))){
                    if ((aux_y >= (y_5-rango)) && (aux_y <= (y_5+rango))){
                        tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                        tengoquelevantar = true;
                        cambiaTodosColor(Color.RED);
                    }
                }
                if (tocado6 && (aux_x >= (x_9-rango)) && (aux_x <= (x_9+rango))){
                    if ((aux_y >= (y_9-rango)) && (aux_y <= (y_9+rango))){
                        tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                        tengoquelevantar = true;
                        cambiaTodosColor(Color.RED);
                    }
                }
                if (tocado3 && (aux_x >= (x_6-rango)) && (aux_x <= (x_6+rango))){
                    if ((aux_y >= (y_6-rango)) && (aux_y <= (y_6+rango))){
                        tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                        tengoquelevantar = true;
                        cambiaTodosColor(Color.RED);
                    }
                }

                if((aux_x >= (x_2-rango2)) && (aux_x <= (x_2+rango2))) {
                    if ((aux_y >= (y_2 - rango2)) && (aux_y <= (y_2 + rango2))) {
                        tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                        tengoquelevantar = true;
                        cambiaTodosColor(Color.RED);
                    }
                }
                if((aux_x >= (x_4-rango2)) && (aux_x <= (x_4+rango2))) {
                    if ((aux_y >= (y_4 - rango2)) && (aux_y <= (y_4 + rango2))) {
                        tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                        tengoquelevantar = true;
                        cambiaTodosColor(Color.RED);
                    }
                }
                if((aux_x >= (x_7-rango2)) && (aux_x <= (x_7+rango2))) {
                    if ((aux_y >= (y_7 - rango2)) && (aux_y <= (y_7 + rango2))) {
                        tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                        tengoquelevantar = true;
                        cambiaTodosColor(Color.RED);
                    }
                }
                if((aux_x >= (x_8-rango2)) && (aux_x <= (x_8+rango2))) {
                    if ((aux_y >= (y_8 - rango2)) && (aux_y <= (y_8 + rango2))) {
                        tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                        tengoquelevantar = true;
                        cambiaTodosColor(Color.RED);
                    }
                }
            }


        }
        else if (event.getAction()==MotionEvent.ACTION_UP){
            tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
            tengoquelevantar = false;
            cambiaTodosColor(Color.BLACK);
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