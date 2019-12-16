package org.zornco.ra_playlist_maker.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import org.zornco.ra_playlist_maker.R
import androidx.preference.Preference


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            val listPreference = findPreference<ListPreference>("list_preference_1")!!
            setListPreferenceData(listPreference)

            listPreference.setOnPreferenceClickListener {
                    setListPreferenceData(listPreference)
                    false
            }
        }

        private fun setListPreferenceData(lp: ListPreference) {
            val entries = DataHolder.storageRoots.toTypedArray<CharSequence>()
            val entryValues = arrayOf<CharSequence>("0", "1")
            lp.entries = entries
            lp.entryValues = entries
            lp.setDefaultValue(DataHolder.storageRoots[0])
        }
    }
}