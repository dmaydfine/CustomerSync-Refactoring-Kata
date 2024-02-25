package codingdojo;

import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CustomerSyncTest {

    private static final String EXTERNAL_ID = "12345";
    private static final String INTERNAL_ID = "45435";
    private static final Integer BONUS_POINTS_BALANCE = 123;
    private static final Address ADDRESS = new Address("123 main st", "Helsingborg", "SE-123 45");
    private static final String PERSON_NAME = "Alice";
    private static final String COMPANY_NAME = "Acme Inc.";
    private static final String COMPANY_NUMBER = "470813-8895";
    private static final List<ShoppingList> SHOPPING_LIST = Arrays.asList(new ShoppingList("lipstick", "blusher"));

    @Test
    public void syncCompany_byExternalId_shouldUpdate() {
        // ARRANGE
        ExternalCustomer externalCustomer = createExternalCompany();
        Customer customer = createCustomerWithSameCompanyAs(externalCustomer);

        CustomerDataLayer db = mock(CustomerDataLayer.class);
        when(db.findByExternalId(EXTERNAL_ID)).thenReturn(customer);
        CustomerSync sut = new CustomerSync(db);

        // ACT
        boolean created = sut.syncWithDataLayer(externalCustomer);

        // ASSERT
        assertFalse(created);
        ArgumentCaptor<Customer> argument = ArgumentCaptor.forClass(Customer.class);
        verify(db, atLeastOnce()).updateCustomerRecord(argument.capture());
        Customer updatedCustomer = argument.getValue();
        assertEquals(COMPANY_NAME, updatedCustomer.getName());
        assertEquals(EXTERNAL_ID, updatedCustomer.getExternalId());
        assertNull(updatedCustomer.getMasterExternalId());
        assertEquals(COMPANY_NUMBER, updatedCustomer.getCompanyNumber());
        assertEquals(ADDRESS, updatedCustomer.getAddress());
        assertEquals(SHOPPING_LIST, updatedCustomer.getShoppingLists());
        assertEquals(CustomerType.COMPANY, updatedCustomer.getCustomerType());
        assertNull(updatedCustomer.getPreferredStore());
    }

    @Test
    public void syncPerson_byExternalId_shouldUpdate() {
        // ARRANGE
        ExternalCustomer externalCustomer = createExternalPerson();
        Customer customer = createPerson();

        CustomerDataLayer db = mock(CustomerDataLayer.class);
        when(db.findByExternalId(EXTERNAL_ID)).thenReturn(customer);
        CustomerSync sut = new CustomerSync(db);

        // ACT
        boolean created = sut.syncWithDataLayer(externalCustomer);

        // ASSERT
        assertFalse(created);
        ArgumentCaptor<Customer> argument = ArgumentCaptor.forClass(Customer.class);
        verify(db, atLeastOnce()).updateCustomerRecord(argument.capture());
        Customer updatedCustomer = argument.getValue();
        assertEquals(PERSON_NAME, updatedCustomer.getName());
        assertEquals(EXTERNAL_ID, updatedCustomer.getExternalId());
        assertNull(updatedCustomer.getMasterExternalId());
        assertNull(updatedCustomer.getCompanyNumber());
        assertEquals(ADDRESS, updatedCustomer.getAddress());
        assertEquals(SHOPPING_LIST, updatedCustomer.getShoppingLists());
        assertEquals(CustomerType.PERSON, updatedCustomer.getCustomerType());
        assertEquals(BONUS_POINTS_BALANCE, updatedCustomer.getBonusPointsBalance());
        assertNull(updatedCustomer.getPreferredStore());
    }

    @Test
    public void syncPerson_byExternalId_shouldCreate() {
        // ARRANGE
        ExternalCustomer externalCustomer = createExternalPerson();

        CustomerDataLayer db = mock(CustomerDataLayer.class);
        when(db.findByExternalId(EXTERNAL_ID)).thenReturn(null);  // no entry means we create a new customer
        when(db.createCustomerRecord(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        CustomerSync sut = new CustomerSync(db);

        // ACT
        boolean created = sut.syncWithDataLayer(externalCustomer);

        // ASSERT
        assertTrue(created);
        ArgumentCaptor<Customer> argument = ArgumentCaptor.forClass(Customer.class);
        verify(db, atLeastOnce()).updateCustomerRecord(argument.capture());
        Customer updatedCustomer = argument.getValue();
        assertEquals(PERSON_NAME, updatedCustomer.getName());
        assertEquals(EXTERNAL_ID, updatedCustomer.getExternalId());
        assertEquals(EXTERNAL_ID, updatedCustomer.getMasterExternalId());
        assertNull(updatedCustomer.getCompanyNumber());
        assertEquals(ADDRESS, updatedCustomer.getAddress());
        assertEquals(SHOPPING_LIST, updatedCustomer.getShoppingLists());
        assertEquals(CustomerType.PERSON, updatedCustomer.getCustomerType());
        assertEquals(BONUS_POINTS_BALANCE, updatedCustomer.getBonusPointsBalance());
        assertNull(updatedCustomer.getPreferredStore());
    }


    private ExternalCustomer createExternalCompany() {
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(EXTERNAL_ID);
        externalCustomer.setName(COMPANY_NAME);
        externalCustomer.setAddress(ADDRESS);
        externalCustomer.setCompanyNumber(COMPANY_NUMBER);
        externalCustomer.setShoppingLists(SHOPPING_LIST);
        return externalCustomer;
    }

    private Customer createCustomerWithSameCompanyAs(ExternalCustomer externalCustomer) {
        Customer customer = new Customer();
        customer.setCompanyNumber(externalCustomer.getCompanyNumber());
        customer.setCustomerType(CustomerType.COMPANY);
        customer.setInternalId(INTERNAL_ID);
        customer.setExternalId(EXTERNAL_ID);
        return customer;
    }

    private ExternalCustomer createExternalPerson() {
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(EXTERNAL_ID);
        externalCustomer.setName(PERSON_NAME);
        externalCustomer.setBonusPointsBalance(BONUS_POINTS_BALANCE);
        externalCustomer.setAddress(ADDRESS);
        externalCustomer.setShoppingLists(SHOPPING_LIST);
        return externalCustomer;
    }

    private Customer createPerson() {
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.PERSON);
        customer.setInternalId(INTERNAL_ID);
        customer.setExternalId(EXTERNAL_ID);
        return customer;
    }

}
