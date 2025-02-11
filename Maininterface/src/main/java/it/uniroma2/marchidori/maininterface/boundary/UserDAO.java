package it.uniroma2.marchidori.maininterface.boundary;


import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.User;

import java.io.FileNotFoundException;
import java.util.List;

public interface UserDAO {
    //metodi che utilizza il dao
    void saveUser(String email, String password);
    User getUserByEmail(String email);
    void saveUsersEntityData(User user);
    String serializeCharacterSheet(CharacterSheet cs);
    String serializeLobby(Lobby lobby);
    User loadUserData(User user) throws FileNotFoundException;
    /*User getUserByEmail(String email);
    List<String> getUserLobbies(String email);
    void removeUserLobby(String email,String lobby);
    void saveUserLobbies(String email, List<String> lobbyNames);*/

}