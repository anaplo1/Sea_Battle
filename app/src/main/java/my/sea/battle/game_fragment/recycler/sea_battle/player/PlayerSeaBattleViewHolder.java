package my.sea.battle.game_fragment.recycler.sea_battle.player;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import my.sea.battle.R;
import my.sea.battle.game_fragment.field.BattleFieldCell;
import my.sea.battle.game_fragment.ship.MineShip;
import my.sea.battle.game_fragment.ship.OneCellShip;
import my.sea.battle.game_fragment.ship.ThreeCellShip;
import my.sea.battle.game_fragment.ship.TwoCellShip;

//Представление ячейки пользователя
public class PlayerSeaBattleViewHolder extends RecyclerView.ViewHolder {

    public PlayerSeaBattleViewHolder(View itemView) {
        super(itemView);
    }

    //Вывод переданной информации
    void bind(BattleFieldCell cell) {
        if (cell.isLetterCell && cell.symbol != null) {
            TextView symbolTextView = (TextView) itemView.findViewById(R.id.symbolTextView);
            symbolTextView.setText(cell.symbol);
        }
        if (cell.hasShip) {
            if (cell.ship instanceof OneCellShip) {
                itemView.setBackgroundResource(R.drawable.one_cell_ship);
            }
            if (cell.ship instanceof TwoCellShip) {
                if (cell.isTwoCellShipLeft) {
                    itemView.setBackgroundResource(R.drawable.two_cell_ship_left);
                } else {
                    itemView.setBackgroundResource(R.drawable.two_cell_ship_right);
                }
            }
            if (cell.ship instanceof ThreeCellShip) {
                if (cell.isThreeCellShipLeft) {
                    itemView.setBackgroundResource(R.drawable.three_cell_ship_left);
                } else if (cell.isThreeCellShipMiddle) {
                    itemView.setBackgroundResource(R.drawable.three_cell_ship_middle);
                } else {
                    itemView.setBackgroundResource(R.drawable.three_cell_ship_right);
                }
            }
            if (cell.ship instanceof MineShip) {
                itemView.setBackgroundResource(R.drawable.mine_cell);
            }
        }
        if (cell.hasShip && cell.isShot) {
            itemView.setBackgroundColor(Color.RED);
        }
        if (cell.isShot && !cell.hasShip) {
            itemView.setBackgroundColor(Color.GRAY);
        }
    }
}
