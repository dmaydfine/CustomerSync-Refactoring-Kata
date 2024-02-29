package codingdojo;

import java.util.Collection;

public class CustomerDataAccess {

    private final CustomerDataLayer customerDataLayer;

    public CustomerDataAccess(CustomerDataLayer customerDataLayer) {
        this.customerDataLayer = customerDataLayer;
    }

    public CustomerMatches loadCompany(String externalId, String companyNumber) {
        CustomerMatches matches = new CustomerMatches();
        Customer matchByExternalId = this.customerDataLayer.findByExternalId(externalId);
        if (matchByExternalId != null) {
            matches.setCustomer(matchByExternalId);
            matches.setMatchTerm("ExternalId");
            Customer matchByMasterId = this.customerDataLayer.findByMasterExternalId(externalId);
            if (matchByMasterId != null) matches.addDuplicate(matchByMasterId);
        } else {
            Customer matchByCompanyNumber = this.customerDataLayer.findByCompanyNumber(companyNumber);
            if (matchByCompanyNumber != null) {
                matches.setCustomer(matchByCompanyNumber);
                matches.setMatchTerm("CompanyNumber");
            }
        }

        return matches;
    }

    public CustomerMatches loadPerson(String externalId) {
        CustomerMatches matches = new CustomerMatches();
        Customer matchByPersonalNumber = this.customerDataLayer.findByExternalId(externalId);
        matches.setCustomer(matchByPersonalNumber);
        if (matchByPersonalNumber != null) matches.setMatchTerm("ExternalId");
        return matches;
    }

    public boolean createOrUpdate(Customer customer) {
        if (customer.getInternalId() == null) {
            createCustomerAndRelations(customer);
            return true;
        }
        updateCustomerAndRelations(customer);
        return false;
    }
    public void createOrUpdateDuplicates(Collection<Customer> duplicates) {
        for (Customer duplicate : duplicates) {
            if (duplicate.getInternalId() == null) {
                createCustomerAndRelations(duplicate);
            } else {
                updateCustomerAndRelations(duplicate);
            }
        }
    }

    private void updateShoppingList(ShoppingList consumerShoppingList) {
        this.customerDataLayer.updateShoppingList(consumerShoppingList);
    }

    private Customer createCustomerAndRelations(Customer customer) {
        for (var shoppingList : customer.getShoppingLists()) {
            this.updateShoppingList(shoppingList);
        }
        return this.createCustomerRecord(customer);
    }

    private Customer updateCustomerAndRelations(Customer customer) {
        for (var shoppingList : customer.getShoppingLists()) {
            this.updateShoppingList(shoppingList);
        }
        return this.updateCustomerRecord(customer);
    }

    private Customer createCustomerRecord(Customer customer) {
        return this.customerDataLayer.createCustomerRecord(customer);
    }

    private Customer updateCustomerRecord(Customer customer) {
        return this.customerDataLayer.updateCustomerRecord(customer);
    }
}
