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
            NavigationItem(1, string(R.string.nav_menu_inquire)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(2, string(R.string.nav_menu_transfer)),
            NavigationItem(3, string(R.string.nav_menu_auto_transfer)),
            NavigationItem(4, string(R.string.nav_menu_atm_smart_withdraw)),
            NavigationItem(5, string(R.string.nav_menu_withdraw_list)),

            NavigationItem(6, string(R.string.nav_menu_regist)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(7, string(R.string.nav_menu_open_accout)),
            NavigationItem(8, string(R.string.nav_menu_time_deposit)),
            NavigationItem(9, string(R.string.nav_menu_free_saving)),
            NavigationItem(10, string(R.string.nav_menu_bankbook)),
            NavigationItem(11, string(R.string.nav_menu_stock)),

            NavigationItem(12, string(R.string.nav_menu_apply_loan)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(13, string(R.string.nav_menu_cash_dough_loan)),
            NavigationItem(14, string(R.string.nav_menu_minus_backbook_loan)),
            NavigationItem(15, string(R.string.nav_menu_credit_loan)),
            NavigationItem(16, string(R.string.nav_menu_personal_loan), true),
            NavigationItem(17, string(R.string.nav_menu_house_rent_loan)),

            NavigationItem(18, string(R.string.nav_menu_foreign_remit)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(19, string(R.string.nav_menu_send_foreign_remit)),
            NavigationItem(20, string(R.string.nav_menu_retrieve_foreign_remit)),
            NavigationItem(21, string(R.string.nav_menu_foreign_remit_list)),
            NavigationItem(22, string(R.string.nav_menu_assign_bank)),

            NavigationItem(23, string(R.string.nav_menu_my)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(24, string(R.string.nav_menu_my_card)),
            NavigationItem(25, string(R.string.nav_menu_my_card_list)),
            NavigationItem(26, string(R.string.nav_menu_my_card_benefits)),

            NavigationItem(27, string(R.string.nav_menu_info)).apply { type = NavigationItem.TYPE_SEPARATOR },
            NavigationItem(28, string(R.string.nav_menu_info_event), true)
        )

        initAdapter(R.layout.navigation_menu_item, R.layout.navigation_separator_item)
        items.set(list)
    }
}