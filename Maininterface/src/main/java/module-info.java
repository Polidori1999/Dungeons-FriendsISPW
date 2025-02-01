module it.uniroma2.marchidori.maininterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires javafx.graphics;
    requires java.desktop;
    requires java.logging;

    // Apre il package principale ai file FXML (controller, ecc.)
    opens it.uniroma2.marchidori.maininterface to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface;

    // Apre / esporta l'entity
    exports it.uniroma2.marchidori.maininterface.entity;
    opens it.uniroma2.marchidori.maininterface.entity to javafx.fxml;

    // Aggiungi questa riga per permettere a javafx.base di riflettere sulle classi nel package .bean
    opens it.uniroma2.marchidori.maininterface.bean to javafx.base;
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
    opens it.uniroma2.marchidori.maininterface.bean.charactersheet to javafx.base;
    exports it.uniroma2.marchidori.maininterface.boundary.user;
    opens it.uniroma2.marchidori.maininterface.boundary.user to javafx.fxml;
}
