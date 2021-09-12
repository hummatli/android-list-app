package com.mobline.presentation.extensions

import android.content.res.ColorStateList
import android.view.View
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mobline.marleyspoon.presentation.R

fun EditText.clear() {
    setText("")
}

//For view's visibility
fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun ChipGroup.addChip(name: String) {
    addView(Chip(context).apply {
        text = name
        isClickable = false
        isCheckable = false
        chipEndPadding = 2f
        chipStartPadding = 2f

        chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray_10))
        setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.grayish_violet
                )
            )
        )
    })
}