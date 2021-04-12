package com.reza.submission2bfaa.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.reza.submission2bfaa.R
import com.reza.submission2bfaa.ui.fragment.MyPreferenceFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()

    }
}