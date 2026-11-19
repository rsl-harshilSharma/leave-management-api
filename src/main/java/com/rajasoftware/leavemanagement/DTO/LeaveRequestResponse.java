package com.rajasoftware.leavemanagement.DTO;

import java.time.LocalDate;

import com.rajasoftware.leavemanagement.Model.LeaveStatus;
import com.rajasoftware.leavemanagement.Model.LeaveType;

public record LeaveRequestResponse(
        String id,
        String employeeId,
        LeaveType leaveType,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        LeaveStatus status) {
}
