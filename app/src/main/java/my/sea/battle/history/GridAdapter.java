package my.sea.battle.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.sea.battle.R;
import my.sea.battle.db.SeaBattleFieldCellHistory;
import my.sea.battle.db.SeaBattleFieldHistory;

//Адаптер для схематичного вывода информации о игре
public class GridAdapter extends RecyclerView.Adapter<GridViewHolder> {

    //Лист ячеек из бд
    private List<SeaBattleFieldCellHistory> data;

    public GridAdapter(SeaBattleFieldHistory seaBattleFieldCellHistories) {
        this.data = seaBattleFieldCellHistories.seaBattleFieldHistoryList;
    }


    //Создаем представление в виде холдера
    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.symbol_cell, parent, false);
        return new GridViewHolder(view);
    }

    //Передаем данные в холдер
    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    //Кол-во повторов
    @Override
    public int getItemCount() {
        return data.size();
    }
}
