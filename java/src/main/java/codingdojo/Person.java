package codingdojo;

import java.util.List;

public class Person extends Customer {
    private Integer bonusPointsBalance;

    public void setBonusPointsBalance(Integer bonusPointsBalance) {
        this.bonusPointsBalance = bonusPointsBalance;
    }

    public Integer getBonusPointsBalance() {
        return this.bonusPointsBalance;
    }

    @Override
    public void setFieldsFromExternalDto(ExternalCustomer externalCustomer) {
        this.setName(externalCustomer.getName());
        this.setBonusPointsBalance(externalCustomer.getBonusPointsBalance());
        this.setAddress(externalCustomer.getPostalAddress());
        this.setPreferredStore(externalCustomer.getPreferredStore());
        List<ShoppingList> consumerShoppingLists = externalCustomer.getShoppingLists();
        for (ShoppingList consumerShoppingList : consumerShoppingLists) {
            this.addShoppingList(consumerShoppingList);
        }
    }
}
