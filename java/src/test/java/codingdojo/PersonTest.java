package codingdojo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class PersonTest {
    private Person underTest = new Person();

    private static final String EXTERNAL_ID = "12345";
    private static final Integer BONUS_POINTS_BALANCE = 123;
    private static final Address ADDRESS = new Address("123 main st", "Helsingborg", "SE-123 45");
    private static final String PERSON_NAME = "Alice";
    private static final List<ShoppingList> SHOPPING_LIST = Arrays.asList(new ShoppingList("lipstick", "blusher"));
    private static final String PREFERRED_STORE = "preferred";

    @BeforeEach
    void init() {
        this.underTest = new Person();
    }
    @Test
    public void setFields_externalIsNull_shouldDoNothing() {
        // ACT
        this.underTest.setFieldsFromExternalDto(null);

        // ASSERT
        assertNull(this.underTest.getName());
        assertNull(this.underTest.getBonusPointsBalance());
        assertNull(this.underTest.getAddress());
        assertNull(this.underTest.getPreferredStore());
        assertTrue(this.underTest.getShoppingLists().isEmpty());
    }

    @Test
    public void setFields_validExternal_shouldSet() {
        // ARRANGE
        ExternalCustomer externalCustomer = createExternalPerson();

        // ACT
        this.underTest.setFieldsFromExternalDto(externalCustomer);

        // ASSERT
        assertEquals(PERSON_NAME, this.underTest.getName());
        assertEquals(BONUS_POINTS_BALANCE, this.underTest.getBonusPointsBalance());
        assertEquals(ADDRESS, this.underTest.getAddress());
        assertEquals(PREFERRED_STORE, this.underTest.getPreferredStore());
        assertEquals(SHOPPING_LIST, this.underTest.getShoppingLists());
    }

    private ExternalCustomer createExternalPerson() {
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(EXTERNAL_ID);
        externalCustomer.setName(PERSON_NAME);
        externalCustomer.setBonusPointsBalance(BONUS_POINTS_BALANCE);
        externalCustomer.setAddress(ADDRESS);
        externalCustomer.setShoppingLists(SHOPPING_LIST);
        externalCustomer.setPreferredStore(PREFERRED_STORE);
        return externalCustomer;
    }
}