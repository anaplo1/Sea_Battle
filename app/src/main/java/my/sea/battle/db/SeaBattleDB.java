package my.sea.battle.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

//Класс бд, хранит в себе таблицы - Entity и TypeConverter'ы для преобразования сложных классов
@Database(entities = {SeaBattleHistory.class, UserEntity.class}, version = 1)
@TypeConverters({SeaBattleFieldConverter.class})
public abstract class SeaBattleDB extends RoomDatabase {
    //Тут мы имеем ссылку на дао после создания рума
    public abstract SeaBattleDao seaBattleDao();
}
