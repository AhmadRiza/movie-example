package com.riza.example.dateformatter

import com.riza.example.common.number.NumberFormatterImpl
import org.junit.Assert
import org.junit.Test

/**
 * Created by ahmadriza on 16/08/22.
 * Copyright (c) 2022 Kitabisa. All rights reserved.
 */

class NumberFormatterTest {

    private val numberFormatter = NumberFormatterImpl()

    @Test
    fun shouldShowCorrectOutput() {

        Assert.assertEquals(
            "1.9k",
            numberFormatter.prettyCount(1900),
        )
        Assert.assertEquals(
            "19.0k",
            numberFormatter.prettyCount(19000)
        )

        Assert.assertEquals(
            "19.0M",
            numberFormatter.prettyCount(19_000_000),
        )

        Assert.assertEquals(
            "190",
            numberFormatter.prettyCount(190)
        )
    }
}
