package it.uniroma2.marchidori.maininterface.boundary.joinlobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.util.List;

public class JoinLobbyPlayerBoundary extends JoinLobbyBoundary {

    // Recupera l'utente corrente dalla Sessione
    public User currentEntity = Session.getCurrentUser();

    public JoinLobbyPlayerBoundary() {
        super(); // Usa il costruttore senza parametri di JoinLobbyBoundary (che richiama la factory)
    }

    @Override
    public void initialize() {
        super.initialize();

        // Colonna "Join": se la lobby è già joinata, il pulsante non viene mostrato
        joinButtonColumn.setCellFactory(col -> new TableCell<>() {
            private final Button joinBtn = new Button("Join");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Ottieni il LobbyBean della riga corrente
                    LobbyBean lobby = getTableView().getItems().get(getIndex());

                    // Se la lobby è già joinata, non mostriamo il pulsante
                    if (isLobbyJoined(lobby)) {
                        setGraphic(null);
                    } else {
                        joinBtn.setText("Join");
                        joinBtn.setOnAction(event -> {
                            String message = "Vuoi entrare nella lobby '" + lobby.getName() + "'?";
                            confirmationPopupController.show(
                                    message,
                                    10,
                                    () -> join(lobby), // azione da eseguire se conferma
                                    () -> System.out.println("Azione annullata o scaduta.")
                            );
                        });
                        setGraphic(joinBtn);
                    }
                }
            }

            // Metodo helper: verifica se la lobby è già presente nella lista joinata
            private boolean isLobbyJoined(LobbyBean lobby) {
                if (currentUser != null && currentUser.getJoinedLobbies() != null) {
                    for (LobbyBean lb : currentUser.getJoinedLobbies()) {
                        if (lb.getName().equals(lobby.getName())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        // Colonna "Add to Favourite" (toggle)
        favouriteButton.setCellFactory(col -> new TableCell<>() {
            private final Button favouriteBtn = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Ottieni la lobby della riga corrente
                    LobbyBean lobby = getTableView().getItems().get(getIndex());

                    // Imposta il testo del pulsante in base allo stato di "favourite"
                    if (isLobbyFavorite(lobby.getName(), currentUser.getFavouriteLobbies())) {
                        favouriteBtn.setText("Remove from Favourite");
                    } else {
                        favouriteBtn.setText("Add to Favourite");
                    }

                    favouriteBtn.setOnAction(event -> {
                        if (isLobbyFavorite(lobby.getName(), currentUser.getFavouriteLobbies())) {
                            // Rimuovi la lobby dai preferiti e aggiorna il testo
                            boolean uno = currentUser.removeLobbyByName(lobby.getName());
                            boolean due = currentEntity.removeLobbyByName(lobby.getName());
                            if (uno && due) {
                                favouriteBtn.setText("Add to Favourite");
                                System.out.println("Lobby rimossa dai preferiti.");
                            }
                        } else {
                            // Aggiungi la lobby ai preferiti e aggiorna il testo
                            Lobby temp = controller.beanToEntity(lobby);
                            currentUser.addLobbyToFavourite(lobby);
                            currentEntity.addLobbyToFavourite(temp);
                            favouriteBtn.setText("Remove from Favourite");
                            System.out.println("Lobby aggiunta ai preferiti.");
                        }
                    });
                    setGraphic(favouriteBtn);
                }
            }
        });
    }

    // Metodo helper: verifica se una lobby è già tra quelle joinate
    public boolean isLobbyFavorite(String nameLobby, List<LobbyBean> favouriteLobbies) {
        if (favouriteLobbies == null || nameLobby == null) {
            return false;
        }
        for (LobbyBean lobby : favouriteLobbies) {
            if (lobby.getName().equals(nameLobby)) {
                return true;
            }
        }
        return false;
    }

    // Metodo che esegue l'azione di join
    private void join(LobbyBean lobby) {
        System.out.println(">>> DEBUG: Sto eseguendo join su: " + lobby.getName());
        Lobby temp = controller.beanToEntity(lobby);
        // Aggiungi la lobby alla lista joinata di currentUser e currentEntity
        currentUser.addLobby(lobby);
        currentEntity.addLobby(temp);
        // Rinfresca la TableView in modo che la cella venga aggiornate (nascondendo il pulsante "Join")
        lobbyTableView.refresh();
    }
}
