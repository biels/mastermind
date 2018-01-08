package com.mastermind.ui.javafx;

import com.mastermind.services.ServiceManager;
import com.mastermind.services.players.PlayersService;
import com.mastermind.services.players.responses.ListPlayersResponse;
import com.mastermind.services.players.responses.types.PlayerRowData;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;


public class PlayersFragment extends Fragment {
    GridPane grid;
    PlayersService playersService;
    Button btnAddPlayer;
    TableView<PlayerRowData> tblPlayers;
    ObservableList<PlayerRowData> data;
    @Override
    protected void onLoad() {
        playersService = ServiceManager.getPlayersService();
        grid = (GridPane) getNode();
        getData();
        btnAddPlayer = (Button) lookup("#btnAddPlayer");
        btnAddPlayer.setOnAction(event -> getApplication().actionNewPlayer());
        buildTable();
    }

    private void getData() {
        ListPlayersResponse listPlayersResponse = playersService.listPlayers();
        data = FXCollections.observableArrayList(listPlayersResponse.getPlayerRows());
    }

    private void buildTable() {
        tblPlayers = new TableView<>();
        TableColumn<PlayerRowData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getName()));
        TableColumn<PlayerRowData, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getType()));
        tblPlayers.setItems(data);
        tblPlayers.getColumns().addAll(nameColumn, typeColumn);
        grid.add(tblPlayers, 1, 1, 1, 3);
    }

}
