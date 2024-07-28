package com.fatihden.myapplication.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fatihden.myapplication.databinding.CardViewBinding

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.DetailHolder>() {

    class DetailHolder(
        val binding:CardViewBinding
    ) : RecyclerView.ViewHolder(binding.root)
    {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: DetailHolder, position: Int) {
        TODO("Not yet implemented")
    }


}