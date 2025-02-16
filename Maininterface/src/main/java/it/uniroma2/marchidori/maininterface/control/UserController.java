package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.DM;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;
import static it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher.logger;

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

    public void switchRole(RoleEnum role) {
        logger.info("SwitchRole chiamato con role: " + role);
        if(role == PLAYER) {
            currentUser.setRoleBehavior(DM);
            currentEntity.setRoleBehavior(DM);
        } else if (role == DM) {
            currentUser.setRoleBehavior(PLAYER);
            currentEntity.setRoleBehavior(PLAYER);
        }
        logger.info("Nuovo ruolo impostato: " + currentUser.getRoleBehavior());
    }

}
