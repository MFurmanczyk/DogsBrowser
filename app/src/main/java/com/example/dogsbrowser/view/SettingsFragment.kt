package com.example.dogsbrowser.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.dogsbrowser.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}