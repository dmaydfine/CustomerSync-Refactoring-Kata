package codingdojo;

public class CompanyByExternalId implements CompanyMatchingStrategy {
    @Override
    public void match(CustomerMatches matches, ExternalCustomer externalCustomer) {
        Customer company = matches.getCustomer();
        String companyNumberFromDb = company.getCompanyNumber();
        if (!externalCustomer.getCompanyNumber().equals(companyNumberFromDb)) {
            // We save the existing entry as a duplicate and create a new one when the companyNumber changes.
            company.setMasterExternalId(null);
            matches.addDuplicate(company);
            matches.setCustomer(null);
            matches.setMatchTerm(null);
        }
    }
}
