package com.example.dogsbrowser.view

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.dogsbrowser.R
import com.example.dogsbrowser.databinding.FragmentDetailBinding
import com.example.dogsbrowser.databinding.SendSmsDialogBinding
import com.example.dogsbrowser.model.DogBreed
import com.example.dogsbrowser.model.DogPalette
import com.example.dogsbrowser.model.SMSInfo
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

    private var sendSmsStarted = false

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by activityViewModels()

    private var currentDog: DogBreed? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
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
            currentDog = it
            binding.dog = currentDog
            it.imageUrl?.let {
                setBackgroundColor(it)
            }
        })
    }

    private fun setBackgroundColor(url: String) {
        Glide.with(this).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog breed!")
                intent.putExtra(Intent.EXTRA_TEXT, "${currentDog?.dogBreed} is bred for ${currentDog?.bredFor}. Its lifespan is about ${currentDog?.lifespan}.")
                intent.putExtra(Intent.EXTRA_STREAM, currentDog?.imageUrl)
                startActivity(Intent.createChooser(intent, "Share with"))
            }
            R.id.action_send_sms -> {
                sendSmsStarted = true
                (activity as MainActivity).checkSmsPermission()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean) {
        if (sendSmsStarted && permissionGranted) {
            context?.let {
                val smsInfo = SMSInfo(
                    "",
                    "${currentDog?.dogBreed} is bred for ${currentDog?.bredFor}. Its lifespan is about ${currentDog?.lifespan}.",
                    currentDog?.imageUrl
                )
                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                    LayoutInflater.from(it),
                    R.layout.send_sms_dialog,
                    null,
                    false
                )

                dialogBinding.smsInfo = smsInfo

                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") { _, _ ->
                        if(!dialogBinding.smsDestination.text.isNullOrEmpty()) {
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }

                    }
                    .setNegativeButton("Cancel") { _, _ ->

                    }
                    .show()

            }
        }
    }

    private fun sendSms(smsInfo: SMSInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null, smsInfo.text, pendingIntent, null)
    }
}