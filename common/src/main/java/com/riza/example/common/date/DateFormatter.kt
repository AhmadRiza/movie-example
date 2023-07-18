package com.riza.example.common.date

import java.util.Date

/**
 * Created by ahmadriza on 06/12/21
 */

interface DateFormatter {
    fun getDateOrNull(date: String, dateFormat: DateFormat): Date?
    fun getFormattedDate(date: Date, dateFormat: DateFormat): String
    fun getFormattedDate(date: Long, dateFormat: DateFormat): String
    fun getFormattedDateOrEmpty(
        date: String,
        originFormat: DateFormat,
        targetFormat: DateFormat
    ): String
    fun getRelativelyFormattedDate(date: Long, dateFormat: DateFormat): String
    fun checkIsValidDate(dateString: String, dateFormat: DateFormat): Boolean
}
