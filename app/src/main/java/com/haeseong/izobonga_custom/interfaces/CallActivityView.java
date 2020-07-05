package com.haeseong.izobonga_custom.interfaces;

import com.haeseong.izobonga_custom.models.Customer;

import java.util.ArrayList;

public interface CallActivityView {
    void initCustomers(ArrayList<Customer> customers);
    void added(Customer customer);
    void called(int position);
    void deleted(int position);

    void validateSuccessSMS(String message);
    void validateFailureSMS(String message);
    void validateSuccessResetTicket(String message);

}
