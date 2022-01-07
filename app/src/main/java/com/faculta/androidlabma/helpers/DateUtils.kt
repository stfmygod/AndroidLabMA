package com.faculta.androidlabma.helpers

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val pattern = "dd/MM/yyyy HH:mm:ss"
    fun getStringFromDate(date: Date): String {
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.format(date)
    }
}