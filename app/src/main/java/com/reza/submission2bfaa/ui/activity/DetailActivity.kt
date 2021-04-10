package com.reza.submission2bfaa.ui.activity

import android.content.ContentValues
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.reza.submission2bfaa.R
import com.reza.submission2bfaa.adapter.SectionsPagerAdapter
import com.reza.submission2bfaa.model.User
import com.reza.submission2bfaa.databinding.ActivityDetailBinding
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import com.bumptech.glide.request.target.Target
import com.reza.submission2bfaa.db.DatabaseContract
import com.reza.submission2bfaa.db.UserHelper
import com.reza.submission2bfaa.helper.MappingHelper

@RequiresApi(Build.VERSION_CODES.M)
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

        val actionBar = supportActionBar
        actionBar!!.title = nameOfUser
        actionBar.setDisplayHomeAsUpEnabled(true)
        createTab()
        getDataAPIDetail(nameOfUser)


        onCheckFav()
        binding.btnFav.setOnClickListener(this)


    }


    private fun onCheckFav(){

        val noteHelper = UserHelper.getInstance(applicationContext)
        noteHelper.open()
        val cursor = noteHelper.queryById(userData.username)
        val ans = MappingHelper.mapCursorToArrayList(cursor)


        if (ans.isNullOrEmpty()){
            binding.btnFav.text = getString(R.string.add_to_favourite)
            binding.btnFav.setBackgroundColor(getColor(R.color.pink1))
            isLove ="false"
        }
        else  {
            binding.btnFav.text = getString(R.string.removefav)
            binding.btnFav.setBackgroundColor(getColor(R.color.white))
            isLove = ans[0].fav.toString()
        }


    }

    override fun onClick(v: View?) {
        if (v?.id == binding.btnFav.id){


            if (isLove =="true"){
                binding.btnFav.text = getString(R.string.add_to_favourite)
                binding.btnFav.setBackgroundColor(getColor(R.color.pink1))
                userHelper.deleteById(username1)
                isLove = "false"

            }

            else if(isLove=="false"){
                binding.btnFav.text = getString(R.string.removefav)
                binding.btnFav.setBackgroundColor(getColor(R.color.white))
                isLove = "true"
                val values = ContentValues()
                values.put(DatabaseContract.NoteColumns.USERNAME_DB,username1)
                values.put(DatabaseContract.NoteColumns.PHOTO_DB,img1)
                values.put(DatabaseContract.NoteColumns.FAVOURITE_DB,isLove)
                userHelper.insert(values)

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
        binding.nameDetail.text = user1.name
        binding.companyDetail.text =user1.company
        binding.repoDetail.text= user1.repo
        binding.locationDetail.text = user1.location


        Glide.with(this)
            .load(user1.photo)
            .fitCenter()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .override(Target.SIZE_ORIGINAL)
            .into(binding.imgDetail)
    }




}