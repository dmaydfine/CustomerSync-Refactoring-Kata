package codingdojo;

public class Company extends Customer {
    @Override
    public void setFieldsFromExternalDto(ExternalCustomer externalCustomer) {
        this.setName(externalCustomer.getName());
        this.setCompanyNumber(externalCustomer.getCompanyNumber());
    }
}
