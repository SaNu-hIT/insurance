package com.ilaftalkful.mobileonthego.utilities

import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

class DateUtil {

    companion object {
        var fromServer: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        var myFormat: SimpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        var requestformat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
        var myrequestformat: SimpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)

        var dateSample: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        var civilIdFormat : SimpleDateFormat = SimpleDateFormat("yy-MM-dd", Locale.ENGLISH)
        var civilIdDateFormat : SimpleDateFormat = SimpleDateFormat("yyMMdd", Locale.ENGLISH)

        var format: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)


        fun getDateToFromat(reciveDate: String?): String {
            return dateSample.format(dateSample.parse(reciveDate))
        }

        fun getCivilIdDob(reciveDate: String?): String {
            return civilIdFormat.format(civilIdDateFormat.parse(reciveDate))
        }
        fun getDobInCivilIdDob(reciveDate: String?): String {
            return civilIdDateFormat.format(myFormat.parse(reciveDate))
        }

        fun getStringDateToFromat(reciveDate: String?): String {
            return myFormat.format(fromServer.parse(reciveDate))
        }

        fun getDateTorequestFromat(reciveDate: String?): String {
            return requestformat.format(myrequestformat.parse(reciveDate))
        }
        fun getDateTorequestFromaForEditFamily(reciveDate: String?): String {
            return requestformat.format(fromServer.parse(reciveDate))
        }

        fun getDateFormatFromDOb(reciveDate: String?): String {
            return format.format(myrequestformat.parse(reciveDate))
        }

        fun getDateToProfile(reciveDate: String?): String {
            return requestformat.format(myFormat.parse(reciveDate))
        }

        fun getDiffYears(first: Date?, last: Date?): Int {
            val a = getCalendar(first)
            val b = getCalendar(last)
            var diff = b[YEAR] - a[YEAR]
            if (a[MONTH] > b[MONTH] ||
                    a[MONTH] === b[MONTH] && a[DATE] > b[DATE]) {
                diff--
            }
            return diff
        }

        fun getCalendar(date: Date?): Calendar {
            val cal = Calendar.getInstance(Locale.US)
            cal.time = date
            return cal
        }
    }



}