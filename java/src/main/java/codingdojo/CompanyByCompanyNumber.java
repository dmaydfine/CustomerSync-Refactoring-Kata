package codingdojo;

public class CompanyByCompanyNumber implements CompanyMatchingStrategy {
    @Override
    public void match(CustomerMatches matches, ExternalCustomer externalCustomer) throws ConflictException {
        Customer company = matches.getCustomer();
        String externalIdFromDb = company.getExternalId();

        var externalId = externalCustomer.getExternalId();
        if (externalIdFromDb != null && !externalId.equals(externalIdFromDb)) {
            var externalCompanyNumber = externalCustomer.getCompanyNumber();
            throw new ConflictException("Existing customer for externalCustomer " + externalCompanyNumber + " doesn't match external id " + externalId + " instead found " + externalIdFromDb );
        }
        company.setExternalId(externalId);
        company.setMasterExternalId(externalId);
        matches.addDuplicate(null);
    }
}
