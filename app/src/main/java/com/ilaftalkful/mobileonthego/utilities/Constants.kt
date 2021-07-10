package com.ilaftalkful.mobileonthego.utilities

class Constants {
    companion object{
        val PAYMENT_URL: String="payment_url"
        val PHONE_CALL: Int =555
        val MYLOGS: Int? = 3
        val SCREEN_MYLOGS: String = "mylogs_screen"
        private const val TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000
        private const val TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
         const val DIVIDER_MODULO =
            3 // means divider position is every 5th symbol beginning with 1

         const val DIVIDER_POSITION =
            DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0

         const val DIVIDER = '-'
        val CARD_DATE_TOTAL_DIGITS: Int=8
        val CARD_DATE_DIVIDER_MODULO: Int = 2
        val CARD_DATE_DIVIDER = '-'
        val CARD_DATE_TOTAL_SYMBOLS: Int =10
        val FILE_FORMAT: String? =".png"
        val IMAGE_DATA:String ="data"
        val CAMERA_INTENT: Int = 999
        val CAMERA_PERMISSION_REQUEST_CODE: Int =10001
        val PERMISSION_FINGER_PRINT =1001
        val EXTERNAL_READ_PERMISSION_REQUEST_CODE=2000
        val PHONE_GALLERY_INTENT=2200
        val LANGUAGE: String="EN"
        val IS_FROM_SCREENS: String = "is_from_screen"
        val IS_FROM_RESET: String = "is_from_reset"
        val UNAUTH_ERROR: Int = 401
        val AUTHORIZATION_KEY: String ="Authorization"
        val LANGUAGE_KEY: String ="Language"
        val BEARER_KEY: String ="Bearer "
        val HEALTH: String = "Health"
        val FGA: String = "FGA"
        val MARINE: String = "Marine"
        val TRAVEL: String = "Travel"
        val DASHBOARD_DATA: String ="dashBoardData"
        val ISFROM_FGA: String ="isfromfga"
        val ISFROM_MOTOR_CLAIM: String ="isfrommotorclaim"

        val DAMAGE_ICONS: Int? =1
        val POLICE_REPORT: Int? =11
        val VEHICLE_REG: Int? =21
        val CAR_OWNER_ID: Int? =31
        val DRIVER_CIVIL_ID: Int? =41
        val DRIVER_DRIVER_ID: Int? =51
        val POLICY_FIRST_PAGE: Int? =61
        val PASSPORT_NO: Int? =71
        val VOICE_NOTE: Int? =101


    }
}