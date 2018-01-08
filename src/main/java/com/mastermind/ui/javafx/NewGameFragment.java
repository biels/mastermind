package com.mastermind.ui.javafx;

import com.mastermind.services.ServiceManager;
import com.mastermind.services.game.GameService;
import com.mastermind.services.game.responses.types.AIPlayerRowData;
import com.mastermind.services.game.responses.types.UserGameState;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

import java.util.List;

public class NewGameFragment extends Fragment{
    GridPane grid;
    GameService gameService;
    Button btnEditPlayers, btnPlay;
    TableView<AIPlayerRowData> tblEnemies;
    ObservableList<AIPlayerRowData> data;
    @Override
    protected void onLoad() {
        gameService = ServiceManager.getGameService();
        grid = (GridPane) getNode();
        btnEditPlayers = (Button) lookup("#btnEditPlayers");
        btnPlay = (Button) lookup("#btnPlay");
        btnPlay.setOnAction(event -> onPlay());
        btnEditPlayers.setOnAction(event -> Application.Action.EDIT_PLAYERS.execute());
        buildTable();
    }
    @Override
    protected void onResume() {
        getData();
        tblEnemies.setItems(data);
    }
    private void getData() {
        List<AIPlayerRowData> response = gameService.listEnemyPlayers();
        data = FXCollections.observableArrayList(response);
    }
    private void buildTable() {
        tblEnemies = new TableView<>();
        TableColumn<AIPlayerRowData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getName()));
        TableColumn<AIPlayerRowData, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getType()));
        tblEnemies.getColumns().addAll(nameColumn, typeColumn);
        grid.add(tblEnemies, 1, 1, 2, 1);
    }
    private void onPlay(){
        int selectedIndex = tblEnemies.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1) return;
        UserGameState state = gameService.newGame(selectedIndex);
        close();
    }

}
