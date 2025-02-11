package it.uniroma2.marchidori.maininterface.boundary;


import it.uniroma2.marchidori.maininterface.entity.User;

import java.util.List;

public interface UserDAO {
    //metodi che utilizza il dao
    void saveUser(String email, String password);
    User getUserByEmail(String email);
    List<String> getUserLobbies(String email);
    void removeUserLobby(String email,String lobby);
    void saveUserLobbies(String email, List<String> lobbyNames);

}