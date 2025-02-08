package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.User;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.DM;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;

public class UserController implements UserAwareInterface {


    public UserController() {
        //costruttore vuoto
    }

    @Override
    public void setCurrentUser(UserBean user) {
        //implementa inferfaccia
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
