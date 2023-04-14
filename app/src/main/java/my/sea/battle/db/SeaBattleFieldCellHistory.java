package my.sea.battle.db;

//Класс обертка для хранения в бд
public class SeaBattleFieldCellHistory {

    public Boolean isLetterCell;
    public String symbol;
    public Boolean hasShip;
    public Boolean isShot;

    public SeaBattleFieldCellHistory(Boolean isLetterCell, String symbol, Boolean hasShip, Boolean isShot) {
        this.isLetterCell = isLetterCell;
        this.symbol = symbol;
        this.hasShip = hasShip;
        this.isShot = isShot;
    }
}
