package com.fsacchi.schoolmate.core.features.home.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.data.model.job.TypeJob
import com.fsacchi.schoolmate.databinding.TypeJobBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TypeJobBottomSheet : BottomSheetDialogFragment() {

    private var listener: (TypeJob) -> Unit = {}
    private lateinit var binding: TypeJobBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, saved: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.type_job_bottom_sheet, root, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivClose.clickListener {
            dismiss()
        }

        binding.cvJob.clickListener {
            dismiss()
            listener.invoke(TypeJob.Job)
        }

        binding.cvTest.clickListener {
            dismiss()
            listener.invoke(TypeJob.Test)
        }

        binding.cvHomeWork.clickListener {
            dismiss()
            listener.invoke(TypeJob.HomeWork)
        }
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(javaClass.simpleName) == null) {
            show(manager, javaClass.simpleName)
        }
    }

    fun setListener(listener: (TypeJob) -> Unit) = apply {
        this.listener = listener
    }


    companion object {
        fun newInstance() = TypeJobBottomSheet()
    }
}
