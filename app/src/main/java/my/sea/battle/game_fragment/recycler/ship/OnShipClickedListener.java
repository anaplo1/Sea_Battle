package my.sea.battle.game_fragment.recycler.ship;

import my.sea.battle.game_fragment.ship.Ship;

//Интерфейс для передачи информации о выборе корабля для установки
public interface OnShipClickedListener {

    void onShipClicked(Ship ship);

}
