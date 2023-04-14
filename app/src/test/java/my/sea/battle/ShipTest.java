package my.sea.battle;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import my.sea.battle.game_fragment.field.BattleFieldCell;
import my.sea.battle.game_fragment.field.SeaBattleField;
import my.sea.battle.game_fragment.ship.MineShip;
import my.sea.battle.game_fragment.ship.OneCellShip;
import my.sea.battle.game_fragment.ship.Ship;
import my.sea.battle.game_fragment.ship.ThreeCellShip;
import my.sea.battle.game_fragment.ship.TwoCellShip;
import static my.sea.battle.game_fragment.GameViewModelFunHolder.getStartBattleField;
import static my.sea.battle.game_fragment.GameViewModelFunHolder.getStartShips;
import static my.sea.battle.game_fragment.GameViewModelFunHolder.isPositionForShip;
import static my.sea.battle.game_fragment.GameViewModelFunHolder.processSymbol;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ShipTest {

    private List<Ship> startShips;

    @Before
    public void setUp() {
        startShips = getStartShips();
    }

    @Test
    public void testStartShipsSize() {
        assertEquals("Неправильное количество начальных кораблей", 7, startShips.size());
    }

    @Test
    public void testStartShipsContent() {
        assertTrue("Отсутствует OneCellShip в начальных кораблях", containsShipType(OneCellShip.class));
        assertTrue("Отсутствует MineShip в начальных кораблях", containsShipType(MineShip.class));
        assertTrue("Отсутствует TwoCellShip в начальных кораблях", containsShipType(TwoCellShip.class));
        assertTrue("Отсутствует ThreeCellShip в начальных кораблях", containsShipType(ThreeCellShip.class));
    }

    private boolean containsShipType(Class<? extends Ship> shipClass) {
        for (Ship ship : startShips) {
            if (ship.getClass().equals(shipClass)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testProcessSymbol() {
        assertEquals("A", processSymbol(9));
        assertEquals("B", processSymbol(18));
        assertEquals("C", processSymbol(27));
        assertEquals("D", processSymbol(36));
        assertEquals("E", processSymbol(45));
        assertEquals("F", processSymbol(54));
        assertEquals("G", processSymbol(63));
        assertEquals("H", processSymbol(72));
    }

    @Test
    public void testGetStartBattleField() {
        // Вызываем тестируемый метод
        SeaBattleField seaBattleField = getStartBattleField();

        // Проверяем результаты
        assertNotNull(seaBattleField); // Проверяем, что результат не null
        assertEquals(81, seaBattleField.getBattleFieldCellList().size()); // Проверяем размер списка

        // Проверяем состояние объектов, созданных внутри метода
        List<BattleFieldCell> battleFieldCellList = seaBattleField.getBattleFieldCellList();
        BattleFieldCell letterFieldCell = battleFieldCellList.get(0);
        BattleFieldCell emptyFieldCell = battleFieldCellList.get(9);
        assertEquals("", letterFieldCell.getSymbol()); // Проверяем символ для letterFieldCell
        assertEquals("A", emptyFieldCell.getSymbol()); // Проверяем символ для emptyFieldCell
    }


    @Test
    public void testIsPositionForShip() {
        // Создаем экземпляр класса, содержащего тестируемый метод

        // Проверяем значения, для которых ожидается true
        assertTrue(isPositionForShip(0));
        assertTrue(isPositionForShip(1));
        assertTrue(isPositionForShip(2));
        assertTrue(isPositionForShip(3));
        assertTrue(isPositionForShip(4));
        assertTrue(isPositionForShip(5));
        assertTrue(isPositionForShip(6));
        assertTrue(isPositionForShip(7));
        assertTrue(isPositionForShip(8));
        assertTrue(isPositionForShip(9));
        assertTrue(isPositionForShip(18));
        assertTrue(isPositionForShip(27));
        assertTrue(isPositionForShip(36));
        assertTrue(isPositionForShip(45));
        assertTrue(isPositionForShip(54));
        assertTrue(isPositionForShip(63));
        assertTrue(isPositionForShip(72));

        // Проверяем значения, для которых ожидается false
        assertFalse(isPositionForShip(10));
        assertFalse(isPositionForShip(17));
        assertFalse(isPositionForShip(19));
        assertFalse(isPositionForShip(26));
        assertFalse(isPositionForShip(28));
        assertFalse(isPositionForShip(35));
        assertFalse(isPositionForShip(37));
        assertFalse(isPositionForShip(44));
        assertFalse(isPositionForShip(46));
        assertFalse(isPositionForShip(53));
        assertFalse(isPositionForShip(55));
        assertFalse(isPositionForShip(62));
        assertFalse(isPositionForShip(64));
        assertFalse(isPositionForShip(71));
        assertFalse(isPositionForShip(73));
        assertFalse(isPositionForShip(80));
        assertFalse(isPositionForShip(81));
    }
}
