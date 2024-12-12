package com.fsacchi.schoolmate.core.extensions

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff.Mode.SRC_IN
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.text.SpannableStringBuilder
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.fsacchi.schoolmate.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

//region ViewGroup
fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showSoftKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible(show: Boolean) {
    if (show) visible() else gone()
}

fun View.enable(enabled: Boolean) {
    if (enabled) enable() else disable()
}

fun View.disable() {
    isEnabled = false
}

fun View.enable() {
    isEnabled = true
}

fun NestedScrollView.scrollTo(view: View) {
    post { smoothScrollTo(0, view.top) }
}

infix fun View.color(@ColorRes color: Int) = context color color

infix fun View.drawable(@DrawableRes drawable: Int) = context drawable drawable

fun ImageView.setDrawable(@DrawableRes drawable: Int, tagged: Boolean = false) {
    setImageDrawable(context drawable drawable)
    if (tagged) tag = drawable
}

class OnDebouncedClickListener(
    private val delayInMilliSeconds: Long,
    val action: () -> Unit
) : View.OnClickListener {
    var enable = true

    override fun onClick(view: View?) {
        if (enable) {
            enable = false
            view?.postDelayed({
                enable = true
            }, delayInMilliSeconds)
            action()
        }
    }
}

fun View.setOnDebouncedClickListener(delayInMilliSeconds: Long = 2000, action: () -> Unit) {
    val onDebouncedClickListener = OnDebouncedClickListener(delayInMilliSeconds, action)
    setOnClickListener(onDebouncedClickListener)
}

fun View.clickListener(closeKeyboard: Boolean = false, action: () -> Unit) {
    if (closeKeyboard) hideSoftKeyboard()
    setOnDebouncedClickListener { action() }
}

/**
 * must be called in onResume
 */
fun BottomSheetDialogFragment.setupFullScreen(block: BottomSheetBehavior<*>.() -> Unit) {
    val id = com.google.android.material.R.id.design_bottom_sheet
    requireView().viewTreeObserver.addOnGlobalLayoutListener {
        dialog?.findViewById<FrameLayout>(id)?.let {
            block(BottomSheetBehavior.from(it))
        }
    }
}

//endregion

//region TextView
inline fun TextView.setOnActionGo(crossinline action: View.() -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_GO
    setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_GO || event.keyCode == KeyEvent.KEYCODE_ENTER) {
            v.apply { action() }
            true
        } else false
    }
}

inline fun TextView.setOnActionDone(crossinline action: View.() -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_DONE
    setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE || event.keyCode == KeyEvent.KEYCODE_ENTER) {
            v.apply {
                action()
                hideSoftKeyboard()
            }
            true
        } else false
    }
}

inline fun TextView.setOnActionSearch(crossinline action: View.() -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_SEARCH
    setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {
            v.apply { action() }
            true
        } else false
    }
}

fun TextView.htmlText(html: String) {
    text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

fun TextView.text(label: String, value: String) {
    val spannable = SpannableStringBuilder()
        .color(color(R.color.defaultText)) { append(label) }
        .append(" ")
        .bold { color(color(R.color.defaultText)) { append(value) } }

    text = spannable
}

fun TextView.text(@StringRes label: Int, @StringRes value: Int) {
    text(context.getString(label), context.getString(value))
}

fun TextView.text(@StringRes label: Int, value: String) {
    text(context.getString(label), value)
}
//endregion

//region Toast
fun AppCompatActivity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun AppCompatActivity.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    context?.toast(message, duration)
}

fun Fragment.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    context?.toast(resId, duration)
}

fun View.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    context.toast(message, duration)
}

fun View.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    context.toast(resId, duration)
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}

//endregion

//region RecyclerView
fun RecyclerView.addVerticalDivider() {
    addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
}
//endregion

//region ImageView
fun ImageView.loadImage(uri: String, block: RequestCreator.() -> Unit = {}) {
    Picasso.get()
        .load(uri)
        .apply { block(this) }
        .into(this)
}
//endregion

//region EditText
fun TextInputEditText.text() = text.toString()

fun EditText.alphaNumericInput(allCaps: Boolean = true, limit: Int = 14, allowSpace: Boolean = false) {
    val filter = InputFilter { source, start, end, dest, _, _ ->
        val builder = StringBuilder()
        val regex = if (allowSpace) "[a-zA-Z0-9 ]".toRegex() else "[a-zA-Z0-9]".toRegex()
        for (i in start until end) {
            val c = source[i]
            if (c.toString().matches(regex)) {
                if (dest.length > limit || source.length > limit) {
                    builder.append(dest)
                    break
                } else {
                    if (source.length > 1) {
                        if (source[0].isDigit()) return@InputFilter ""
                        else builder.append(c)
                    } else if (dest.isNotEmpty() || !source[0].isDigit()) {
                        builder.append(c)
                    }
                }
            }
        }

        val allCharactersValid = builder.length == end - start
        if (allCharactersValid) null else builder.toString()
    }
    var array = arrayOf(filter)
    if (allCaps) array += AllCaps()
    filters = array
}
//endregion

//region View Pager
fun ViewPager.pageSelectedListener(onSelected: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, offsetPixels: Int) {}

        override fun onPageSelected(nextPosition: Int) {
            onSelected(nextPosition)
        }
    })
}
//endregion

//region Drawable
fun Drawable.changeColor(color: Int) = apply {
    colorFilter = PorterDuffColorFilter(color, SRC_IN)
}
//endregion

//region ViewPager
fun Fragment.associateViewPagerWithChip(viewPager: ViewPager, chipGroup: ChipGroup, listener: (Int) -> Unit = {}) {
    chipGroup.setOnCheckedChangeListener { _, checkedId ->
        val chipChecked: Chip? = chipGroup.findViewById(checkedId) as Chip?
        if (chipChecked != null) {
            val tabBarSelected = (chipChecked.tag as String).toInt()
            viewPager.setCurrentItem(tabBarSelected, true)
        }
    }

    viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            listener.invoke(position)
            chipGroup.check(chipGroup.getChildAt(position).id)
        }
    })
}
// endregion

