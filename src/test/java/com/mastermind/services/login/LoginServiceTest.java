package com.mastermind.services.login;

import com.mastermind.model.persistence.RepositoryManager;
import com.mastermind.model.persistence.repositories.impl.RepositoriesInMemoryImpl;
import com.mastermind.services.ServiceManager;
import com.mastermind.services.login.responses.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
    private LoginService service;

    @BeforeEach
    void setUp() {
        RepositoryManager.attachImplementation(new RepositoriesInMemoryImpl());
        ServiceManager.initState();
        service = ServiceManager.getLoginService();
    }

    @Test
    void simpleLogin() {
        ServiceManager.getPlayersService().createHumanPlayer("human", "1234");
        LoginResponse human = service.login("human", "1234");
        assertTrue(human.isSuccess());
        assertNotNull(ServiceManager.getState().getLoggedInPlayer());
    }

    @Test
    void login() {
        LoginResponse response = service.login("username", "password");
        assertFalse(response.isSuccess());
        assertFalse(service.isLoggedIn());
        assertNull(service.getLoggedInPlayerName());
        ServiceManager.getPlayersService().createRandomAIPlayer("random-ai", 0x36L);
        response = service.login("random-ai", "somepassword");
        assertFalse(response.isSuccess());
        assertFalse(service.isLoggedIn());
        ServiceManager.getPlayersService().createHumanPlayer("humanplayer", "password");
        response = service.login("username", "password");
        assertFalse(response.isSuccess());
        response = service.login("humanplayer", "password");
        assertTrue(response.isSuccess());
        assertNotNull(response.getMessages());
        assertTrue(service.isLoggedIn());
    }

    @Test
    void getLoggedInPlayerName() {
        ServiceManager.getPlayersService().createHumanPlayer("humanplayer", "password");
        LoginResponse response = service.login("humanplayer", "password");
        assertTrue(response.isSuccess());
        assertTrue(service.isLoggedIn());
        assertEquals("humanplayer", service.getLoggedInPlayerName());
    }

    @Test
    void logout() {
        ServiceManager.getPlayersService().createHumanPlayer("humanplayer", "password");
        LoginResponse response = service.login("humanplayer", "password");
        assertTrue(response.isSuccess());
        assertTrue(service.isLoggedIn());
        service.logout();
        assertFalse(service.isLoggedIn());
    }
}