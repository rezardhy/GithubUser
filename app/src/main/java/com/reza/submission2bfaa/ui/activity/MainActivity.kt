package com.reza.submission2bfaa.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpClient.log
import com.loopj.android.http.AsyncHttpResponseHandler
import com.reza.submission2bfaa.R
import com.reza.submission2bfaa.adapter.RVAdapter
import com.reza.submission2bfaa.databinding.ActivityMainBinding
import com.reza.submission2bfaa.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<User>()
    private lateinit var username1: String
    private lateinit var photo1: String

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_change_settings->{
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_fav_menu -> {
                Toast.makeText(this, "Fav button", Toast.LENGTH_SHORT).show()

                val i = Intent(this,FavouriteActivity::class.java)
                startActivity(i)
            }
        }

        return super.onOptionsItemSelected(item)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUser.setHasFixedSize(true)
        getDataAPI("sidiqp")
        searchUsername()

    }

    private fun searchUsername(){
        val searcManager = getSystemService(Context.SEARCH_SERVICE)as SearchManager
        val searchView = binding.search
        searchView.setSearchableInfo(searcManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                list.clear()
                getDataAPI(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


    }

    private fun showRecyclerList() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        if (list.isEmpty()){
            Toast.makeText(this@MainActivity, "username tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
        else{
            val listUserAdapter = RVAdapter(list)
            binding.rvUser.adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object : RVAdapter.OnItemClickCallback{
                override fun onItemClicked(data: User) {
                    showSelectedUser(data)
                }
            })
        }
    }

    private fun showSelectedUser(user: User) {
        val i = Intent(this, DetailActivity::class.java)
        i.putExtra(DetailActivity.EXTRA_USER,user)
        startActivity(i)
    }

    private fun getDataAPI(username :String){

        binding.progressBar.visibility= View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization","token 5ce5cfd69e0d29e428b993bcce338e412af4e845")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    // pengambilan data
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()){
                        val item = items.getJSONObject(i)
                        username1 = item.getString("login")
                        photo1 = item.getString("avatar_url")



                        val userGit = User(
                                username = username1,
                                photo = photo1
                        )
                        list.add(userGit)
                        log.d(TAG,username1)
                    }
                    //memanggil recycler list
                    showRecyclerList()


                }catch (e : Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()

            }
        })


    }


}