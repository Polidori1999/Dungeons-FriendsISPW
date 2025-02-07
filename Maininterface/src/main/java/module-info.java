module it.uniroma2.marchidori.maininterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires javafx.graphics;
    requires java.desktop;
    requires java.logging;
    requires jbcrypt;


    opens it.uniroma2.marchidori.maininterface to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface;
    exports it.uniroma2.marchidori.maininterface.entity;
    opens it.uniroma2.marchidori.maininterface.entity to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface.bean;
    opens it.uniroma2.marchidori.maininterface.bean to javafx.base;
    exports it.uniroma2.marchidori.maininterface.bean.charactersheetb;
    opens it.uniroma2.marchidori.maininterface.bean.charactersheetb to javafx.base;
    exports it.uniroma2.marchidori.maininterface.boundary;
    opens it.uniroma2.marchidori.maininterface.boundary to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface.boundary.charactersheet;
    opens it.uniroma2.marchidori.maininterface.boundary.charactersheet to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface.boundary.joinlobby;
    opens it.uniroma2.marchidori.maininterface.boundary.joinlobby to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface.boundary.managelobby;
    opens it.uniroma2.marchidori.maininterface.boundary.managelobby to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface.boundary.login;
    opens it.uniroma2.marchidori.maininterface.boundary.login to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface.boundary.user;
    opens it.uniroma2.marchidori.maininterface.boundary.user to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface.enumerate;
    opens it.uniroma2.marchidori.maininterface.enumerate to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface.boundary.consultrules;
    opens it.uniroma2.marchidori.maininterface.boundary.consultrules to javafx.fxml;
    opens it.uniroma2.marchidori.maininterface.control;
    exports it.uniroma2.marchidori.maininterface.control to javafx.fxml;


}
