package my.sea.battle.game_fragment.recycler.ship;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.sea.battle.game_fragment.ship.Ship;

//Представление корабля для выбора
public class ShipViewHolder extends RecyclerView.ViewHolder {
    public ShipViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void bind(Ship ship) {}
}
