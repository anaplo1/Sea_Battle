package my.sea.battle.auth.register;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;
import my.sea.battle.auth.PasswordEncoder;
import my.sea.battle.db.SeaBattleDB;
import my.sea.battle.db.UserEntity;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<Boolean> isUserRegisterLiveData = new MutableLiveData();

    LiveData<Boolean> getUserRegisterLiveData() {
        return isUserRegisterLiveData;
    }

    public RegisterViewModel() {
    }

    void registerUser(Editable email, Editable password, Context context) {
        new Thread(() -> {
            SeaBattleDB db = Room.databaseBuilder(context,
                    SeaBattleDB.class, "sea-battle-database").build();
            db.seaBattleDao().insertNewUser(new UserEntity(email.toString(), PasswordEncoder.encodePassword(password.toString())));
            new Handler(Looper.getMainLooper()).post(() -> {
                isUserRegisterLiveData.postValue(true);
            });
        }).start();
    }
}
