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
import com.fsacchi.schoolmate.data.model.file.FileUserModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.databinding.ItemDisciplineBinding
import com.fsacchi.schoolmate.databinding.ItemFileBinding
import com.fsacchi.schoolmate.databinding.ItemJobBinding

class FileListAdapter(
    override val layoutRes: Int = R.layout.item_file
) : BaseAdapter<FileUserModel, ItemFileBinding>(BaseDiffCallback()) {

    var listenerOptions: (FileUserModel) -> Unit = {}

    @SuppressLint("SetTextI18n")
    override fun ItemFileBinding.bind(item: FileUserModel) {
        ivArrow.clickListener { listenerOptions.invoke(item) }
    }
}
