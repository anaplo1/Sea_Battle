package my.sea.battle.db;

import java.util.List;

//Класс-обертка для хранения в бд
public class SeaBattleFieldHistory {

    public List<SeaBattleFieldCellHistory> seaBattleFieldHistoryList;

    public SeaBattleFieldHistory(List<SeaBattleFieldCellHistory> seaBattleFieldHistoryList) {
        this.seaBattleFieldHistoryList = seaBattleFieldHistoryList;
    }

}
