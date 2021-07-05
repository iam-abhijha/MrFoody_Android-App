package com.mrtechy.mrfoody.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mrtechy.mrfoody.R
import com.mrtechy.mrfoody.adapter.HomeRecyclerAdapter
import com.mrtechy.mrfoody.model.Restaurant
import com.mrtechy.mrfoody.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter

    val restaurantInfoList = arrayListOf<Restaurant>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerHome = view.findViewById(R.id.recyclerHome)

        layoutManager = LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if(ConnectionManager().checkConnectivity(activity as Context)){


            val jsonObjectRequest = object :JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                Response.Listener<JSONObject> {

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")

                        if(success){

                            val resArray = data.getJSONArray("data")

                            for(i in 0 until resArray.length()){

                                val resJsonObj = resArray.getJSONObject(i)
                                val restaurantObject = Restaurant(

                                    resJsonObj.getString("id").toInt(),
                                    resJsonObj.getString("name"),
                                    resJsonObj.getString("rating"),
                                    resJsonObj.getString("cost_for_one").toInt(),
                                    resJsonObj.getString("image_url")
                                )

                                restaurantInfoList.add(restaurantObject)
                                recyclerAdapter = HomeRecyclerAdapter(activity as Context,restaurantInfoList)

                                recyclerHome.adapter = recyclerAdapter
                                recyclerHome.layoutManager = layoutManager

                                recyclerHome.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerHome.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )

                                )


                            }

                        }
                        else{
                            Toast.makeText(activity as Context,"Error Occurred",Toast.LENGTH_LONG).show()
                        }

                    }catch (e:JSONException){
                        Toast.makeText(activity as Context, "Json Error!",Toast.LENGTH_SHORT).show()
                    }

                },Response.ErrorListener {

                    //Errors
                    Toast.makeText(activity as Context,"Volley Error!",Toast.LENGTH_SHORT).show()
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "5b0f43e4a5ef14"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        }
        else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Not Found!!!")
            dialog.setPositiveButton("Open Settings") { text, listener ->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }


            dialog.setNegativeButton("Exit"){text, listener ->

                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

}