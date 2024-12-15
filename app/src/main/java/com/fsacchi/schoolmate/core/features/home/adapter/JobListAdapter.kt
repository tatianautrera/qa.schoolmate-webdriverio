package com.fsacchi.schoolmate.core.features.home.adapter

import android.annotation.SuppressLint
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.TextDrawable
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.toEmoji
import com.fsacchi.schoolmate.core.platform.adapter.BaseAdapter
import com.fsacchi.schoolmate.core.platform.adapter.BaseDiffCallback
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.databinding.ItemDisciplineBinding
import com.fsacchi.schoolmate.databinding.ItemJobBinding

class JobListAdapter(
    override val layoutRes: Int = R.layout.item_job
) : BaseAdapter<JobModel, ItemJobBinding>(BaseDiffCallback()) {

    var listenerOptions: (JobModel) -> Unit = {}
    var listenerFinish: (JobModel) -> Unit = {}

    @SuppressLint("SetTextI18n")
    override fun ItemJobBinding.bind(item: JobModel) {
        ivArrow.clickListener { listenerOptions.invoke(item) }
        cvTypeJob.clickListener {
            rootListener(item)
        }
        cbFinishJob.clickListener {
            item.status = if (cbFinishJob.isChecked) "S" else ""
            notifyDataSetChanged()
            listenerFinish.invoke(item)
        }
    }
}
