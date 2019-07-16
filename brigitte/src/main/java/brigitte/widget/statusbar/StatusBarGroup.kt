package brigitte.widget.statusbar

import androidx.lifecycle.ViewModel

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-07-15 <p/>
 */

class StatusBarViewModel : ViewModel() {
    val color: StatusBarColor? = null
}

data class StatusBarColor(val color: Int, val darkColor: Int)