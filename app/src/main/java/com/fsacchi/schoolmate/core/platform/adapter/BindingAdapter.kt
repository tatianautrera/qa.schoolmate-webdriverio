package com.fsacchi.schoolmate.core.platform.adapter

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.DateMasks
import com.fsacchi.schoolmate.core.extensions.color
import com.fsacchi.schoolmate.core.extensions.disable
import com.fsacchi.schoolmate.core.extensions.drawable
import com.fsacchi.schoolmate.core.extensions.enable
import com.fsacchi.schoolmate.core.extensions.format
import com.fsacchi.schoolmate.core.extensions.formatWithUTC
import com.fsacchi.schoolmate.core.extensions.gone
import com.fsacchi.schoolmate.core.extensions.htmlText
import com.fsacchi.schoolmate.core.extensions.loadImage
import com.fsacchi.schoolmate.core.extensions.toUpper
import com.fsacchi.schoolmate.core.extensions.visible
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.data.model.job.TypeJob
import com.google.android.material.chip.Chip
import java.util.Date

object BindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["app:image_url", "app:image_placeholder"], requireAll = false)
    fun setImageUrl(imageView: ImageView, url: String?, drawable: Drawable?) {
        if (url != null) {
            imageView.loadImage(url) {
                drawable?.let {
                    placeholder(it)
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["app:image_background_url"], requireAll = false)
    fun setImageBackgroundUrl(imageView: ImageView, listBackground: List<String>?) {
        if (listBackground.isNullOrEmpty()) return
        imageView.loadImage(listBackground.random())
    }

    @JvmStatic
    @BindingAdapter("app:image_resource")
    fun setImageResource(imageView: ImageView, @DrawableRes resource: Int) {
        if (resource == 0) return
        imageView.setImageDrawable(imageView drawable resource)
    }

    @JvmStatic
    @BindingAdapter("app:image_resource")
    fun setBackground(view: View, resource: Int) {
        view.background = view drawable resource
    }


    @JvmStatic
    @BindingAdapter("app:disable")
    fun enableView(view: View, disable: Boolean) {
        if (disable) view.disable() else view.enable()
    }

    @JvmStatic
    @BindingAdapter("app:visible")
    fun visibilityView(view: View, visible: Boolean) {
        view.visible(visible)
    }

    @JvmStatic
    @BindingAdapter("app:visible_by_text")
    fun visibilityViewByText(view: View, text: String?) {
        view.visible(!text.isNullOrEmpty())
    }

    @JvmStatic
    @BindingAdapter("app:state_selected")
    fun selectView(view: View, selected: Boolean) {
        view.isSelected = selected
    }

    @JvmStatic
    @BindingAdapter("app:text_resource")
    fun setTextResource(view: TextView, @StringRes resource: Int) {
        if (resource == 0) return
        val text = view.context.getString(resource)
        view.htmlText(text)
    }

    @JvmStatic
    @BindingAdapter("app:text_html")
    fun setHtmlText(view: TextView, html: String?) {
        view.htmlText(html ?: "")
    }

    @JvmStatic
    @BindingAdapter("app:text_upper")
    fun setUpperText(view: TextView, value: String?) {
        view.text = value?.toUpper()
    }

    @JvmStatic
    @BindingAdapter(value = ["app:date_text", "app:date_format"], requireAll = false)
    fun setDateText(view: TextView, value: Date?, dateFormat: String?) {
        view.text = value?.format(dateFormat ?: DateMasks.appFormat)
    }

    @JvmStatic
    @BindingAdapter(value = ["app:date_hour_text", "app:date_format"], requireAll = false)
    fun setDateHourText(view: TextView, value: Date?, dateFormat: String?) {
        val textDateHour = StringBuilder()
        textDateHour.append(value?.format(dateFormat ?: DateMasks.appFormat))
        textDateHour.append(" às ")
        textDateHour.append(value?.formatWithUTC(DateMasks.hourFormat))
        view.text = textDateHour.toString()
    }

    @JvmStatic
    @BindingAdapter(value = ["app:date_utc_text", "app:date_format"], requireAll = false)
    fun setDateUtcText(view: TextView, value: Date?, dateFormat: String?) {
        view.text = value?.formatWithUTC(dateFormat ?: DateMasks.appFormat)
    }


    @JvmStatic
    @BindingAdapter("app:underline")
    fun underlineTextView(view: TextView, enable: Boolean) {
        view.paintFlags = if (enable) view.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        else view.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
    }

    @JvmStatic
    @BindingAdapter("app:strikethrough")
    fun strikethroughTextView(view: TextView, enable: Boolean) {
        view.paintFlags = if (enable) view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    @JvmStatic
    @BindingAdapter(value = ["app:value", "app:concat_text"], requireAll = true)
    fun labelValueFormat(view: TextView, value: Long?, concatText: String) {
        value?.let {
            view.text = it.toString().plus(concatText)
        }
    }

    @JvmStatic
    @BindingAdapter("app:chip_type_job")
    fun setChipTypeJob(view: Chip, job: JobModel?) {
        if (job == null) return
        view.visible()
        when (job.typeJob) {
            TypeJob.Test -> {
                view.setText(R.string.test)
                view.setTextColor(view.color(R.color.text_test))
                view.setChipBackgroundColorResource(R.color.back_test)
            }
            TypeJob.HomeWork -> {
                view.setText(R.string.home_work)
                view.setTextColor(view.color(R.color.text_homework))
                view.setChipBackgroundColorResource(R.color.back_homework)
            }
            TypeJob.Job -> {
                view.setText(R.string.job)
                view.setTextColor(view.color(R.color.text_job))
                view.setChipBackgroundColorResource(R.color.back_job)
            }
            null -> view.gone()
        }
    }
}
