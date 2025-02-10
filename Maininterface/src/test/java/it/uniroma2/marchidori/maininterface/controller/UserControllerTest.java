package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.UserController;
import it.uniroma2.marchidori.maininterface.entity.User;
import org.junit.jupiter.api.Test;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.DM;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    private final User currentDm = new User("test",DM,null,null,null);
    private final User currentPlayer = new User("test",PLAYER,null,null,null);

    private final UserBean userDm = new UserBean("","",DM, null,null,null);
    private final UserBean userP = new UserBean("","",PLAYER, null,null,null);


    @Test
    void switchRoleTest(){
        UserController userController = new UserController(currentPlayer);
        userController.setCurrentUser(userDm);
        userController.switchRole(userDm.getRoleBehavior());
        assertEquals(PLAYER, currentPlayer.getRoleBehavior());
    }

    @Test
    void switchRoleTest2(){
        UserController userController = new UserController(currentDm);
        userController.setCurrentUser(userP);
        userController.switchRole(userP.getRoleBehavior());
        assertEquals(DM, currentDm.getRoleBehavior());
    }

    @Test
    void switchRoleTest3(){
        UserController userController = new UserController(currentPlayer);
        userController.setCurrentUser(userP);
        userController.switchRole(null);
        assertEquals(PLAYER, currentPlayer.getRoleBehavior());
    }


}
