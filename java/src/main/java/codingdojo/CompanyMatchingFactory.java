package codingdojo;

public class CompanyMatchingFactory {
    static public CompanyMatchesStrategy from(String matchTerm) {
        if ("ExternalId".equals(matchTerm)) {
            return new CompanyByExternalId();
        }
        if ("CompanyNumber".equals(matchTerm)) {
            return new CompanyByCompanyNumber();
        }

        return null;
    }
}
