package com.riza.example.common.date

import android.annotation.SuppressLint
import com.riza.example.common.R
import com.riza.example.common.locale.LocaleProvider
import com.riza.example.common.time.TimeProvider
import com.riza.example.common.util.ResourceProvider
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Created by ahmadriza on 06/12/21
 */
@Singleton
@SuppressLint("NewApi")
class DateFormatterImpl @Inject constructor(
    private val localeProvider: LocaleProvider,
    private val resourceProvider: ResourceProvider,
    private val timeProvider: TimeProvider
) : DateFormatter {

    override fun getDateOrNull(date: String, dateFormat: DateFormat): Date? {
        return try {
            val dateFormatter = SimpleDateFormat(dateFormat.format, localeProvider.defaultLocale)
            dateFormatter.parse(date)
        } catch (e: ParseException) {
            null
        }
    }

    override fun getFormattedDate(date: Date, dateFormat: DateFormat): String {
        return date.toInstant().toKotlinInstant().toDateTimeString(dateFormat.format)
    }

    override fun getFormattedDate(date: Long, dateFormat: DateFormat): String {
        var time = date

        if (time < 1_000_000_000_000L) {
            time *= 1000
        }

        return getFormattedDate(Date(time), dateFormat)
    }

    override fun getFormattedDateOrEmpty(
        date: String,
        originFormat: DateFormat,
        targetFormat: DateFormat
    ): String {
        val originDate = getDateOrNull(date, originFormat)
        return originDate?.let { getFormattedDate(it, targetFormat) }.orEmpty()
    }

    override fun getRelativelyFormattedDate(date: Long, dateFormat: DateFormat): String {
        val secondMillis = 1000
        val minuteMillis = 60 * secondMillis
        val hourMillis = 60 * minuteMillis
        val dayMillis = 24 * hourMillis
        val now = timeProvider.currentTimeInMillis
        var time = date

        if (time < 1_000_000_000_000L) {
            time *= 1000
        }

        if (time > now || time < 0) {
            return getFormattedDate(time, dateFormat)
        }

        val diff = now - time

        return when {
            diff < minuteMillis -> resourceProvider.getString(R.string.date_just_now)
            diff < 60 * minuteMillis -> resourceProvider.getString(
                R.string.date_minute_ago,
                (diff / minuteMillis).toString()
            )
            diff < 24 * hourMillis -> resourceProvider.getString(
                R.string.date_hour_ago,
                (diff / hourMillis).toString()
            )
            diff < 48 * hourMillis -> resourceProvider.getString(R.string.date_yesterday)
            diff < 7 * dayMillis -> resourceProvider.getString(
                R.string.date_day_ago,
                (diff / dayMillis).toString()
            )
            else -> getFormattedDate(time, dateFormat)
        }
    }

    override fun checkIsValidDate(dateString: String, dateFormat: DateFormat): Boolean {
        val date = getDateOrNull(dateString, dateFormat)
        val dateComparison = date?.let { getFormattedDate(it, dateFormat) }.orEmpty()

        return dateString == dateComparison
    }

    private fun Instant.toDateTimeString(formatPattern: String): String {
        val localDatetime = toLocalDateTime(localeProvider.defaultTimeZone)
        val formatter = DateTimeFormatter.ofPattern(formatPattern, localeProvider.defaultLocale)
        return formatter.format(localDatetime.toJavaLocalDateTime())
    }
}
