package codingdojo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class CompanyTest {
    private Company underTest = new Company();

    private static final String EXTERNAL_ID = "12345";
    private static final Address ADDRESS = new Address("123 main st", "Helsingborg", "SE-123 45");
    private static final String COMPANY_NAME = "Acme Inc.";
    private static final String COMPANY_NUMBER = "470813-8895";
    private static final List<ShoppingList> SHOPPING_LIST = Arrays.asList(new ShoppingList("lipstick", "blusher"));
    private static final String PREFERRED_STORE = "preferred";

    @BeforeEach
    void init() {
        this.underTest = new Company();
    }
    @Test
    public void setFields_externalIsNull_shouldDoNothing() {
        // ACT
        this.underTest.setFieldsFromExternalDto(null);

        // ASSERT
        assertNull(this.underTest.getName());
        assertNull(this.underTest.getCompanyNumber());
        assertNull(this.underTest.getAddress());
        assertNull(this.underTest.getPreferredStore());
        assertTrue(this.underTest.getShoppingLists().isEmpty());
    }

    @Test
    public void setFields_validExternal_shouldSet() {
        // ARRANGE
        ExternalCustomer externalCustomer = createExternalCompany();

        // ACT
        this.underTest.setFieldsFromExternalDto(externalCustomer);

        // ASSERT
        assertEquals(COMPANY_NAME, this.underTest.getName());
        assertEquals(COMPANY_NUMBER, this.underTest.getCompanyNumber());
        assertEquals(ADDRESS, this.underTest.getAddress());
        assertEquals(PREFERRED_STORE, this.underTest.getPreferredStore());
        assertEquals(SHOPPING_LIST, this.underTest.getShoppingLists());
    }

    private ExternalCustomer createExternalCompany() {
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(EXTERNAL_ID);
        externalCustomer.setName(COMPANY_NAME);
        externalCustomer.setAddress(ADDRESS);
        externalCustomer.setCompanyNumber(COMPANY_NUMBER);
        externalCustomer.setShoppingLists(SHOPPING_LIST);
        externalCustomer.setPreferredStore(PREFERRED_STORE);
        return externalCustomer;
    }
}
