package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.DM;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;

public class UserController implements UserAwareInterface {
    private User currentEntity = Session.getInstance().getCurrentUser();
    private UserBean currentUser;


    public UserController() {
        //costruttore vuoto
        currentEntity = Session.getInstance().getCurrentUser();
    }

    public UserController(User currentEntity){
        this.currentEntity = currentEntity;
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    // Metodo per cambiare il ruolo tra PLAYER e DM
    public void switchRole(RoleEnum role) {
        if(role == PLAYER) {
            currentUser.setRoleBehavior(DM);
            currentEntity.setRoleBehavior(DM);
        } else if (role == DM) {
            currentUser.setRoleBehavior(PLAYER);
            currentEntity.setRoleBehavior(PLAYER);
        }
    }
}
