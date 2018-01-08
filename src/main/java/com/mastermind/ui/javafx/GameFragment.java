package com.mastermind.ui.javafx;

import com.mastermind.services.ServiceManager;
import com.mastermind.services.game.GameService;
import com.mastermind.services.game.responses.types.CombinationData;
import com.mastermind.services.game.responses.types.EvaluationData;
import com.mastermind.services.game.responses.types.TrialData;
import com.mastermind.services.game.responses.types.UserGameState;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameFragment extends Fragment {
    private VBox vbxTrials;
    private VBox vbxItems;
    private Pane pnlCode;
    GameService gameService;

    private Color neutralAccent = new Color(0.8, 0.8, 0.8, 1.0);;

    @Override
    protected void onLoad() {
        gameService = ServiceManager.getGameService();
        vbxTrials = (VBox) lookup("#vbxTrials");
        vbxItems = (VBox) lookup("#vbxItems");
        pnlCode = (Pane) lookup("#pnlCode");

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
        render(test);
    }

    public void render(UserGameState state) {
        List<HBox> trialRows = state.getTrials().stream()
                .map(this::renderTrialRow)
                .collect(Collectors.toList());
        vbxTrials.getChildren().clear();
        vbxTrials.getChildren().addAll(trialRows);
        vbxTrials.setSpacing(8);
        renderElementBar(state.getColorCount());
    }

    public HBox renderTrialRow(TrialData trialData) {
        List<Integer> combinationElements = trialData.getCombinationData().getElements();
        List<Circle> renderedElements = combinationElements.stream()
                .map(this::renderElement)
                .collect(Collectors.toList());
        EvaluationData evaluationData = trialData.getEvaluationData();
        Pane renderedEvaluation = renderEvaluation(evaluationData, combinationElements.size());
        HBox hBox = new HBox();
        hBox.getChildren().add(renderedEvaluation);
        hBox.getChildren().addAll(renderedElements);
        hBox.setSpacing(4);
        return hBox;
    }

    private Pane renderEvaluation(EvaluationData evaluationData, int combinationSize) {
        int correctColorCount = evaluationData.getCorrectColorCount();
        int correctPlaceAndColorCount = evaluationData.getCorrectPlaceAndColorCount();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background: #fff2c7;");
        grid.setHgap(4);
        grid.setVgap(4);
        grid.setPrefHeight(30);
        int columns = (int) Math.ceil(combinationSize / 2D);
        grid.setPrefWidth(15*columns);
        for (int i = 0; i < combinationSize; i++) {
            Color fill = neutralAccent;
            if (correctColorCount + correctPlaceAndColorCount >= i) fill = new Color(1.0, 1.0, 1.0, 1.0);
            if (correctPlaceAndColorCount >= i) fill = new Color(0.0, 0.0, 0.0, 1.0);
            if (fill != null)
                grid.add(new Circle(6, fill), i % columns, i >= columns ? 1 : 0);
        }
        return grid;
    }
    private void renderElementBar(int maxColors){
        vbxItems.setSpacing(4);
        vbxItems.getChildren().clear();
        for (int i = 0; i < maxColors; i++) {
            Circle renderedElement = renderElement(i);
            vbxItems.getChildren().add(renderedElement);
        }
    }

    private Circle renderElement(Integer element) {
        Circle circle = new Circle(18, getElementColor(element));
        return circle;
    }

    private Color getElementColor(Integer element) {
        if(element == null) return neutralAccent;
        return new Color(1.0 - (element % 3) * 0.4, 0.1 + (element % 10) * 0.1, 0.1 + ((element + 2) % 5) * 0.1, 1.0);
    }
}
