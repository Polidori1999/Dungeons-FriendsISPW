package it.uniroma2.marchidori.maininterface.entity;

    public interface RoleBehavior {

        /**
         * Esempio di metodo che un ruolo deve poter gestire
         * in modo diverso (ad es. cosa accade quando l'utente "lancia un incantesimo"?).
         */
        void performAction(String action);

        /**
         * Esempio di metodo per controllare se un certo comando
         * o un certo bottone della UI è disponibile per questo ruolo.
         */
        boolean canAccessDMTools();

        String getRoleName();
    }
