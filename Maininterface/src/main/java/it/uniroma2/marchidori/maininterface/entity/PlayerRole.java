package it.uniroma2.marchidori.maininterface.entity;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerRole implements RoleBehavior {

    // 1) Dichiarare il logger come private static final
    private static final Logger LOGGER = Logger.getLogger(PlayerRole.class.getName());

    @Override
    public void performAction(String action) {
        // 2) Usare il log con placeholder (se vuoi evitare la concatenazione)
        LOGGER.log(Level.INFO, () -> "Player esegue l'azione: " + action);
        // Logica dedicata al Player...
    }

    @Override
    public boolean canAccessDMTools() {
        return false;
    }

    @Override
    public String getRoleName() {
        return "Player";
    }
}
