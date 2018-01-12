package com.mastermind.ui.javafx;

import com.mastermind.services.ServiceManager;
import com.mastermind.services.players.responses.ListPlayersResponse;
import com.mastermind.services.players.responses.types.PlayerRowData;
import com.mastermind.services.ranking.RankingService;
import com.mastermind.services.ranking.responses.ListPositionsResponse;
import com.mastermind.services.ranking.responses.types.RankingRowData;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;


public class RankingFragment extends Fragment {
    GridPane grid;
    RankingService rankingService;
    Button btnAddPlayer;
    TableView<RankingRowData> tblPlayers;
    ObservableList<RankingRowData> data;
    @Override
    protected void onLoad() {
        rankingService = ServiceManager.getRankingService();
        grid = (GridPane) getNode();
        btnAddPlayer = (Button) lookup("#btnAddPlayer");
        buildTable();
    }

    @Override
    protected void onResume() {
        getData();
        tblPlayers.setItems(data);
    }

    private void getData() {
        ListPositionsResponse listPositionsResponse = rankingService.listPositions();
        data = FXCollections.observableArrayList(listPositionsResponse.getRankingRows());
    }

    private void buildTable() {
        tblPlayers = new TableView<>();
        TableColumn<RankingRowData, String> nameColumn = new TableColumn<>("Place");
        nameColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(Integer.toString(p.getValue().getPlace())));
        TableColumn<RankingRowData, String> typeColumn = new TableColumn<>("Player");
        typeColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getPlayer()));
        TableColumn<RankingRowData, String> eloColumn = new TableColumn<>("Elo");
        eloColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(Double.toString(p.getValue().getElo())));
        tblPlayers.getColumns().addAll(nameColumn, typeColumn, eloColumn);
        grid.add(tblPlayers, 1, 1, 1, 3);
    }

}
