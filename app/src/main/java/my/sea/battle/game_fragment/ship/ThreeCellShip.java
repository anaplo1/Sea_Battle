package my.sea.battle.game_fragment.ship;

//Трехпалубный корабль
public class ThreeCellShip extends Ship {

    public Boolean isShootOneTime = false;
    public Boolean isShootTwoTime = false;

    public ThreeCellShip() {
        super(3);
    }

    @Override
    public void shootShip() {
        if (isShootOneTime && isShootTwoTime) {
            isDead = true;
        } else if (isShootOneTime) {
            isShootTwoTime = true;
        } else {
            isShootOneTime = true;
        }
    }
}
