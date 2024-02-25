package codingdojo;

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
    }
}
