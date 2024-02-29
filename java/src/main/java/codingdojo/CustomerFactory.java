package codingdojo;

public class CustomerFactory {

    public static Customer from(ExternalCustomer externalCustomer) {
        if (externalCustomer == null) {
            return null;
        }
        if (externalCustomer.isCompany()) {
            Customer company = new Company();
            company.setExternalId(externalCustomer.getExternalId());
            company.setMasterExternalId(externalCustomer.getExternalId());
            return company;
        } else {
            Customer person = new Person();
            person.setExternalId(externalCustomer.getExternalId());
            person.setMasterExternalId(externalCustomer.getExternalId());
            return person;
        }
    }
}
