package codingdojo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerDataAccessTest {
    private static final String EXTERNAL_ID = "external id";
    private static final String COMPANY_NUMBER = "ABC";
    private CustomerDataLayer mockDataLayer;
    private CustomerDataAccess underTest;

    @BeforeAll
    public void init() {
        this.mockDataLayer = mock(CustomerDataLayer.class);
        this.underTest = new CustomerDataAccess(this.mockDataLayer);
    }

    @Test
    public void loadPerson_noMatch_shouldReturnNull() {
        // ARRANGE
        when(this.mockDataLayer.findByExternalId(EXTERNAL_ID)).thenReturn(null);

        // ACT
        var matches = this.underTest.loadPerson(EXTERNAL_ID);

        // ASSERT
        assertNull(matches.getCustomer());
        assertNull(matches.getMatchTerm());
    }

    @Test
    public void loadPerson_match_shouldReturnMatch() {
        // ARRANGE
        Customer expected = new Person();
        when(this.mockDataLayer.findByExternalId(EXTERNAL_ID)).thenReturn(expected);

        // ACT
        var matches = this.underTest.loadPerson(EXTERNAL_ID);

        // ASSERT
        assertEquals(expected, matches.getCustomer());
    }

    @Test
    public void loadCompany_noMatchByExternalIdOrCompanyNumber_shouldReturnNull() {
        // ARRANGE
        when(this.mockDataLayer.findByExternalId(EXTERNAL_ID)).thenReturn(null);
        when(this.mockDataLayer.findByCompanyNumber(COMPANY_NUMBER)).thenReturn(null);

        // ACT
        var matches = this.underTest.loadCompany(EXTERNAL_ID, COMPANY_NUMBER);

        // ASSERT
        assertNull(matches.getCustomer());
        assertNull(matches.getMatchTerm());
    }

    @Test
    public void loadCompany_matchByExternalIdButNoMatchByMaster_shouldReturnNoDuplicate() {
        // ARRANGE
        Customer company = new Company();
        when(this.mockDataLayer.findByExternalId(EXTERNAL_ID)).thenReturn(company);
        when(this.mockDataLayer.findByMasterExternalId(EXTERNAL_ID)).thenReturn(null);

        // ACT
        var matches = this.underTest.loadCompany(EXTERNAL_ID, COMPANY_NUMBER);

        // ASSERT
        assertEquals(company, matches.getCustomer());
        assertTrue(matches.getDuplicates().isEmpty());
    }

    @Test
    public void loadCompany_matchByExternalIdAndMatchByMaster_shouldReturnDuplicate() {
        // ARRANGE
        Customer company = new Company();
        when(this.mockDataLayer.findByExternalId(EXTERNAL_ID)).thenReturn(company);
        Customer master = new Company();
        when(this.mockDataLayer.findByMasterExternalId(EXTERNAL_ID)).thenReturn(master);

        // ACT
        var matches = this.underTest.loadCompany(EXTERNAL_ID, COMPANY_NUMBER);

        // ASSERT
        assertEquals(company, matches.getCustomer());
        assertEquals(1, matches.getDuplicates().size());
        assertEquals(master, matches.getDuplicates().stream().findFirst().get());
    }

    @Test
    public void loadCompany_noMatchByExternalIdButMatchByCompanyNumber_shouldReturn() {
        // ARRANGE
        Customer company = new Company();
        when(this.mockDataLayer.findByExternalId(EXTERNAL_ID)).thenReturn(null);
        when(this.mockDataLayer.findByCompanyNumber(COMPANY_NUMBER)).thenReturn(company);

        // ACT
        var matches = this.underTest.loadCompany(EXTERNAL_ID, COMPANY_NUMBER);

        // ASSERT
        assertEquals(company, matches.getCustomer());
        assertTrue(matches.getDuplicates().isEmpty());
        assertEquals("CompanyNumber", matches.getMatchTerm());
    }



}
