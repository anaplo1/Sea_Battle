package my.sea.battle.game_fragment;

import java.util.ArrayList;
import java.util.List;

import my.sea.battle.game_fragment.field.BattleFieldCell;
import my.sea.battle.game_fragment.field.SeaBattleField;
import my.sea.battle.game_fragment.ship.MineShip;
import my.sea.battle.game_fragment.ship.OneCellShip;
import my.sea.battle.game_fragment.ship.Ship;
import my.sea.battle.game_fragment.ship.ThreeCellShip;
import my.sea.battle.game_fragment.ship.TwoCellShip;

public class GameViewModelFunHolder {

    //Исключаем ячейки с символами из возможных ячеек
    public static Boolean isPositionForShip(Integer position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 18:
            case 27:
            case 36:
            case 45:
            case 54:
            case 63:
            case 72:
                return true;
            default:
                return false;
        }
    }

    //Генерирует пустое поле с буквенными и цифровыми символами по бокам
    public static SeaBattleField getStartBattleField() {
        ArrayList<BattleFieldCell> battleFieldCellList = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            if (i == 0) {
                BattleFieldCell letterFieldCell = new BattleFieldCell();
                letterFieldCell.setLetterCell();
                letterFieldCell.setSymbol("");
                battleFieldCellList.add(letterFieldCell);
                continue;
            }
            if (i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6 || i == 7 || i == 8) {
                BattleFieldCell letterFieldCell = new BattleFieldCell();
                letterFieldCell.setLetterCell();
                letterFieldCell.setSymbol(processNumber(i));
                battleFieldCellList.add(letterFieldCell);
                continue;
            }
            if (i == 9 || i == 18 || i == 27 || i == 36 || i == 45 || i == 54 || i == 63 || i == 72) {
                BattleFieldCell letterFieldCell = new BattleFieldCell();
                letterFieldCell.setLetterCell();
                letterFieldCell.setSymbol(processSymbol(i));
                battleFieldCellList.add(letterFieldCell);
                continue;
            }
            battleFieldCellList.add(new BattleFieldCell());
        }
        return new SeaBattleField(
                battleFieldCellList
        );
    }

    //Удобно преобразуем числа в строки
    private static String processNumber(Integer i) {
        return String.valueOf(i);
    }

    //Выводим символы по позиции для вертикального размещения
    public static String processSymbol(Integer i) {
        if (i == 9) return "A";
        if (i == 18) return "B";
        if (i == 27) return "C";
        if (i == 36) return "D";
        if (i == 45) return "E";
        if (i == 54) return "F";
        if (i == 63) return "G";
        return "H";
    }

    //Список начальных кораблей
    public static List<Ship> getStartShips() {
        ArrayList<Ship> shipList = new ArrayList();
        shipList.add(new OneCellShip());
        shipList.add(new OneCellShip());
        shipList.add(new MineShip());
        shipList.add(new MineShip());
        shipList.add(new TwoCellShip());
        shipList.add(new TwoCellShip());
        shipList.add(new ThreeCellShip());
        return shipList;
    }

}
