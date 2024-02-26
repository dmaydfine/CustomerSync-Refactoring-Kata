package codingdojo;

import java.util.List;

public class Company extends Customer {
    @Override
    public void setFieldsFromExternalDto(ExternalCustomer externalCustomer) {
        this.setName(externalCustomer.getName());
        this.setCompanyNumber(externalCustomer.getCompanyNumber());
        this.setAddress(externalCustomer.getPostalAddress());
        this.setPreferredStore(externalCustomer.getPreferredStore());
        List<ShoppingList> consumerShoppingLists = externalCustomer.getShoppingLists();
        for (ShoppingList consumerShoppingList : consumerShoppingLists) {
            this.addShoppingList(consumerShoppingList);
        }
    }
}
