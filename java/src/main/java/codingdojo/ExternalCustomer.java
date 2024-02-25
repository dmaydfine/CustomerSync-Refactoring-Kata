package codingdojo;

import java.util.List;

public class ExternalCustomer {
    private Address address;
    private String name;
    private String preferredStore;
    private List<ShoppingList> shoppingLists;
    private String externalId;
    private String companyNumber;
    private Integer bonusPointsBalance;

    public String getExternalId() {
        return this.externalId;
    }

    public String getCompanyNumber() {
        return this.companyNumber;
    }

    public boolean isCompany() {
        return this.companyNumber != null;
    }

    public Address getPostalAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreferredStore() {
        return this.preferredStore;
    }

    public void setPreferredStore(String preferredStore) {
        this.preferredStore = preferredStore;
    }

    public List<ShoppingList> getShoppingLists() {
        return this.shoppingLists;
    }

    public void setShoppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getBonusPointsBalance() {
        return this.bonusPointsBalance;
    }

    public void setBonusPointsBalance(Integer bonusPointsBalance) {
        this.bonusPointsBalance = bonusPointsBalance;
    }
}
