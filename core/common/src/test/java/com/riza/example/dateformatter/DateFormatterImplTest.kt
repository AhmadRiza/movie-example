package com.riza.example.dateformatter

import com.riza.example.common.R
import com.riza.example.common.date.DateFormat
import com.riza.example.common.date.DateFormatterImpl
import com.riza.example.common.locale.LocaleProvider
import com.riza.example.common.time.TimeProvider
import com.riza.example.common.time.TimeProviderImpl
import com.riza.example.common.util.ResourceProvider
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Created by ahmadriza on 06/12/21
 */

class DateFormatterImplTest {
    private val localeProvider: LocaleProvider = mockk()
    private val resourceProvider: ResourceProvider = mockk()
    private val timeProvider: TimeProvider = TimeProviderImpl()
    private val dateFormatter = DateFormatterImpl(
        localeProvider = localeProvider,
        resourceProvider = resourceProvider,
        timeProvider = timeProvider
    )

    @Before
    fun setUp() {
        clearAllMocks()
        every {
            localeProvider.defaultLocale
        } returns Locale("in", "ID")
        every {
            localeProvider.defaultTimeZone
        } returns kotlinx.datetime.TimeZone.of("Asia/Jakarta")
    }

    @Test
    fun `getDateOrNull, date is valid, should return non null date`() {
        val result = dateFormatter.getDateOrNull("2021-12-06", DateFormat.YEAR_MONTH_DAY_DASH)
        assertNotNull(result)
    }

    @Test
    fun `getDateOrNull, date isn't valid, should return null and not crash`() {
        val result = dateFormatter.getDateOrNull("2021/12/06", DateFormat.YEAR_MONTH_DAY_DASH)
        assertNull(result)
    }

    @Test
    fun `getFormattedDate, correct date input, should return correct result`() {
        val date = dateFormatter.getDateOrNull(
            "2021-01-11T12:00:02+00:00",
            DateFormat.ISO_TIMESTAMP
        )!!

        assertEquals(
            "11 Januari 2021 - 19:00",
            dateFormatter.getFormattedDate(date, DateFormat.DATE_TIME)
        )

        assertEquals(
            "11 Jan 2021 19:00",
            dateFormatter.getFormattedDate(date, DateFormat.DATE_TIME_2)
        )

        assertEquals(
            "11 Januari 2021",
            dateFormatter.getFormattedDate(date, DateFormat.DATE_ONLY)
        )

        assertEquals(
            "2021-01-11",
            dateFormatter.getFormattedDate(date, DateFormat.YEAR_MONTH_DAY_DASH)
        )

        assertEquals(
            "2021/01/11",
            dateFormatter.getFormattedDate(date, DateFormat.YEAR_MONTH_DAY_SLASH)
        )

        assertEquals(
            "11",
            dateFormatter.getFormattedDate(date, DateFormat.DAY_ONLY)
        )
    }

    @Test
    fun `getFormattedDateOrEmpty, should return correct result`() {
        val actual = dateFormatter.getFormattedDateOrEmpty(
            date = "2021-01-11T12:00:02+00:00",
            originFormat = DateFormat.ISO_TIMESTAMP,
            targetFormat = DateFormat.DATE_TIME
        )
        val expected = "11 Januari 2021 - 19:00"
        assertEquals(expected, actual)
    }

    @Test
    fun `getFormattedDate, 0 Long input, should return minimals standard date`() {
        val actual = dateFormatter.getFormattedDate(0L, DateFormat.DATE_ONLY)
        val expected = "01 Januari 1970"
        assertEquals(expected, actual)
    }

    @Test
    fun `getRelativeFormattedDate, should return correct just now`() {

        every {
            resourceProvider.getString(R.string.date_just_now)
        } returns "Baru saja"

        val now = System.currentTimeMillis()

        assertEquals(
            "Baru saja",
            dateFormatter.getRelativelyFormattedDate(now, DateFormat.DATE_TIME)
        )
    }

    @Test
    fun `getRelativeFormattedDate, should return correct minutes`() {

        every {
            resourceProvider.getString(R.string.date_minute_ago, "3")
        } returns "3 menit lalu"

        val now = System.currentTimeMillis()
        val past = now - 3 * 60000

        assertEquals(
            "3 menit lalu",
            dateFormatter.getRelativelyFormattedDate(past, DateFormat.DATE_TIME)
        )
    }

    @Test
    fun `getRelativeFormattedDate, should return correct hours`() {

        every {
            resourceProvider.getString(R.string.date_hour_ago, "3")
        } returns "3 jam lalu"

        val now = System.currentTimeMillis()
        val past = now - 3 * 60 * 60000

        assertEquals(
            "3 jam lalu",
            dateFormatter.getRelativelyFormattedDate(past, DateFormat.DATE_TIME)
        )
    }

    @Test
    fun `getRelativeFormattedDate, should return correct yesterday`() {

        every {
            resourceProvider.getString(R.string.date_yesterday)
        } returns "Kemarin"

        val now = System.currentTimeMillis()
        val past = now - 25 * 60 * 60000

        assertEquals(
            "Kemarin",
            dateFormatter.getRelativelyFormattedDate(past, DateFormat.DATE_TIME)
        )
    }

    @Test
    fun `getRelativeFormattedDate, should return correct days ago`() {
        every {
            resourceProvider.getString(R.string.date_day_ago, "5")
        } returns "5 hari lalu"

        val now = System.currentTimeMillis()
        val past = now - 5 * 24 * 60 * 60000

        assertEquals(
            "5 hari lalu",
            dateFormatter.getRelativelyFormattedDate(past, DateFormat.DATE_TIME)
        )
    }

    @Test
    fun `getRelativeFormattedDate, should return correct over time`() {
        val overtimePast = dateFormatter.getDateOrNull(
            "2021-01-11T12:00:00+00:00",
            DateFormat.ISO_TIMESTAMP
        )!!.time

        assertEquals(
            "11 Januari 2021 - 19:00",
            dateFormatter.getRelativelyFormattedDate(overtimePast, DateFormat.DATE_TIME)
        )
    }

    @Test
    fun `checkIsValidDate with correct format, should return true`() {
        val date = "2022-01-25"

        assertTrue(
            dateFormatter.checkIsValidDate(date, DateFormat.YEAR_MONTH_DAY_DASH)
        )
    }

    @Test
    fun `checkIsValidDate with incorrect date, should return false`() {
        val date = "2022-25-25"

        assertFalse(
            dateFormatter.checkIsValidDate(date, DateFormat.YEAR_MONTH_DAY_DASH)
        )
    }

    @Test
    fun `checkIsValidDate with incorrect format, should return false`() {
        val date = "2022/01/25"

        assertFalse(
            dateFormatter.checkIsValidDate(date, DateFormat.YEAR_MONTH_DAY_DASH)
        )
    }

    @Test
    fun timeZoneTest() {
        val dateJakarta = dateFormatter.getDateOrNull(
            "2021-01-11T07:00:00+07:00",
            DateFormat.ISO_TIMESTAMP
        )
        val calendarUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            time = dateJakarta!!
        }

        assertEquals(
            0,
            calendarUtc.get(Calendar.HOUR)
        )
    }

    @Test
    fun timeZoneISO2Test() {
        val dateUtc = dateFormatter.getDateOrNull(
            "2023-07-10T17:00:13.000Z",
            DateFormat.ISO_TIMESTAMP_2
        )
        val calendarUtc = Calendar.getInstance().apply {
            time = dateUtc
        }

        assertEquals(
            17,
            calendarUtc.get(Calendar.HOUR_OF_DAY)
        )
    }
}
