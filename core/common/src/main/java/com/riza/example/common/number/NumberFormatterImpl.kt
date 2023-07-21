package com.riza.example.common.number

import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.log10

/**
 * Created by ahmadriza on 16/08/22.
 */
class NumberFormatterImpl @Inject constructor() : NumberFormatter {

    override fun prettyCount(number: Long): String {
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
        val value = floor(log10(number.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.0").format(
                number / Math.pow(
                    10.0,
                    (base * 3).toDouble()
                )
            ) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(number)
        }
    }
}
