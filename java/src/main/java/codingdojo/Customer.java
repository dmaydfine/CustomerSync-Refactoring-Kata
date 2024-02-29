package codingdojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract public class Customer {
    private String externalId;
    private String masterExternalId;
    private Address address;
    private String preferredStore;
    private List<ShoppingList> shoppingLists = new ArrayList<>();
    private String internalId;
    private String name;
    private String companyNumber;


    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setMasterExternalId(String masterExternalId) {
        this.masterExternalId = masterExternalId;
    }

    public String getMasterExternalId() {
        return this.masterExternalId;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return this.address;
    }

    public String getInternalId() {
        return this.internalId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPreferredStore(String preferredStore) {
        this.preferredStore = preferredStore;
    }

    public String getPreferredStore() {
        return this.preferredStore;
    }

    public List<ShoppingList> getShoppingLists() {
        return this.shoppingLists;
    }

    public void setShoppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    public String getName() {
        return this.name;
    }

    public String getCompanyNumber() {
        return this.companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public void addShoppingList(ShoppingList consumerShoppingList) {
        ArrayList<ShoppingList> newList = new ArrayList<>(this.shoppingLists);
        newList.add(consumerShoppingList);
        this.setShoppingLists(newList);
    }

    abstract public void setFieldsFromExternalDto(ExternalCustomer externalCustomer);


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(this.externalId, customer.externalId) &&
                Objects.equals(this.masterExternalId, customer.masterExternalId) &&
                Objects.equals(this.companyNumber, customer.companyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.externalId, this.masterExternalId, this.companyNumber);
    }
}
