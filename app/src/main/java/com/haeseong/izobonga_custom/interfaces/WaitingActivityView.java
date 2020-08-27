package com.haeseong.izobonga_custom.interfaces;

import java.util.ArrayList;

public interface WaitingActivityView {
    void validateSuccess(String message, int ticket, String phoneNumber);

    void validateFailure(String message);

    void modified(ArrayList<Integer> ticketList, int table4, int table6);

    void speak(String ticket);
}
