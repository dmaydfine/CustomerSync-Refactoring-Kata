package codingdojo;

import java.util.ArrayList;
import java.util.Collection;

public class CustomerMatches {
    private final Collection<Customer> duplicates = new ArrayList<>();
    private String matchTerm;
    private Customer customer;

    public Customer getCustomer() {
        return this.customer;
    }

    public boolean hasDuplicates() {
        return !this.duplicates.isEmpty();
    }

    public void addDuplicate(Customer duplicate) {
        this.duplicates.add(duplicate);
    }

    public Collection<Customer> getDuplicates() {
        return this.duplicates;
    }

    public String getMatchTerm() {
        return this.matchTerm;
    }

    public void setMatchTerm(String matchTerm) {
        this.matchTerm = matchTerm;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
