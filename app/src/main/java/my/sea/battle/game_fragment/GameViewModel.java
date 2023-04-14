package my.sea.battle.game_fragment;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import my.sea.battle.game_fragment.field.BattleFieldCell;
import my.sea.battle.game_fragment.field.GameResult;
import my.sea.battle.game_fragment.field.SeaBattleField;
import my.sea.battle.game_fragment.ship.MineShip;
import my.sea.battle.game_fragment.ship.OneCellShip;
import my.sea.battle.game_fragment.ship.Ship;
import my.sea.battle.game_fragment.ship.ThreeCellShip;
import my.sea.battle.game_fragment.ship.TwoCellShip;

import static my.sea.battle.game_fragment.GameViewModelFunHolder.getStartBattleField;
import static my.sea.battle.game_fragment.GameViewModelFunHolder.getStartShips;

public class GameViewModel extends ViewModel {

    //LiveData для обзора поля игрока
    MutableLiveData<SeaBattleField> playerFieldLiveData = new MutableLiveData<>(getStartBattleField());
    //LiveData для обзора поля бота
    MutableLiveData<SeaBattleField> botFieldLiveData = new MutableLiveData<>(getStartBattleField());
    //LiveData для обзора списка кораблей
    MutableLiveData<List<Ship>> shipsLiveData = new MutableLiveData<>(getStartShips());
    //LiveData для обзора ошибок
    MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    //LiveData для обзора начала процесса игры
    MutableLiveData<Boolean> gameStartedLiveData = new MutableLiveData<>(false);
    //LiveData для обзора текущего хода
    MutableLiveData<Boolean> needToTurnLiveData = new MutableLiveData<>();
    //LiveData для обзора результатов игры после окончания
    MutableLiveData<GameResult> gameResultLiveData = new MutableLiveData<>();
    //LiveData для передачи вибрации
    MutableLiveData<Boolean> needVibrate = new MutableLiveData<>();
    //LiveData для доп. хода
    MutableLiveData<Boolean> isUserHitShip = new MutableLiveData<>();
    //Текущий выбранный из списка корабль
    Ship clickedShip = null;

    private volatile Boolean isUserNeedSkipTurn = false;
    private volatile Boolean isBotNeedSkipTurn = false;

    //Метод, который срабатывает при выборе корабля, сохраняем тут наш выбор
    void setClickedShip(Ship ship) {
        this.clickedShip = ship;
    }

    //После выбора при нажатии на поле пользователя устанавливает туда корабль, проверяя проходит ли он по размерам
    //И нет ли рядом с ним других кораблей
    //Если установить его можно, то устанавливает и передает новую вариацию поля через LiveData
    //Если нет, то выбрасывает ошибку в виде всплывающего сообщения через LiveData
    void setShipToPlayerField(Integer position) {
        SeaBattleField currentBattleField = playerFieldLiveData.getValue();
        if (clickedShip == null || currentBattleField == null || !canPlaceShip(position, currentBattleField)) {
            errorLiveData.postValue("Тут невозможно расположить корабль");
            return;
        }
        for (int i = 0; i < currentBattleField.battleFieldCells.size(); i++) {
            if (currentBattleField.battleFieldCells.get(i).isShot) {
                currentBattleField.battleFieldCells.get(i).isShot = false;
            }
        }
        if (clickedShip instanceof OneCellShip) {
            currentBattleField.battleFieldCells.get(position).setHasShip(true);
            currentBattleField.battleFieldCells.get(position).setShip(clickedShip);
        }
        if (clickedShip instanceof TwoCellShip) {
            currentBattleField.battleFieldCells.get(position).setHasShip(true);
            currentBattleField.battleFieldCells.get(position + 1).setHasShip(true);
            currentBattleField.battleFieldCells.get(position).setShip(clickedShip);
            currentBattleField.battleFieldCells.get(position + 1).setShip(clickedShip);
            currentBattleField.battleFieldCells.get(position).isTwoCellShipLeft = true;
            currentBattleField.battleFieldCells.get(position).isTwoCellShipRight = true;
        }
        if (clickedShip instanceof ThreeCellShip) {
            currentBattleField.battleFieldCells.get(position).setHasShip(true);
            currentBattleField.battleFieldCells.get(position + 1).setHasShip(true);
            currentBattleField.battleFieldCells.get(position + 2).setHasShip(true);
            currentBattleField.battleFieldCells.get(position).setShip(clickedShip);
            currentBattleField.battleFieldCells.get(position + 1).setShip(clickedShip);
            currentBattleField.battleFieldCells.get(position + 2).setShip(clickedShip);
            currentBattleField.battleFieldCells.get(position).isThreeCellShipLeft = true;
            currentBattleField.battleFieldCells.get(position + 1).isThreeCellShipMiddle = true;
            currentBattleField.battleFieldCells.get(position + 2).isThreeCellShipRight = true;
        }
        if (clickedShip instanceof MineShip) {
            currentBattleField.battleFieldCells.get(position).setHasShip(true);
            currentBattleField.battleFieldCells.get(position).setShip(clickedShip);
        }
        playerFieldLiveData.postValue(currentBattleField);
        ArrayList<Ship> currentShipList = (ArrayList<Ship>) shipsLiveData.getValue();
        if (currentShipList != null) {
            currentShipList.remove(clickedShip);
            clickedShip = null;
        }
        shipsLiveData.postValue(currentShipList);
    }

    //Срабатывает при нажатии на кнопку старта, начинает игру, передает ход игроку
    //Если корабли не расставлены, то выводит ошибку
    void startGame() {
        if (shipsLiveData.getValue() == null) return;
        if (shipsLiveData.getValue().isEmpty()) {
            generateShipsToBot();
            gameStartedLiveData.postValue(true);
            needToTurnLiveData.postValue(true);
        } else {
            errorLiveData.postValue("Расставьте все корабли");
        }
    }

    //Метод, который рандомной расставляет корабли для бота, работает по аналогии с пользовательским методом
    Boolean setShipToBotField(Integer position) {
        SeaBattleField currentBattleField = botFieldLiveData.getValue();
        if (clickedShip == null || currentBattleField == null || !canPlaceShip(position, currentBattleField))
            return false;
        if (clickedShip instanceof OneCellShip) {
            currentBattleField.battleFieldCells.get(position).setHasShip(true);
            currentBattleField.battleFieldCells.get(position).setShip(clickedShip);
        }
        if (clickedShip instanceof TwoCellShip) {
            currentBattleField.battleFieldCells.get(position).setHasShip(true);
            currentBattleField.battleFieldCells.get(position + 1).setHasShip(true);
            currentBattleField.battleFieldCells.get(position).setShip(clickedShip);
            currentBattleField.battleFieldCells.get(position + 1).setShip(clickedShip);
        }
        if (clickedShip instanceof ThreeCellShip) {
            currentBattleField.battleFieldCells.get(position).setHasShip(true);
            currentBattleField.battleFieldCells.get(position + 1).setHasShip(true);
            currentBattleField.battleFieldCells.get(position + 2).setHasShip(true);
            currentBattleField.battleFieldCells.get(position).setShip(clickedShip);
            currentBattleField.battleFieldCells.get(position + 1).setShip(clickedShip);
            currentBattleField.battleFieldCells.get(position + 2).setShip(clickedShip);
        }
        if (clickedShip instanceof MineShip) {
            currentBattleField.battleFieldCells.get(position).setHasShip(true);
            currentBattleField.battleFieldCells.get(position).setShip(clickedShip);
        }
        botFieldLiveData.postValue(currentBattleField);
        return true;
    }

    //Метод который срабатывает при выстреле в ячейку бота
    //Тут происходит проверка на то, попал ли выстрел в корабль
    //И если он его уничтожил, то так же взрываются соседние клетки
    //Работает это через метод processBotShipDead()
    void onCellShot(Integer position) {
        if (gameStartedLiveData.getValue() == null) return;
        if (!gameStartedLiveData.getValue()) return;
        SeaBattleField currentField = botFieldLiveData.getValue();
        if (currentField == null) return;
        currentField.battleFieldCells.get(position).shotCell();
        if (currentField.battleFieldCells.get(position).hasShip) {
            needVibrate.postValue(true);
            if (currentField.battleFieldCells.get(position).ship.isDead) {
                processBotShipDead(position, currentField.battleFieldCells.get(position).ship);
            }
            isUserHitShip.postValue(true);
            botFieldLiveData.postValue(currentField);
        } else {
            botFieldLiveData.postValue(currentField);
            needToTurnLiveData.postValue(false);
            processBotTurn();
        }
    }

    //Проверка на то, закончена ли игра, срабатывает после каждого хода
    void checkIsPlayerWin() {
        if (gameStartedLiveData.getValue() == null) return;
        if (gameStartedLiveData.getValue()) {
            processPlayerWin();
        }
    }

    //Проверка на то, закончена ли игра, срабатывает после каждого хода
    void checkIsBotWin() {
        if (gameStartedLiveData.getValue() == null) return;
        if (gameStartedLiveData.getValue()) {
            processBotWin();
        }
    }

    //Реализация проверки на то, закончена ли игра, срабатывает после каждого хода
    private void processBotWin() {
        SeaBattleField playerField = playerFieldLiveData.getValue();
        boolean isBotWin = true;
        if (playerField == null) return;
        for (int i = 0; i < playerField.battleFieldCells.size(); i++) {
            if (playerField.battleFieldCells.get(i).hasShip) {
                if (!playerField.battleFieldCells.get(i).ship.isDead) {
                    isBotWin = false;
                }
            }
        }
        if (isBotWin) {
            gameResultLiveData.postValue(new GameResult(
                    false, playerFieldLiveData.getValue(), botFieldLiveData.getValue()
            ));
            gameStartedLiveData.postValue(false);
            playerFieldLiveData.postValue(getStartBattleField());
            botFieldLiveData.postValue(getStartBattleField());
            shipsLiveData.postValue(getStartShips());
            needToTurnLiveData.postValue(true);
        } else {
            needToTurnLiveData.postValue(true);
        }
    }

    //Реализация проверки на то, закончена ли игра, срабатывает после каждого хода
    private void processPlayerWin() {
        SeaBattleField botField = botFieldLiveData.getValue();
        boolean isPlayerWin = true;
        if (botField == null) return;
        for (int i = 0; i < botField.battleFieldCells.size(); i++) {
            if (botField.battleFieldCells.get(i).hasShip) {
                if (!botField.battleFieldCells.get(i).ship.isDead) {
                    isPlayerWin = false;
                }
            }
        }
        if (isPlayerWin) {
            new Handler().postDelayed(() -> {
                gameResultLiveData.postValue(new GameResult(
                        true, playerFieldLiveData.getValue(), botFieldLiveData.getValue()
                ));
                gameStartedLiveData.postValue(false);
                playerFieldLiveData.postValue(getStartBattleField());
                botFieldLiveData.postValue(getStartBattleField());
                shipsLiveData.postValue(getStartShips());
                needToTurnLiveData.postValue(true);
            }, 200);
        } else {
            needToTurnLiveData.postValue(false);
        }
    }

    //Тут описана логика хода бота, он ищет ячейку, в которую можно будет выстрелить,
    //После этого идет такая же валидация, как и у выстрела пользователя
    //Проверяется выстрел в корабль и его взрыв.
    private void processBotTurn() {
        if (isBotNeedSkipTurn) {
            isBotNeedSkipTurn = false;
            needToTurnLiveData.postValue(true);
            return;
        }
        new Handler().postDelayed(() -> {
            SeaBattleField playerField = playerFieldLiveData.getValue();
            if (playerField == null) return;
            ArrayList<Integer> nonShootCells = new ArrayList<>();
            for (int i = 0; i < playerField.battleFieldCells.size(); i++) {
                if (!playerField.battleFieldCells.get(i).isShot && !playerField.battleFieldCells.get(i).isLetterCell && playerField.battleFieldCells.get(i).symbol == null) {
                    nonShootCells.add(i);
                }
            }
            int randomCellIndex = new Random().nextInt(nonShootCells.size());
            playerField.battleFieldCells.get(nonShootCells.get(randomCellIndex)).shotCell();
            if (playerField.battleFieldCells.get(nonShootCells.get(randomCellIndex)).hasShip) {
                needVibrate.postValue(true);
                if (playerField.battleFieldCells.get(nonShootCells.get(randomCellIndex)).ship.isDead) {
                    processPlayerShipDead(nonShootCells.get(randomCellIndex), playerField.battleFieldCells.get(nonShootCells.get(randomCellIndex)).ship);
                }
                isUserHitShip.postValue(false);
                playerFieldLiveData.postValue(playerField);
                processBotTurn();
            } else {
                if (gameStartedLiveData.getValue() == null) return;
                if (gameStartedLiveData.getValue()) {
                    if (isUserNeedSkipTurn) {
                        processBotTurn();
                        isUserNeedSkipTurn = false;
                        playerFieldLiveData.postValue(playerField);
                    } else {
                        playerFieldLiveData.postValue(playerField);
                        needToTurnLiveData.postValue(true);
                    }
                }
            }
        }, 500);
    }

    //Метод для проверки того, взорвался ли целиком корабль бота
    private void processBotShipDead(Integer position, Ship ship) {
        if (ship instanceof OneCellShip) {
            processBotOneCellShipDead(position);
        }

        if (ship instanceof TwoCellShip) {
            processBotTwoCellShipDead(position);
        }

        if (ship instanceof ThreeCellShip) {
            processBotThreeCellShipDead(position);
        }

        if (ship instanceof MineShip) {
            errorLiveData.postValue("Вы попали в мину, бот делает 2 хода");
            isUserNeedSkipTurn = true;
            processBotOneCellShipDead(position);
        }
        processBotTurn();
    }

    //Метод для проверки того, взорвался ли целиком корабль пользователя
    private void processPlayerShipDead(Integer position, Ship ship) {
        if (ship instanceof OneCellShip) {
            processPlayerOneCellShipDead(position);
        }

        if (ship instanceof TwoCellShip) {
            processPlayerTwoCellShipDead(position);
        }

        if (ship instanceof ThreeCellShip) {
            processPlayerThreeCellShipDead(position);
        }

        if (ship instanceof MineShip) {
            errorLiveData.postValue("Бот попал в мину, вы делаете 2 хода");
            isBotNeedSkipTurn = true;
            processPlayerOneCellShipDead(position);
        }
    }

    //Проверка по ячейкам взорвался ли целиком однопалубный корабль бота
    //В дальнейшем этот метод используется для взрыва многопалубных кораблей бота,
    //Т.к. любой многопалубный корабль - это представление однопалубных ячеек
    private void processBotOneCellShipDead(Integer position) {
        SeaBattleField botField = botFieldLiveData.getValue();
        if (botField == null) return;
        Boolean isFirstColumn = position == 10 || position == 19 || position == 28 || position == 37 || position == 46 || position == 55 || position == 64 || position == 73;
        Boolean isFirstLine = position == 10 || position == 11 || position == 12 || position == 13 || position == 14 || position == 15 || position == 16 || position == 17;
        Boolean isLastColumn = position == 17 || position == 26 || position == 35 || position == 44 || position == 53 || position == 62 || position == 71 || position == 80;
        Boolean isLastLine = position == 73 || position == 74 || position == 75 || position == 76 || position == 77 || position == 78 || position == 79 || position == 80;

        if (isFirstColumn && isFirstLine) {
            botField.battleFieldCells.get(position).shotCell();
            botField.battleFieldCells.get(position + 1).shotCell();
            botField.battleFieldCells.get(position + 9).shotCell();
            botField.battleFieldCells.get(position + 10).shotCell();
            botFieldLiveData.postValue(botField);
            needToTurnLiveData.postValue(false);
            return;
        }
        if (isFirstColumn && isLastLine) {
            botField.battleFieldCells.get(position).shotCell();
            botField.battleFieldCells.get(position - 9).shotCell();
            botField.battleFieldCells.get(position + 1).shotCell();
            botField.battleFieldCells.get(position - 8).shotCell();
            botFieldLiveData.postValue(botField);
            needToTurnLiveData.postValue(false);
            return;
        }
        if (isLastColumn && isFirstLine) {
            botField.battleFieldCells.get(position).shotCell();
            botField.battleFieldCells.get(position - 1).shotCell();
            botField.battleFieldCells.get(position + 9).shotCell();
            botField.battleFieldCells.get(position + 8).shotCell();
            botFieldLiveData.postValue(botField);
            needToTurnLiveData.postValue(false);
            return;
        }
        if (isLastColumn && isLastLine) {
            botField.battleFieldCells.get(position).shotCell();
            botField.battleFieldCells.get(position - 1).shotCell();
            botField.battleFieldCells.get(position - 9).shotCell();
            botField.battleFieldCells.get(position - 10).shotCell();
            botFieldLiveData.postValue(botField);
            needToTurnLiveData.postValue(false);
            return;
        }
        if (isFirstColumn) {
            botField.battleFieldCells.get(position).shotCell();
            botField.battleFieldCells.get(position + 1).shotCell();
            botField.battleFieldCells.get(position - 9).shotCell();
            botField.battleFieldCells.get(position + 9).shotCell();
            botField.battleFieldCells.get(position - 8).shotCell();
            botField.battleFieldCells.get(position + 10).shotCell();
            botFieldLiveData.postValue(botField);
            needToTurnLiveData.postValue(false);
            return;
        }
        if (isLastColumn) {
            botField.battleFieldCells.get(position).shotCell();
            botField.battleFieldCells.get(position - 1).shotCell();
            botField.battleFieldCells.get(position - 9).shotCell();
            botField.battleFieldCells.get(position + 9).shotCell();
            botField.battleFieldCells.get(position + 8).shotCell();
            botField.battleFieldCells.get(position - 10).shotCell();
            botFieldLiveData.postValue(botField);
            needToTurnLiveData.postValue(false);
            return;
        }
        if (isFirstLine) {
            botField.battleFieldCells.get(position).shotCell();
            botField.battleFieldCells.get(position - 1).shotCell();
            botField.battleFieldCells.get(position + 1).shotCell();
            botField.battleFieldCells.get(position + 9).shotCell();
            botField.battleFieldCells.get(position + 8).shotCell();
            botField.battleFieldCells.get(position + 10).shotCell();
            botFieldLiveData.postValue(botField);
            needToTurnLiveData.postValue(false);
            return;
        }
        if (isLastLine) {
            botField.battleFieldCells.get(position).shotCell();
            botField.battleFieldCells.get(position - 1).shotCell();
            botField.battleFieldCells.get(position + 1).shotCell();
            botField.battleFieldCells.get(position - 9).shotCell();
            botField.battleFieldCells.get(position - 8).shotCell();
            botField.battleFieldCells.get(position - 10).shotCell();
            botFieldLiveData.postValue(botField);
            needToTurnLiveData.postValue(false);
            return;
        }

        botField.battleFieldCells.get(position).shotCell();
        botField.battleFieldCells.get(position - 1).shotCell();
        botField.battleFieldCells.get(position + 1).shotCell();
        botField.battleFieldCells.get(position - 9).shotCell();
        botField.battleFieldCells.get(position + 9).shotCell();
        botField.battleFieldCells.get(position - 10).shotCell();
        botField.battleFieldCells.get(position - 8).shotCell();
        botField.battleFieldCells.get(position + 8).shotCell();
        botField.battleFieldCells.get(position + 10).shotCell();
        botFieldLiveData.postValue(botField);
        needToTurnLiveData.postValue(false);
    }

    //Проверка на взрыв двухпалубного
    private void processBotTwoCellShipDead(Integer position) {
        SeaBattleField botField = botFieldLiveData.getValue();
        if (botField == null) return;
        boolean isLastColumn = position == 17 || position == 26 || position == 35 || position == 44 || position == 53 || position == 62 || position == 71 || position == 80;
        if (isLastColumn) {
            processBotOneCellShipDead(position);
            processBotOneCellShipDead(position - 1);
            return;
        }
        if (botField.battleFieldCells.get(position + 1).hasShip) {
            processBotOneCellShipDead(position);
            processBotOneCellShipDead(position + 1);
            return;
        }
        if (botField.battleFieldCells.get(position - 1).hasShip) {
            processBotOneCellShipDead(position);
            processBotOneCellShipDead(position - 1);
        }
    }

    //Проверка на взрыв трехпалубного
    private void processBotThreeCellShipDead(Integer position) {
        SeaBattleField botField = botFieldLiveData.getValue();
        if (botField == null) return;
        boolean isLastColumn = position == 17 || position == 26 || position == 35 || position == 44 || position == 53 || position == 62 || position == 71 || position == 80;
        boolean isPreLastColumn = position == 16 || position == 25 || position == 34 || position == 43 || position == 52 || position == 61 || position == 70 || position == 79;
        if (isLastColumn) {
            processBotOneCellShipDead(position);
            processBotOneCellShipDead(position - 1);
            processBotOneCellShipDead(position - 2);
            return;
        }
        if (isPreLastColumn) {
            if (botField.battleFieldCells.get(position + 1).hasShip) {
                processBotOneCellShipDead(position);
                processBotOneCellShipDead(position + 1);
                processBotOneCellShipDead(position - 1);
                return;
            } else {
                processBotOneCellShipDead(position);
                processBotOneCellShipDead(position - 1);
                processBotOneCellShipDead(position - 2);
                return;
            }
        }

        if (botField.battleFieldCells.get(position + 1).hasShip) {
            if (botField.battleFieldCells.get(position + 2).hasShip) {
                processBotOneCellShipDead(position);
                processBotOneCellShipDead(position + 1);
                processBotOneCellShipDead(position + 2);
                return;
            } else {
                processBotOneCellShipDead(position);
                processBotOneCellShipDead(position + 1);
                processBotOneCellShipDead(position - 1);
                return;
            }
        }

        if (botField.battleFieldCells.get(position - 1).hasShip) {
            if (botField.battleFieldCells.get(position - 2).hasShip) {
                processBotOneCellShipDead(position);
                processBotOneCellShipDead(position - 1);
                processBotOneCellShipDead(position - 2);
            } else {
                processBotOneCellShipDead(position);
                processBotOneCellShipDead(position - 1);
                processBotOneCellShipDead(position + 1);
            }
        }
    }

    //Проверка на взрыв однопалубного корабля у игрока
    //Аналогично боту
    private void processPlayerOneCellShipDead(Integer position) {
        SeaBattleField playerField = playerFieldLiveData.getValue();
        if (playerField == null) return;
        Boolean isFirstColumn = position == 10 || position == 19 || position == 28 || position == 37 || position == 46 || position == 55 || position == 64 || position == 73;
        Boolean isFirstLine = position == 10 || position == 11 || position == 12 || position == 13 || position == 14 || position == 15 || position == 16 || position == 17;
        Boolean isLastColumn = position == 17 || position == 26 || position == 35 || position == 44 || position == 53 || position == 62 || position == 71 || position == 80;
        Boolean isLastLine = position == 73 || position == 74 || position == 75 || position == 76 || position == 77 || position == 78 || position == 79 || position == 80;

        if (isFirstColumn && isFirstLine) {
            playerField.battleFieldCells.get(position).shotCell();
            playerField.battleFieldCells.get(position + 1).shotCell();
            playerField.battleFieldCells.get(position + 9).shotCell();
            playerField.battleFieldCells.get(position + 10).shotCell();
            playerFieldLiveData.postValue(playerField);
            needToTurnLiveData.postValue(true);
            return;
        }
        if (isFirstColumn && isLastLine) {
            playerField.battleFieldCells.get(position).shotCell();
            playerField.battleFieldCells.get(position - 9).shotCell();
            playerField.battleFieldCells.get(position + 1).shotCell();
            playerField.battleFieldCells.get(position - 8).shotCell();
            playerFieldLiveData.postValue(playerField);
            needToTurnLiveData.postValue(true);
            return;
        }
        if (isLastColumn && isFirstLine) {
            playerField.battleFieldCells.get(position).shotCell();
            playerField.battleFieldCells.get(position - 1).shotCell();
            playerField.battleFieldCells.get(position + 9).shotCell();
            playerField.battleFieldCells.get(position + 8).shotCell();
            playerFieldLiveData.postValue(playerField);
            needToTurnLiveData.postValue(true);
            return;
        }
        if (isLastColumn && isLastLine) {
            playerField.battleFieldCells.get(position).shotCell();
            playerField.battleFieldCells.get(position - 1).shotCell();
            playerField.battleFieldCells.get(position - 9).shotCell();
            playerField.battleFieldCells.get(position - 10).shotCell();
            playerFieldLiveData.postValue(playerField);
            needToTurnLiveData.postValue(true);
            return;
        }
        if (isFirstColumn) {
            playerField.battleFieldCells.get(position).shotCell();
            playerField.battleFieldCells.get(position + 1).shotCell();
            playerField.battleFieldCells.get(position - 9).shotCell();
            playerField.battleFieldCells.get(position + 9).shotCell();
            playerField.battleFieldCells.get(position + 10).shotCell();
            playerField.battleFieldCells.get(position - 8).shotCell();
            playerFieldLiveData.postValue(playerField);
            needToTurnLiveData.postValue(true);
            return;
        }
        if (isLastColumn) {
            playerField.battleFieldCells.get(position).shotCell();
            playerField.battleFieldCells.get(position - 1).shotCell();
            playerField.battleFieldCells.get(position - 9).shotCell();
            playerField.battleFieldCells.get(position + 9).shotCell();
            playerField.battleFieldCells.get(position + 8).shotCell();
            playerField.battleFieldCells.get(position - 10).shotCell();
            playerFieldLiveData.postValue(playerField);
            needToTurnLiveData.postValue(true);
            return;
        }
        if (isFirstLine) {
            playerField.battleFieldCells.get(position).shotCell();
            playerField.battleFieldCells.get(position - 1).shotCell();
            playerField.battleFieldCells.get(position + 1).shotCell();
            playerField.battleFieldCells.get(position + 9).shotCell();
            playerField.battleFieldCells.get(position + 8).shotCell();
            playerField.battleFieldCells.get(position + 10).shotCell();
            playerFieldLiveData.postValue(playerField);
            needToTurnLiveData.postValue(true);
            return;
        }
        if (isLastLine) {
            playerField.battleFieldCells.get(position).shotCell();
            playerField.battleFieldCells.get(position - 1).shotCell();
            playerField.battleFieldCells.get(position + 1).shotCell();
            playerField.battleFieldCells.get(position - 9).shotCell();
            playerField.battleFieldCells.get(position - 8).shotCell();
            playerField.battleFieldCells.get(position - 10).shotCell();
            playerFieldLiveData.postValue(playerField);
            needToTurnLiveData.postValue(true);
            return;
        }

        playerField.battleFieldCells.get(position).shotCell();
        playerField.battleFieldCells.get(position - 1).shotCell();
        playerField.battleFieldCells.get(position + 1).shotCell();
        playerField.battleFieldCells.get(position - 9).shotCell();
        playerField.battleFieldCells.get(position + 9).shotCell();
        playerField.battleFieldCells.get(position - 10).shotCell();
        playerField.battleFieldCells.get(position - 8).shotCell();
        playerField.battleFieldCells.get(position + 8).shotCell();
        playerField.battleFieldCells.get(position + 10).shotCell();
        playerFieldLiveData.postValue(playerField);
        needToTurnLiveData.postValue(true);
    }

    //Проверка на взрыв двух корабля у игрока
    private void processPlayerTwoCellShipDead(Integer position) {
        SeaBattleField playerField = playerFieldLiveData.getValue();
        if (playerField == null) return;
        boolean isLastColumn = position == 17 || position == 26 || position == 35 || position == 44 || position == 53 || position == 62 || position == 71 || position == 80;
        if (isLastColumn) {
            processPlayerOneCellShipDead(position);
            processPlayerOneCellShipDead(position - 1);
            return;
        }
        if (playerField.battleFieldCells.get(position + 1).hasShip) {
            processPlayerOneCellShipDead(position);
            processPlayerOneCellShipDead(position + 1);
            return;
        }
        if (playerField.battleFieldCells.get(position - 1).hasShip) {
            processPlayerOneCellShipDead(position);
            processPlayerOneCellShipDead(position - 1);
        }
    }

    //Проверка на взрыв трехпалубного корабля у игрока
    private void processPlayerThreeCellShipDead(Integer position) {
        SeaBattleField playerField = playerFieldLiveData.getValue();
        if (playerField == null) return;
        boolean isLastColumn = position == 17 || position == 26 || position == 35 || position == 44 || position == 53 || position == 62 || position == 71 || position == 80;
        boolean isPreLastColumn = position == 16 || position == 25 || position == 34 || position == 43 || position == 52 || position == 61 || position == 70 || position == 79;
        if (isLastColumn) {
            processPlayerOneCellShipDead(position);
            processPlayerOneCellShipDead(position - 1);
            processPlayerOneCellShipDead(position - 2);
            return;
        }
        if (isPreLastColumn) {
            if (playerField.battleFieldCells.get(position + 1).hasShip) {
                processPlayerOneCellShipDead(position);
                processPlayerOneCellShipDead(position + 1);
                processPlayerOneCellShipDead(position - 1);
                return;
            } else {
                processPlayerOneCellShipDead(position);
                processPlayerOneCellShipDead(position - 1);
                processPlayerOneCellShipDead(position - 2);
                return;
            }
        }

        if (playerField.battleFieldCells.get(position + 1).hasShip) {
            if (playerField.battleFieldCells.get(position + 2).hasShip) {
                processPlayerOneCellShipDead(position);
                processPlayerOneCellShipDead(position + 1);
                processPlayerOneCellShipDead(position + 2);
                return;
            } else {
                processPlayerOneCellShipDead(position);
                processPlayerOneCellShipDead(position + 1);
                processPlayerOneCellShipDead(position - 1);
                return;
            }
        }

        if (playerField.battleFieldCells.get(position - 1).hasShip) {
            if (playerField.battleFieldCells.get(position - 2).hasShip) {
                processPlayerOneCellShipDead(position);
                processPlayerOneCellShipDead(position - 1);
                processPlayerOneCellShipDead(position - 2);
            } else {
                processPlayerOneCellShipDead(position);
                processPlayerOneCellShipDead(position - 1);
                processPlayerOneCellShipDead(position + 1);
            }
        }
    }

    //Метод, который проверяет, есть ли место для установки корабля на поле игрока
    private Boolean canPlaceShip(Integer position, SeaBattleField seaBattleField) {
        if (GameViewModelFunHolder.isPositionForShip(position)) {
            return false;
        }
        if (clickedShip instanceof OneCellShip && isPositionForOneCellShip(position, seaBattleField)) {
            return true;
        }
        if (clickedShip instanceof TwoCellShip && isPositionForTwoCellShip(position, seaBattleField)) {
            return true;
        }
        if (clickedShip instanceof ThreeCellShip && isPositionForThreeCellShip(position, seaBattleField)) {
            return true;
        }
        if (clickedShip instanceof MineShip && isPositionForOneCellShip(position, seaBattleField)) {
            return true;
        }
        return false;
    }

    //Проверяет влезет ли однопалубный корабль на точку, выбранную пользователем
    private Boolean isPositionForOneCellShip(Integer position, SeaBattleField seaBattleField) {
        if (seaBattleField == null) return false;
        Boolean isFirstColumn = position == 10 || position == 19 || position == 28 || position == 37 || position == 46 || position == 55 || position == 64 || position == 73;
        Boolean isFirstLine = position == 10 || position == 11 || position == 12 || position == 13 || position == 14 || position == 15 || position == 16 || position == 17;
        Boolean isLastColumn = position == 17 || position == 26 || position == 35 || position == 44 || position == 53 || position == 62 || position == 71 || position == 80;
        Boolean isLastLine = position == 73 || position == 74 || position == 75 || position == 76 || position == 77 || position == 78 || position == 79 || position == 80;

        if (isFirstColumn && isFirstLine) {
            return !seaBattleField.battleFieldCells.get(position + 1).hasShip && !seaBattleField.battleFieldCells.get(position + 9).hasShip && !seaBattleField.battleFieldCells.get(position + 10).hasShip;
        }
        if (isFirstColumn && isLastLine) {
            return !seaBattleField.battleFieldCells.get(position + 1).hasShip && !seaBattleField.battleFieldCells.get(position - 9).hasShip && !seaBattleField.battleFieldCells.get(position - 8).hasShip;
        }
        if (isLastColumn && isFirstLine) {
            return !seaBattleField.battleFieldCells.get(position - 1).hasShip && !seaBattleField.battleFieldCells.get(position + 9).hasShip && !seaBattleField.battleFieldCells.get(position + 8).hasShip;
        }
        if (isLastColumn && isLastLine) {
            return !seaBattleField.battleFieldCells.get(position - 1).hasShip && !seaBattleField.battleFieldCells.get(position - 9).hasShip && !seaBattleField.battleFieldCells.get(position - 10).hasShip;
        }
        if (isFirstColumn) {
            return !seaBattleField.battleFieldCells.get(position + 1).hasShip && !seaBattleField.battleFieldCells.get(position + 9).hasShip && !seaBattleField.battleFieldCells.get(position - 9).hasShip && !seaBattleField.battleFieldCells.get(position - 8).hasShip && !seaBattleField.battleFieldCells.get(position + 10).hasShip;
        }
        if (isFirstLine) {
            return !seaBattleField.battleFieldCells.get(position + 9).hasShip && !seaBattleField.battleFieldCells.get(position + 1).hasShip && !seaBattleField.battleFieldCells.get(position - 1).hasShip && !seaBattleField.battleFieldCells.get(position + 8).hasShip && !seaBattleField.battleFieldCells.get(position + 10).hasShip;
        }
        if (isLastColumn) {
            return !seaBattleField.battleFieldCells.get(position - 1).hasShip && !seaBattleField.battleFieldCells.get(position - 9).hasShip && !seaBattleField.battleFieldCells.get(position + 9).hasShip && !seaBattleField.battleFieldCells.get(position - 10).hasShip && !seaBattleField.battleFieldCells.get(position + 8).hasShip;
        }
        if (isLastLine) {
            return !seaBattleField.battleFieldCells.get(position - 9).hasShip && !seaBattleField.battleFieldCells.get(position - 1).hasShip && !seaBattleField.battleFieldCells.get(position + 1).hasShip && !seaBattleField.battleFieldCells.get(position - 10).hasShip && !seaBattleField.battleFieldCells.get(position - 8).hasShip;
        }
        return !seaBattleField.battleFieldCells.get(position - 9).hasShip && !seaBattleField.battleFieldCells.get(position + 9).hasShip && !seaBattleField.battleFieldCells.get(position + 1).hasShip && !seaBattleField.battleFieldCells.get(position - 1).hasShip && !seaBattleField.battleFieldCells.get(position - 10).hasShip && !seaBattleField.battleFieldCells.get(position + 10).hasShip && !seaBattleField.battleFieldCells.get(position + 8).hasShip && !seaBattleField.battleFieldCells.get(position - 8).hasShip;
    }

    //Проверяет влезет ли двухпалубный корабль на точку, выбранную пользователем
    private Boolean isPositionForTwoCellShip(Integer position, SeaBattleField seaBattleField) {
        boolean isLastColumn = position == 17 || position == 26 || position == 35 || position == 44 || position == 53 || position == 62 || position == 71 || position == 80;
        if (isLastColumn) return false;
        return isPositionForOneCellShip(position, seaBattleField) && isPositionForOneCellShip(position + 1, seaBattleField);
    }

    //Проверяет влезет ли трехпалубный корабль на точку, выбранную пользователем
    private Boolean isPositionForThreeCellShip(Integer position, SeaBattleField seaBattleField) {
        boolean isLastColumn = position == 17 || position == 26 || position == 35 || position == 44 || position == 53 || position == 62 || position == 71 || position == 80;
        boolean isPreLastColumn = position == 16 || position == 25 || position == 34 || position == 43 || position == 52 || position == 61 || position == 70 || position == 79;
        if (isLastColumn) return false;
        if (isPreLastColumn) return false;
        return isPositionForOneCellShip(position, seaBattleField) && isPositionForOneCellShip(position + 1, seaBattleField) && isPositionForOneCellShip(position + 2, seaBattleField);
    }

    //Создаем кораблей для бота
    private void generateShipsToBot() {
        ArrayList<Ship> startShipsList = (ArrayList<Ship>) getStartShips();
        while (startShipsList.size() != 0) {
            Integer randomCell = new Random().nextInt(72) + 9;
            int randomShip = new Random().nextInt(startShipsList.size());
            clickedShip = startShipsList.get(randomShip);
            if (setShipToBotField(randomCell)) {
                startShipsList.remove(clickedShip);
                clickedShip = null;
            }
        }
    }
}
