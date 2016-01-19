package sandra.examples.asr.asrwithintent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

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

public class Brujula extends View {



    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int width = 0;
    private int height = 0;
    private Matrix matrix;
    private Bitmap bitmap;
    private float bearing;

    public Brujula(Context context){
        super(context);
        initialize();
    }
    public Brujula(Context context, AttributeSet attr){
        super(context, attr);
        initialize();
    }

    private void initialize(){
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flecha);
    }

    public void setBearing(float b){
        bearing = b;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);

    }
    protected void onDraw(Canvas canvas){
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        if (bitmapWidth > canvasWidth || bitmapHeight > canvasHeight){
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmapWidth*0.85), (int)(bitmapHeight*0.85),true);
        }
        int bitmapX = bitmap.getWidth()/2;
        int bitmapY = bitmap.getHeight()/2;
        int parentX = width /2;
        int parentY = height/2;
        int centerX = parentX - bitmapX;
        int centerY = parentY - bitmapY;

        int rotation = (int) ( 360-bearing);
        matrix.reset();
        matrix.setRotate(rotation, bitmapX, bitmapY);
        matrix.postTranslate(centerX, centerY);
        canvas.drawBitmap(bitmap,matrix,paint);
    }
}