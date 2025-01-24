module it.uniroma2.marchidori.maininterface {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.uniroma2.marchidori.maininterface to javafx.fxml;
    exports it.uniroma2.marchidori.maininterface;
}