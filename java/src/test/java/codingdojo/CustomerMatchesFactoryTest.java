package codingdojo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerMatchesFactoryTest {
    private final CustomerMatchesFactory underTest = new CustomerMatchesFactory();
    private CustomerDataAccess mockDbAccess;
    private static final String COMPANY_NUMBER = "ABC";
    private static final String EXTERNAL_ID = "12345";


    @BeforeAll
    public  void init() {
        this.mockDbAccess = mock(CustomerDataAccess.class);
    }
    @Test
    public void from_forExternalCompanyButPersonExistsInDb_shouldThrow() {
        // ARRANGE
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setCompanyNumber(COMPANY_NUMBER);
        externalCustomer.setExternalId(EXTERNAL_ID);
        Customer person = new Person();
        CustomerMatches personMatches = new CustomerMatches();
        personMatches.setCustomer(person);
        when(this.mockDbAccess.loadCompany(EXTERNAL_ID, COMPANY_NUMBER)).thenReturn(personMatches);

        // ACT + ASSERT
        assertThrows(ConflictException.class, () -> {
            this.underTest.from(externalCustomer, this.mockDbAccess);
        });
    }

    @Test
    public void from_forExternalPersonButCompanyExistsInDb_shouldThrow() {
        // ARRANGE
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(EXTERNAL_ID);
        Customer company = new Company();
        CustomerMatches companyMatches = new CustomerMatches();
        companyMatches.setCustomer(company);
        when(this.mockDbAccess.loadPerson(EXTERNAL_ID)).thenReturn(companyMatches);

        // ACT + ASSERT
        assertThrows(ConflictException.class, () -> {
            this.underTest.from(externalCustomer, this.mockDbAccess);
        });
    }

    @Test
    public void from_forExternalPersonButNoEntryInDb_shouldReturnEmptyMatch() throws ConflictException {
        // ARRANGE
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(EXTERNAL_ID);
        CustomerMatches withoutMatch = new CustomerMatches();
        withoutMatch.setCustomer(null);
        when(this.mockDbAccess.loadPerson(EXTERNAL_ID)).thenReturn(withoutMatch);

        // ACT
        var actual = this.underTest.from(externalCustomer, this.mockDbAccess);

        // ASSERT
        assertNull(actual.getCustomer());
    }

    @Test
    public void from_forPersonByExternalId_shouldReturnDbEntry() throws ConflictException {
        // ARRANGE
        ExternalCustomer externalCustomer = new ExternalCustomer();
        externalCustomer.setExternalId(EXTERNAL_ID);
        CustomerMatches matches = new CustomerMatches();
        Customer person = new Person();
        person.setExternalId(EXTERNAL_ID);
        matches.setCustomer(person);
        when(this.mockDbAccess.loadPerson(EXTERNAL_ID)).thenReturn(matches);

        // ACT
        var actual = this.underTest.from(externalCustomer, this.mockDbAccess);

        // ASSERT
        assertEquals(person, actual.getCustomer());
    }

    @Test
    public void from_forNull_shouldReturnNull() throws ConflictException {
        // ACT
        var actual = this.underTest.from(null, null);

        // ASSERT
        assertNull(actual);
    }
}
