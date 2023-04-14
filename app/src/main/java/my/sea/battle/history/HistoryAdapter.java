package my.sea.battle.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.sea.battle.R;
import my.sea.battle.db.SeaBattleHistory;

//Адаптер для вывода списка игр
public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    public HistoryAdapter(List<SeaBattleHistory> seaBattleHistoryList) {
        this.data = seaBattleHistoryList;
    }

    //Список игр из бд
    private List<SeaBattleHistory> data;

    //Создаем представление в виде холдера
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    //Передаем в холдер данные
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    //Кол-во повторений
    @Override
    public int getItemCount() {
        return data.size();
    }
}
