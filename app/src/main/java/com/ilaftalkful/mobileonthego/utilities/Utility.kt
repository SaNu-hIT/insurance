package com.ilaftalkful.mobileonthego.utilities

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.UserNotAuthenticatedException
import android.text.Editable
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.security.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.security.cert.CertificateException


class Utility {
    companion object{

        private const val CHARSET_NAME = "UTF-8"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val TRANSFORMATION = (KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)

        private const val AUTHENTICATION_DURATION_SECONDS = 30

        private val keyguardManager: KeyguardManager? = null
        private const val SAVE_CREDENTIALS_REQUEST_CODE = 1

        @RequiresApi(Build.VERSION_CODES.M)
        @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class, UnsupportedEncodingException::class,
                BadPaddingException::class, IllegalBlockSizeException::class)
        fun saveUserPin(pin: String, context: Context) {
            // encrypt the password
            try {
                val secretKey: SecretKey = createKey()
                val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                val encryptionIv: ByteArray = cipher.getIV()
                val passwordBytes = pin.toByteArray(charset(CHARSET_NAME))
                val encryptedPasswordBytes: ByteArray = cipher.doFinal(passwordBytes)
                val encryptedPassword: String = Base64.encodeToString(encryptedPasswordBytes, Base64.DEFAULT)

                // store the login data in the shared preferences
                // only the password is encrypted, IV used for the encryption is stored

                IlafSharedPreference(context).setStringPrefValue(IlafSharedPreference.Constants.USER_PASSWORD, encryptedPassword)
                IlafSharedPreference(context).setStringPrefValue(IlafSharedPreference.Constants.ENCRYPTION_IV, Base64.encodeToString(encryptionIv, Base64.DEFAULT))
            } catch (e: UserNotAuthenticatedException) {
                e.printStackTrace()
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun createKey(): SecretKey {
            return try {
                val keyGenerator: KeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
                keyGenerator.init(KeyGenParameterSpec.Builder("Ilaf_key_alias",
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(false)
                        .setUserAuthenticationValidityDurationSeconds(AUTHENTICATION_DURATION_SECONDS)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build())
                keyGenerator.generateKey()
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("Failed to create a symmetric key", e)
            } catch (e: NoSuchProviderException) {
                throw RuntimeException("Failed to create a symmetric key", e)
            } catch (e: InvalidAlgorithmParameterException) {
                throw RuntimeException("Failed to create a symmetric key", e)
            }
        }

        @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class,
                IOException::class, NoSuchPaddingException::class, UnrecoverableKeyException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, BadPaddingException::class, IllegalBlockSizeException::class)
        fun getUserPin(context: Context): String? {
            // load login data from shared preferences (
            // only the password is encrypted, IV used for the encryption is loaded from shared preferences
            val base64EncryptedPassword = IlafSharedPreference(context).getStringPrefValue(IlafSharedPreference.Constants.USER_PASSWORD)
            val base64EncryptionIv =  IlafSharedPreference(context).getStringPrefValue(IlafSharedPreference.Constants.ENCRYPTION_IV)
            val encryptionIv = Base64.decode(base64EncryptionIv, Base64.DEFAULT)
            val encryptedPassword = Base64.decode(base64EncryptedPassword, Base64.DEFAULT)

            // decrypt the password
            val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)
            val secretKey = keyStore.getKey("Ilaf_key_alias", null) as SecretKey
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(encryptionIv))
            val passwordBytes = cipher.doFinal(encryptedPassword)
            return String(passwordBytes, charset(CHARSET_NAME))
        }

        var permissonList = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.USE_BIOMETRIC,
                Manifest.permission.USE_FINGERPRINT
        )
        var callPermisionList = arrayOf(
                Manifest.permission.CALL_PHONE
        )
        fun checkInternetConnection(ctx: Context?): Boolean {
                val connectivityManager = ctx?.getSystemService(Context.CONNECTIVITY_SERVICE)
                return if (connectivityManager is ConnectivityManager) {
                    val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                    networkInfo?.isConnected ?: false
                } else return false
        }

        fun isLogedIn(context: Context): Boolean {
            return IlafSharedPreference(context).getBooleanPrefValue(IlafSharedPreference.Constants.IS_LOGEDIN_USER)
        }

        fun hasPermissions(context: Context): Boolean = permissonList.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        fun callEmailPermision(context: Context): Boolean = callPermisionList.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        open fun isInputCorrect(s: Editable, size: Int, dividerPosition: Int, divider: Char): Boolean {
            var isCorrect = s.length <= size
            for (i in 0 until s.length) {
                if (i > 0 && ((i + 1) % dividerPosition == 0 ) ) {
                    if (divider == '-') {
                        if (i == 2 || i ==5) {
                            isCorrect = isCorrect and (divider == s[i])
                        }
                    }
                } else {
                    isCorrect = isCorrect and Character.isDigit(s[i])
                }
            }
            return isCorrect
        }



        open fun buildCorrectString(digits: CharArray, dividerPosition: Int, divider: Char): String {
            val formatted = StringBuilder()
            for (i in digits.indices) {
                if (digits[i].toInt() != 0) {
                    formatted.append(digits[i])
                    if(dividerPosition!=9) {
                        if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                            formatted.append(divider)
                        }
                    }
                }
            }
            return formatted.toString()
        }

        open fun getDigitArray(s: Editable, size: Int): CharArray {
            val digits = CharArray(size)
            var index = 0
            var i = 0
            while (i < s.length && index < size) {
                val current = s[i]
                if (Character.isDigit(current)) {
                    digits[index] = current
                    index++
                }
                i++
            }
            return digits
        }

        fun   getBitmapFromUri(mContext: Context, uri: Uri): Bitmap? {
            var bitmap : Bitmap? = null
            var isLargerSize : Boolean  = false;
            try {
                var   iStream :  InputStream? = mContext.contentResolver.openInputStream(uri);
                if (iStream != null) {
                    var size:Int  = iStream.available();

                    var options : BitmapFactory.Options =  BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    bitmap = BitmapFactory.decodeStream(iStream, null, null);
                }
            } catch (e: IOException) {
                e.printStackTrace();
            } catch (e: OutOfMemoryError) {
//handle outof memmory error
            } finally {
                if (isLargerSize) {
                }
            }
            return bitmap;
        }
    }



}