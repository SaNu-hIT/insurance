package com.ilaftalkful.mobileonthego.utilities

import android.content.Context
import android.util.Patterns
import com.ilaftalkful.mobileonthego.R
import java.util.regex.Pattern

class IlafValidator(context: Context) {

    companion object {

        fun isValidUserPassword(password: String?, confirmPassword: String?,context:Context?): String? {
            var errorMSg: String? = null
            if (!password.equals(confirmPassword)) {
                errorMSg = context?.getString(R.string.password_mismatch)
                return errorMSg
            }

            return errorMSg
        }


        fun validateUserPasswordLength(password: String?): Boolean {
            var minLength = 6

            return isValidLength(password, minLength)
        }

        fun isValidUserPassword(password: String?,context:Context?): String? {
            var errorMSg: String? = null
            if (isNullOrEmpty(password)) {
                errorMSg = context?.getString(R.string.passport_no_empty)
                return errorMSg
            }
            if (hasSpace(password)) {
                errorMSg = context?.getString(R.string.space_pasword)
                return errorMSg
            }
            if (!hasNumericValues(password)) {
                errorMSg = context?.getString(R.string.one_numeric)
                return errorMSg
            }
            if (!containsAtleastOneAlphabet(password)) {
                errorMSg = context?.getString(R.string.at_least_one_alphabet)
                return errorMSg
            }
            if (!isValidLength(password, 6)) {
                errorMSg = context?.getString(R.string.password_length)
                return errorMSg
            }
            return errorMSg
        }

        private fun containsAtleastOneAlphabet(s: String?): Boolean {

            if (s != null) {
                return s.matches(".*[a-zA-Z]+.*".toRegex())
            }
            return false
        }

        fun hasSpace(value: String?): Boolean {
            if (value != null) {
                return value.contains(" ")
            }
            return true
        }

        private fun hasNumericValues(value: String?): Boolean {
            if (value != null) {
                return value.matches(".*\\d+.*".toRegex())
            }
            return true
        }

        fun isValidLength(value: String?, minLength: Int): Boolean {

            if (value != null) {
                if (value.length < minLength) {
                    return false
                }
            }
            return true
        }

        fun isValidEmails(email: String,context:Context?): String? {
            var errorMSg: String? = null
            if (isNullOrEmpty(email)) {
                errorMSg = context?.getString(R.string.email_empty)
                return errorMSg
            }
            if (!isValidEmail(email)) {
                errorMSg = context?.getString(R.string.invalid_email)
                return errorMSg
            }
            return errorMSg
        }

        fun isValidUserName(email: String,context:Context?): String? {
            var errorMSg: String? = null
            if (isNullOrEmpty(email)) {
                errorMSg = context?.getString(R.string.email_empty)
                return errorMSg
            }
            if (!isValidEmail(email)) {
                errorMSg = context?.getString(R.string.invalid_email)
                return errorMSg
            }
            val separated =
                email.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (separated == null || separated.size < 2 || separated[separated.size - 1].length < 2) {
                errorMSg =context?.getString(R.string.invalid_email)
                return errorMSg
            }

            if (Character.isWhitespace(email[0]) || Character.isWhitespace(email[email.length - 1])) {
                errorMSg =context?.getString(R.string.invalid_email)
                return errorMSg
            }

            return errorMSg
        }

        fun isNullOrEmpty(str: String?): Boolean {
            if (str != null && !str.isEmpty())
                return false
            return true
        }

        fun isValidEmail(target: CharSequence?): Boolean {

            val emailPattern =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            return target?.toString()?.matches(emailPattern.toRegex()) ?: false
        }

        fun userNameContainsSpecialCharacter(value: String): Boolean {
            var p = Pattern.compile("[^a-z0-9@ ]", Pattern.CASE_INSENSITIVE)
            val m = p.matcher(value)
            val b = m.find()
            return b
        }

        fun isValidMobile(phone: String,context:Context?): String? {
            var errorMSg: String? = null
            if (phone.isNullOrEmpty()){
                errorMSg = context?.getString(R.string.invalid_phone)
            }else if (phone.length <9) {
                if(!Pattern.matches("[0-9]+",phone)){
                    errorMSg = context?.getString(R.string.invalid_phone)
                }
            }else if (phone.length ==9) {
                if(!phone.contains(" ")){
                    errorMSg = context?.getString(R.string.invalid_phone)
                }
            }
            if (!Patterns.PHONE.matcher(phone).matches()) {
                errorMSg = context?.getString(R.string.invalid_phone)
            }
            return errorMSg
        }

        fun isValidCivilId(civilId: String, dob: String, context: Context?): String? {
            var errorMSg: String? = null
            if (civilId.isNullOrEmpty()){
                errorMSg = context?.getString(R.string.invalid_civil_id)
            }else if (civilId.length < 12) {
                errorMSg = context?.getString(R.string.invalid_civil_id)
            }else {
                var civilIdDob = civilId.substring(1,7)
                var dateDob = DateUtil.getDobInCivilIdDob(dob)
                if(!civilIdDob.equals(dateDob)){
                    errorMSg = context?.getString(R.string.invalid_civil_id)
                }

            }

            return errorMSg
        }

        fun userNameContainsSpace(value: String?): Boolean {
            return value!!.contains(" ")
        }

        fun isGenderSelected(gender: String?,context: Context?): String? {
            var errorMSg: String? = null
            if (gender == null || gender.toInt() <= 0) {
                errorMSg =context?.getString(R.string.select_one)
            }
            return errorMSg

        }

        fun isValidName(value: String,context: Context?): String? {
            var errorMSg: String? = null
            if (value.isNullOrEmpty()){
                errorMSg = context?.getString(R.string.name_empty)
            }
            if (!Pattern.matches("[a-zA-Z ]+", value)) {
                errorMSg = context?.getString(R.string.naem_alphabet_only)
            }
            return errorMSg
        }


        fun isValidSum(value: String,context:Context?): String? {
            var errorMSg: String? = null
            if (value.isNullOrEmpty()) {
                errorMSg = context?.getString(R.string.insured_asum)
            } else if (Pattern.matches("[a-zA-Z]+", value)) {
                errorMSg = context?.getString(R.string.no_alphabets_in_sum)
            } else if (value.toInt() <= 0) {
                errorMSg = context?.getString(R.string.should_not_zero)
            }
            return errorMSg
        }

        fun isValidPolicy(value: String,context:Context?): String? {
            var errorMSg: String? = null
            if (value.isNullOrEmpty()) {
                errorMSg =  context?.getString(R.string.policy_empty)
            }
            return errorMSg
        }

        fun isValidPasportNumber(value: String,context:Context?): String? {
            var errorMSg: String? = null
            if (value.isNullOrEmpty()) {
                errorMSg = context?.getString(R.string.passport_no_empty)
            }
            return errorMSg
        }


        fun isValidDOBNameError(value: String,context:Context?): String? {
            var errorMSg: String? = null
            if (value.isNullOrEmpty()) {
                errorMSg = context?.getString(R.string.dob_empty)
            }
            return errorMSg
        }

        fun isValidRelation(value: String,context:Context?): String? {
            var errorMSg: String? = null
            if (value.isNullOrEmpty()) {
                errorMSg = context?.getString(R.string.relation_emoty)
            }
            return errorMSg
        }


        fun isValidPoliceReport(value: String,context:Context?): String? {
            var errorMSg: String? = null
            if (value.isNullOrEmpty()) {
                errorMSg = context?.getString(R.string.police_report_empty)
            }
            return errorMSg
        }

        fun isValidDate(value: String,context:Context?): String? {
            var errorMSg: String? = null
            if (value.isNullOrEmpty()) {
                errorMSg = context?.getString(R.string.invalid_date)
            }

            return errorMSg
        }

    }

}