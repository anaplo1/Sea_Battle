package my.sea.battle.history;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.sea.battle.R;
import my.sea.battle.db.SeaBattleHistory;

//Представление ячейки прошедшей игры
public class HistoryViewHolder extends RecyclerView.ViewHolder {

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    TextView historyTitle;
    TextView dateTextView;

    RecyclerView playerGrid;
    RecyclerView botGrid;

    //Вывод на экран полученной информации из бд
    void bind(SeaBattleHistory seaBattleHistory) {
        historyTitle = itemView.findViewById(R.id.historyTitle);
        dateTextView = itemView.findViewById(R.id.gameDate);
        playerGrid = itemView.findViewById(R.id.userBoard);
        botGrid = itemView.findViewById(R.id.botBoard);
        if (seaBattleHistory.isWin) {
            historyTitle.setText("Победа");
            historyTitle.setTextColor(Color.GREEN);
        } else {
            historyTitle.setText("Поражение");
            historyTitle.setTextColor(Color.RED);
        }
        dateTextView.setText(seaBattleHistory.date);

        playerGrid.setLayoutManager(new GridLayoutManager(itemView.getContext(), 9));
        playerGrid.setAdapter(new GridAdapter(seaBattleHistory.userBattleField));

        botGrid.setLayoutManager(new GridLayoutManager(itemView.getContext(), 9));
        botGrid.setAdapter(new GridAdapter(seaBattleHistory.botBattleField));
    }
}
