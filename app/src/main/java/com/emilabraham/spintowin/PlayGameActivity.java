package com.emilabraham.spintowin;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PlayGameActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor gyro;
    private Sensor rotvec;
    private MyListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }*/

        Intent intent = getIntent();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        rotvec = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mListener = new MyListener();

        TextView textView = (TextView) findViewById(R.id.zaxis);
        textView.setTextSize(40);

        //sets textView to be zaxis value as a string
        textView.setText(String.format("%f", mListener.printValue()));
    }

    @Override
    protected void onResume() {
        //When activity starts or regains focus, start the listener
        super.onResume();
        mListener.start();
    }

    @Override
    protected void onPause() {
        //When the activity stops or loses focus, stop the listener
        super.onPause();
        mListener.stop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_play_game,
                    container, false);
            return rootView;
        }
    }*/

    class MyListener implements SensorEventListener {
        private Sensor mRotationVectorSensor;
        //The value of the zaxis
        public float zaxis = 0;

        public MyListener() {
            mRotationVectorSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }

        public void start() {
            //enable listener when activity is resumed or started, asking for 10 millisecond updates
            mSensorManager.registerListener(this, mRotationVectorSensor, 100000000);
        }

        public void stop() {
            //disable listener when activity is stopped
            mSensorManager.unregisterListener(this);
        }
        public float printValue() {
            return zaxis;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                //values are from -1 to 1

                //X is 0 with parallel to ground, face up.
                //X is 1 when parallel to ground, face down.
                //System.out.println("X: " + event.values[0]);

                //Y shifts around -1 to 1 when you hold the phone parallel to your face, vertically
                //and turn it like a top. So rotate it in an axis parallel to ground.
                //The 1 and 0 and -1 values are hard to pinpoint.
                //System.out.println("Y: " + event.values[1]);

                //Changes if you were to throw the phone like a frisbee. Regardless if parallel or
                //not to the ground.
                //I'm pretty sure z-axis is what I want.
                //update the zaxis value
                zaxis = event.values[2];
                System.out.println("Z: " + zaxis);
                TextView textView = (TextView) findViewById(R.id.zaxis);
                textView.setText(String.format("%f", printValue()));

            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

}
