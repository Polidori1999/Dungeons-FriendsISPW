package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;


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
                    confirmationPopupController.show(
                            message,
                            10,
                            () -> {
                                try {
                                    // Aggiungi il nuovo giocatore nel primo slot vuoto
                                    //controller.addPlayerToLobby(Converter.lobbyBeanToEntity(lobbyBean), currentUser.getEmail());
                                    controller.addLobby(lobbyBean); // oppure aggiorna la lobby
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                joinButtonColumn.getTableView().refresh();
                            },
                            () -> logger.info("Azione annullata o scaduta.")
                    );
                }

        );

        // Setup dynamic button for adding/removing lobby from favourites
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

}
