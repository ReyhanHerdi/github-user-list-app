package com.example.githubuserlist.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateHelper {
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/mm/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}