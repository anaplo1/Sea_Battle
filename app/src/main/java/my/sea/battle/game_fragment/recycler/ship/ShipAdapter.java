package my.sea.battle.game_fragment.recycler.ship;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.sea.battle.R;
import my.sea.battle.game_fragment.ship.MineShip;
import my.sea.battle.game_fragment.ship.OneCellShip;
import my.sea.battle.game_fragment.ship.Ship;
import my.sea.battle.game_fragment.ship.ThreeCellShip;
import my.sea.battle.game_fragment.ship.TwoCellShip;

//Адаптер выводящий список кораблей на экран
public class ShipAdapter extends RecyclerView.Adapter<ShipViewHolder> {

    public ShipAdapter(List<Ship> shipList, OnShipClickedListener shipClickedListener) {
        this.data = shipList;
        this.shipClickedListener = shipClickedListener;
    }

    private List<Ship> data;
    private List<ShipViewHolder> savedHolders = new ArrayList<>();
    private OnShipClickedListener shipClickedListener;
    private final Integer ONE_CELL_SHIP_VIEW_TYPE = 1;
    private final Integer TWO_CELL_SHIP_VIEW_TYPE = 2;
    private final Integer THREE_CELL_SHIP_VIEW_TYPE = 3;
    private final Integer MINE_CELL_VIEW_TYPE = 4;

    //Определяем вид корабля
    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof OneCellShip) return ONE_CELL_SHIP_VIEW_TYPE;
        if (data.get(position) instanceof TwoCellShip) return TWO_CELL_SHIP_VIEW_TYPE;
        if (data.get(position) instanceof ThreeCellShip) return THREE_CELL_SHIP_VIEW_TYPE;
        if (data.get(position) instanceof MineShip) return MINE_CELL_VIEW_TYPE;
        return ONE_CELL_SHIP_VIEW_TYPE;
    }

    //В зависимости от вида выводим соответствующую ячейку на экран
    @NonNull
    @Override
    public ShipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ONE_CELL_SHIP_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_cell_ship, parent, false);
            ShipViewHolder shipViewHolder = new ShipViewHolder(view);
            savedHolders.add(shipViewHolder);
            return shipViewHolder;
        }
        if (viewType == TWO_CELL_SHIP_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.two_cell_ship, parent, false);
            ShipViewHolder shipViewHolder = new ShipViewHolder(view);
            savedHolders.add(shipViewHolder);
            return shipViewHolder;
        }
        if (viewType == THREE_CELL_SHIP_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.three_cell_ship, parent, false);
            ShipViewHolder shipViewHolder = new ShipViewHolder(view);
            savedHolders.add(shipViewHolder);
            return shipViewHolder;
        }
        if (viewType == MINE_CELL_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_cell, parent, false);
            ShipViewHolder shipViewHolder = new ShipViewHolder(view);
            savedHolders.add(shipViewHolder);
            return shipViewHolder;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.two_cell_ship, parent, false);
        ShipViewHolder shipViewHolder = new ShipViewHolder(view);
        savedHolders.add(shipViewHolder);
        return shipViewHolder;
    }

    //Передаем в нее данные и вешаем слушатель на нажатия
    @Override
    public void onBindViewHolder(@NonNull ShipViewHolder holder, int position) {
        holder.bind(data.get(position));
        holder.itemView.setOnClickListener(view -> {
            shipClickedListener.onShipClicked(data.get(position));
            for (int i = 0; i < savedHolders.size(); i++) {
                savedHolders.get(i).itemView.setBackgroundColor(Color.TRANSPARENT);
            }
            view.setBackgroundColor(Color.GREEN);
        });
    }

    //Количество повторений
    @Override
    public int getItemCount() {
        return data.size();
    }
}
