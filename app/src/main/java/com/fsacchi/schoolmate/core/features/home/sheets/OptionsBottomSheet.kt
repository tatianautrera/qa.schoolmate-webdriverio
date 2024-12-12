package com.fsacchi.schoolmate.core.features.home.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.addVerticalDivider
import com.fsacchi.schoolmate.core.extensions.toArrayList
import com.fsacchi.schoolmate.core.features.home.OptionItem
import com.fsacchi.schoolmate.core.platform.adapter.BaseAdapter
import com.fsacchi.schoolmate.core.platform.adapter.BaseDiffCallback
import com.fsacchi.schoolmate.databinding.ItemOptionsBinding
import com.fsacchi.schoolmate.databinding.OptionsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OptionsBottomSheet : BottomSheetDialogFragment() {

    private var listener: (OptionItem) -> Unit = {}
    private val adapter by lazy { OptionsAdapter() }
    private lateinit var binding: OptionsBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, saved: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.options_bottom_sheet, root, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvOptions.adapter = adapter
        binding.rvOptions.addVerticalDivider()

        adapter.submitList(arguments?.getParcelableArrayList(OPTIONS_LIST))
        adapter.rootListener = {
            dismiss()
            listener(it)
        }
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(javaClass.simpleName) == null) {
            show(manager, javaClass.simpleName)
        }
    }

    fun setListener(listener: (OptionItem) -> Unit) = apply {
        this.listener = listener
    }

    private class OptionsAdapter(override val layoutRes: Int = R.layout.item_options) :
        BaseAdapter<OptionItem, ItemOptionsBinding>(BaseDiffCallback())

    companion object {
        private const val OPTIONS_LIST = "options_list"

        fun newInstance(list: List<OptionItem>) = OptionsBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(OPTIONS_LIST, list.toArrayList())
            }
        }
    }
}
