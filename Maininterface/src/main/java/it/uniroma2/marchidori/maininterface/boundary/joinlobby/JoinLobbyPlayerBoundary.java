package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

import java.util.List;

public class JoinLobbyPlayerBoundary extends JoinLobbyBoundary {

    // User corrente: se risulta null lo recuperiamo dalla Session


    public JoinLobbyPlayerBoundary() {
        super(); // Usa il costruttore di JoinLobbyBoundary
    }

    @Override
    public void initialize() {
        super.initialize();
        // Imposta le cell factory delle colonne "Join" e "Favourite"
        joinButtonColumn.setCellFactory(col -> createJoinButtonCell());
        favouriteButton.setCellFactory(col -> createFavouriteButtonCell());
    }

    // ----------------------- METODI PER IL PULSANTE JOIN -----------------------
    private TableCell<LobbyBean, Void> createJoinButtonCell() {
        return new TableCell<>() {
            private final Button joinBtn = new Button("Join");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                LobbyBean lobby = getTableView().getItems().get(getIndex());
                if (isLobbyJoined(lobby)) {
                    setGraphic(null);
                } else {
                    configureJoinButton(joinBtn, lobby);
                    setGraphic(joinBtn);
                }
            }
        };
    }

    private void configureJoinButton(Button btn, LobbyBean lobby) {
        btn.setText("Join");
        btn.setOnAction(event -> {
            String message = "Vuoi entrare nella lobby '" + lobby.getName() + "'?";
            confirmationPopupController.show(
                    message,
                    10,
                    () -> join(lobby),
                    () -> System.out.println("Azione annullata o scaduta.")
            );
        });
    }

    private boolean isLobbyJoined(LobbyBean lobby) {
        return currentUser != null &&
                currentUser.getJoinedLobbies() != null &&
                currentUser.getJoinedLobbies().stream()
                        .anyMatch(lb -> lb.getName().equals(lobby.getName()));
    }

    // ----------------------- METODI PER IL PULSANTE FAVOURITE -----------------------
    private TableCell<LobbyBean, Void> createFavouriteButtonCell() {
        return new TableCell<>() {
            private final Button favBtn = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                LobbyBean lobby = getTableView().getItems().get(getIndex());
                configureFavouriteButton(favBtn, lobby);
                setGraphic(favBtn);
            }
        };
    }

    private void configureFavouriteButton(Button btn, LobbyBean lobby) {
        boolean isFav = isLobbyFavorite(lobby.getName(), currentUser.getFavouriteLobbies());
        btn.setText(isFav ? "Remove from Favourite" : "Add to Favourite");
        btn.setOnAction(event -> {
            if (isLobbyFavorite(lobby.getName(), currentUser.getFavouriteLobbies())) {
                if (controller.removeLobbyByName(lobby.getName())) {
                    btn.setText("Add to Favourite");
                    System.out.println("Lobby rimossa dai preferiti.");
                }
            } else {
                Lobby temp = controller.beanToEntity(lobby);
                controller.addLobbyToFavourite(lobby);
                btn.setText("Remove from Favourite");
                System.out.println("Lobby aggiunta ai preferiti.");
            }
        });
    }

    public boolean isLobbyFavorite(String nameLobby, List<LobbyBean> favouriteLobbies) {
        if (favouriteLobbies == null || nameLobby == null) return false;
        return favouriteLobbies.stream().anyMatch(lb -> lb.getName().equals(nameLobby));
    }

    // ----------------------- METODO PER ESEGUIRE JOIN -----------------------
    private void join(LobbyBean lobby) {
        System.out.println(">>> DEBUG: Sto eseguendo join su: " + lobby.getName());
        // Assicurati che currentEntity sia non null
        Lobby temp = controller.beanToEntity(lobby);
        controller.addLobby(lobby);
        controller.addLobby(temp);
        // Refresh della TableView per aggiornare la visualizzazione
        getTableView().refresh();
    }


    // Presumiamo che getTableView() sia disponibile (se non lo è, usa il riferimento corretto)
    private TableView<LobbyBean> getTableView() {
        // Se il campo lobbyTableView è definito in JoinLobbyBoundary, puoi semplicemente:
        return lobbyTableView;
    }
}
