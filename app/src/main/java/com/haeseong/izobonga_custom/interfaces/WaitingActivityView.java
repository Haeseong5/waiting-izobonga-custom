package com.haeseong.izobonga_custom.interfaces;

public interface WaitingActivityView {
    void validateSuccess(String message, int ticket);

    void validateFailure(String message);

    void modified(long size, int table4, int table6);

    void speak(String ticket);

}
