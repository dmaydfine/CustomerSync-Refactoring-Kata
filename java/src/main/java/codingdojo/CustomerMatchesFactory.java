package codingdojo;

public class CustomerMatchesFactory {
    public CustomerMatches from(ExternalCustomer externalCustomer, CustomerDataAccess customerDataAccess) throws ConflictException {
        if (externalCustomer == null) {
            return null;
        }
        CustomerMatches customerMatches;
        if (externalCustomer.isCompany()) {
            customerMatches = this.loadCompany(externalCustomer, customerDataAccess);
        } else {
            customerMatches = this.loadPerson(externalCustomer, customerDataAccess);
        }
        return  customerMatches;
    }

    private CustomerMatches loadCompany(ExternalCustomer externalCustomer, CustomerDataAccess customerDataAccess) throws ConflictException {
        final String externalId = externalCustomer.getExternalId();
        final String externalCompanyNumber = externalCustomer.getCompanyNumber();

        var matches = customerDataAccess.loadCompany(externalId, externalCompanyNumber);

        if (matches.getCustomer() != null && !(matches.getCustomer() instanceof Company)) {
            throw new ConflictException("Existing customer for externalCustomer " + externalId + " already exists and is not a company");
        }

        CompanyMatchingStrategy strategy = CompanyMatchingFactory.from(matches.getMatchTerm());
        if (strategy != null) {
            strategy.match(matches, externalCustomer);
        }
        return matches;
    }

    private CustomerMatches loadPerson(ExternalCustomer externalCustomer, CustomerDataAccess customerDataAccess) throws ConflictException {
        final String externalId = externalCustomer.getExternalId();

        var matches = customerDataAccess.loadPerson(externalId);

        if (matches.getCustomer() != null) {
            if (!(matches.getCustomer() instanceof Person)) {
                throw new ConflictException("Existing customer for externalCustomer " + externalId + " already exists and is not a person");
            }
        }

        return matches;
    }
}
