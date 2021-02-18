package com.example.dogsbrowser.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.dogsbrowser.R
import com.example.dogsbrowser.databinding.FragmentDetailBinding
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

        viewModel.setDog()
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.dog.observe(viewLifecycleOwner, {
            it?.let {
                binding.dogName.text = it.dogBreed
                binding.dogLifespan.text = it.lifespan
                binding.dogPurpose.text = it.bredFor
                binding.dogTemperament.text = it.temperament
            }
        })
    }
}