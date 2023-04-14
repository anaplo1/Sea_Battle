package my.sea.battle.history;


import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.sea.battle.R;
import my.sea.battle.db.SeaBattleFieldCellHistory;

//Представление ячейки в схематичном виде
public class GridViewHolder extends RecyclerView.ViewHolder {

    public GridViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    //При получении данных схематично заполняет ячейку
    void bind(SeaBattleFieldCellHistory seaBattleFieldCellHistory) {
        if (seaBattleFieldCellHistory.isLetterCell) {
            TextView symbolTextView = itemView.findViewById(R.id.symbolTextView);
            symbolTextView.setText(seaBattleFieldCellHistory.symbol);
        } else {
            if (seaBattleFieldCellHistory.hasShip) {
                itemView.setBackgroundColor(Color.GREEN);
            }
            if (seaBattleFieldCellHistory.hasShip && seaBattleFieldCellHistory.isShot) {
                itemView.setBackgroundColor(Color.RED);
            }
            if (seaBattleFieldCellHistory.isShot && !seaBattleFieldCellHistory.hasShip) {
                itemView.setBackgroundColor(Color.GRAY);
            }
            if (!seaBattleFieldCellHistory.isShot) {
                itemView.setBackgroundResource(R.drawable.battle_cell_background);
            }
        }
    }
}
