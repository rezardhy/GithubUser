package com.reza.submission2bfaa.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.reza.favouriteapp.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashlogo: ImageView = findViewById(R.id.logoGithub)
        val titleApp : TextView = findViewById(R.id.titleApp)

        titleApp.alpha = 0f
        splashlogo.alpha = 0f

        titleApp.animate().setDuration(2500).alpha(2f)
        splashlogo.animate().setDuration(2500).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}