package com.example.imagebank.kakao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.imagebank.MainActivity
import com.example.imagebank.model.remote.KakaoRestSearchService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase.assertNotNull
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-28 <p/>
 *

*SAMPLE*
```
{
    "documents": [
        {
            "collection": "blog",
            "datetime": "2018-12-16T09:40:08.000+09:00",
            "display_sitename": "티스토리",
            "doc_url": "http://filternews.tistory.com/225",
            "height": 602,
            "image_url": "http://cfile23.uf.tistory.com/image/99CC573C5C159EE22F823F",
            "thumbnail_url": "https://search4.kakaocdn.net/argon/130x130_85_c/9d3F8Bwbei3",
            "width": 400
        },
        {
            "collection": "blog",
            "datetime": "2018-12-16T09:40:08.000+09:00",
            "display_sitename": "티스토리",
            "doc_url": "http://filternews.tistory.com/225",
            "height": 500,
            "image_url": "http://cfile4.uf.tistory.com/image/99C56E365C159EE32E6705",
            "thumbnail_url": "https://search4.kakaocdn.net/argon/130x130_85_c/4Key6MXTFf9",
            "width": 400
        }
    ],
    "meta": {
        "is_end": false,
        "pageable_count": 3952,
        "total_count": 647384
    }
}
```
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class ImageSearchTest {
    @Inject lateinit var rest: KakaoRestSearchService

    fun search() {
//        curl -v -X GET "https://dapi.kakao.com/v2/search/image" \
//        --data-urlencode "query=설현" \
//        -H "Authorization: KakaoAK e302331ef568c1a4af2053c77eef1b89"
//
//        rest.image("설현")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                println("TOTAL : ${it.meta.total_count}")
//                println("")
//
//                assertNotNull(it)
//            }
    }


    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)
}