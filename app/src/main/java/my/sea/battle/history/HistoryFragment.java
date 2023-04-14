package my.sea.battle.history;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import my.sea.battle.R;
import my.sea.battle.db.SeaBattleDB;
import my.sea.battle.menu_fragment.MenuFragment;

public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        super(R.layout.history_fragment);
    }

    private RecyclerView historyRecycler;
    private HistoryAdapter historyAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyRecycler = view.findViewById(R.id.historyRecycler);
        ImageView historyBackButton = view.findViewById(R.id.historyBackButton);
        //По нажатию назад навигируемся в меню
        historyBackButton.setOnClickListener(v -> {
            Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment
            ).navigate(R.id.menuFragment);
        });
        // В отдельном потоке ходим в БД
        new Thread(() -> {
            SeaBattleDB db = Room.databaseBuilder(requireActivity().getApplicationContext(),
                    SeaBattleDB.class, "sea-battle-database").build();
            historyAdapter = new HistoryAdapter(db.seaBattleDao().getFullHistory());
            //Переключаемся на главный и передаем данные в Recycler
            new Handler(Looper.getMainLooper()).post(() -> {
                historyRecycler.setAdapter(historyAdapter);
            });
        }).start();

    }
}
