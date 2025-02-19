package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import java.io.IOException;
import java.util.ArrayList;

public class JoinLobbyGuestBoundary extends JoinLobbyPlayerBoundary {

    @Override
    public void initialize() throws IOException {
        super.initialize();
        if (currentUser == null) {
            currentUser = new UserBean(

                    "guest@example.com",
                    "guest",
                    RoleEnum.GUEST,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
        }

        // Configura la colonna "Join" utilizzando il metodo della classe utility
        TableColumnUtils.setupButtonColumn(
                joinButtonColumn,  // Assicurarsi che sia TableColumn<LobbyBean, Button>
                "Join",
                lobby -> handleSame()
        );

        // Configura la colonna "Favourite" utilizzando il metodo della classe utility
        TableColumnUtils.setupButtonColumn(
                favouriteButton,  // Assicurarsi che sia TableColumn<LobbyBean, Button>
                "Add to Favourite",
                lobby -> handleSame()
        );
    }

    /**
     * Gestisce l'azione per il pulsante "Join".
     */

    private void handleSame() {
        if (confirmationPopup != null) {
            String message = "you are gettin redirected to login";
            confirmationPopup.show(message, 10,
                    this::onConfirm,
                    this::onCancel);
        } else {
            logger.severe("Errore: ConfirmationPopup non inizializzato o pendingDeleteBean è null");
        }
    }

    /**
     * Gestisce l'azione per il pulsante "Add to Favourite".
     */
    private void onConfirm() {
        try {
            Session.getInstance().clear();
            currentUser = null;
            changeScene(SceneNames.LOGIN);
        } catch (IOException e) {
            throw new SceneChangeException(e.getMessage());
        }
    }
    private void onCancel() {
        //empty
    }
}
