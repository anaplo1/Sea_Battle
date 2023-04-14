package my.sea.battle.db;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {

    private static final String PREFS_NAME = "MyPrefs"; // Имя файла SharedPreferences
    private static final String KEY_EMAIL = "email"; // Ключ для хранения email
    private Context context;

    public SharedPrefsManager(Context context){
        this.context = context;
    }

    // Метод для сохранения значения email в SharedPreferences
    public void saveEmail(String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Метод для извлечения значения email из SharedPreferences
    public String getEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    // Метод для удаления всех значений из SharedPreferences
    public void clearSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
