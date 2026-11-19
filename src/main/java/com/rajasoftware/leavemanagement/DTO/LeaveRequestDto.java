package com.rajasoftware.leavemanagement.DTO;

import java.time.LocalDate;

import com.rajasoftware.leavemanagement.Model.LeaveType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LeaveRequestDto(
        @NotBlank @Size(max = 50) String employeeId,
        @NotNull LeaveType leaveType,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotBlank @Size(max = 1000) String reason) {

    @AssertTrue(message = "startDate must be on or before endDate")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }
}
