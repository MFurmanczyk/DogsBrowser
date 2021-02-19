package com.example.dogsbrowser.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.dogsbrowser.R
import com.example.dogsbrowser.databinding.ItemDogBinding
import com.example.dogsbrowser.model.DogBreed
import com.example.dogsbrowser.util.getProgressDrawable
import com.example.dogsbrowser.util.loadImage

class DogsListAdapter (val dogsList: ArrayList<DogBreed>): RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {

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
            holder.binding.dogName.text = dogsList[position].dogBreed
            holder.binding.lifespan.text = dogsList[position].lifespan
            holder.viewContainer.setOnClickListener {
                val action = ListFragmentDirections.actionDetailFragment()
                action.dogUuid = dogsList[position].uuid
                Navigation.findNavController(it).navigate(action)
            }
        holder.binding.imageView.loadImage(dogsList[position].imageUrl, getProgressDrawable(holder.binding.imageView.context))
    }

    override fun getItemCount(): Int = dogsList.size
}