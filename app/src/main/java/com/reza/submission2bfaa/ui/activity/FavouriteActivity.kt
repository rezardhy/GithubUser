package com.reza.submission2bfaa.ui.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.reza.submission2bfaa.R
import com.reza.submission2bfaa.adapter.FavAdapter
import com.reza.submission2bfaa.adapter.RVAdapter
import com.reza.submission2bfaa.databinding.ActivityFavouriteBinding
import com.reza.submission2bfaa.db.UserHelper
import com.reza.submission2bfaa.helper.MappingHelper
import com.reza.submission2bfaa.model.FavUser
import com.reza.submission2bfaa.model.User

@RequiresApi(Build.VERSION_CODES.M)
class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityFavouriteBinding
    private lateinit var adapter:RVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvFavUser.setHasFixedSize(true)
        showFavUser()

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.favourite)
        actionBar.setDisplayHomeAsUpEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showFavUser() {
        binding.rvFavUser.layoutManager = LinearLayoutManager(this)

        binding.progressBar.visibility = View.VISIBLE
        var userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()
        val cursor = userHelper.queryAll()
        val users = MappingHelper.mapCursorToArrayList(cursor)
        binding.progressBar.visibility = View.INVISIBLE
        if (users.size > 0) {
            val listUserAdapter = FavAdapter(users)
            binding.rvFavUser.adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object : FavAdapter.OnItemClickCallback{
                override fun onItemClicked(data: FavUser) {

                    val userGit = User(
                            username = data.username!!,
                            photo = data.photo!!
                    )


                    showSelectedUser(userGit)
                }
            })


        } else {
            binding.tvNoFav.text=getString(R.string.favempty)
        }
    }

    private fun showSelectedUser(user: User) {
        val i = Intent(this, DetailActivity::class.java)
        i.putExtra(DetailActivity.EXTRA_USER,user)
        startActivity(i)
        finish()
    }


}