package my.sea.battle.menu_fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import my.sea.battle.R;
import my.sea.battle.db.SharedPrefsManager;

public class MenuFragment extends Fragment {

    public MenuFragment() {
        super(R.layout.menu_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button startGameButton = view.findViewById(R.id.startGameButton);
        // По нажатию на старт навигируемся в фрагмент с игрой
        startGameButton.setOnClickListener(v -> {
            Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment
            ).navigate(R.id.gameFragment);
        });
        Button openHistoryButton = view.findViewById(R.id.openHistoryButton);
        // По нажатию на историю навигируемся на нее
        openHistoryButton.setOnClickListener(v -> Navigation.findNavController(
                requireActivity(),
                R.id.nav_host_fragment
        ).navigate(R.id.historyFragment));

        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(requireContext());
        if (!sharedPrefsManager.getEmail().equals("")) {
            ((TextView) view.findViewById(R.id.userEmail)).setText("Ваш email: " + sharedPrefsManager.getEmail());
            view.findViewById(R.id.auth).setVisibility(View.GONE);
            view.findViewById(R.id.exit).setVisibility(View.VISIBLE);
        } else {
            ((TextView) view.findViewById(R.id.userEmail)).setText("Вы не авторизованы");
            view.findViewById(R.id.exit).setVisibility(View.GONE);
            view.findViewById(R.id.auth).setVisibility(View.VISIBLE);
        }
        view.findViewById(R.id.auth).setOnClickListener(v -> {
            Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment
            ).navigate(R.id.loginFragment);
        });
        view.findViewById(R.id.exit).setOnClickListener(v -> {
            sharedPrefsManager.clearSharedPreferences();
            ((TextView) view.findViewById(R.id.userEmail)).setText("Вы не авторизованы");
            view.findViewById(R.id.auth).setVisibility(View.VISIBLE);
            view.findViewById(R.id.exit).setVisibility(View.GONE);
        });
    }
}
