package com.reza.submission2bfaa.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.reza.submission2bfaa.R
import com.reza.submission2bfaa.databinding.ActivitySettingsBinding
import com.reza.submission2bfaa.ui.fragment.MyPreferenceFragment

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()
        binding.TVLanguage.setOnClickListener{
            val i = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(i)
        }

    }
}