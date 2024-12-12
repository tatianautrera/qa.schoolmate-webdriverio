package com.fsacchi.schoolmate.core.components

import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.fragArgs
import com.fsacchi.schoolmate.core.extensions.gone
import com.fsacchi.schoolmate.core.extensions.visible
import com.fsacchi.schoolmate.databinding.DialogAlertMessageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AlertMessageDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAlertMessageBinding

    private var posListener: (() -> Unit)? = null
    private var negListener: (() -> Unit)? = null
    private var disListener: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_alert_message, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {

            if (it.getString(EXTRA_TITLE_TEXT)?.isNotEmpty() == true)
                binding.title = it.getString(EXTRA_TITLE_TEXT)
            else
                binding.title = it.getInt(EXTRA_TITLE, 0).getText() ?: "Schoolmate"

            if (it.getString(EXTRA_MESSAGE_TEXT)?.isNotEmpty() == true)
                binding.message = it.getString(EXTRA_MESSAGE_TEXT)
            else
                binding.message = it.getInt(EXTRA_MESSAGE, 0).getText()

            binding.positiveText = it.getInt(EXTRA_POSITIVE_BUTTON, 0).getText()
            binding.negativeText = it.getInt(EXTRA_NEGATIVE_BUTTON, 0).getText()

            isCancelable = it.getBoolean(EXTRA_CANCELABLE, true)

            val showCloseButton = it.getBoolean(EXTRA_CLOSE_BUTTON, true)
            binding.ivClose.visible(showCloseButton)

            binding.tvMessage.apply {
                if (it.getBoolean(EXTRA_MESSAGE_BOLD)) setTypeface(typeface, Typeface.BOLD)

                val textSize = it.getInt(EXTRA_TEXT_SIZE, 0)
                if (textSize != 0) setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(textSize))

                gravity = it.getInt(EXTRA_TEXT_GRAVITY)
            }

            if (negListener == null) {
                binding.btnNegative.gone()
            }

            insertListeners()
        }
    }

    private fun Int.getText(): String? {
        return takeIf { int -> int != 0 }?.let { text ->
            getString(text)
        }
    }

    private fun insertListeners() {
        binding.btnPositive.clickListener {
            posListener?.invoke()
            dismiss()
        }

        binding.btnNegative.clickListener {
            negListener?.invoke()
            dismiss()
        }

        binding.ivClose.clickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        disListener?.invoke()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, AlertMessageDialog::class.java.name)
    }

    class Builder(val context: Context) {
        private val bundle = Bundle()

        private var message by fragArgs<Int>(EXTRA_MESSAGE, bundle)
        private var messageText by fragArgs<String>(EXTRA_MESSAGE_TEXT, bundle)
        private var title by fragArgs<Int>(EXTRA_TITLE, bundle)
        private var titleText by fragArgs<String>(EXTRA_TITLE_TEXT, bundle)
        private var cancelable by fragArgs<Boolean>(EXTRA_CANCELABLE, bundle)
        private var closeButton by fragArgs<Boolean>(EXTRA_CLOSE_BUTTON, bundle)
        private var imageIcon by fragArgs<Int>(EXTRA_IMAGE_ICON, bundle)

        private var positiveButton by fragArgs<Int>(EXTRA_POSITIVE_BUTTON, bundle)
        private var negativeButton by fragArgs<Int>(EXTRA_NEGATIVE_BUTTON, bundle)

        private var positiveListener: (() -> Unit)? = null
        private var negativeListener: (() -> Unit)? = null
        private var dismissListener: (() -> Unit)? = null

        private var textSize by fragArgs<Int>(EXTRA_TEXT_SIZE, bundle)
        private var gravity by fragArgs<Int>(EXTRA_TEXT_GRAVITY, bundle)
        private var boldMessage by fragArgs<Boolean>(EXTRA_MESSAGE_BOLD, bundle)

        fun title(title: Int) = apply {
            this.title = title
        }

        fun title(title: String) = apply {
            this.titleText = title
        }

        fun message(message: Int) = apply {
            this.message = message
        }

        fun message(message: String?) = apply {
            this.messageText = message ?: ""
        }

        fun cancelable(cancelable: Boolean = true, showCloseButton: Boolean = true) {
            this.cancelable = cancelable
            this.closeButton = showCloseButton
        }

        fun Builder.positiveButton(positiveButton: Int) {
            this.positiveButton = positiveButton
        }

        fun Builder.negativeButton(negativeButton: Int) {
            this.negativeButton = negativeButton
        }

        fun Builder.positiveListener(positiveListener: (() -> Unit)?) {
            this.positiveListener = positiveListener
        }

        fun Builder.negativeListener(negativeListener: (() -> Unit)? = null) {
            this.negativeListener = negativeListener
        }

        fun Builder.dismissListener(dismissListener: (() -> Unit)?) {
            this.dismissListener = dismissListener
        }

        fun Builder.imageIcon(drawable: Int) {
            this.imageIcon = drawable
        }

        fun build() = AlertMessageDialog().apply {
            arguments = bundle
            posListener = positiveListener
            negListener = negativeListener
            disListener = dismissListener
        }
    }

    companion object {
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_TITLE_TEXT = "EXTRA_TITLE_TEXT"
        private const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        private const val EXTRA_MESSAGE_TEXT = "EXTRA_MESSAGE_TEXT"
        private const val EXTRA_CANCELABLE = "EXTRA_CANCELABLE"
        private const val EXTRA_CLOSE_BUTTON = "EXTRA_CLOSE_BUTTON"
        private const val EXTRA_IMAGE_ICON = "EXTRA_IMAGE_ICON"

        private const val EXTRA_POSITIVE_BUTTON = "EXTRA_POSITIVE_BUTTON"
        private const val EXTRA_NEGATIVE_BUTTON = "EXTRA_NEGATIVE_BUTTON"

        private const val EXTRA_TEXT_SIZE = "EXTRA_TEXT_SIZE"
        private const val EXTRA_MESSAGE_BOLD = "EXTRA_MESSAGE_BOLD"
        private const val EXTRA_TEXT_GRAVITY = "EXTRA_TEXT_GRAVITY"
    }
}
