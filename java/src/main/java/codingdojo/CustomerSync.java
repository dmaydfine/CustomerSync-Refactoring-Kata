package codingdojo;

import java.util.List;

public class CustomerSync {

    private final CustomerDataAccess customerDataAccess;

    public CustomerSync(CustomerDataLayer customerDataLayer) {
        this(new CustomerDataAccess(customerDataLayer));
    }

    public CustomerSync(CustomerDataAccess db) {
        this.customerDataAccess = db;
    }

    public boolean syncWithDataLayer(ExternalCustomer externalCustomer) {

        CustomerMatches customerMatches;
        if (externalCustomer.isCompany()) {
            customerMatches = loadCompany(externalCustomer);
        } else {
            customerMatches = loadPerson(externalCustomer);
        }
        Customer customer = customerMatches.getCustomer();

        if (customer == null) {
            customer = CustomerFactory.from(externalCustomer);
        }

        populateFields(externalCustomer, customer);

        boolean created = false;
        if (customer.getInternalId() == null) {
            customer = createCustomer(customer);
            created = true;
        } else {
            updateCustomer(customer);
        }
        updateContactInfo(externalCustomer, customer);

        if (customerMatches.hasDuplicates()) {
            for (Customer duplicate : customerMatches.getDuplicates()) {
                updateDuplicate(externalCustomer, duplicate);
            }
        }

        updateRelations(externalCustomer, customer);
        updatePreferredStore(externalCustomer, customer);

        return created;
    }

    private void updateRelations(ExternalCustomer externalCustomer, Customer customer) {
        List<ShoppingList> consumerShoppingLists = externalCustomer.getShoppingLists();
        for (ShoppingList consumerShoppingList : consumerShoppingLists) {
            this.customerDataAccess.updateShoppingList(customer, consumerShoppingList);
        }
    }

    private Customer updateCustomer(Customer customer) {
        return this.customerDataAccess.updateCustomerRecord(customer);
    }

    private void updateDuplicate(ExternalCustomer externalCustomer, Customer duplicate) {
        if (duplicate == null) {
            duplicate = CustomerFactory.from(externalCustomer);
        }

        duplicate.setName(externalCustomer.getName());

        if (duplicate.getInternalId() == null) {
            createCustomer(duplicate);
        } else {
            updateCustomer(duplicate);
        }
    }

    private void updatePreferredStore(ExternalCustomer externalCustomer, Customer customer) {
        customer.setPreferredStore(externalCustomer.getPreferredStore());
    }

    private Customer createCustomer(Customer customer) {
        return this.customerDataAccess.createCustomerRecord(customer);
    }

    private void populateFields(ExternalCustomer externalCustomer, Customer customer) {
        customer.setFieldsFromExternalDto(externalCustomer);
    }

    private void updateContactInfo(ExternalCustomer externalCustomer, Customer customer) {
        customer.setAddress(externalCustomer.getPostalAddress());
    }

    public CustomerMatches loadCompany(ExternalCustomer externalCustomer) {

        final String externalId = externalCustomer.getExternalId();
        final String externalCompanyNumber = externalCustomer.getCompanyNumber();

        CustomerMatches matches = this.customerDataAccess.loadCompanyCustomer(externalId, externalCompanyNumber);

        if (matches.getCustomer() != null && !(matches.getCustomer() instanceof Company)) {
            throw new ConflictException("Existing customer for externalCustomer " + externalId + " already exists and is not a company");
        }

        Company company = (Company) matches.getCustomer();
        if ("ExternalId".equals(matches.getMatchTerm())) {
            String companyNumberFromDb = company.getCompanyNumber();
            if (!externalCompanyNumber.equals(companyNumberFromDb)) {
                // We save the existing entry as a duplicate and create a new one when the companyNumber changes.
                company.setMasterExternalId(null);
                matches.addDuplicate(company);
                matches.setCustomer(null);
                matches.setMatchTerm(null);
            }
        } else if ("CompanyNumber".equals(matches.getMatchTerm())) {
            String externalIdFromDb = company.getExternalId();
            if (externalIdFromDb != null && !externalId.equals(externalIdFromDb)) {
                throw new ConflictException("Existing customer for externalCustomer " + externalCompanyNumber + " doesn't match external id " + externalId + " instead found " + externalIdFromDb );
            }
            company.setExternalId(externalId);
            company.setMasterExternalId(externalId);
            matches.addDuplicate(null);
        }

        return matches;
    }

    public CustomerMatches loadPerson(ExternalCustomer externalCustomer) {
        final String externalId = externalCustomer.getExternalId();

        CustomerMatches matches = this.customerDataAccess.loadPersonCustomer(externalId);

        if (matches.getCustomer() != null) {
            if (!(matches.getCustomer() instanceof Person)) {
                throw new ConflictException("Existing customer for externalCustomer " + externalId + " already exists and is not a person");
            }

            if (!"ExternalId".equals(matches.getMatchTerm())) {
                Customer customer = matches.getCustomer();
                customer.setExternalId(externalId);
                customer.setMasterExternalId(externalId);
            }
        }

        return matches;
    }
}
