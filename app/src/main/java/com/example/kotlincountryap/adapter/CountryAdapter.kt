package com.example.kotlincountryap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincountryap.R
import com.example.kotlincountryap.databinding.ItemCountryBinding
import com.example.kotlincountryap.model.ClickedInterface
import com.example.kotlincountryap.model.Country
import com.example.kotlincountryap.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.item_country.view.*


class CountryAdapter(val countryList: ArrayList<Country>) : RecyclerView.Adapter<CountryAdapter.Holder>() ,ClickedInterface{

    class Holder(var view: ItemCountryBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // val view=LayoutInflater.from(parent.context).inflate(R.layout.item_country,parent,false)
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemCountryBinding>(
            inflater,
            R.layout.item_country,
            parent,
            false
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.country = countryList[position]

        holder.view.listener= this


        /*

        holder.itemView.name.text=countryList.get(position).countryNAme
        holder.itemView.region.text=countryList.get(position).countryRegion

        holder.itemView.setOnClickListener {
            val action=FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }

        holder.itemView.imageview.downloadFromUrl(countryList[position].imageUrl,
            placeHolderProgressBar(holder.itemView.context))

                                                                                            */
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun updateCountryList(newCountryList: List<Country>) {

        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun clicked(v: View) {
        val uuid = v.countryUuidText.text.toString().toInt()
        val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid)
        Navigation.findNavController(v).navigate(action)
    }


}