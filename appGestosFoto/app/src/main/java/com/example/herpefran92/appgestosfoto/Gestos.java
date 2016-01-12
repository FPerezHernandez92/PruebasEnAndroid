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
import android.widget.Toast;

public class Gestos extends AppCompatActivity implements OnClickListener{

    ImageView img;
    Intent i;
    Bitmap bmp;
    final static int cons = 0;

    TextView gesto;
    Boolean tocado1=false, tocado5=false, tocado9=false, tocado6=false, tocado3=false, fotolanzada = false;
    boolean tocado2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestos);
        gesto = (TextView) findViewById(R.id.gesto);
        init();
    }

    public void init(){
        img = (ImageView) findViewById(R.id.imagen);
        Toast toast = Toast.makeText(this, "Realiza el patrón que se indica arriba", Toast.LENGTH_LONG);
        toast.show();
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
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_MOVE){
            int aux_x = (int) event.getRawX();
            int aux_y = (int) event.getRawY();
            int rango = 50;
            int x_1 = 162, y_1 = 346, x_5 = 387, y_5 = 475, x_9 = 610, y_9 = 625;
            int x_6 = 614, y_6 = 482, x_3 = 616, y_3 = 341;
            int rango2 = 30;
            int x_2 = 384, y_2 = 341, x_4 = 163, y_4 = 481, x_7 = 158, y_7 = 621, x_8 = 381, y_8 = 618;
            int ast_x = 155, ast_y = 753, x_0 = 381, y_0 = 759, alm_x = 613, alm_y = 756;

            if (aux_y < 220 || aux_y > 834 || aux_x < 36 || aux_x > 735){
                tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = tocado2 = false;
                gesto.setText("Intentalo de nuevo");
            }

            if (!tocado1){
                tocado1 = compruebaToque(aux_x, aux_y, x_1, y_1, rango);
                if (tocado1){
                    gesto.setText("Tocado1");
                    fotolanzada = false;
                }
            }
            else if (tocado1) {
                if (!tocado5) {
                    tocado5 = compruebaToque(aux_x, aux_y, x_5, y_5, rango);
                    if (tocado5) {
                        gesto.setText("Tocado1-5");
                    }
                } else if (tocado5) {
                    if (!tocado9) {
                        tocado9 = compruebaToque(aux_x, aux_y, x_9, y_9, rango);
                        if (tocado9) {
                            gesto.setText("Tocado1-5-9");
                        }
                    } else if (tocado9) {
                        if (!tocado6) {
                            tocado6 = compruebaToque(aux_x, aux_y, x_6, y_6, rango);
                            if (tocado6) {
                                gesto.setText("Tocado1-5-9-6");
                            }
                        } else if (tocado6) {
                            if (!tocado3) {
                                tocado3 = compruebaToque(aux_x, aux_y, x_3, y_3, rango);
                                if (tocado3) {
                                    gesto.setText("Tocado1-5-9-6-3");
                                }
                            } else if (tocado3) {
                                if (!tocado2) {
                                    tocado2 = compruebaToque(aux_x, aux_y, x_2, y_2, rango);
                                    if (tocado2) {
                                        gesto.setText("Gesto aceptado");
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
                    gesto.setText("Intentalo de nuevo");
                }
            }
            if (tocado9 && (aux_x >= (x_5-rango)) && (aux_x <= (x_5+rango))){
                if ((aux_y >= (y_5-rango)) && (aux_y <= (y_5+rango))){
                    tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                    gesto.setText("Intentalo de nuevo");
                }
            }
            if (tocado6 && (aux_x >= (x_9-rango)) && (aux_x <= (x_9+rango))){
                if ((aux_y >= (y_9-rango)) && (aux_y <= (y_9+rango))){
                    tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                    gesto.setText("Intentalo de nuevo");
                }
            }
            if (tocado3 && (aux_x >= (x_6-rango)) && (aux_x <= (x_6+rango))){
                if ((aux_y >= (y_6-rango)) && (aux_y <= (y_6+rango))){
                    tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                    gesto.setText("Intentalo de nuevo");
                }
            }

            if((aux_x >= (x_2-rango2)) && (aux_x <= (x_2+rango2))) {
                if ((aux_y >= (y_2 - rango2)) && (aux_y <= (y_2 + rango2))) {
                    tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                    gesto.setText("Intentalo de nuevo");
                }
            }
            if((aux_x >= (x_4-rango2)) && (aux_x <= (x_4+rango2))) {
                if ((aux_y >= (y_4 - rango2)) && (aux_y <= (y_4 + rango2))) {
                    tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                    gesto.setText("Intentalo de nuevo");
                }
            }
            if((aux_x >= (x_7-rango2)) && (aux_x <= (x_7+rango2))) {
                if ((aux_y >= (y_7 - rango2)) && (aux_y <= (y_7 + rango2))) {
                    tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                    gesto.setText("Intentalo de nuevo");
                }
            }
            if((aux_x >= (x_8-rango2)) && (aux_x <= (x_8+rango2))) {
                if ((aux_y >= (y_8 - rango2)) && (aux_y <= (y_8 + rango2))) {
                    tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                    gesto.setText("Intentalo de nuevo");
                }
            }
            if((aux_x >= (x_0-rango2)) && (aux_x <= (x_0+rango2))) {
                if ((aux_y >= (y_0 - rango2)) && (aux_y <= (y_0 + rango2))) {
                    tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                    gesto.setText("Intentalo de nuevo");
                }
            }
            if((aux_x >= (ast_x-rango2)) && (aux_x <= (ast_x+rango2))) {
                if ((aux_y >= (ast_y - rango2)) && (aux_y <= (ast_y + rango2))) {
                    tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                    gesto.setText("Intentalo de nuevo");
                }
            }
            if((aux_x >= (alm_x-rango2)) && (aux_x <= (alm_x+rango2))) {
                if ((aux_y >= (alm_y - rango2)) && (aux_y <= (alm_y + rango2))) {
                    tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
                    gesto.setText("Intentalo de nuevo");
                }
            }
        }
        else if (event.getAction()==MotionEvent.ACTION_UP){
            tocado1 = tocado3 = tocado5 = tocado6 = tocado9 = false;
            gesto.setText("Intentalo de nuevo");
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