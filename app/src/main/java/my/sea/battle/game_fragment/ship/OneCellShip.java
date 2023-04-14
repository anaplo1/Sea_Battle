package my.sea.battle.game_fragment.ship;

//Однопалубный корабль
public class OneCellShip extends Ship {

    public OneCellShip() {
        super(1);
    }

    @Override
    public void shootShip() {
        this.isDead = true;
    }

}
