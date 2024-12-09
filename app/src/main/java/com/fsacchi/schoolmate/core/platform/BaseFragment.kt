package com.fsacchi.schoolmate.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.CustomToolbar
import com.fsacchi.schoolmate.core.extensions.finish
import com.fsacchi.schoolmate.core.extensions.hideSoftKeyboard

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    lateinit var binding: T
    protected abstract val layoutRes: Int

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, bundle: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, c, false)
        return binding.run {
            lifecycleOwner = this@BaseFragment
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()

        start()
    }

    abstract fun start()

    protected fun popBackStack() {
        requireView().hideSoftKeyboard()
        if (!findNavController().popBackStack()) {
            finish()
        }
    }
}
