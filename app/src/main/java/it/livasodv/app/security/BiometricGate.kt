package it.livasodv.app.security
import androidx.biometric.BiometricManager
object BiometricPolicy {
 const val AUTO_LOGOUT_MS=180_000L
 fun authenticators()=BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
}
