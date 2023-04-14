package my.sea.battle.auth.register;

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

public class RegisterFragment extends Fragment {

    private RegisterViewModel viewModel;

    public RegisterFragment() {
        super(R.layout.register_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        view.findViewById(R.id.registerBackButton).setOnClickListener(v -> Navigation.findNavController(
                requireActivity(),
                R.id.nav_host_fragment
        ).navigate(R.id.loginFragment));
        view.findViewById(R.id.register).setOnClickListener(v -> {
            if (((EditText) view.findViewById(R.id.emailRegistration)).getText().length() == 0 || ((EditText) view.findViewById(R.id.passwordRegistration)).getText().length() == 0) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.registerUser(
                        ((EditText) view.findViewById(R.id.emailRegistration)).getText(),
                        ((EditText) view.findViewById(R.id.passwordRegistration)).getText(),
                        requireActivity().getApplicationContext()
                );
            }
        });

        viewModel.getUserRegisterLiveData().observe(getViewLifecycleOwner(), userRegistred -> {
            if (userRegistred) {
                Toast.makeText(requireContext(), "Успешная регистрация", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(
                        requireActivity(),
                        R.id.nav_host_fragment
                ).navigate(R.id.loginFragment);
            } else {
                Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
