package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import java.util.List;

public class JoinLobbyPlayerBoundary extends JoinLobbyBoundary {
    public JoinLobbyPlayerBoundary() {
        super(); // Usa il costruttore senza parametri di JoinLobbyBoundary (che richiama la factory)
    }

    @Override
    public void initialize() {
        super.initialize();

        // Colonna "Join"
        joinButtonColumn.setCellFactory(col -> new TableCell<>() {
            private final Button joinBtn = new Button("Join");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    LobbyBean lobby = getTableView().getItems().get(getIndex());
                    joinBtn.setOnAction(event -> {
                        String message = "Vuoi entrare nella lobby '" + lobby.getName() + "'?";
                        confirmationPopupController.show(
                                message,
                                10,
                                () -> join(lobby), // azione da eseguire se conferma
                                () -> System.out.println("Azione annullata o scaduta.") // azione se annulla o scade
                        );
                    });
                    setGraphic(joinBtn);
                }
            }
        });

        // Colonna "Add to Favourite" (modificata per toggle)
        favouriteButton.setCellFactory(col -> new TableCell<>() {
            private final Button favouriteBtn = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Ottieni la lobby corrispondente alla riga corrente
                    LobbyBean lobby = getTableView().getItems().get(getIndex());

                    // Controlla se la lobby è già tra quelle favorite
                    if (isLobbyFavorite(lobby.getName(), currentUser.getFavouriteLobbies())) {
                        favouriteBtn.setText("Remove from Favourite");
                    } else {
                        favouriteBtn.setText("Add to Favourite");
                    }

                    favouriteBtn.setOnAction(event -> {
                        // Al click, controlla nuovamente se la lobby è tra quelle favorite
                        if (isLobbyFavorite(lobby.getName(), currentUser.getFavouriteLobbies())) {
                            // Se è presente, rimuovila e cambia il testo
                            boolean removed = currentUser.removeLobbyByName(lobby.getName());
                            if (removed) {
                                favouriteBtn.setText("Add to Favourite");
                                System.out.println("Lobby rimossa dai preferiti.");
                            }
                        } else {
                            // Se non è presente, aggiungila
                            Lobby temp = controller.beanToEntity(lobby);
                            currentUser.addLobbyToFavourite(temp);
                            favouriteBtn.setText("Remove from Favourite");
                            System.out.println("Lobby aggiunta ai preferiti.");
                        }
                    });
                    setGraphic(favouriteBtn);
                }
            }
        });
    }

    // Metodo helper per controllare se una lobby è già nei preferiti
    public boolean isLobbyFavorite(String nameLobby, List<Lobby> favouriteLobbies) {
        if (favouriteLobbies == null || nameLobby == null) {
            return false;
        }
        for (Lobby lobby : favouriteLobbies) {
            if (lobby.getLobbyName().equals(nameLobby)) {
                return true;
            }
        }
        return false;
    }

    private void join(LobbyBean lobby) {
        Lobby temp = controller.beanToEntity(lobby);
        currentUser.addLobby(temp);
    }
}
