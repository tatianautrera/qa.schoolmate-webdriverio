package com.fsacchi.schoolmate.core.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.databinding.AlertMessageCardBinding

class AlertMessageCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val binding: AlertMessageCardBinding
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.alert_message_card, this, true)
        binding.root.alpha = 0f
        binding.root.visibility = View.GONE

        val attr = context.obtainStyledAttributes(attrs, R.styleable.AlertMessage, defStyle, 0)

        binding.titleValue = attr.getString(R.styleable.AlertMessage_title)
        binding.messageValue = attr.getString(R.styleable.AlertMessage_message)

        attr.recycle()
    }

    fun showAlert(title: String, message: String?, isError: Boolean) {
        binding.titleValue = title
        binding.messageValue = message.orEmpty()
        val colorBackgroundCard = if (isError) R.color.warning else R.color.status_green
        val iconCard = if (isError) R.drawable.ic_emoji_sad else R.drawable.ic_emoji_happy

        binding.cvBackground.setCardBackgroundColor(context.getColor(colorBackgroundCard))
        binding.icIcon.setImageDrawable(context.getDrawable(iconCard))
        animateVisibility(show = true)

        postDelayed({ hideAlert() }, 2500)
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
}
