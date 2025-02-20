package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.exception.AccountAlreadyExistsException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoMem implements UserDAO {

    private List<User> userList = new ArrayList<>();

    public UserDaoMem() {
        //costruttore vuoto
    }


    @Override
    public void saveUser(String email, String password) throws AccountAlreadyExistsException{
        if(getUserByEmail(email) != null) {
            throw new AccountAlreadyExistsException("Account already exists for email "+email);
        }

        String hasehPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
        userList.add(new User(email,hasehPassword,new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }

    @Override
    public User getUserByEmail(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


    public void updateUsersEntityData(User user){
        //void
    }

    @Override
    public User loadUserData(User user) throws FileNotFoundException {
        return getUserByEmail(user.getEmail());
    }

    public List<User> getUserList() {
        return userList;
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}