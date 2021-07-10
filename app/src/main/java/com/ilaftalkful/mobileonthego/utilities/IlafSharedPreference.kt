package com.ilaftalkful.mobileonthego.utilities

import android.content.Context


class IlafSharedPreference {

    private val SHARED_PREF_NAME = "IlafPreference"
    internal var mContext: Context? = null
//    private var sharedPreferenceLiveData: SharedPreferenceStringLiveData? = null


    constructor(context: Context?) {
        this.mContext = context
    }


 /*   fun getSharedPrefs(): SharedPreferenceStringLiveData? {
        return sharedPreferenceLiveData
    }

    fun setSharedPreferences(key: String?, value: String) {
        val userDetails: SharedPreferences = mContext!!.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
        val editor = userDetails.edit()
        editor.putString(key, value)
        editor.apply()
        sharedPreferenceLiveData = SharedPreferenceStringLiveData(userDetails, key, value)
    }*/



    /**
     * Setting a boolean key value to Shared preference
     *
     * @param key
     * @param value
     */
    fun setDoublePrefValue(key: String, value: Double?) {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        if (null == value)
            editor.putLong(key, java.lang.Double.doubleToRawLongBits(0.0))
        else
            editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        editor.commit()
        return
    }

    fun getDouble(key: String): Double {

        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        return java.lang.Double.longBitsToDouble(
            pref.getLong(
                key, java.lang.Double.doubleToLongBits(
                    0.0
                )
            )
        )
    }

    /**
     * Getting boolean key value fromDeviceCursor shared preference
     *
     * @param key
     * @return
     */
    fun getBooleanPrefValue(key: String): Boolean {
        if (mContext == null) {
            return false
        }
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        return pref.getBoolean(key, false)
    }

    /**
     * Getting boolean key value fromDeviceCursor shared preference
     *
     * @param key
     * @return
     */
    fun getBooleanPrefValue2(key: String): Boolean {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        return pref.getBoolean(key, true)
    }

    /**
     * Getting boolean key value fromDeviceCursor shared preference
     *
     * @param key
     * @return
     */
    fun getLongPrefValue(key: String): Long {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        var value: Long = 0
        try {
            value = pref.getLong(key, 0)
            if (0L == value) {
                return -1
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return value
    }

    /**
     * Setting a boolean key value to Shared preference
     *
     * @param key
     * @param value
     */
    fun setBooleanPrefValue(key: String, value: Boolean) {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()

        editor.putBoolean(key, value?:false)
        editor.commit()

    }

    /**
     * Getting float key value fromDeviceCursor shared preference
     *
     * @param key
     * @return
     */
    fun getFloatPrefValue(key: String): Float {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        return pref.getFloat(key, 0.000000.toFloat())
    }

    /**
     * Setting a boolean key value to Shared preference
     *
     * @param key
     * @param value
     */
    fun setFloatPrefValue(key: String, value: Float) {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()

        editor.putFloat(key, value)
        editor.commit()

    }

    /**
     * Setting a boolean key value to Shared preference
     *
     * @param key
     * @param value
     */
    fun setLongPrefValue(key: String, value: Long?) {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()

        if (value != null) {
            editor.putLong(key, value)
        }
        editor.commit()

    }

    /**
     * Getting boolean key value fromDeviceCursor shared preference
     *
     * @param key
     * @return
     */
    fun getStringPrefValue(key: String): String? {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return pref.getString(key, null)
    }

    /**
     * Setting a boolean key value to Shared preference
     *
     * @param key
     * @param value
     */
    fun setStringPrefValue(key: String, value: String?) {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()

    }

    /**
     * Getting integer key value fromDeviceCursor shared preference
     *
     * @param key
     * @return
     */
    fun getIntegerPrefValue(key: String): Int {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        return pref.getInt(key, -1)
    }

    /**
     * Setting a integer key value to Shared preference
     *
     * @param key
     * @param value
     */
    fun setIntegerPrefValue(key: String, value: Int?) {
        val pref = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()

        value?.let { editor.putInt(key, it) }
        editor.commit()
    }

    fun clear() {
        val settings = mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        settings.edit().clear().commit()
    }

    interface Constants {
        companion object {
            val TRAVEL_MAX_START_DAYS: String = "max_start_days"
            val TRAVEL_TC: String = "travel_tc"
            val MOTOR_TC: String = "motor_tc"
            val HEALTH_TC: String = "health_tc"
            val MARINE_TC: String = "marine_tc"
            val FGA_TC: String = "fga_tc"

            val MAX_AGE: String = "max_age"
            val ENCRYPTION_IV: String = "encryption_iv"
            val IS_LOGEDIN_USER="loged_in_user"
            val TOKEN_KEY="token"
            val LANGUAGE_KEY="language"
            val USER_EMAIL = "user_email"
            val IS_MALE = "user_gender"
            val USER_DOB = "user_dob"
            val USER_MOBILE_NUMBER= "user_phone"
            val USER_PHONE_CODE= "user_phone_code"
            val USER_NAME = "user_name"
            val USER_PASSWORD = "user_password"
            val USER_CIVIL_ID = "civil_id"
            val PREF_VALUE = "PRIFs"
            val USER_ID = "user_id"
            val IS_OM_SKIP = "marketing_is_skip"
            val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
            val IS_FINGERPRINT = "is_fingerprint_enabled"
            val LANGUAGE_ENGLISH_KEY="en"
            val LANGUAGE_ARABIC_KEY="ar"
            val LANGUAGE_RESET_TOKEN="token_reset"
            val PLAN_SELECTED="plan_selected"

        }
    }
}