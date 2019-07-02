package com.example.imagebank

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun dateStringToUnixTime() {
        //https://stackoverflow.com/questions/20238280/date-in-to-utc-format-java
        //https://stackoverflow.com/questions/6993365/convert-string-date-into-timestamp-in-android
        val datestr = "2018-12-16T09:40:08.000+09:00"
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        val date = formatter.parse(datestr)

        assertEquals(1544920808000, date.time)
    }
}
