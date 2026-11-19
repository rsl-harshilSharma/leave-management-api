package com.rajasoftware.leavemanagement.Exception;

public class LeaveNotFoundException extends RuntimeException {

    public LeaveNotFoundException(String id) {
        super("Leave request with id '" + id + "' was not found");
    }
}
