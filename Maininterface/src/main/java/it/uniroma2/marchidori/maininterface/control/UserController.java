package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.DM;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;

public class UserController implements UserAwareInterface {
    private UserBean currentUser;
    private User currentEntity = Session.getCurrentUser();
    public UserController() {
        //boh

    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    // Metodo per cambiare il ruolo tra PLAYER e DM
    public void switchRole(UserBean user) {
        if(user.getRoleBehavior() == PLAYER) {
            user.setRoleBehavior(DM);
        } else if (user.getRoleBehavior() == DM) {
            user.setRoleBehavior(PLAYER);
        }
    }

    public void switchRole(User user) {
        if(user.getRoleBehavior() == PLAYER) {
            user.setRoleBehavior(DM);
        } else if (user.getRoleBehavior() == DM) {
            user.setRoleBehavior(PLAYER);
        }
    }



}
