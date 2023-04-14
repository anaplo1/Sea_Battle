package my.sea.battle.game_fragment.ship;

//Класс описывающий мину
public class MineShip extends Ship {

    public MineShip() {
        super(1);
    }

    @Override
    public void shootShip() {
        this.isDead = true;
    }
}
