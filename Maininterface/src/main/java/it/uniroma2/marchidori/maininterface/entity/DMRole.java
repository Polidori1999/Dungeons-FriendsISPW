package it.uniroma2.marchidori.maininterface.entity;

import java.util.logging.Level;
import java.util.logging.Logger;


public class DMRole implements RoleBehavior {

    private static final Logger LOGGER = Logger.getLogger(DMRole.class.getName());

    @Override
    public void performAction(String action) {
        // Implementazione specifica per il Dungeon Master
        LOGGER.log(Level.INFO, () -> "Player esegue l'azione: " + action);
        // Logica dedicata al Dungeon Master...
    }

    @Override
    public boolean canAccessDMTools() {
        // Il DM può accedere a strumenti avanzati
        return true;
    }

    @Override
    public String getRoleName() {
        return "DungeonMaster";
    }
}
