package my.sea.battle.game_fragment.field;

import java.util.List;

//Класс - поле битвы
public class SeaBattleField {

    public List<BattleFieldCell> battleFieldCells;

    public SeaBattleField(List<BattleFieldCell> battleFieldCells) {
        this.battleFieldCells = battleFieldCells;
    }

    public List<BattleFieldCell> getBattleFieldCellList() {
        return battleFieldCells;
    }
}
