package my.sea.battle.game_fragment.field;

//Класс, который служит для передачи информации в бд после окончания игры
public class GameResult {

    public Boolean isUserWin;
    public SeaBattleField userBattleField;
    public SeaBattleField botBattleField;

    public GameResult(Boolean isUserWin, SeaBattleField userBattleField, SeaBattleField botBattleField) {
        this.isUserWin = isUserWin;
        this.userBattleField = userBattleField;
        this.botBattleField = botBattleField;
    }
}
