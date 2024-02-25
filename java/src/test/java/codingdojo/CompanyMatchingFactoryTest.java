package codingdojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyMatchingFactoryTest {
    @Test
    public void from_forExternalId_shouldReturnExternalIdStrategy() {
        // ACT
        var companyMatchingStrategy = CompanyMatchingFactory.from("ExternalId");

        // ASSERT
        assertTrue(companyMatchingStrategy instanceof CompanyByExternalId);
    }

    @Test
    public void from_forCompanyNumber_shouldReturnCompanyNumberStrategy() {
        // ACT
        var companyMatchingStrategy = CompanyMatchingFactory.from("CompanyNumber");

        // ASSERT
        assertTrue(companyMatchingStrategy instanceof CompanyByCompanyNumber);
    }

    @Test
    public void from_forNull_shouldReturnNull() {
        // ACT
        var companyMatchingStrategy = CompanyMatchingFactory.from(null);

        // ASSERT
        assertNull(companyMatchingStrategy);
    }

    @Test
    public void from_forInvalidString_shouldReturnNull() {
        // ACT
        var companyMatchingStrategy = CompanyMatchingFactory.from("test");

        // ASSERT
        assertNull(companyMatchingStrategy);
    }
}
