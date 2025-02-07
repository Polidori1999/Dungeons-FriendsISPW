package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import java.util.logging.Logger;

public class JoinLobbyPlayerBoundary extends JoinLobbyBoundary {

    protected static final Logger logger = Logger.getLogger(JoinLobbyPlayerBoundary.class.getName());

    public JoinLobbyPlayerBoundary() {
        super(); // Usa il costruttore di JoinLobbyBoundary
    }

    @Override
    public void initialize() {
        super.initialize();

        // Configura la colonna "Join" in modo dinamico usando il metodo utility
        TableColumnUtils.<LobbyBean>setupDynamicButtonColumn(
                joinButtonColumn, // Assicurarsi che sia TableColumn<LobbyBean, Button>
                lobby -> controller.isLobbyJoined(lobby) ? "" : "Join",
                lobby -> false,  // Nessun pulsante viene disabilitato (la visualizzazione è gestita dal testo vuoto)
                lobby -> {
                    String message = "Vuoi entrare nella lobby '" + lobby.getName() + "'?";
                    confirmationPopupController.show(
                            message,
                            10,
                            () -> controller.addLobby(lobby),
                            () -> logger.info("Azione annullata o scaduta.")
                    );
                }
        );

        // Configura la colonna "Favourite" in modo dinamico usando il metodo utility
        TableColumnUtils.<LobbyBean>setupDynamicButtonColumn(
                favouriteButton, // Assicurarsi che sia TableColumn<LobbyBean, Button>
                lobby -> controller.isLobbyFavorite(lobby.getName(), currentUser.getFavouriteLobbies())
                        ? "Remove from Favourite"
                        : "Add to Favourite",
                lobby -> false,
                lobby -> {
                    if (controller.isLobbyFavorite(lobby.getName(), currentUser.getFavouriteLobbies())) {
                        if (controller.removeLobbyByName(lobby.getName())) {
                            logger.info("Lobby rimossa dai preferiti.");
                        }
                    } else {
                        controller.addLobbyToFavourite(lobby);
                        logger.info("Lobby aggiunta ai preferiti.");
                    }
                }
        );
    }
}