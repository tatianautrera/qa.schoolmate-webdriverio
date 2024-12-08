package com.fsacchi.schoolmate.core.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.databinding.AlertMessageCardBinding
import com.fsacchi.schoolmate.databinding.BottomBarBinding
import java.util.Date

class BottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val menuSelected: MenuBottom = MenuBottom.AGENDA
    private var listenerMenu: ((MenuBottom) -> Unit)? = null
    private val binding: BottomBarBinding
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_bar, this, true)

        setMenuSelected(menuSelected)
        insertListeners()
    }

    fun setListener(listener: (MenuBottom) -> Unit) {
        listenerMenu = listener
    }

    private fun insertListeners() {
        binding.clAgenda.clickListener {
            setMenuSelected(MenuBottom.AGENDA)
            listenerMenu?.invoke(MenuBottom.AGENDA)
        }

        binding.clFiles.clickListener {
            setMenuSelected(MenuBottom.FILE)
            listenerMenu?.invoke(MenuBottom.FILE)
        }

        binding.clDiscipline.clickListener {
            setMenuSelected(MenuBottom.DISCIPLINE)
            listenerMenu?.invoke(MenuBottom.DISCIPLINE)
        }
    }

    private fun setMenuSelected(menu: MenuBottom) {
        when(menu) {
            MenuBottom.DISCIPLINE -> {
                binding.cvAgenda.setBackgroundResource(R.drawable.rounded)
                binding.cvDiscipline.setBackgroundResource(R.drawable.rounded_selected)
                binding.cvFile.setBackgroundResource(R.drawable.rounded)
            }
            MenuBottom.AGENDA -> {
                binding.cvAgenda.setBackgroundResource(R.drawable.rounded_selected)
                binding.cvDiscipline.setBackgroundResource(R.drawable.rounded)
                binding.cvFile.setBackgroundResource(R.drawable.rounded)
            }
            MenuBottom.FILE -> {
                binding.cvAgenda.setBackgroundResource(R.drawable.rounded)
                binding.cvDiscipline.setBackgroundResource(R.drawable.rounded)
                binding.cvFile.setBackgroundResource(R.drawable.rounded_selected)
            }
        }
    }

    private fun hideAlert() {
        animateVisibility(show = false)
    }

    private fun animateVisibility(show: Boolean) {
        binding.root.animate()
            .alpha(if (show) 1f else 0f)
            .setDuration(400)
            .withStartAction {
                if (show) binding.root.visibility = View.VISIBLE
            }
            .withEndAction {
                if (!show) binding.root.visibility = View.GONE
            }
            .start()
    }

    enum class MenuBottom {
        DISCIPLINE,
        AGENDA,
        FILE
    }
}
