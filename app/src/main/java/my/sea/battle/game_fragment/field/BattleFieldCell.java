package my.sea.battle.game_fragment.field;

import my.sea.battle.game_fragment.ship.Ship;

//Класс описывающий одну ячейку на экране
public class BattleFieldCell {
    public Boolean isLetterCell = false;
    public String symbol = null;
    public Ship ship = null;
    public Boolean isShipDead = false;
    public Boolean hasShip = false;
    public Boolean isShot = false;
    public Boolean isTwoCellShipLeft = false;
    public Boolean isTwoCellShipRight = false;
    public Boolean isThreeCellShipLeft = false;
    public Boolean isThreeCellShipMiddle = false;
    public Boolean isThreeCellShipRight = false;

    public BattleFieldCell() {

    }

    public void setLetterCell() {
        this.isLetterCell = true;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setHasShip(Boolean hasShip) {
        this.hasShip = hasShip;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void shotCell() {
        if (ship != null) {
            ship.shootShip();
            isShipDead = ship.isDead;
        }
        this.isShot = true;
    }

    public String getSymbol() {
        return symbol;
    }

}
