package com.example.imagebank.kakao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.imagebank.MainActivity
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-28 <p/>
 */

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class VideoSearchTest {

    /*
    {
	"documents": [
	    {
            "author": "M2",
            "datetime": "2018-05-31T19:12:47.000+09:00",
            "play_time": 251,
            "thumbnail": "https://search3.kakaocdn.net/argon/138x78_80_pr/9MY6kuvWARe",
            "title": "[MPD직캠] 에이오에이 설현 직캠 4K '빙글뱅글(Bingle Bangle)' (AOA Seol Hyun FanCam) | @MCOUNTDOWN_2018.5.31",
            "url": "http://www.youtube.com/watch?v=njmYkmMBrxs"
        }, {
            "author": "M2",
            "datetime": "2016-05-19T19:28:00.000+09:00",
            "play_time": 196,
            "thumbnail": "https://search1.kakaocdn.net/argon/138x78_80_pr/583QISb9pYq",
            "title": "[MPD직캠] 에이오에이 설현 직캠 Good Luck AOA Seol Hyun Fancam @엠카운트다운_160519",
            "url": "http://www.youtube.com/watch?v=hgvshqzQke4"
        }],
        "meta": {
            "is_end": false,
            "pageable_count": 800,
            "total_count": 8406
        }
    }

     */

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)
}