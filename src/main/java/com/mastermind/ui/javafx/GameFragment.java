package com.mastermind.ui.javafx;

import com.mastermind.services.ServiceManager;
import com.mastermind.services.game.GameService;
import com.mastermind.services.game.responses.types.CombinationData;
import com.mastermind.services.game.responses.types.EvaluationData;
import com.mastermind.services.game.responses.types.TrialData;
import com.mastermind.services.game.responses.types.UserGameState;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameFragment extends Fragment {
    private VBox vbxTrials;
    private VBox vbxItems;
    private Pane pnlCode, pnlBand;
    private Button btnCommit, btnBandDismiss;
    private Label lblMessage, lblRound, lblFooter, lblBandTitle, lblBandSubtitle;
    GameService gameService;

    int selectedItem = 0;
    int selectedSlot = 0;
    boolean needToshow = true;
    private Color neutralAccent = new Color(0.8, 0.8, 0.8, 1.0);
    boolean bandEnabled = false;
    @Override
    protected void onLoad() {
        gameService = ServiceManager.getGameService();
        vbxTrials = (VBox) lookup("#vbxTrials");
        vbxItems = (VBox) lookup("#vbxItems");
        pnlCode = (Pane) lookup("#pnlCode");
        pnlBand = (Pane) lookup("#pnlBand");
        btnCommit = (Button) lookup("#btnCommit");
        btnBandDismiss = (Button) lookup("#btnBandDismiss");
        lblMessage = (Label) lookup("#lblMessage");
        lblRound = (Label) lookup("#lblRound");
        lblFooter = (Label) lookup("#lblFooter");
        lblBandTitle = (Label) lookup("#lblBandTitle");
        lblBandSubtitle = (Label) lookup("#lblBandSubtitle");

        btnCommit.setOnAction(event -> onCommit());


        UserGameState test = new UserGameState();
        test.setColorCount(5);

        List<TrialData> trials = new ArrayList<>();
        TrialData td1 = new TrialData();

        CombinationData cd1 = new CombinationData();
        cd1.setElements(Arrays.asList(0, 2, 2, 2));
        td1.setCombinationData(cd1);

        EvaluationData ed1 = new EvaluationData();
        ed1.setCorrectColorCount(0);
        ed1.setCorrectPlaceAndColorCount(4);
        td1.setEvaluationData(ed1);

        trials.add(td1);

        TrialData td2 = new TrialData();
        CombinationData cd2 = new CombinationData();
        cd2.setElements(Arrays.asList(1, 1, 2, 2));
        td2.setCombinationData(cd2);

        EvaluationData ed2 = new EvaluationData();
        ed2.setCorrectColorCount(2);
        ed2.setCorrectPlaceAndColorCount(2);
        td2.setEvaluationData(ed2);

        trials.add(td2);

        TrialData td3 = new TrialData();
        CombinationData cd3 = new CombinationData();
        cd3.setElements(Arrays.asList(1, 1, 3, 2));
        td3.setCombinationData(cd3);

        EvaluationData ed3 = new EvaluationData();
        ed3.setCorrectColorCount(2);
        ed3.setCorrectPlaceAndColorCount(0);
        td3.setEvaluationData(ed3);

        trials.add(td3);

        test.setTrials(trials);
        UserGameState state = gameService.getUserGameState();
        render(state);
    }

    @Override
    protected void onResume() {
        super.onResume();
        render(gameService.getUserGameState());
    }

    public void render(UserGameState state) {
        UserGameState.MatchStatus status = state.getMatchStatus();
        boolean notStarted = status == UserGameState.MatchStatus.NOT_STARTED;
        boolean finished = status == UserGameState.MatchStatus.FINISHED;
        boolean inProgress = status == UserGameState.MatchStatus.IN_PROGRESS;
        boolean notCreated = status == UserGameState.MatchStatus.NOT_CREATED;
        if (state.getTrials() != null) {
            List<HBox> trialRows = state.getTrials().stream()
                    .map(this::renderTrialRow)
                    .collect(Collectors.toList());
            if (true) {
                TrialData trialData = new TrialData();
                CombinationData cd = new CombinationData();
                cd.setElements(new ArrayList<>(Collections.nCopies(state.getSlotCount(), null)));
                trialData.setCombinationData(cd);
                trialRows.add(renderTrialRow(trialData));
            }
            vbxTrials.getChildren().clear();
            vbxTrials.getChildren().addAll(trialRows);
        }
        vbxTrials.setSpacing(8);
        lblMessage.setText(status.name() + ": " + state.getMessage() + " be: " + bandEnabled);
        if (notStarted || inProgress || finished) {
            int trialCount = 0;
            if (state.getTrials() != null) trialCount = state.getTrials().size();
            lblFooter.setText(state.getLocalPlayerName() + " vs " + state.getEnemyPlayerName()
                    + " - " + "Trials: " + trialCount + " / " + state.getMaxTrialCount() + " - " + ("Round: " + (state.getCurrentRound() + 1) + " / " + state.getTotalRoundCount()));
            renderElementBar(state, state.getColorCount());
            btnCommit.setVisible(true);
        } else {
            lblFooter.setText("Ready to start a new game");
            renderElementBar(state, 0);
            btnCommit.setVisible(false);
        }

        if (status == UserGameState.MatchStatus.NOT_CREATED) {
            //lblRound.setVisible(false);
            //lblMessage.setVisible(false);
        }
        boolean band = state.isCurrentRoundFinished();
        pnlBand.setVisible((state.isCurrentRoundFinished() || finished) && bandEnabled);
        if (band) {
            boolean localWins = state.getLocalWins();
            String color = localWins ? "#11ffa3" : "#ff7351";
            //pnlBand.setStyle("-fx-background: " + color + ";");
            if (finished) {
                lblBandTitle.setText(localWins ? " Victory" : "Defeat");
                lblBandSubtitle.setText(state.getLocalPlayerEloIncrement() + " ELO");
                btnBandDismiss.setText("Play Again");
                btnBandDismiss.setOnAction(event -> {
                    pnlBand.setVisible(false);
                    bandEnabled = false;
                    gameService.repeatGame();
                    render(gameService.getUserGameState());
                });
            } else {
                lblBandTitle.setText(state.getLastFinishedRoundWinner() + " wins this round!");

                lblBandSubtitle.setText("Round: " + (state.getCurrentRound()) + " / " + state.getTotalRoundCount());
                btnBandDismiss.setText("Next Round");
                btnBandDismiss.setOnAction(event -> {
                    pnlBand.setVisible(false);
                    bandEnabled = false;
                });
            }

        }
        if(state.getCode() != null){
            pnlCode.getChildren().clear();
            List<Circle> circles = renderCombination(state.getCode().getElements());
            HBox hBox = new HBox();
            hBox.getChildren().addAll(circles);
            hBox.setSpacing(4);
            pnlCode.getChildren().add(hBox);
        }
    }

    private void onCommit() {
        gameService.commitMove();
        bandEnabled = true;
        render(gameService.getUserGameState());
        needToshow = true;

    }

    public HBox renderTrialRow(TrialData trialData) {
        List<Integer> combinationElements = trialData.getCombinationData().getElements();
        List<Circle> renderedElements = renderCombination(combinationElements);
        EvaluationData evaluationData = trialData.getEvaluationData();
        Pane renderedEvaluation = renderEvaluation(evaluationData, combinationElements.size());
        HBox hBox = new HBox();
        hBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        hBox.getChildren().add(renderedEvaluation);
        hBox.getChildren().addAll(renderedElements);
        hBox.setSpacing(4);
        return hBox;
    }

    private List<Circle> renderCombination(List<Integer> combinationElements) {
        List<Circle> renderedElements = combinationElements.stream()
                .map(this::renderElement)
                .collect(Collectors.toList());
        List<Circle> boundRenderedElements = new ArrayList<>();
        for (int i = 0; i < renderedElements.size(); i++) {
            Circle circle = renderedElements.get(i);
            circle.setUserData(i);
            circle.setOnMouseClicked(event -> {
                Integer index = (Integer) circle.getUserData();
                // Set [index] to [selectedItem]

                render(gameService.placeColor(index, selectedItem));
            });
            boundRenderedElements.add(circle);
        }
        return renderedElements;
    }

    private Pane renderEvaluation(EvaluationData evaluationData, int combinationSize) {
        GridPane grid = new GridPane();
        if (evaluationData == null) return grid;
        int correctColorCount = evaluationData.getCorrectColorCount();
        int correctPlaceAndColorCount = evaluationData.getCorrectPlaceAndColorCount();
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background: #fff2c7;");
        grid.setHgap(4);
        grid.setVgap(4);
        grid.setPrefHeight(30);
        int columns = (int) Math.ceil(combinationSize / 2D);
        grid.setPrefWidth(15 * columns);
        for (int i = 0; i < combinationSize; i++) {
            Color fill = neutralAccent;
            if (correctColorCount + correctPlaceAndColorCount > i) fill = new Color(1.0, 1.0, 1.0, 1.0);
            if (correctPlaceAndColorCount > i) fill = new Color(0.0, 0.0, 0.0, 1.0);
            if (fill != null)
                grid.add(new Circle(6, fill), i % columns, i >= columns ? 1 : 0);
        }
        return grid;
    }

    private void renderElementBar(UserGameState state, int maxColors) {
        vbxItems.setSpacing(4);
        vbxItems.getChildren().clear();
        for (int i = 0; i < maxColors; i++) {
            Circle renderedElement = renderElement(i);
            renderedElement.setUserData(i);
            int finalI = i;
            renderedElement.setOnMouseClicked(event -> {
                setSelectedItem(finalI);
                //if(needToshow){
                //needToshow = false;
                render(gameService.placeColor(selectedSlot, selectedItem));
                selectedSlot = (selectedSlot + 1) % state.getSlotCount();
                //}
            });
            renderedElement.setOnDragDetected(event -> {
                Dragboard db = renderedElement.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                content.putString(Integer.toString((Integer) renderedElement.getUserData()));
                db.setContent(content);
                event.consume();
            });
            vbxItems.getChildren().add(renderedElement);
        }
    }

    private Circle renderElement(Integer element) {
        Circle circle = new Circle(18, getElementColor(element));
        return circle;
    }

    private Color getElementColor(Integer element) {
        if (element == null) return neutralAccent;
        return new Color(1.0 - (element % 3) * 0.4, 0.1 + (element % 10) * 0.1, 0.1 + ((element + 2) % 5) * 0.1, 1.0);
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }
}
