package com.raja.myfyp.Activities

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.RadioGroup
import com.google.android.gms.common.internal.Constants
import com.raja.myfyp.LANGUAGE_POSITION_KEY
import com.raja.myfyp.PREF_NAME_LANGUAGE
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityAccessibilityBinding
import com.zeugmasolutions.localehelper.LocaleHelper
import java.util.Locale


class AccessibilityActivity : BaseActivity() {
    lateinit var binding: ActivityAccessibilityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccessibilityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences =
            getSharedPreferences(PREF_NAME_LANGUAGE, Context.MODE_PRIVATE)
        val savedPosition =
            sharedPreferences.getInt(LANGUAGE_POSITION_KEY, 0)

        if(savedPosition==0){
            binding.englishLangRadio.isChecked=true
            binding.urduLangRadio.isChecked=false
        }
        else{
            binding.englishLangRadio.isChecked=false
            binding.urduLangRadio.isChecked=true
        }

        binding.radioGroupId.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            // Check which radio button is selected
            if (checkedId == R.id.english_lang_radio) {
                sharedPreferences.edit().putInt("LANG_POS", 0).apply()

                val locale = Locale("en")
                Locale.setDefault(locale)
                val configuration: Configuration = resources.configuration
                configuration.locale = locale
                configuration.setLayoutDirection(locale)
                localeDelegate.setLocale(this, locale)
                LocaleHelper.setLocale(this, locale)

                BaseActivity().updateLocale(this as AccessibilityActivity, locale)
            } else if (checkedId == R.id.urdu_lang_radio) {
                sharedPreferences.edit().putInt("LANG_POS", 1).apply()

                val locale = Locale("ur")
                Locale.setDefault(locale)
                val configuration: Configuration = resources.configuration
                configuration.locale = locale
                configuration.setLayoutDirection(locale)
                localeDelegate.setLocale(this, locale)
                LocaleHelper.setLocale(this, locale)

                BaseActivity().updateLocale(this as AccessibilityActivity, locale)
            }
        })

        binding.backBtnImg.setOnClickListener {
            finish()
        }

    }
}