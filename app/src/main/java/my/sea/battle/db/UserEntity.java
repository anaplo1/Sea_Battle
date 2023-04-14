package my.sea.battle.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Описание ячейки в таблице пользователей
@Entity(tableName = "Users")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String email;
    public String password;

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
