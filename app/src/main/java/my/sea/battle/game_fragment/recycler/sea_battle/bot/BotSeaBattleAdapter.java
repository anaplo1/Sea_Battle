package my.sea.battle.game_fragment.recycler.sea_battle.bot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.sea.battle.R;
import my.sea.battle.game_fragment.field.BattleFieldCell;
import my.sea.battle.game_fragment.field.SeaBattleField;

//Адаптер для вывода поля сражения бота
public class BotSeaBattleAdapter extends RecyclerView.Adapter<BotSeaBattleViewHolder> {

    public BotSeaBattleAdapter(SeaBattleField seaBattleField, OnCellShotClicked cellShot) {
        this.data = seaBattleField.battleFieldCells;
        this.cellShot = cellShot;
    }

    private List<BattleFieldCell> data;
    private OnCellShotClicked cellShot;
    private final Integer LETTER_CELL = 1;
    private Integer EMPTY_CELL = 2;
    private Boolean isClickEnabled = true;

    //Определяем вид ячейки
    @Override
    public int getItemViewType(int position) {
        if (data.get(position).isLetterCell) return LETTER_CELL;
        return EMPTY_CELL;
    }

    //Создаем или ячейку с символом или игровую ячейку
    @NonNull
    @Override
    public BotSeaBattleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LETTER_CELL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.symbol_cell, parent, false);
            return new BotSeaBattleViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.battle_field_cell, parent, false);
        return new BotSeaBattleViewHolder(view);
    }

    //Передаем данные и вешаем слушатель на нажатие на ячейку бота
    @Override
    public void onBindViewHolder(@NonNull BotSeaBattleViewHolder holder, int position) {
        holder.bind(data.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (isClickEnabled) {
                if (data.get(position).isLetterCell || data.get(position).isShot) return;
                cellShot.onCellShot(position);
            }
        });
    }

    //Количество повтором
    @Override
    public int getItemCount() {
        return data.size();
    }

    //Метод дл отключения кликов на поле бота во время его хода
    public void setClickEnabled(Boolean isClickEnabled) {
        this.isClickEnabled = isClickEnabled;
    }

}
