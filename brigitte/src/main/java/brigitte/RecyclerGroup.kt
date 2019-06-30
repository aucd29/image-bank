package brigitte

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.*
import brigitte.arch.SingleLiveEvent
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 11. 6. <p/>
 *
 * ====
 * recycler 에 animation 을 사용하지 않을려면
 *
 * val itemAnimator = ObservableField<RecyclerView.ItemAnimator?>()
 *
 * 를 viewmodel 에 선언하고 xml 에
 *
 * app:bindItemAnimator="@{model.itemAnimator}"
 *
 * 를 선언한다. 이때 itemAnimator 에 별도의 RecyclerView.ItemAnimator 를 지정하지 않으면 된다.
 * ====
 */

/** item 비교 인터페이스 */
interface IRecyclerDiff {
    fun compare(item: IRecyclerDiff): Boolean
}

/** Item 타입 비교 인터페이스 */
interface IRecyclerItem {
    var type: Int
}

/** 아이템 위치 정보 인터페이스 */
interface IRecyclerPosition {
    var position: Int
}

interface IRecyclerExpandable<T> {
    var toggle: ObservableBoolean
    var childList: List<T>

    fun toggle(list: List<T>, adapter: RecyclerView.Adapter<*>? = null) {
        var i = 0
        if (list is ArrayList) {
            if (!this.toggle.get()) {
                i = findIndex(list)

                list.addAll(i, childList)
                adapter?.notifyItemRangeInserted(i, childList.size)
            } else {
                if (adapter != null) {
                    i = findIndex(list)
                }

                list.removeAll(childList)
                adapter?.notifyItemRangeRemoved(i, childList.size)
            }
        }

        this.toggle.set(!toggle.get())
    }

    private fun findIndex(list: List<T>): Int {
        var i = 0
        if (list is ArrayList) {
            for (it in list) {
                ++i

                if (it == this) {
                    break
                }
            }
        }

        return i
    }
}

/** view holder */
class RecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var mBinding: ViewDataBinding
}

/**
 * xml 에서 event 와 data 를 binding 하므로 obtainViewModel 과 출력할 데이터를 내부적으로 알아서 설정 하도록
 * 한다.
 */
class RecyclerAdapter<T: IRecyclerDiff>(
    private val mLayouts: Array<String>
) : RecyclerView.Adapter<RecyclerHolder>() {
    companion object {
        private val mLog = LoggerFactory.getLogger(RecyclerAdapter::class.java)

        private const val METHOD_NAME_VIEW_MODEL = "setModel"
        private const val METHOD_NAME_ITEM       = "setItem"
        private const val METHOD_NAME_BIND       = "bind"

        fun bindingClassName(context: Context, layoutId: String): String {
            var classPath = context.packageName
            classPath += ".databinding."
            classPath += Character.toUpperCase(layoutId[0])

            var i = 1
            while (i < layoutId.length) {
                var c = layoutId[i]

                if (c == '_') {
                    c = layoutId[++i]
                    classPath += Character.toUpperCase(c)
                } else {
                    classPath += c
                }

                ++i
            }

            classPath += "Binding"

            return classPath
        }

        fun invokeMethod(binding: ViewDataBinding, methodName: String, argType: Class<*>, argValue: Any, log: Boolean) {
            try {
                val method = binding.javaClass.getDeclaredMethod(methodName, *arrayOf(argType))
                method.invoke(binding, *arrayOf(argValue))
            } catch (e: Exception) {
                if (log) {
                    e.printStackTrace()
                    mLog.debug("NOT FOUND : ${e.message}")
                }
            }
        }
    }

    var items: ArrayList<T> = arrayListOf()
    lateinit var viewModel: ViewModel
    var viewHolderCallback: ((RecyclerHolder, Int) -> Unit)? = null
    var isScrollToPosition = true
    var isNotifySetChanged = false

    constructor(layoutId: String) : this(arrayOf(layoutId))

    /**
     * 전달 받은 layout ids 와 IRecyclerItem 을 통해 화면에 출력해야할 layout 를 찾고
     * 해당 layout 의 RecyclerHolder 를 생성 한다.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val context = parent.context
        val layoutId = context.resources.getIdentifier(mLayouts[viewType], "layout", context.packageName)
        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)

        if (mLog.isTraceEnabled) {
            mLog.trace("LAYOUT ID : ${mLayouts[viewType]} ($layoutId)")
        }

        val classPath = bindingClassName(context, mLayouts[viewType])

        if (mLog.isTraceEnabled) {
            mLog.trace("BINDING CLASS $classPath")
        }

        val bindingClass = Class.forName(classPath)
        val method = bindingClass.getDeclaredMethod(METHOD_NAME_BIND, *arrayOf(View::class.java))
        val binding = method.invoke(null, *arrayOf(view)) as ViewDataBinding
        val vh = RecyclerHolder(view)
        vh.mBinding = binding

        return vh
    }

    /**
     * view holder 에 view model 과 item 을 설정 시킨다.
     *
     */
    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        viewModel.let { invokeMethod(holder.mBinding, METHOD_NAME_VIEW_MODEL, it.javaClass, it, false) }

        items.let { it[position].let { item ->
            when (item) {
                is IRecyclerPosition -> item.position = position
            }

            invokeMethod(holder.mBinding, METHOD_NAME_ITEM, item.javaClass, item, true)
        } }

        holder.mBinding.executePendingBindings()
        viewHolderCallback?.invoke(holder, position)
    }

//    override fun onBindViewHolder(holder: RecyclerHolder, position: Int, payloads: MutableList<Any>) {
//        if (payloads.isEmpty()) {
//            super.onBindViewHolder(holder, position, payloads)
//        } else {
//// TODO
//        }
//    }

    /**
     * 화면에 출력해야할 아이템의 총 개수를 반환 한다.
     */
    override fun getItemCount() = items.size

    /**
     * 특정 위치의 item 타입을 반환 한다.
     */
    override fun getItemViewType(position: Int): Int {
        when (val item = items.get(position)) {
            is IRecyclerItem -> return item.type
        }

        return 0
    }

    /**
     * 아이템을 설정 한다. 이때 DiffUtil.calculateDiff 를 통해 데이터의 변동 지점을
     * 알아서 찾아 변경 시켜 준다.
     *
     * FIXME [aucd29][2019. 4. 1.]
     * ----
     * 현재 문제점
     * 1. recycler item 에 ObservableBoolean 형태로 CheckBox 를 둔 상태
     * 2. checkbox 를 통해 아이템 하나를 삭제 하면 디비에서 새로운 list 를 생성해서 setItems 을 호출 함
     * 3. DiffUtil 을 통해 해당 위치이 view 를 갱신 시킴
     * 4. 이때 onBindViewHolder 의 setItem 이 호출되지는 않기에 xml 에 item 값이 이전의 데이터를 바라보고 있게 됨
     * 5. 이후 view model 의 데이터를 수정하면 view model 의 item 과 xml 의 item 이 다르기에 원하는 동작을 하지 않음
     * ----
     * 해결 방법?
     * ----
     * -> 임시로 일단 checkbox 를 호출하기 전에 notifyDataSetChanged 를 호출 함 다른 방법이 있는지 찾아봐야할 듯
     * ----
     * 잠시 생각한게 diff util 로 삭제될 데이터 위치와 추가해야할 데이터 위치를 알 수 있으므로 현재 데이터를
     * items = newItems 할게 아니고 items 내에 특정 위치를 삭제 또는 추가한 뒤 inserted 의 경우
     * notifyDataSetChanged(position) 을 호출해주면 부하가 좀 적지 않을까? 라는 생각이 듬
     * 일단 arrayList 로 하는데 이런 구조라면 linked list 가 더 적합할 듯 한 ?
     * ----
     */
    fun setItems(recycler: RecyclerView, newItems: ArrayList<T>) {
        if (items.size == 0 || isNotifySetChanged) {
            items = newItems
            notifyDataSetChanged()

            return
        }

        val oldItems = items
        val result = DiffUtil.calculateDiff(object: DiffUtil.Callback() {
            override fun getOldListSize() = oldItems.size
            override fun getNewListSize() = newItems.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldItems[oldItemPosition] == newItems[newItemPosition]

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldItems[oldItemPosition]
                val newItem = newItems[newItemPosition]

                return oldItem.compare(newItem)
            }

            // https://medium.com/mindorks/diffutils-improving-performance-of-recyclerview-102b254a9e4a
//            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
//                return super.getChangePayload(oldItemPosition, newItemPosition)
//            }
        })

        if (mLog.isDebugEnabled) {
            mLog.debug("OLD ${oldItems.hashCode()}")
            mLog.debug("NEW ${newItems.hashCode()}")
            mLog.debug("DISPATCH UPDATES TO $this")
        }

        // https://stackoverflow.com/questions/43458146/diffutil-in-recycleview-making-it-autoscroll-if-a-new-item-is-added
        result.dispatchUpdatesTo(object: ListUpdateCallback {
            private var mFirstInsert = -1

            // 데이터가 추가되었다면
            override fun onInserted(position: Int, count: Int) {
                if (mFirstInsert == -1 || mFirstInsert > position) {
                    mFirstInsert = position

                    if (isScrollToPosition) {
                        recycler.smoothScrollToPosition(mFirstInsert)
                    }
                }

                if (mLog.isDebugEnabled) {
                    mLog.debug("INSERTED (pos: $position) (cnt: $count)")
                }

                notifyItemRangeInserted(position, count)
            }

            // 데이터가 삭제 되었다면
            override fun onRemoved(position: Int, count: Int) {
                if (mLog.isDebugEnabled) {
                    mLog.debug("REMOVED (pos: $position) (cnt: $count)")
                }

                notifyItemRangeRemoved(position, count)
            }

            // 데이터 위치가 변화 되었다면
            override fun onMoved(fromPosition: Int, toPosition: Int) {
                if (mLog.isDebugEnabled) {
                    mLog.debug("MOVED (from: $fromPosition) (to: $toPosition)")
                }

                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                if (mLog.isDebugEnabled) {
                    mLog.debug("CHANGED (pos: $position) (cnt: $count)")
                }

                notifyItemRangeChanged(position, count, payload)
            }
        })

        items = newItems
    }
}

/**
 * Recycler View 에 사용될 items 정보와 adapter 를 쉽게 설정하게 만드는 ViewModel
 */
open class RecyclerViewModel<T: IRecyclerDiff>(app: Application)
    : AndroidViewModel(app), ICommandEventAware {
    companion object {
        private val mLog = LoggerFactory.getLogger(RecyclerViewModel::class.java)
    }

    override val commandEvent = SingleLiveEvent<Pair<String, Any>>()

    val items           = ObservableField<List<T>>()
    val adapter         = ObservableField<RecyclerAdapter<T>>()
    val itemTouchHelper = ObservableField<ItemTouchHelper>()
    val threshold       = 1
    var dataLoading     = false

    /**
     * adapter 에 사용될 layout 을 설정한다.
     *
     * @param ids 문자열 형태의 layout 이름
     */
    fun initAdapter(vararg ids: String) {
        val adapter = RecyclerAdapter<T>(ids.toList().toTypedArray())
        adapter.viewModel = this

        this.adapter.set(adapter)
    }

    // https://developer88.tistory.com/102
    // https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf
    // recycler 에서 item 를 long touch 하거나 특정 view 를 drag 했을때 화면 전환을 위한

    fun initItemTouchHelper(callback: ItemMovedCallback, bindingCallback: ((ViewDataBinding) -> View?)? = null) {
        itemTouchHelper.set(ItemTouchHelper(callback))

        bindingCallback?.let {
            adapter.get()?.viewHolderCallback = { holder, pos ->
                it.invoke(holder.mBinding)?.setOnTouchListener { v, ev ->
                    if (ev.action == MotionEvent.ACTION_DOWN) {
                        itemTouchHelper.get()?.startDrag(holder)
                    }

                    false
                }
            }
        }
    }

    fun snackbar(@StringRes resid: Int) = snackbar(string(resid))
    fun toast(@StringRes resid: Int) = toast(string(resid))
    fun errorLog(e: Throwable) {
        if (mLog.isDebugEnabled) {
            e.printStackTrace()
        }

        mLog.error("ERROR: ${e.message}")
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // DragCallback
    //
    ////////////////////////////////////////////////////////////////////////////////////

    inner class ItemMovedCallback(val mItemMovedListener:((fromPos: Int, toPos: Int) -> Unit)? = null)
        : ItemTouchHelper.Callback() {
        private var mLongPressDrag = false
        private var mSwipeDrag     = false

//        private var dragFrom = -1
//        private var dragTo   = -1

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags  = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder): Boolean {

            val fromPos = viewHolder.adapterPosition
            val toPos = target.adapterPosition

            if (mLog.isTraceEnabled) {
                mLog.trace("MOVE RECYCLER ITEM : $fromPos -> $toPos")
            }

//            if (dragFrom == -1) {
//                dragFrom = fromPos
//            }
//            dragTo = toPos

            Collections.swap(items.get(), fromPos, toPos)
            mItemMovedListener?.invoke(fromPos, toPos)
            adapter.get()?.notifyItemMoved(fromPos, toPos)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // swipe 로 삭제 할때
        }

        override fun isLongPressDragEnabled() = mLongPressDrag
        override fun isItemViewSwipeEnabled() = mSwipeDrag

        // https://stackoverflow.com/questions/35920584/android-how-to-catch-drop-action-of-itemtouchhelper-which-is-used-with-recycle
        // 최종적으로 drop 되었을때 디비를 바꿔볼까 싶었는데 이게 0 -> 4 값이 서로 변경되는 형태가 아니므로
        // 불가함을 =_ = 깨닳고 이동 할때마다 변경됨을 mItemMovedListener 를 통해 전달하도록 변경

//        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//            super.clearView(recyclerView, viewHolder)
//
//            if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
//                if (mLog.isDebugEnabled) {
//                    mLog.debug("ITEM MOVED FROM $dragFrom TO $dragTo")
//                }
//
//                mItemMovedListener?.invoke(dragFrom, dragTo)
//            }
//
//            dragFrom = -1
//            dragTo   = -1
//        }
    }

    fun isNextLoad(lastVisiblePos: Int): Boolean {
        if (lastVisiblePos == -1) return false

        return items.get()?.let {
            if (mLog.isDebugEnabled) {
                mLog.debug("LAST VISIBLE POS ${lastVisiblePos}\nLAST ITEM POS ${it.size}")
            }

            !dataLoading && it.size - lastVisiblePos <= threshold
        } ?: false
    }
}

inline fun <T: IRecyclerExpandable<T>> List<T>.toggleExpandableItems(type: Int,
    targetCallback: (T) -> ObservableBoolean) {

    forEach {
        if (it is IRecyclerItem && it.type == type) {
            it.childList.toggleItems(targetCallback)
        }
    }
}

class InfiniteScrollListener(val callback: (Int) -> Unit) : RecyclerView.OnScrollListener() {
    companion object {
        private val mLog = LoggerFactory.getLogger(InfiniteScrollListener::class.java)
    }

    lateinit var recycler: RecyclerView

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (mLog.isDebugEnabled) {
            mLog.debug("SCROLL STATE : $newState")
        }

    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val manager = recycler.layoutManager

        if (mLog.isDebugEnabled) {
            mLog.debug("SCROLLED : $dy")
        }

        if (dy <= 0) {
            return ;
        }

        val lastVisibleItemPosition = if (manager is LinearLayoutManager) {
            manager.findLastVisibleItemPosition()
        } else if (manager is StaggeredGridLayoutManager) {
            val positions = manager.findLastVisibleItemPositions(null)
            var position = positions[0]
            for (i in 1 until positions.size) {
                if (position < positions[i]) {
                    position = positions[i]
                }
            }

            position
        } else { -1 }

        callback.invoke(lastVisibleItemPosition)
    }
}

inline fun StaggeredGridLayoutManager.findLastVisibleItemPosition(): Int {
    val positions = findLastVisibleItemPositions(null)
    var position = positions[0]
    for (i in 1 until positions.size) {
        if (position < positions[i]) {
            position = positions[i]
        }
    }

    return position
}