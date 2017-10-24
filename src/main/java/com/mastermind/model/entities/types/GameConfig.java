package com.mastermind.model.entities.types;
import com.mastermind.model.entities.base.Entity;

public class GameConfig extends Entity {
    private int difficulty;
    private int colors;
    private int combinations;
    private int extension;
    private boolean repeat;

    public GameConfig(int id) {
        super(id);
        difficulty = 0;
        colors = 3;
        extension = 3;
        colors = 8;
        repeat = false;
    }
    public void changeDificulty(int newDifficulty){
        difficulty = newDifficulty;
    }
    public void changeColors(int newColors){
        if(newColors < extension && !repeat) System.out.println("No es pot tindre tants pocs colors amb una extensio tan gran i repeat desactivat");
        else colors = newColors;
    }
    public void changeCombinations(int newCombinations){
        combinations = newCombinations;

    }
    public void changeExtension(int newExtension){
        if(newExtension>colors && !repeat) System.out.println("No es pot tindre una convinacio tan gran amb tan pocs colors i repeat off");
        else extension = newExtension;
    }
    public void repeatSetter(){
        if(extension>colors) System.out.println("no pots desactivar repetir amb aquesta configuracio de colors i extensio ja que no es podria plenar la  convinacio de colors");
        else repeat = !repeat;
    }
}
