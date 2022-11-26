package com.example.aphorisms.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Aphorism(
    @DrawableRes val authorPic: Int,
    @StringRes val authorName: Int,
    @StringRes val authorAboutFull: Int,
    @StringRes val aphorism: Int
)

