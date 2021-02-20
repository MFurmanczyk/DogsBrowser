package com.example.dogsbrowser.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.dogsbrowser.R
import com.example.dogsbrowser.databinding.FragmentDetailBinding
import com.example.dogsbrowser.model.DogPalette
import com.example.dogsbrowser.util.getProgressDrawable
import com.example.dogsbrowser.util.loadImage
import com.example.dogsbrowser.viewmodel.DetailsViewModel
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {

    private var dogUuid = 0

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }

        viewModel.setDog(dogUuid)

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.dog.observe(viewLifecycleOwner, {
            binding.dog = it
            it.imageUrl?.let {
                setBackgroundColor(it)
            }
        })
    }

    private fun setBackgroundColor(url: String) {
        Glide.with(this).asBitmap().load(url).into(object: CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                Palette.from(resource).generate {
                    val intColor = it?.lightMutedSwatch?.rgb ?: 0
                    val palette = DogPalette(intColor)
                    binding.palette = palette
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                //TODO("Not yet implemented")
            }

        })
    }
}