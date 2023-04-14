package my.sea.battle;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import my.sea.battle.db.SharedPrefsManager;
import my.sea.battle.music.BgMusicService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Запускаем сервис проигрывающий музыку
        startService(new Intent(this, BgMusicService.class));
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        if (!new SharedPrefsManager(this).getEmail().equals("")) {
            // Открываем фрагмент с меню
            if (savedInstanceState == null) {
                navController.navigate(R.id.menuFragment);
            }
        } else {
            // Переходим в авторизацию
            navController.navigate(R.id.loginFragment);
        }
    }

    // Отключаем программное нажатие назад
    @Override
    public void onBackPressed() {
    }
}
