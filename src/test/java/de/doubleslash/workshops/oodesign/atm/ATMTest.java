package de.doubleslash.workshops.oodesign.atm;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Testet die Klasse ATM.
 */
public class ATMTest {

    private CardReader cardReaderMock;
    private AccountingService accountingServiceMock;
    private MoneyDispenser moneyDispenserMock;

    // Instanz der zu testenden Klasse
    private ATM testee;

    @Before
    public void setUp() {
        cardReaderMock = Mockito.mock(CardReader.class);
        accountingServiceMock = Mockito.mock(AccountingRESTServiceClient.class);
        moneyDispenserMock = Mockito.mock(MoneyDispenser.class);
        testee = new ATM(cardReaderMock, accountingServiceMock, moneyDispenserMock);
    }

    @Test
    public void accountingServiceShouldBeCalledWithCorrectAmountAndAccountNumberWhenPinIsCorrect() throws CardReaderException {
        // arrange
        // Kontonummer bestimmen, die vom CardReader gelesen wird
        Mockito.when(cardReaderMock.readAccountNumber()).thenReturn(4711);

        // simulieren dass die PIN vom Kunden korrekt eingegeben wurde
        Mockito.when(cardReaderMock.verifyPin(1234)).thenReturn(true);

        // act
        testee.withdrawMoney(1234, 100.0);

        // assert
        // prüfen ob der AccountingService mit korrektem Betrag u. Kontonummer aufgerufen wurde
        Mockito.verify(accountingServiceMock).withdrawAmount(100.0, 4711);
    }

    @Test
    public void moneyShouldBeDispensedWhenPinIsCorrect() {
        // arrange
        // simulieren dass die PIN vom Kunden korrekt eingegeben wurde
        Mockito.when(cardReaderMock.verifyPin(1234)).thenReturn(true);
        // simulieren dass die Verbuchung via account service erfolgreich war
        Mockito.when(accountingServiceMock.withdrawAmount(anyDouble(), anyInt())).thenReturn(true);

        // act
        testee.withdrawMoney(1234, 100.0);

        // assert
        // prüfen dass der die dispenseCash-Methode vom MoneyDispenser aufgerufen wurde
        Mockito.verify(moneyDispenserMock).dispenseCash(100.0);
    }

    @Test
    public void accountingServiceShouldNotBeCalledWhenCardCannotBeRead() throws CardReaderException {
        // arrange
        // simulieren dass die PIN vom Kunden korrekt eingegeben wurde
        Mockito.when(cardReaderMock.verifyPin(1234)).thenReturn(true);
        // simulieren dass eine CardReaderException beim Aufruf von readAccountNumber() geworfen wird
        Mockito.when(cardReaderMock.readAccountNumber()).thenThrow(new CardReaderException());

        // act
        testee.withdrawMoney(1234, 100.0);

        // assert
        // prüfen dass der AccountingService NICHT aufgerufen wurde
        Mockito.verify(accountingServiceMock, never()).withdrawAmount(anyDouble(), anyInt());
    }

    @Test
    public void accountingServiceShouldNotBeCalledWhenPinIsIncorrect() {
        // arrange
        // simulieren der Eingabe einer falschen PIN
        Mockito.when(cardReaderMock.verifyPin(anyInt())).thenReturn(false);

        // act
        testee.withdrawMoney(1234, 100.0);

        // assert
        // prüfen dass der AccountingService NICHT aufgerufen wurde
        Mockito.verify(accountingServiceMock, never()).withdrawAmount(anyDouble(), anyInt());
    }

    @Test
    public void moneyShouldNotBeDispensedWhenPinIsIncorrect() {
        // arrange
        // simulieren der Eingabe einer falschen PIN
        Mockito.when(cardReaderMock.verifyPin(anyInt())).thenReturn(false);

        // act
        testee.withdrawMoney(1234, 100.0);

        // assert
        // prüfen dass der MoneyDispenser NICHT aufgerufen wurde
        Mockito.verify(moneyDispenserMock, never()).dispenseCash(anyDouble());
    }

    @Test
    public void moneyShouldNotBeDispensedWhenTransactionWasNotBooked() {
        // arrange
        // simulieren dass die PIN vom Kunden korrekt eingegeben wurde
        Mockito.when(cardReaderMock.verifyPin(1234)).thenReturn(true);

        // simulieren dass die Verbuchung via AccountingService nicht erfolgreich war
        Mockito.when(accountingServiceMock.withdrawAmount(anyDouble(), anyInt())).thenReturn(false);

        // act
        testee.withdrawMoney(1234, 100.0);

        // assert
        //prüfen dass der MoneyDispenser NICHT aufgerufen wurde
        Mockito.verify(moneyDispenserMock, never()).dispenseCash(anyDouble());
    }

}
