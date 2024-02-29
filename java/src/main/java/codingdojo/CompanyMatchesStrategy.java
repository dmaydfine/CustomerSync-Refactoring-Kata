package codingdojo;

public interface CompanyMatchesStrategy {
     void match(CustomerMatches matches, ExternalCustomer externalCustomer) throws ConflictException;
}
