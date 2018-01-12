package com.mastermind.persistence;

import com.mastermind.model.entities.types.HumanPlayer;
import com.mastermind.model.entities.types.MinimaxAIPlayer;
import com.mastermind.model.entities.types.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Serialization {
    @Test
    void serializePlayer() {
        Player p1 = new HumanPlayer("Biel", "12345");
        String p1s = p1.serialize();
        assertEquals("HumanPlayer-Biel-1200.0-32.0-12345", p1s);

        Player p2 = new MinimaxAIPlayer("Mini", 12);
        String p2s = p2.serialize();
        assertEquals("MinimaxAIPlayer-Mini-1200.0-32.0-12", p2s);
    }

    @Test
    void deserializePlayer() {
        HumanPlayer p1 = (HumanPlayer) Player.deserialize
                ("HumanPlayer-Biel-1200.0-32.0-12345");
        assertEquals(p1.getName(), "Biel");
        assertEquals(p1.getPassword(), "12345");
        assertEquals(p1.getElo(), (Double) 1200.0);
        assertEquals(p1.getEloKHint(), (Double) 32.0);

        MinimaxAIPlayer p2 = (MinimaxAIPlayer) Player.deserialize
                ("MinimaxAIPlayer-Mini-1200.0-32.0-12");
        assertEquals(p2.getName(), "Mini");
        assertEquals(p2.getDepth(), 12);
        assertEquals(p2.getElo(), (Double) 1200.0);
        assertEquals(p2.getEloKHint(), (Double) 32.0);
    }
    void serializeMatch(){

    }
}
