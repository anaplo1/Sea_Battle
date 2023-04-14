package my.sea.battle.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import androidx.room.TypeConverter;

//Конвертер для сложного класса, чтобы сохранить его в бд
public class SeaBattleFieldConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static SeaBattleFieldHistory fromString(String value) {
        Type listType = new TypeToken<List<SeaBattleFieldCellHistory>>() {
        }.getType();
        List<SeaBattleFieldCellHistory> cells = gson.fromJson(value, listType);
        return new SeaBattleFieldHistory(cells);
    }

    @TypeConverter
    public static String fromSeaBattleField(SeaBattleFieldHistory field) {
        return gson.toJson(field.seaBattleFieldHistoryList);
    }

}
