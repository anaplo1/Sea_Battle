package my.sea.battle.game_fragment.ship;

//Класс, который позволяет нам обобщить все корабли к одной сущности
public abstract class Ship {
    public Integer cellCount;
    public Boolean isDead = false;

    public Ship(Integer cellCount) {
        this.cellCount = cellCount;
    }

    public abstract void shootShip();
}
