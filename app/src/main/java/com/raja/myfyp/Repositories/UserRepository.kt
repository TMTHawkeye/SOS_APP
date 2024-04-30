package com.raja.myfyp.Repositories

import android.content.Context
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

class UserRepository(val context: Context) {
    private val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    fun getCountryCodes(callback: (List<String>) -> Unit) {
        val countryCodes = ArrayList<String>()

        val locales = Locale.getAvailableLocales()

        for (locale in locales) {
            val countryCode = locale.country
            if (countryCode.isNotEmpty() && !countryCodes.contains(countryCode)) {
                val phoneCode = phoneNumberUtil.getCountryCodeForRegion(countryCode).toString()
                countryCodes.add("+$phoneCode")
            }
        }

        val distinctCountryCodes = countryCodes.distinct().sorted()
        callback.invoke(distinctCountryCodes)

    }
}
