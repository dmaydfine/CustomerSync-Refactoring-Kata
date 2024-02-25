package codingdojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerFactoryTest {

    private static final String COMPANY_NUMBER = "ABC";
    private static final String EXTERNAL_ID = "12345";

    @Test
    public void from_forCompany_shouldCreateCompany() {
        // ARRANGE
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setCompanyNumber(COMPANY_NUMBER);
        externalCustomer.setExternalId(EXTERNAL_ID);

        // ACT
        Customer actual = CustomerFactory.from(externalCustomer);

        // ASSERT
        assertTrue(actual instanceof Company);
        assertEquals(EXTERNAL_ID, actual.getExternalId());
        assertEquals(EXTERNAL_ID, actual.getMasterExternalId());
    }

    @Test
    public void from_forPerson_shouldCreatePerson() {
        // ARRANGE
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setCompanyNumber(null);
        externalCustomer.setExternalId(EXTERNAL_ID);

        // ACT
        Customer actual = CustomerFactory.from(externalCustomer);

        // ASSERT
        assertTrue(actual instanceof Person);
        assertEquals(EXTERNAL_ID, actual.getExternalId());
        assertEquals(EXTERNAL_ID, actual.getMasterExternalId());
    }

    @Test
    public void from_forNull_shouldReturnNull() {
        // ACT
        Customer actual = CustomerFactory.from(null);

        // ASSERT
        assertNull(actual);

    }
}
