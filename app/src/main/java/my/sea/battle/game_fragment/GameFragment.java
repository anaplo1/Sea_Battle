package my.sea.battle.game_fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import my.sea.battle.R;
import my.sea.battle.db.SeaBattleDB;
import my.sea.battle.db.SeaBattleFieldCellHistory;
import my.sea.battle.db.SeaBattleFieldHistory;
import my.sea.battle.db.SeaBattleHistory;
import my.sea.battle.game_fragment.recycler.sea_battle.bot.BotSeaBattleAdapter;
import my.sea.battle.game_fragment.recycler.sea_battle.bot.OnCellShotClicked;
import my.sea.battle.game_fragment.recycler.sea_battle.player.OnSetShipClicked;
import my.sea.battle.game_fragment.recycler.sea_battle.player.PlayerSeaBattleAdapter;
import my.sea.battle.game_fragment.recycler.ship.OnShipClickedListener;
import my.sea.battle.game_fragment.recycler.ship.ShipAdapter;
import my.sea.battle.game_fragment.ship.Ship;
import my.sea.battle.menu_fragment.MenuFragment;

public class GameFragment extends Fragment implements OnShipClickedListener, OnSetShipClicked, OnCellShotClicked {

    private GameViewModel viewModel;

    RecyclerView playerBattleFieldRecycler;
    PlayerSeaBattleAdapter playerBattleAdapter;

    RecyclerView botBattleFieldRecycler;
    BotSeaBattleAdapter botBattleAdapter;

    RecyclerView shipRecycler;
    ShipAdapter shipAdapter;

    TextView turnTextView;
    Button startGameButton;
    ImageView gameBackButton;
    ProgressBar botProgressBar;

    public GameFragment() {
        super(R.layout.game_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        turnTextView = view.findViewById(R.id.turn);
        startGameButton = view.findViewById(R.id.startGame);
        gameBackButton = view.findViewById(R.id.gameBackButton);
        botProgressBar = view.findViewById(R.id.botTurnProgressBar);

        //Подписка на обновление поля боя пользователя, при обновлении перерисовываем
        viewModel.playerFieldLiveData.observe(getViewLifecycleOwner(), seaBattleField -> {
            playerBattleFieldRecycler = view.findViewById(R.id.playerBattleField);
            playerBattleAdapter = new PlayerSeaBattleAdapter(seaBattleField, this);
            playerBattleFieldRecycler.setLayoutManager(new GridLayoutManager(getContext(), 9));
            playerBattleFieldRecycler.setAdapter(playerBattleAdapter);
            playerBattleAdapter.notifyDataSetChanged();
        });

        //Подписка на обновление поля боя бота, при обновлении перерисовываем
        viewModel.botFieldLiveData.observe(getViewLifecycleOwner(), seaBattleField -> {
            botBattleFieldRecycler = view.findViewById(R.id.botBattleField);
            botBattleAdapter = new BotSeaBattleAdapter(seaBattleField, this);
            botBattleFieldRecycler.setLayoutManager(new GridLayoutManager(getContext(), 9));
            botBattleFieldRecycler.setAdapter(botBattleAdapter);
            botBattleAdapter.notifyDataSetChanged();
        });

        //Подписка на изменение в выборе кораблей при установке их на поле
        viewModel.shipsLiveData.observe(getViewLifecycleOwner(), shipList -> {
            shipRecycler = view.findViewById(R.id.shipRecycler);
            shipAdapter = new ShipAdapter(shipList, this);
            shipRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            shipRecycler.setAdapter(shipAdapter);
            shipAdapter.notifyDataSetChanged();
        });

        //Слушатель начала игры для скрытия ненужных View
        viewModel.gameStartedLiveData.observe(getViewLifecycleOwner(), isGameStarted -> {
            if (isGameStarted) {
                botBattleAdapter.setClickEnabled(true);
                turnTextView.setVisibility(View.VISIBLE);
                startGameButton.setVisibility(View.INVISIBLE);
                shipRecycler.setVisibility(View.INVISIBLE);
            } else {
                botBattleAdapter.setClickEnabled(false);
                turnTextView.setVisibility(View.INVISIBLE);
                startGameButton.setVisibility(View.VISIBLE);
                shipRecycler.setVisibility(View.VISIBLE);
            }
        });

        //Слушатель для ошибок, выводит их в всплывающее сообещние
        viewModel.errorLiveData.observe(getViewLifecycleOwner(), errorMessage -> Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show());

        //Слушатель текущего хода
        viewModel.needToTurnLiveData.observe(getViewLifecycleOwner(), playerTurn -> {
            if (playerTurn) {
                viewModel.checkIsBotWin();
                botBattleAdapter.setClickEnabled(true);
                turnTextView.setText("Ваш ход");
                botProgressBar.setVisibility(View.GONE);
            } else {
                viewModel.checkIsPlayerWin();
                botBattleAdapter.setClickEnabled(false);
                botProgressBar.setVisibility(View.VISIBLE);
                turnTextView.setText("Ход противника");
            }
        });

        //Слушатель, который ждет окончания игры, чтобы передать данные в бд
        viewModel.gameResultLiveData.observe(getViewLifecycleOwner(), gameResult -> {
            new Thread(() -> {
                SeaBattleDB db = Room.databaseBuilder(requireActivity().getApplicationContext(),
                        SeaBattleDB.class, "sea-battle-database").build();
                db.seaBattleDao().insertNewGame(
                        new SeaBattleHistory(gameResult.isUserWin,
                                new Date().toString(),
                                new SeaBattleFieldHistory(
                                        gameResult.userBattleField.battleFieldCells.stream().map(battleFieldCell -> new SeaBattleFieldCellHistory(
                                                battleFieldCell.isLetterCell,
                                                battleFieldCell.symbol,
                                                battleFieldCell.hasShip,
                                                battleFieldCell.isShot
                                        )).collect(Collectors.toList())
                                ),
                                new SeaBattleFieldHistory(
                                        gameResult.botBattleField.battleFieldCells.stream().map(battleFieldCell -> new SeaBattleFieldCellHistory(
                                                battleFieldCell.isLetterCell,
                                                battleFieldCell.symbol,
                                                battleFieldCell.hasShip,
                                                battleFieldCell.isShot
                                        )).collect(Collectors.toList())
                                )
                        ));
            }).start();
            if (gameResult.isUserWin) {
                playerBattleAdapter.setClickEnabled(true);
                botBattleAdapter.setClickEnabled(false);
                Toast.makeText(requireContext(), "Поздравляем с победой", Toast.LENGTH_SHORT).show();
            } else {
                playerBattleAdapter.setClickEnabled(true);
                botBattleAdapter.setClickEnabled(false);
                Toast.makeText(requireContext(), "К сожалению, вы проиграли", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.needVibrate.observe(getViewLifecycleOwner(), aBoolean -> {
            Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator.hasVibrator()) {
                // Для Android API уровня 26 (Android 8.0) и выше, использовать VibratorInfo для установки времени вибрации.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);
                    vibrator.vibrate(vibrationEffect);
                } else {
                    // Для более ранних версий Android, установите время вибрации в миллисекундах.
                    vibrator.vibrate(500);
                }
            }
        });

        viewModel.isUserHitShip.observe(getViewLifecycleOwner(), userHitShip -> {
            if(userHitShip){
                Toast.makeText(requireContext(), "Вы попали в корабль, вы делаете дополнительный ход", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Бот попал в корабль, бот делает дополнительный ход", Toast.LENGTH_SHORT).show();
            }
        });

        //При нажатии на старт говорим ViewModel, что можно начинать процесс игры
        startGameButton.setOnClickListener(button -> {
            viewModel.startGame();
        });

        //Переход в меню на стрелку назад
        gameBackButton.setOnClickListener(v -> Navigation.findNavController(
                requireActivity(),
                R.id.nav_host_fragment
        ).navigate(R.id.menuFragment));
    }

    //Срабатывает при выборе корабля из списка
    @Override
    public void onShipClicked(Ship ship) {
        viewModel.setClickedShip(ship);
    }

    //Срабатывает при установке корабля
    @Override
    public void onBattleFieldClicked(Integer position) {
        viewModel.setShipToPlayerField(position);
    }

    //Срабатывет при выстреле в поле бота
    @Override
    public void onCellShot(Integer position) {
        viewModel.onCellShot(position);
    }
}
