package com.riza.example.common.date

/**
 * Created by ahmadriza on 06/12/21
 */

enum class DateFormat(val format: String) {
    DATE_TIME("dd MMMM yyyy - HH:mm"),
    DATE_TIME_2("dd MMM yyyy HH:mm"),
    DATE_ONLY("dd MMMM yyyy"),
    DAY_ONLY("dd"),
    MONTH_YEAR("MMMM yyyy"),
    ISO_TIMESTAMP("yyyy-MM-dd'T'HH:mm:ssXXX"),
    ISO_TIMESTAMP_2("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    YEAR_MONTH_DAY_DASH("yyyy-MM-dd"),
    YEAR_MONTH_DAY_SLASH("yyyy/MM/dd"),
}
