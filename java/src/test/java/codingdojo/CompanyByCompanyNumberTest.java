package codingdojo;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyByCompanyNumberTest {
    private static final String EXTERNAL_ID = "external id";
    private final CompanyByCompanyNumber underTest = new CompanyByCompanyNumber();

    @Test
    public void match_mismatchInExternalId_shouldThrow() {
        // ARRANGE
        CustomerMatches matches = new CustomerMatches();
        matches.setCustomer(new Company());
        matches.getCustomer().setExternalId(EXTERNAL_ID);
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId("different");

        // ACT + ASSERT
        assertThrows(ConflictException.class, () -> {
            this.underTest.match(matches, externalCustomer);
        });
    }

    @Test
    public void match_externalIdInMatchNull_shouldAcceptExternalId() {
        // ARRANGE
        CustomerMatches matches = new CustomerMatches();
        matches.setCustomer(new Company());
        matches.getCustomer().setExternalId(null);
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(EXTERNAL_ID);

        // ACT
        this.underTest.match(matches, externalCustomer);

        // ASSERT
        var duplicates = matches.getDuplicates();
        assertEquals(1, duplicates.size());
        assertTrue(duplicates.stream().allMatch(Objects::isNull));
        var actual = matches.getCustomer();
        assertEquals(EXTERNAL_ID, actual.getExternalId());
        assertEquals(EXTERNAL_ID, actual.getMasterExternalId());
    }

    @Test
    public void match_matchingExternalId_shouldAcceptExternalId() {
        // ARRANGE
        CustomerMatches matches = new CustomerMatches();
        matches.setCustomer(new Company());
        matches.getCustomer().setExternalId(EXTERNAL_ID);
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(EXTERNAL_ID);

        // ACT
        this.underTest.match(matches, externalCustomer);

        // ASSERT
        var duplicates = matches.getDuplicates();
        assertEquals(1, duplicates.size());
        assertTrue(duplicates.stream().allMatch(Objects::isNull));
        var actual = matches.getCustomer();
        assertEquals(EXTERNAL_ID, actual.getExternalId());
        assertEquals(EXTERNAL_ID, actual.getMasterExternalId());
    }
}
