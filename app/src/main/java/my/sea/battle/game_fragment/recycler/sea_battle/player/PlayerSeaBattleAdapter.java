package my.sea.battle.game_fragment.recycler.sea_battle.player;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.sea.battle.R;
import my.sea.battle.game_fragment.field.BattleFieldCell;
import my.sea.battle.game_fragment.field.SeaBattleField;

//Адаптер для создания игрового поля пользователя
public class PlayerSeaBattleAdapter extends RecyclerView.Adapter<PlayerSeaBattleViewHolder> {

    private final List<BattleFieldCell> data;
    private final OnSetShipClicked setShipClicked;
    private final Integer LETTER_CELL = 1;
    private Integer EMPTY_CELL = 2;
    private Boolean isClickEnabled = true;

    public PlayerSeaBattleAdapter(SeaBattleField field, OnSetShipClicked setShipClicked) {
        this.setShipClicked = setShipClicked;
        this.data = field.battleFieldCells;
    }

    //Определяем вид ячейки
    @Override
    public int getItemViewType(int position) {
        if (data.get(position).isLetterCell) return LETTER_CELL;
        return EMPTY_CELL;
    }

    //Создаем или символьное или игровое представление
    @NonNull
    @Override
    public PlayerSeaBattleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LETTER_CELL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.symbol_cell, parent, false);
            return new PlayerSeaBattleViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.battle_field_cell, parent, false);
        return new PlayerSeaBattleViewHolder(view);
    }

    //Передаем данные и вешаем слушатель
    @Override
    public void onBindViewHolder(@NonNull PlayerSeaBattleViewHolder holder, int position) {
        holder.bind(data.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (isClickEnabled) {
                if (data.get(position).isLetterCell && data.get(position).isShot) return;
                setShipClicked.onBattleFieldClicked(position);
            }
        });
    }

    //Количество повторов
    @Override
    public int getItemCount() {
        return data.size();
    }

    //Метод для отключения нажатия после установки всех кораблей
    public void setClickEnabled(Boolean isClickEnabled) {
        this.isClickEnabled = isClickEnabled;
    }

}
