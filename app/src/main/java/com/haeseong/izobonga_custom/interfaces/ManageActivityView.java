package com.haeseong.izobonga_custom.interfaces;

import com.haeseong.izobonga_custom.models.Customer;

import java.util.ArrayList;

public interface ManageActivityView {
    void validateSuccess(ArrayList<Customer> customers);
    void validateSuccessDelete(String message, int position);
    void validateFailure(String message);
}
