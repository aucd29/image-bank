package com.example.imagebank.ui.main.navigation

import android.app.Application
import androidx.databinding.ObservableInt
import brigitte.RecyclerViewModel
import brigitte.string
import com.example.imagebank.R
import com.example.imagebank.model.local.NavigationGridItem
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke
 * Choi</a> on 2019-06-30 <p/>
 */

class NaviGridViewModel @Inject constructor(application: Application
) : RecyclerViewModel<NavigationGridItem>(application) {

    companion object {
        const val CMD_NAV_GRID_EVENT = "cmd-nav-grid-event"
    }

    val gridCount = ObservableInt(4)

    init {
        val list = arrayListOf<NavigationGridItem>()

        list.add(NavigationGridItem(string(R.string.nav_grid_service_center), R.drawable.ic_mood_black_24dp))
        list.add(NavigationGridItem(string(R.string.nav_grid_security), R.drawable.ic_lock_outline_black_24dp))
        list.add(NavigationGridItem(string(R.string.nav_grid_notice), R.drawable.ic_notifications_none_black_24dp))
        list.add(NavigationGridItem(string(R.string.nav_grid_setting), R.drawable.ic_settings_black_24dp))

        initAdapter("navigation_grid_item")
        items.set(list)
    }
}