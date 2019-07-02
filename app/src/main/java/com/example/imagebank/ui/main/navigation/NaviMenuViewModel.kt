package com.example.imagebank.ui.main.navigation

import android.app.Application
import brigitte.RecyclerViewModel
import brigitte.string
import com.example.imagebank.R
import com.example.imagebank.model.local.NavigationItem
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-06-30 <p/>
 */

class NaviMenuViewModel @Inject constructor(application: Application
) : RecyclerViewModel<NavigationItem>(application) {

    companion object {
        const val CMD_NAV_MENU_EVENT = "cmd-nav-menu-event"
    }

    init {
        val list = arrayListOf(
            NavigationItem(string(R.string.nav_menu_inquire)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(string(R.string.nav_menu_transfer)),
            NavigationItem(string(R.string.nav_menu_auto_transfer)),
            NavigationItem(string(R.string.nav_menu_atm_smart_withdraw)),
            NavigationItem(string(R.string.nav_menu_withdraw_list)),

            NavigationItem(string(R.string.nav_menu_regist)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(string(R.string.nav_menu_open_accout)),
            NavigationItem(string(R.string.nav_menu_time_deposit)),
            NavigationItem(string(R.string.nav_menu_free_saving)),
            NavigationItem(string(R.string.nav_menu_bankbook)),
            NavigationItem(string(R.string.nav_menu_stock)),

            NavigationItem(string(R.string.nav_menu_apply_loan)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(string(R.string.nav_menu_cash_dough_loan)),
            NavigationItem(string(R.string.nav_menu_minus_backbook_loan)),
            NavigationItem(string(R.string.nav_menu_credit_loan)),
            NavigationItem(string(R.string.nav_menu_personal_loan), true),
            NavigationItem(string(R.string.nav_menu_house_rent_loan)),

            NavigationItem(string(R.string.nav_menu_foreign_remit)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(string(R.string.nav_menu_send_foreign_remit)),
            NavigationItem(string(R.string.nav_menu_retrieve_foreign_remit)),
            NavigationItem(string(R.string.nav_menu_foreign_remit_list)),
            NavigationItem(string(R.string.nav_menu_assign_bank)),

            NavigationItem(string(R.string.nav_menu_my)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(string(R.string.nav_menu_my_card)),
            NavigationItem(string(R.string.nav_menu_my_card_list)),
            NavigationItem(string(R.string.nav_menu_my_card_benefits)),

            NavigationItem(string(R.string.nav_menu_info)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(string(R.string.nav_menu_info_event), true)
        )

        initAdapter("navigation_menu_item", "navigation_separator_item")
        items.set(list)
    }
}