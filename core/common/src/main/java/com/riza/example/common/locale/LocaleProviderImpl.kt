package com.riza.example.common.locale

import java.util.Locale
import javax.inject.Inject
import kotlinx.datetime.TimeZone

class LocaleProviderImpl @Inject constructor() : LocaleProvider {

    override val defaultLocale: Locale
        get() = Locale("en", "ID")

    override val defaultTimeZone: TimeZone
        get() = TimeZone.currentSystemDefault()

    override fun setDefaultLocale(locale: Locale) {
        Locale.setDefault(locale)
    }
}
