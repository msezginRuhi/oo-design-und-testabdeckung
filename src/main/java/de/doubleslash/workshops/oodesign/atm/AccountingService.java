package de.doubleslash.workshops.oodesign.atm;

/**
 * Interface zur Verbuchung von Abhebungen. Entkoppelt die Klasse ATM von der konkreten Implementierung
 * {@link AccountingRESTServiceClient}.
 */
public interface AccountingService {

    /**
     * Verbucht eine Geldabhebung auf dem Kundenkonto.
     *
     * @param amount der abgehobene Betrag.
     * @param bankAccountNumber die Kontonummer des Kunden.
     *
     * @return {@code true} wenn die Verbuchung erfolgreich war, andernfalls {@code false}.
     */
    boolean withdrawAmount(double amount, int bankAccountNumber);

}
