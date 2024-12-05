package com.fsacchi.schoolmate.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseDialog<T : ViewDataBinding> : BottomSheetDialogFragment() {

    protected lateinit var binding: T
    protected abstract val layoutRes: Int

    protected abstract fun init()

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, group, false)
        init()
        return binding.root
    }

    fun show(fragmentManager: FragmentManager) =
        apply { show(fragmentManager, javaClass.simpleName) }
}
