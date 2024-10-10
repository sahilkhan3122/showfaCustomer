package base

import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

open class BaseViewModel<N> : ViewModel() {

    lateinit var mViewModel: WeakReference<N>

    fun getNavigator():N?{
        return mViewModel.get()
    }

    fun setNavigator(navigator: N) {
        mViewModel = WeakReference(navigator)

    }

}