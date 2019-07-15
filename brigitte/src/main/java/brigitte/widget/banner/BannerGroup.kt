package brigitte.widget.banner

import android.app.Application
import brigitte.BasePagerAdapter
import brigitte.CommandEventViewModel
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-03 <p/>
 */

class BannerViewModel @Inject constructor(application: Application

) : CommandEventViewModel(application) {

    companion object {
        private val mLog = LoggerFactory.getLogger(BannerViewModel::class.java)
    }

}

//class BannerPagerAdapter<T: >: BasePagerAdapter() {
//    override fun getCount(): Int {
//    }
//}