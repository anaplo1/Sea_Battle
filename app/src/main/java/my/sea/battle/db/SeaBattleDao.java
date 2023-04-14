package my.sea.battle.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

//Интерфейс описывающий все нужные запросы к бд
@Dao
public interface SeaBattleDao {

    @Insert
    void insertNewGame(SeaBattleHistory user);

    @Query("SELECT * FROM SeaBattleHistory")
    List<SeaBattleHistory> getFullHistory();

    @Query("SELECT * FROM Users")
    List<UserEntity> getUsers();

    @Insert
    void insertNewUser(UserEntity user);

}
