package com.example.dogsbrowser.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.contentValuesOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.dogsbrowser.R
import com.example.dogsbrowser.databinding.ItemDogBinding
import com.example.dogsbrowser.model.DogBreed
import com.example.dogsbrowser.util.getProgressDrawable
import com.example.dogsbrowser.util.loadImage

class DogsListAdapter (val dogsList: ArrayList<DogBreed>): RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DogClickListener {

    class DogViewHolder(var viewContainer: View): RecyclerView.ViewHolder(viewContainer) {

        val binding: ItemDogBinding by lazy { ItemDogBinding.bind(viewContainer) }

    }

    fun updateDogList(newDogList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.binding.dog = dogsList[position]
        holder.binding.listener = this
    }

    override fun getItemCount(): Int = dogsList.size

    override fun onDogClicked(v: View, dogUuid: Int) {
        val action = ListFragmentDirections.actionDetailFragment()
        action.dogUuid = dogUuid
        Navigation.findNavController(v).navigate(action)

    }
}