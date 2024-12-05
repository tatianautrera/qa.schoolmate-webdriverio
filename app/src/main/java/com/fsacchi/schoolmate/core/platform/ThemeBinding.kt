package com.fsacchi.schoolmate.core.platform

import android.R.attr
import android.content.res.ColorStateList
import android.graphics.PorterDuff.Mode.SRC_IN
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.databinding.BindingAdapter
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.changeColor
import com.fsacchi.schoolmate.core.extensions.color
import com.fsacchi.schoolmate.core.extensions.drawable
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.color.MaterialColors.ALPHA_DISABLED
import com.google.android.material.color.MaterialColors.ALPHA_DISABLED_LOW
import com.google.android.material.color.MaterialColors.ALPHA_FULL
import com.google.android.material.color.MaterialColors.ALPHA_MEDIUM
import com.google.android.material.color.MaterialColors.layer
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object ThemeBinding {

    @JvmStatic
    @BindingAdapter("app:button_style")
    fun buttonStyle(view: MaterialButton, hexColor: String?) {
        if (hexColor == null) return
        val color = hexColor.toColorInt()

        val states = arrayOf(intArrayOf(attr.state_enabled), intArrayOf(-attr.state_enabled))

        val colors = IntArray(states.size)

        colors[0] = color
        colors[1] = "#D9D8D9".toColorInt()
        val colorStateList = ColorStateList(states, colors)
        view.backgroundTintList = colorStateList
        view.strokeColor = colorStateList
    }

    @JvmStatic
    @BindingAdapter("app:text_button_style")
    fun textButtonStyle(view: MaterialButton, hexColor: String?) {
        if (hexColor == null) return
        val color = hexColor.toColorInt()

        val states = arrayOf(intArrayOf(attr.state_enabled, attr.state_pressed))
        val colors = intArrayOf(ColorUtils.setAlphaComponent(color, 60))

        view.backgroundTintList = ColorStateList(states, colors)
        view.setTextColor(color)
    }

    @JvmStatic
    @BindingAdapter("app:outline_style")
    fun outlineStyle(view: MaterialButton, hexColor: String?) {
        if (hexColor == null) return
        val color = hexColor.toColorInt()

        val states = arrayOf(intArrayOf(attr.state_enabled, attr.state_pressed))
        val colors = intArrayOf(ColorUtils.setAlphaComponent(color, 60))

        view.setTextColor(color)
        view.strokeColor = ColorStateList(states, colors)
    }

    @JvmStatic
    @BindingAdapter("app:end_icon_color")
    fun endIconColor(view: TextInputLayout, hexColor: String?) {
        if (hexColor == null) return
        val color = hexColor.toColorInt()
        val states = arrayOf(intArrayOf(attr.state_enabled))

        view.setEndIconTintList(ColorStateList(states, intArrayOf(color)))

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            view.endIconDrawable?.changeColor(color)
        }
    }

    @JvmStatic
    @BindingAdapter("app:icon_style")
    fun iconStyle(view: AppCompatImageView, hexColor: String?) {
        if (hexColor == null) return
        val color = hexColor.toColorInt()
        val states = arrayOf(intArrayOf(attr.state_enabled))
        view.imageTintList = ColorStateList(states, intArrayOf(color))

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            view.colorFilter = PorterDuffColorFilter(color, SRC_IN)
        }
    }

    @JvmStatic
    @BindingAdapter("app:tab_style")
    fun tabStyle(view: TabLayout, hexColor: String?) {
        if (hexColor == null) return

        view.setSelectedTabIndicatorColor(hexColor.toColorInt())
    }

    @JvmStatic
    @BindingAdapter("app:input_style")
    fun inputStyle(view: TextInputLayout, hexColor: String?) {
        if (hexColor == null) return
        val color = hexColor.toColorInt()

        val states = arrayOf(intArrayOf(attr.state_enabled))
        val colors = intArrayOf(color)

        if (hexColor != "#C95D33")
            view.defaultHintTextColor = ColorStateList(states, colors)

        view.boxStrokeColor = color
        (view.editText as? TextInputEditText)?.let {
            editStyle(it, hexColor)
        }
    }

    @JvmStatic
    @BindingAdapter("app:edit_style")
    fun editStyle(view: TextInputEditText, hexColor: String?) {
        if (hexColor == null) return
        val color = hexColor.toColorInt()

        view.highlightColor = ColorUtils.setAlphaComponent(color, 128)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            view.textCursorDrawable = view.textCursorDrawable?.changeColor(color)
            view.textSelectHandle
                ?.changeColor(color)?.let { view.setTextSelectHandle(it) }

            view.textSelectHandleLeft
                ?.changeColor(color)?.let { view.setTextSelectHandleLeft(it) }

            view.textSelectHandleRight
                ?.changeColor(color)?.let { view.setTextSelectHandleRight(it) }
        } else {
            view.changeColorByReflection(color, "mCursorDrawable", "mCursorDrawableRes")
            view.changeColorByReflection(color, "mSelectHandleCenter", "mTextSelectHandleRes", false)

            val fieldNames = listOf("mCursorDrawableRes", "mTextSelectHandleRightRes", "mTextSelectHandleLeftRes")
            view.changeColorByReflection(color, "mCursorDrawable", fieldNames)
        }
    }

    // region reflection
    private fun EditText.changeColorByReflection(
        color: Int,
        drawableName: String,
        fieldNames: String,
        cursorList: Boolean = true
    ) {
        changeColorByReflection(color, drawableName, listOf(fieldNames), cursorList)
    }

    private fun EditText.changeColorByReflection(
        color: Int,
        drawable: String,
        fields: List<String>,
        list: Boolean = true
    ) {
        try {
            // Get the drawable and set a color filter
            val drawables = fields.map { name ->
                val field = TextView::class.java.getDeclaredField(name)
                field.isAccessible = true

                val drawableId = field.getInt(this)
                (context drawable drawableId)?.apply {
                    colorFilter = PorterDuffColorFilter(color, SRC_IN)
                }
            }

            // Get the editor
            var field = TextView::class.java.getDeclaredField("mEditor")
            field.isAccessible = true
            val editor = field.get(this)

            field = editor.javaClass.getDeclaredField(drawable)
            field.isAccessible = true
            field.set(editor, if (list) drawables.toTypedArray() else drawables.first())
        } catch (ignored: Throwable) {
        }
    }

    //endregion

    //region switch
    @JvmStatic
    @BindingAdapter("app:switch_style")
    fun switchStyle(view: SwitchMaterial, hexColor: String?) {
        if (hexColor == null) return
        val color = hexColor.toColorInt()

        view.thumbTintList = view.tintList(color, R.color.white)
        view.trackTintList = view.trackTintList(color)
    }

    @JvmStatic
    @BindingAdapter("app:switch_value")
    fun switchValue(view: SwitchMaterial, value: Boolean) {
        view.isChecked = value
    }

    private val states = arrayOf(
        intArrayOf(attr.state_enabled, attr.state_checked),
        intArrayOf(attr.state_enabled, -attr.state_checked),
        intArrayOf(-attr.state_enabled, attr.state_checked),
        intArrayOf(-attr.state_enabled, -attr.state_checked)
    )

    private fun SwitchMaterial.tintList(color: Int, colorSurface: Int): ColorStateList {
        val colors = IntArray(states.size)

        colors[0] = layer(colorSurface, color, ALPHA_FULL)
        colors[1] = context color colorSurface
        colors[2] = layer(color, color(R.color.white), ALPHA_DISABLED)
        colors[3] = context color colorSurface
        return ColorStateList(states, colors)
    }

    private fun SwitchMaterial.trackTintList(color: Int): ColorStateList {
        val colors = IntArray(states.size)
        val activated = ColorUtils.setAlphaComponent(color, 60)

        colors[0] = layer(color, color(R.color.white), ALPHA_MEDIUM)
        colors[1] = layer(color(R.color.switch_grey), color(R.color.white), ALPHA_DISABLED_LOW)
        colors[2] = layer(activated, color(R.color.white), ALPHA_DISABLED_LOW)
        colors[3] = layer(color(R.color.switch_grey), color(R.color.white), ALPHA_DISABLED_LOW)
        return ColorStateList(states, colors)
    }
    //endregion

    //region checkbox
    @JvmStatic
    @BindingAdapter("app:checkbox_style")
    fun checkboxStyle(view: MaterialCheckBox, hexColor: String?) {
        if (hexColor == null) return

        val context = view.context
        val color = hexColor.toColorInt()

        val colors = IntArray(states.size)
        val colorSurface = R.color.oto_grey

        colors[0] = layer(context color colorSurface, color, ALPHA_FULL)
        colors[1] = context color colorSurface
        colors[2] = layer(color, context color R.color.white, ALPHA_DISABLED)
        colors[3] = context color colorSurface

        view.buttonTintList = ColorStateList(states, colors)
    }
    //endregion

    @JvmStatic
    @BindingAdapter("app:radio_style")
    fun radioButtonStyle(view: MaterialRadioButton, hexColor: String?) {
        if (hexColor == null) return
        val color = hexColor.toColorInt()

        val colors = IntArray(states.size)
        val colorSurface = R.color.oto_grey

        colors[0] = layer(view.context color colorSurface, color, ALPHA_FULL)
        colors[1] = view.context color colorSurface
        colors[2] = layer(color, view.context color R.color.white, ALPHA_DISABLED)
        colors[3] = view.context color colorSurface

        view.buttonTintList = ColorStateList(states, colors)
    }

    @JvmStatic
    @BindingAdapter("app:select_style")
    fun selectStyle(view: MaterialRadioButton, hexColor: String?) {
        if (hexColor == null) return
        val color = hexColor.toColorInt()

        view.background.changeColor(color)
    }

}
