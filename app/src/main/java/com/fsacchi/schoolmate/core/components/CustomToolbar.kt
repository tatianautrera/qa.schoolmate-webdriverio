package com.fsacchi.schoolmate.core.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.drawable
import com.fsacchi.schoolmate.databinding.ToolbarBinding

class CustomToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val binding: ToolbarBinding
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.toolbar, this, true)

        val attr = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, defStyle, 0)

        binding.title = attr.getString(R.styleable.CustomToolbar_title_toolbar)
        attr.recycle()
    }

    val toolbar: Toolbar by lazy { binding.toolbar }

    fun setTitle(title: String) {
        binding.title = title
        binding.executePendingBindings()
    }

    fun showBackIcon(show: Boolean) {
        if (show) {
            binding.toolbar.navigationIcon = context.drawable(R.drawable.ic_back)
        } else {
            binding.toolbar.navigationIcon = null
        }
    }
}
