package codingdojo;

import java.util.Collection;

public class CustomerSync {

    private final CustomerDataAccess customerDataAccess;
    private final CustomerMatchesFactory customerMatchesFactory;

    public CustomerSync(CustomerDataLayer customerDataLayer, CustomerMatchesFactory customerMatchesFactory) {
        this.customerDataAccess = new CustomerDataAccess(customerDataLayer);
        this.customerMatchesFactory = customerMatchesFactory;
    }

    public boolean syncWithDataLayer(ExternalCustomer externalCustomer) throws ConflictException {
        var customerMatches = this.customerMatchesFactory.from(externalCustomer, this.customerDataAccess);

        // We need to create a new customer object in case we could not find one in the database.
        var customer = customerMatches.getCustomer();
        if (customer == null) {
            customer = CustomerFactory.from(externalCustomer);
        }

        this.populateFieldsAndDuplicates(externalCustomer, customer, customerMatches.getDuplicates());

        var created = this.customerDataAccess.createOrUpdate(customer);
        this.customerDataAccess.createOrUpdateDuplicates(customerMatches.getDuplicates());

        return created;
    }

    private void updateDuplicate(ExternalCustomer externalCustomer, Customer duplicate) {
        if (duplicate == null) {
            duplicate = CustomerFactory.from(externalCustomer);
        }

        duplicate.setName(externalCustomer.getName());
    }

    private void populateFieldsAndDuplicates(ExternalCustomer externalCustomer, Customer customer, Collection<Customer> duplicates) {
        customer.setFieldsFromExternalDto(externalCustomer);

        for (Customer duplicate : duplicates) {
            updateDuplicate(externalCustomer, duplicate);
        }
    }
}
