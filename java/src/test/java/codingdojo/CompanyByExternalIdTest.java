package codingdojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CompanyByExternalIdTest {
    private static final String COMPANY_NUMBER = "test company number";
    private final CompanyByExternalId underTest = new CompanyByExternalId();

    @Test
    public void match_mismatchInCompanyNumber_shouldReturnEmptyCustomerAndDuplicate() {
        // ARRANGE
        CustomerMatches matches = new CustomerMatches();
        matches.setCustomer(new Company());
        matches.getCustomer().setCompanyNumber(COMPANY_NUMBER);
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setCompanyNumber("different");

        // ACT
        this.underTest.match(matches, externalCustomer);

        // ASSERT
        var duplicates = matches.getDuplicates();
        assertEquals(1, duplicates.size());
        var duplicate = duplicates.stream().findFirst().get();
        assertEquals(COMPANY_NUMBER, duplicate.getCompanyNumber());
        assertNull(duplicate.getMasterExternalId());
        assertNull(matches.getCustomer());
        assertNull(matches.getMatchTerm());
    }

    @Test
    public void match_matchingCompanyNumber_shouldReturnWithoutDuplicates() {
        // ARRANGE
        CustomerMatches matches = new CustomerMatches();
        matches.setCustomer(new Company());
        matches.getCustomer().setCompanyNumber(COMPANY_NUMBER);
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setCompanyNumber(COMPANY_NUMBER);

        // ACT
        this.underTest.match(matches, externalCustomer);

        // ASSERT
        var duplicates = matches.getDuplicates();
        assertEquals(0, duplicates.size());
        assertEquals(COMPANY_NUMBER, matches.getCustomer().getCompanyNumber());
    }
}
