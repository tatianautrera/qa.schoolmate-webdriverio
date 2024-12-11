package com.fsacchi.schoolmate.core.features.home.adapter

import android.annotation.SuppressLint
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.TextDrawable
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.extensions.toEmoji
import com.fsacchi.schoolmate.core.platform.adapter.BaseAdapter
import com.fsacchi.schoolmate.core.platform.adapter.BaseDiffCallback
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.databinding.ItemDisciplineBinding

class DisciplineListAdapter(
    override val layoutRes: Int = R.layout.item_discipline
) : BaseAdapter<DisciplineModel, ItemDisciplineBinding>(BaseDiffCallback()) {

    @SuppressLint("SetTextI18n")
    override fun ItemDisciplineBinding.bind(item: DisciplineModel) {
        tvDisciplineName.text = item.name.capitalizeFirstLetter()

        val emojiString = item.emoji.toInt().toEmoji()
        val emojiDrawable = TextDrawable(emojiString, root.context)
        ivEmoji.setImageDrawable(emojiDrawable)
        tvTeacher.text = "Prof. ${item.teacher.capitalizeFirstLetter()}"
    }
}
