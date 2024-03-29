package com.mastermind.ui.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public abstract class Fragment {
    private Application application;
    private Node node;
    private Pane parent;
    private String getFXMLName() {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        String s = getClass().getSimpleName()
                .replaceAll(regex, replacement)
                .toLowerCase();
        return s;
    }
    protected void loadFXML(){
        if(node != null)return;
        try {
            node = FXMLLoader.load(getClass().getResource("/" + getFXMLName() + ".fxml"));
            onLoad();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public Node getNode() {
        return node;
    }

    protected abstract void onLoad();
    protected void onResume(){

    }

    protected Node lookup(String selector){
        return getNode().lookup(selector);
    }

    public void displayIn(Pane parent) {
        loadFXML();
        this.parent = parent;
        parent.getChildren().setAll(getNode());
        onResize();
    }
    public void onResize(){
        Node node = getNode();
        if(node instanceof Pane){
            Pane pane = (Pane) node;
            pane.setPrefWidth(parent.getWidth());
            pane.setPrefHeight(parent.getHeight());
        }
    }
    protected Application getApplication() {
        return Application.instance;
    }
    protected void close(){
        getApplication().popFragment();
    }
}
