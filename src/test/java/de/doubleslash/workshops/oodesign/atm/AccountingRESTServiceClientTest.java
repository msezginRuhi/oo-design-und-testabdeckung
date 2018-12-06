package de.doubleslash.workshops.oodesign.atm;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AccountingRESTServiceClientTest {

    private TestLog testLog;

    private AccountingService testee;

    @Before
    public void setUp() {
        testLog = new TestLog();
        testee = new AccountingRESTServiceClient(testLog);
    }

    @Test
    public void accountingServiceShouldLogTransaction() {
        // arrange (nothing to do)

        // act
        testee.withdrawAmount(100.00, 543_210);

        // assert
        String expectedLogMessage = "AccountingRESTServiceClient: Verbuche Auszahlung von Betrag 100.0 auf Kontonummer 543210.";
        // prüfen dass die Transaktion geloggt wurde
        List<String> loggedMessages = testLog.getInfoMessages();
        assertThat(loggedMessages, hasItem(expectedLogMessage));
    }

}
