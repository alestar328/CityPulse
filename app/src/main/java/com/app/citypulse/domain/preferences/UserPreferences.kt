import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    // Claves para guardar los datos
    private val EMAIL_KEY = "email"
    private val PASSWORD_KEY = "password"

    // Métodos para guardar y recuperar los datos
    fun saveUserData(email: String, password: String) {
        preferences.edit().apply {
            putString(EMAIL_KEY, email)
            putString(PASSWORD_KEY, password)
            apply()  // Para guardar de manera asíncrona
        }
    }

    fun getUserEmail(): String? {
        return preferences.getString(EMAIL_KEY, null)
    }

    fun getUserPassword(): String? {
        return preferences.getString(PASSWORD_KEY, null)
    }

    fun clearUserData() {
        preferences.edit().clear().apply()
    }
}
