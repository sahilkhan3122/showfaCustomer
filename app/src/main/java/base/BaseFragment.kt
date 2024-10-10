package base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, V : ViewModel> : Fragment() {

    abstract val layoutId: Int
    abstract val bindingVariable: Int


    @Inject
    lateinit var viewModel: V

    lateinit var binding: T

    @Inject
    lateinit var activity: Activity

    abstract fun setupObservable()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        performingDataBinding(inflater, container)
        return binding.root
    }

    fun performingDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.setVariable(bindingVariable, viewModel)
        binding.executePendingBindings()
        activity = requireActivity()
        setUpUi(binding.root)
        setupObservable()
    }

    private fun setUpUi(view: View) {

    }

}