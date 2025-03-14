package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;

import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import javafx.application.Platform;


import java.io.IOException;
import java.util.logging.Logger;

public class JoinLobbyPlayerBoundary extends JoinLobbyBoundary {

    protected static final Logger logger = Logger.getLogger(JoinLobbyPlayerBoundary.class.getName());

    public JoinLobbyPlayerBoundary() {
        super(); // Usa il costruttore di JoinLobbyBoundary
    }

    @Override
    public void initialize() throws IOException {
        super.initialize();


        // Setup dynamic button for joining lobby
        TableColumnUtils.<LobbyBean>setupDynamicButtonColumn(
                joinButtonColumn, // Assicurarsi che sia TableColumn<LobbyBean, Button>
                lobbyBean -> controller.isLobbyJoined(lobbyBean) ? "" : "Join",
                lobbyBean -> false,  // Nessun pulsante viene disabilitato (la visualizzazione è gestita dal testo vuoto)
                lobbyBean -> {
                    String message = "Vuoi entrare nella lobby '" + lobbyBean.getName() + "'?";
                    confirmationPopup.show(
                            message,
                            10,
                            () -> {
                                // Aggiungi il nuovo giocatore nel primo slot vuoto o aggiorna la lobby
                                controller.addLobby(lobbyBean);
                                Platform.runLater(() -> joinButtonColumn.getTableView().refresh());
                            },
                            this::onCancel
                    );
                }
        );

        // Setup per il bottone dinamico per aggiungere/rimuovere lobby dai favoriti
        TableColumnUtils.<LobbyBean>setupDynamicButtonColumn(
                favouriteButton, // Assicurarsi che sia TableColumn<LobbyBean, Button>
                lobbyBean -> controller.isLobbyFavorite(lobbyBean.getName(), currentUser.getFavouriteLobbies())
                        ? "Remove from Favourite"
                        : "Add to Favourite",
                lobbyBean -> false,
                lobbyBean -> {
                    if (controller.isLobbyFavorite(lobbyBean.getName(), currentUser.getFavouriteLobbies())) {
                        if (controller.removeLobbyByName(lobbyBean.getName())) {
                            logger.info("Lobby rimossa dai preferiti.");
                        }
                    } else {
                        controller.addLobbyToFavourite(lobbyBean);
                        logger.info("Lobby aggiunta ai preferiti.");
                    }
                    // Forza il refresh della TableView per rivalutare il testo del pulsante
                    favouriteButton.getTableView().refresh();
                }
        );
    }

    protected void onCancel() {
        //empty method
    }
}
