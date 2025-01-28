module it.uniroma2.marchidori.maininterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires javafx.graphics;
    requires java.desktop;


    opens it.uniroma2.marchidori.maininterface to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface;
    exports it.uniroma2.marchidori.maininterface.entity;
    opens it.uniroma2.marchidori.maininterface.entity to javafx.fxml;
}