package my.sea.battle.game_fragment.ship;

//Двухпалубный корабль
public class TwoCellShip extends Ship {

    public Boolean isShootOneTime = false;

    public TwoCellShip() {
        super(2);
    }

    @Override
    public void shootShip() {
        if (isShootOneTime) {
            isDead = true;
        } else {
            isShootOneTime = true;
        }
    }
}
