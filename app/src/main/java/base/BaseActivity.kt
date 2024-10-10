package base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V : ViewModel> : AppCompatActivity() {

    abstract val layoutId: Int
    abstract val bindingVariable: Int

    @Inject
    lateinit var mViewModel: V

    lateinit var binding: T

    lateinit var activity: Activity

//    @Inject
//    lateinit var myDataStore: MyDataStore

    abstract fun setupObservable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    private fun performDataBinding() {
        activity = this
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.setVariable(bindingVariable, mViewModel)
        binding.executePendingBindings()
        setupObservable()
        setupUI(binding.root)
        binding.root.rootView.clearFocus()
    }

    fun setupUI(view: View) {
        if (view !is EditText) {
        }
    }


    fun apiError(code: Int, msg: String) {
        if (!checkIsSessionOut(code, msg)) {
//            NetworkConnectionDialog(this).sessionExpiredDialogShow(this,msg,getString())
        }


    }

    open fun checkIsSessionOut(code: Int, msg: String): Boolean {
        if (code == 403) {
            var userLoginMobileNumber = ""
//            myDataStore.getMobileNumber.asLiveData().observe(this) {
//                userLoginMobileNumber = it
//            }
            apiError(code, msg)
        }
        return false
    }


}


