package com.example.imagebank.espresso

import androidx.test.espresso.IdlingRegistry
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.example.imagebank.MainActivity
import com.example.imagebank.R
import com.example.imagebank.common.FetchingIdlingResource
import com.example.imagebank.common.hasRecyclerViewItem
import com.example.imagebank.common.performClick
import org.junit.*
import org.junit.runner.RunWith

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-04 <p/>
 */

//@RunWith(AndroidJUnit4ClassRunner::class)
@RunWith(AndroidJUnit4::class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class SearchTest {

    companion object {
        @ClassRule
        @JvmField
        val grantExternalStoragePermissionRule: GrantPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    private val mFetchingIdlingResource = FetchingIdlingResource()

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(mFetchingIdlingResource)
        mActivityTestRule.activity
    }

    @Test
    fun search() {
        performClick(R.id.search)

        // https://medium.com/@elye.project/instrumental-test-better-espresso-without-sleep-d3391b19a581
        Thread.sleep(3000)
        //https://stackoverflow.com/questions/36399787/how-to-count-recyclerview-items-with-espresso
        hasRecyclerViewItem(R.id.search_recycler)
    }

}