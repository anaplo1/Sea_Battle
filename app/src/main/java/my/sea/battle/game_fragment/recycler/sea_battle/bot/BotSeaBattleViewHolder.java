package my.sea.battle.game_fragment.recycler.sea_battle.bot;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import my.sea.battle.R;
import my.sea.battle.game_fragment.field.BattleFieldCell;

//Схематичное представление игровой ячейки бота
class BotSeaBattleViewHolder extends RecyclerView.ViewHolder {

    public BotSeaBattleViewHolder(View itemView) {
        super(itemView);
    }

    void bind(BattleFieldCell cell) {
        if (cell.isLetterCell && cell.symbol != null) {
            TextView symbolTextView = (TextView) itemView.findViewById(R.id.symbolTextView);
            symbolTextView.setText(cell.symbol);
        }
        if (cell.hasShip && cell.isShot) {
            itemView.setBackgroundColor(Color.RED);
        }
        if (cell.isShot && !cell.hasShip) {
            itemView.setBackgroundColor(Color.GRAY);
        }
    }
}
