package my.sea.battle.auth.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import my.sea.battle.R;
import my.sea.battle.menu_fragment.MenuFragment;

public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;

    public LoginFragment() {
        super(R.layout.login_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        view.findViewById(R.id.register).setOnClickListener(v -> Navigation.findNavController(
                requireActivity(),
                R.id.nav_host_fragment
        ).navigate(R.id.registerFragment));

        view.findViewById(R.id.withoutRegister).setOnClickListener(v -> Navigation.findNavController(
                requireActivity(),
                R.id.nav_host_fragment
        ).navigate(R.id.menuFragment));

        view.findViewById(R.id.loginButton).setOnClickListener(v -> {
            if (((EditText) view.findViewById(R.id.emailEditText)).getText().length() == 0 || ((EditText) view.findViewById(R.id.passwordEditText)).getText().length() == 0) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.loginUser(
                        ((EditText) view.findViewById(R.id.emailEditText)).getText(),
                        ((EditText) view.findViewById(R.id.passwordEditText)).getText(),
                        requireActivity().getApplicationContext()
                );
            }
        });

        viewModel.getUserRegisterLiveData().observe(getViewLifecycleOwner(), userRegistred -> {
            if (userRegistred) {
                Toast.makeText(requireContext(), "Успешная авторизация", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(
                        requireActivity(),
                        R.id.nav_host_fragment
                ).navigate(R.id.menuFragment);
            } else {
                Toast.makeText(requireContext(), "Вы не зарегестрированы", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
