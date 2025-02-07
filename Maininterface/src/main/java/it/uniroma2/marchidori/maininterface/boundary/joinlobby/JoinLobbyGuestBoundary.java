package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;


import java.io.IOException;
import java.util.ArrayList;

public class JoinLobbyGuestBoundary extends JoinLobbyPlayerBoundary {

    @Override
    public void initialize() {
        super.initialize();
        if (currentUser == null) {
            currentUser = new UserBean("guest", "guest@example.com","guest", RoleEnum.GUEST, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        // Imposta la factory per le colonne in modo più modulare
        joinButtonColumn.setCellFactory(col -> createJoinCell());
        favouriteButton.setCellFactory(col -> createFavouriteCell());
    }

    /**
     * Crea una TableCell contenente un pulsante "Join".
     */
    private TableCell<LobbyBean, Void> createJoinCell() {
        return new TableCell<>() {
            private Button joinBtn;

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    if (joinBtn == null) {
                        joinBtn = new Button("Join");
                        joinBtn.setOnAction(event -> handleJoinAction());
                    }
                    setGraphic(joinBtn);
                }
            }
        };
    }

    /**
     * Crea una TableCell contenente un pulsante "Add to Favourite".
     */
    private TableCell<LobbyBean, Void> createFavouriteCell() {
        return new TableCell<>() {
            private Button favouriteBtn;

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    if (favouriteBtn == null) {
                        favouriteBtn = new Button("Add to Favourite");
                        favouriteBtn.setOnAction(event -> handleFavouriteAction());
                    }
                    setGraphic(favouriteBtn);
                }
            }
        };
    }

    /**
     * Gestisce l'azione per il pulsante "Join".
     */
    private void handleJoinAction() {
        try {
            changeScene(SceneNames.LOGIN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gestisce l'azione per il pulsante "Add to Favourite".
     */
    private void handleFavouriteAction() {
        try {
            changeScene(SceneNames.LOGIN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
