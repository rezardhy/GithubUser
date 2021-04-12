package com.reza.submission2bfaa.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.*
import com.reza.submission2bfaa.R
import com.reza.submission2bfaa.alarm.AlarmReceiver


class MyPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var SWITCHKEY: String
    private lateinit var isSwitchPreference: SwitchPreference

    private lateinit var PREFKEY: String
    private lateinit var isPref : EditTextPreference


    private lateinit var alarmReceiver: AlarmReceiver




    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        alarmReceiver = AlarmReceiver()
        init()
        setSummaries()

    }

    private fun init(){
        SWITCHKEY = resources.getString(R.string.switch_key)
        isSwitchPreference = findPreference<SwitchPreference>(SWITCHKEY) as SwitchPreference

        PREFKEY = resources.getString(R.string.languague_key)
        isPref = findPreference<EditTextPreference>(PREFKEY) as EditTextPreference


    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences

        isSwitchPreference.isChecked=sh.getBoolean(SWITCHKEY,false)


    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

    }
    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {

        if (key == SWITCHKEY){
            isSwitchPreference.isChecked = sharedPreferences.getBoolean(SWITCHKEY,false)
            val sh = preferenceManager.sharedPreferences
            val isCheck = sh.getBoolean(SWITCHKEY,false)

            if (isCheck==true){

                activity?.let {
                    Toast.makeText(it,"Alarm On",Toast.LENGTH_SHORT).show()
                    alarmReceiver.setRepeatingAlarm(
                        it, AlarmReceiver.TYPE_REPEATING,
                        "20:15", "Ayo Kembali Produktif")
                }

            }
            else{
                activity?.let {
                    Toast.makeText(it,"Alarm Off",Toast.LENGTH_SHORT).show()
                    alarmReceiver.cancelAlarm(it, AlarmReceiver.TYPE_REPEATING)
                }
            }
        }

        if (key == PREFKEY){
            val i = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(i)
        }
    }
}