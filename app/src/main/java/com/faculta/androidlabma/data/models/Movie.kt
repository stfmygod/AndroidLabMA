package com.faculta.androidlabma.data.models

import java.io.Serializable
import java.util.*

data class Movie(
    val id: String? = null,
    val name: String? = null,
    val date: Date? = null,
    val rating: Int? = null,
    val isSeenAtCinema: Boolean? = null,
    val photoPath: String? = null
): Serializable
