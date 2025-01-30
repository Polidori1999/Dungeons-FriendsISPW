package it.uniroma2.marchidori.maininterface.exception;

    /**
     * Eccezione dedicata per gestire gli errori di cambio scena.
     * Estende RuntimeException così che sia unchecked.
     */
    public class SceneChangeException extends RuntimeException {

        // Costruttore di base
        public SceneChangeException() {
            super();
        }

        // Costruttore con messaggio di errore personalizzato
        public SceneChangeException(String message) {
            super(message);
        }

        // Costruttore con messaggio e causa sottostante (chaining dell'eccezione)
        public SceneChangeException(String message, Throwable cause) {
            super(message, cause);
        }

        // Costruttore con sola causa sottostante
        public SceneChangeException(Throwable cause) {
            super(cause);
        }
    }
