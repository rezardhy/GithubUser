package com.reza.favouriteapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.reza.favouriteapp.R
import com.reza.favouriteapp.adapter.RVAdapter
import com.reza.favouriteapp.databinding.FragmentFollowerTabBinding
import com.reza.favouriteapp.model.User
import com.reza.favouriteapp.ui.activity.MainActivity
import cz.msebera.android.httpclient.Header
import org.json.JSONArray


class FollowerTabFragment : Fragment() {
    private var _binding: FragmentFollowerTabBinding?=null
    private lateinit var username1: String
    private lateinit var photo1: String
    private val list = ArrayList<User>()


    companion object {
        private val TAG = FollowerTabFragment::class.java.simpleName
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String?) =
            FollowerTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)

                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFollower()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding= FragmentFollowerTabBinding.inflate(inflater,container,false)
        return _binding?.root
    }

    private fun getDataFollower(){

        _binding!!.progressBarFollower.visibility=View.VISIBLE
        val nameUser = arguments?.getString(ARG_USERNAME)
        AsyncHttpClient.log.d(TAG,nameUser)
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$nameUser/followers"
        client.addHeader("Authorization", MainActivity.token)
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                //_binding!!.progressBarFollower.visibility =View.VISIBLE

                val result = String(responseBody)
                try {
                    // pengambilan data
                    val responseObject = JSONArray(result)

                    for (i in 0 until responseObject.length()){
                        val item = responseObject.getJSONObject(i)
                        username1 = item.getString("login")
                        photo1 = item.getString("avatar_url")



                        val userGit = User(
                            username = username1,
                            photo = photo1
                        )
                        list.add(userGit)
                        AsyncHttpClient.log.d(TAG,username1)
                    }
                    //memanggil recycler list
                    if (responseObject.length()==0){
                        _binding!!.zeroFollowerTV.text = "$nameUser tidak diikuti siapapun"
                    }
                    showRecyclerList()


                }catch (e : Exception){

                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
                _binding!!.progressBarFollower.isVisible = false

            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal


                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()

            }
        })



    }

    private fun showRecyclerList() {

        _binding?.followerRV?.layoutManager = LinearLayoutManager(activity)
        //followingRV.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = RVAdapter(list)
        _binding?.followerRV?.adapter = listUserAdapter



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}