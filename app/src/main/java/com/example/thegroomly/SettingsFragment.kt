package com.example.thegroomly

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.recyclerview.widget.RecyclerView

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Dark mode toggle
        findPreference<SwitchPreferenceCompat>("pref_dark_mode")
            ?.setOnPreferenceChangeListener { _, newValue ->
                val on = newValue as Boolean
                AppCompatDelegate.setDefaultNightMode(
                    if (on) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
                true
            }

        // Jezik
        val langPref = findPreference<ListPreference>("pref_language")
        langPref?.setOnPreferenceChangeListener { pref, _ ->
            pref.summary = (pref as ListPreference).entry
            true
        }
        langPref?.summary = langPref?.entry
    }

    override fun onCreateRecyclerView(
        inflater: android.view.LayoutInflater,
        parent: android.view.ViewGroup,
        savedInstanceState: Bundle?
    ): RecyclerView {
        val rv = super.onCreateRecyclerView(inflater, parent, savedInstanceState)
        // dodaj malo razmaka na vrhu
        val top = resources.getDimensionPixelSize(R.dimen.settings_top_padding)
        rv.setPadding(0, top, 0, 0)
        rv.clipToPadding = false
        return rv
    }
}
