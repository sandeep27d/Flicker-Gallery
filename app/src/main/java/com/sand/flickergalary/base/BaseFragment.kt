package com.sand.flickergalary.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel<*, *, *>>() : Fragment() {

    lateinit var binding: T
    protected val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory { createCustomVm() })
            .get((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>)
    }

    private fun viewModelFactory(f: () -> BaseViewModel<*, *, *>?): ViewModelProvider.Factory? {
        var vm = f()
        return if (vm != null) {
            Factory(vm)
        } else {
            null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, contentLayout(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        onViewReady(binding, viewModel, savedInstanceState)
        return binding.root
    }

    // Abstract Functions
    abstract fun contentLayout(): Int
    abstract fun onViewReady(binding: T, vm: VM, savedInstanceState: Bundle?)

    // Extra Functionality
    open fun createCustomVm(): VM? = null

    class Factory(private val vm: BaseViewModel<*, *, *>) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(aClass: Class<T>): T = vm as T
    }
}