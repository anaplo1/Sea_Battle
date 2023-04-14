package my.sea.battle.auth.login;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;
import my.sea.battle.auth.PasswordEncoder;
import my.sea.battle.db.SeaBattleDB;
import my.sea.battle.db.SharedPrefsManager;
import my.sea.battle.db.UserEntity;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> isUserRegisterLiveData = new MutableLiveData();

    LiveData<Boolean> getUserRegisterLiveData() {
        return isUserRegisterLiveData;
    }

    public LoginViewModel() {
    }

    void loginUser(Editable email, Editable password, Context context) {
        new Thread(() -> {
            Boolean isUserRegister;
            SeaBattleDB db = Room.databaseBuilder(context,
                    SeaBattleDB.class, "sea-battle-database").build();
            isUserRegister = isUserExist(
                    db.seaBattleDao().getUsers(),
                    email.toString(),
                    PasswordEncoder.encodePassword(password.toString())
            );
            if (isUserRegister) {
                new SharedPrefsManager(context).saveEmail(email.toString());
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                isUserRegisterLiveData.postValue(isUserRegister);
            });
        }).start();
    }

    public Boolean isUserExist(List<UserEntity> users, String email, String password) {
        for (UserEntity user : users) {
            if (user.email.equals(email) && user.password.equals(password)) {
                return true; // Если найдено совпадение, возвращаем true
            }
        }
        return false; // Если совпадение не найдено, возвращаем false
    }
}
