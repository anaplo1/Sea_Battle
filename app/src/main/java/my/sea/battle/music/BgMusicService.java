package my.sea.battle.music;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

// Сервис для проигрывания музыки
public class BgMusicService extends Service {
    MediaPlayer player;

    public BgMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //Запускает плеер при получении команды с интента
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return Service.START_STICKY;
    }

    // При старте инитит плеер и запускает его с зацикливанием
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this,
                getResources().getIdentifier("main_theme", "raw", getPackageName()));
        player.setLooping(true);
        player.start();
    }

    // Останавливает с привязкой
    public void onStop() {
        player.stop();
    }

    // Уничтожает плеер, когда пользователь убил приложение
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }
}
