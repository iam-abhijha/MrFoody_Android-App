package com.mrtechy.mrfoody.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.mrtechy.mrfoody.R
import com.mrtechy.mrfoody.model.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(
    val context:Context,
    private val itemList: ArrayList<Restaurant>
): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.txtResName.text = restaurant.name
        holder.txtResRating.text = restaurant.rating
        holder.txtResPrice.text = restaurant.cost_for_one.toString()

       Picasso.get().load(restaurant.image_url).error(R.drawable.secondary_icon).into(holder.imgRes);

        holder.linearLayoutRow.setOnClickListener{
            Toast.makeText(context,"Clicked ${holder.txtResName.text}",Toast.LENGTH_SHORT).show()
        }

    }

    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtResName:TextView = view.findViewById(R.id.txtResName)
        val txtResRating :TextView = view.findViewById(R.id.txtResRating)
        val txtResPrice:TextView = view.findViewById(R.id.txtResPrice)
        val imgRes :ImageView = view.findViewById(R.id.imgRes)
        val linearLayoutRow:LinearLayout = view.findViewById(R.id.linerLayoutRow)

    }


}
