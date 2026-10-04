package com.example.smartpantry;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.RadioGroup;
import android.widget.Switch;

public class Settings extends BaseActivity {
        public static final String PREFS_NAME = "smart_pantry_prefs";
        public static final String KEY_EXPIRY_ALERTS = "expiry_alerts_enabled";
        public static final String KEY_UNITS_PREFERENCE = "units_preference";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.settings);
            setTitle("Settings: ");
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

            Switch expirySwitch = findViewById(R.id.switch_expiry_alerts);
            RadioGroup unitsGroup = findViewById(R.id.radio_group_units);
            expirySwitch.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, true));

            boolean isMetric = prefs.getString(KEY_UNITS_PREFERENCE, "metric").equals("metric");
            unitsGroup.check(isMetric ? R.id.radio_metric : R.id.radio_imperial);
            expirySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply());
            unitsGroup.setOnCheckedChangeListener((group, checkedId) ->{
                String value = checkedId == R.id.radio_metric ? "metric" : "imperial";
                prefs.edit().putString(KEY_UNITS_PREFERENCE, value).apply();
            });
            setupBottomNavigation(R.id.nav_settings);
        }
    }