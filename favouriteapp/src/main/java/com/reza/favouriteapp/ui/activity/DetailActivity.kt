package com.reza.submission2bfaa.ui.activity

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.reza.favouriteapp.R
import com.reza.favouriteapp.adapter.SectionsPagerAdapter
import com.reza.favouriteapp.model.User
import com.reza.favouriteapp.databinding.ActivityDetailBinding
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import com.bumptech.glide.request.target.Target
import com.reza.favouriteapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.reza.favouriteapp.db.DatabaseContract.NoteColumns.Companion.FAVOURITE_DB
import com.reza.favouriteapp.db.DatabaseContract.NoteColumns.Companion.PHOTO_DB
import com.reza.favouriteapp.db.DatabaseContract.NoteColumns.Companion.USERNAME_DB
import com.reza.favouriteapp.db.UserHelper


class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityDetailBinding
    private lateinit var userData: User

    private lateinit var userHelper:UserHelper
    private var isLove = "false"

    private lateinit var  userGit:User
    private lateinit var name1 :String
    private lateinit var username1: String
    private lateinit var img1 :String
    private lateinit var company1: String
    private lateinit var location1: String
    private lateinit var repo1: String

    private lateinit var uriWithId: Uri



    companion object {
        private val TAG = DetailActivity::class.java.simpleName
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,R.string.tab_text_2
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userData = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val nameOfUser:String = userData.username

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()


        uriWithId = Uri.parse("$CONTENT_URI/$nameOfUser")


        val actionBar = supportActionBar
        actionBar!!.title = nameOfUser
        actionBar.setDisplayHomeAsUpEnabled(true)
        createTab()
        binding.btnFav.visibility = View.INVISIBLE
        getDataAPIDetail(nameOfUser)

        Log.d("uri",uriWithId.toString())

        onCheckFav()
        binding.btnFav.setOnClickListener(this)


    }


    private fun onCheckFav(){

      val cursor = contentResolver.query(uriWithId, null, null, null, null)

      if (cursor == null) {

          binding.btnFav.text = getString(R.string.add_to_favourite)
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              binding.btnFav.setBackgroundColor(getColor(R.color.pink1))
          }
          isLove ="false"
      }
      else{
          binding.btnFav.text = getString(R.string.removefav)
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              binding.btnFav.setBackgroundColor(getColor(R.color.white))
          }
          isLove = "true"

          cursor.close()

      }


    }

    override fun onClick(v: View?) {
        if (v?.id == binding.btnFav.id){
            if (isLove =="true"){
                binding.btnFav.text = getString(R.string.add_to_favourite)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.btnFav.setBackgroundColor(getColor(R.color.pink1))
                }

                Log.d("delete",uriWithId.toString())
                //userHelper.deleteById(username1)
                contentResolver.delete(uriWithId, null, null)
                isLove = "false"

            }

            else if(isLove=="false"){
                binding.btnFav.text = getString(R.string.removefav)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.btnFav.setBackgroundColor(getColor(R.color.white))
                }

                isLove = "true"
                val values = ContentValues()
                values.put(USERNAME_DB,username1)
                values.put(PHOTO_DB,img1)
                values.put(FAVOURITE_DB,isLove)
                //userHelper.insert(values)
                contentResolver.insert(CONTENT_URI,values)

            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun createTab(){
        val sectionsPagerAdapter = SectionsPagerAdapter(this,supportFragmentManager)
        val viewPager = binding.viewPager
        sectionsPagerAdapter.userName=userData.username
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            binding.progressBarDetail.visibility =  View.VISIBLE
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun getDataAPIDetail(userGetDataAPIDetail :String){

        binding.progressBarDetail.visibility= View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$userGetDataAPIDetail"
        client.addHeader("Authorization", MainActivity.token)
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBarDetail.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    // pengambilan data
                    val responseObject = JSONObject(result)
                    username1 = responseObject.getString("login")
                    name1 = responseObject.getString("name")
                    img1 = responseObject.getString("avatar_url")
                    company1 = responseObject.getString("company")
                    location1 = responseObject.getString("location")
                    repo1 = responseObject.getString("public_repos")

                    if(username1=="null"){
                        username1="-"
                    }
                    if(name1=="null"){
                        name1="-"
                    }
                    if(company1=="null"){
                        company1="-"
                    }
                    if(location1=="null"){
                        location1="-"
                    }
                    if(repo1=="null"){
                        repo1="0"
                    }

                    userGit = User(
                        username = username1,
                        photo = img1,
                        name = name1,
                        company = company1,
                        location = location1,
                        repo = repo1
                    )

                    binding.btnFav.visibility=View.VISIBLE

                }catch (e : Exception){
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
                showDataUser(userGit)
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBarDetail.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()

            }
        })


    }

    private fun showDataUser(user1:User){
        binding.apply{
            nameDetail.text = user1.name
            companyDetail.text =user1.company
            repoDetail.text= user1.repo
            locationDetail.text = user1.location
        }

        Glide.with(this)
            .load(user1.photo)
            .fitCenter()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .override(Target.SIZE_ORIGINAL)
            .into(binding.imgDetail)
    }

}