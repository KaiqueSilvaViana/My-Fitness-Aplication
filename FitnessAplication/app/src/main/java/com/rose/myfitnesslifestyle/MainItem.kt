package com.rose.myfitnesslifestyle

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MainItem(
    val id: String,
    @DrawableRes val drawableStart: Int,
    @DrawableRes val drawableEnd: Int,
    @StringRes val stringId: Int,
    val color: Int
)
