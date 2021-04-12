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

    //key switch preferences
    private lateinit var SWITCHKEY: String
    private lateinit var isSwitchPreference: SwitchPreference


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
                        "09:00", "Ayo Kembali Produktif")
                }

            }
            else{
                activity?.let {
                    Toast.makeText(it,"Alarm Off",Toast.LENGTH_SHORT).show()
                    alarmReceiver.cancelAlarm(it, AlarmReceiver.TYPE_REPEATING)
                }
            }
        }


    }
}