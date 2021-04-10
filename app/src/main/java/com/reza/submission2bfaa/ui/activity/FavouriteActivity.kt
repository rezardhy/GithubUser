package com.reza.submission2bfaa.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.reza.submission2bfaa.R
import com.reza.submission2bfaa.adapter.RVAdapter
import com.reza.submission2bfaa.databinding.ActivityFavouriteBinding
import com.reza.submission2bfaa.db.UserHelper
import com.reza.submission2bfaa.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityFavouriteBinding
    private lateinit var adapter:RVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUser.setHasFixedSize(true)
        //loadNotesAsync()

    }


}