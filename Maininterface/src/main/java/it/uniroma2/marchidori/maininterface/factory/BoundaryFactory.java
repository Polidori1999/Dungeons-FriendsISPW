package it.uniroma2.marchidori.maininterface.factory;

import java.lang.reflect.InvocationTargetException;

public class BoundaryFactory {

    private BoundaryFactory(){}

    public static <T> T createBoundary(Class<T> controllerClass) {
        try {
            // Utilizza il costruttore no-arg per creare una nuova istanza
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                 | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Errore nella creazione del controller: "
                    + controllerClass.getName(), e);
        }
    }
}
