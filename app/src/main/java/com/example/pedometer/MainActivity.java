package com.example.pedometer;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private SharedPreferences sharedPreferences;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private boolean isSensorPresent = false;
    private PedometerViewModel pedometerViewModel;
    private int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Скрыть ActionBar
        setContentView(R.layout.activity_main);

        pedometerViewModel = new ViewModelProvider(this).get(PedometerViewModel.class);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent = true;
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_today) {
                    selectedFragment = new TodayFragment();
                } else if (itemId == R.id.navigation_summary) {
                    selectedFragment = new SummaryFragment();
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
                }
                return true;
            }
        });

        // Установить начальный фрагмент
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new TodayFragment()).commit();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            updateUI();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Не требуется для данного примера
    }

    private void updateUI() {
        double distance = stepCount * 0.8; // 10 шагов = 8 метров
        double calories = stepCount * 0.035; // 1000 шагов = 35 калорий

        pedometerViewModel.setStepCount(stepCount);
        pedometerViewModel.setDistance(distance / 1000);
        pedometerViewModel.setCalories(calories);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }

    private void saveDailyData(int steps, double distance, double calories) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 6; i > 0; i--) {
            editor.putInt("steps_" + i, sharedPreferences.getInt("steps_" + (i - 1), 0));
            editor.putLong("distance_" + i, sharedPreferences.getLong("distance_" + (i - 1), 0));
            editor.putLong("calories_" + i, sharedPreferences.getLong("calories_" + (i - 1), 0));
        }
        editor.putInt("steps_0", steps);
        editor.putLong("distance_0", Double.doubleToRawLongBits(distance));
        editor.putLong("calories_0", Double.doubleToRawLongBits(calories));
        editor.apply();
    }
}
