module it.uniroma2.marchidori.maininterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;


    opens it.uniroma2.marchidori.maininterface to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface;
}