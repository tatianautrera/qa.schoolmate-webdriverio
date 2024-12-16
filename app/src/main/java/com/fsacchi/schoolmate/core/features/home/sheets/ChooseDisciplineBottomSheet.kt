package com.fsacchi.schoolmate.core.features.home.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.TextDrawable
import com.fsacchi.schoolmate.core.extensions.addVerticalDivider
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.extensions.toArrayList
import com.fsacchi.schoolmate.core.extensions.toEmoji
import com.fsacchi.schoolmate.core.features.home.OptionItem
import com.fsacchi.schoolmate.core.platform.adapter.BaseAdapter
import com.fsacchi.schoolmate.core.platform.adapter.BaseDiffCallback
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.databinding.ChooseDisciplineBottomSheetBinding
import com.fsacchi.schoolmate.databinding.ItemChooseDisciplineBinding
import com.fsacchi.schoolmate.databinding.ItemOptionsBinding
import com.fsacchi.schoolmate.databinding.OptionsBottomSheetBinding
import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseDisciplineBottomSheet : BottomSheetDialogFragment() {

    private var listener: (DisciplineModel) -> Unit = {}
    private val adapter by lazy { ChooseDisciplineAdapter() }
    private lateinit var binding: ChooseDisciplineBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, saved: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.choose_discipline_bottom_sheet, root, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvOptions.adapter = adapter
        binding.rvOptions.addVerticalDivider()

        adapter.submitList(arguments?.getParcelableArrayList(DISCIPLINE_LIST))
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

    fun setListener(listener: (DisciplineModel) -> Unit) = apply {
        this.listener = listener
    }

    private class ChooseDisciplineAdapter(override val layoutRes: Int = R.layout.item_choose_discipline) :
        BaseAdapter<DisciplineModel, ItemChooseDisciplineBinding>(BaseDiffCallback()) {

        override fun ItemChooseDisciplineBinding.bind(item: DisciplineModel) {
            tvDisciplineName.text = item.name.capitalizeFirstLetter()

            val emojiString = item.emoji.toInt().toEmoji()
            val emojiDrawable = TextDrawable(emojiString, root.context)
            ivEmoji.setImageDrawable(emojiDrawable)
            tvTeacher.text = "Prof. ${item.teacher.capitalizeFirstLetter()}"
            }
        }

    companion object {
        private const val DISCIPLINE_LIST = "discipline_list"

        fun newInstance(list: List<DisciplineModel>) = ChooseDisciplineBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(DISCIPLINE_LIST, list.toArrayList())
            }
        }
    }
}
