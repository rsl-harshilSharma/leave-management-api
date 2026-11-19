package com.rajasoftware.leavemanagement.Model;

import java.time.LocalDate;

public record LeaveRequest(
        String id,
        String employeeId,
        LeaveType leaveType,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        LeaveStatus status) {
}
