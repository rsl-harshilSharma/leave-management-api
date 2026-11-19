package com.rajasoftware.leavemanagement.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import com.rajasoftware.leavemanagement.DTO.LeaveRequestDto;
import com.rajasoftware.leavemanagement.Exception.LeaveNotFoundException;
import com.rajasoftware.leavemanagement.Model.LeaveRequest;
import com.rajasoftware.leavemanagement.Model.LeaveStatus;
import com.rajasoftware.leavemanagement.Model.LeaveType;
import com.rajasoftware.leavemanagement.Repository.LeaveRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeaveRequestServiceTest {

    @Mock
    private LeaveRequestRepository repository;

    @InjectMocks
    private LeaveRequestService service;

    @Test
    void createAssignsIdAndPendingStatus() {
        LeaveRequestDto request = request();
        when(repository.save(any(LeaveRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.create(request);

        assertNotNull(response.id());
        assertEquals(LeaveStatus.PENDING, response.status());
        assertEquals(request.employeeId(), response.employeeId());
    }

    @Test
    void updateReplacesRequestFieldsAndPreservesStatus() {
        LeaveRequest existing = new LeaveRequest(
                "leave-1", "employee-1", LeaveType.ANNUAL,
                LocalDate.parse("2026-12-01"), LocalDate.parse("2026-12-02"),
                "Vacation", LeaveStatus.APPROVED);
        when(repository.findById("leave-1")).thenReturn(Optional.of(existing));
        when(repository.save(any(LeaveRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.update("leave-1", request());

        assertEquals("leave-1", response.id());
        assertEquals("employee-2", response.employeeId());
        assertEquals(LeaveStatus.APPROVED, response.status());
    }

    @Test
    void getByIdThrowsWhenRequestDoesNotExist() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(LeaveNotFoundException.class, () -> service.getById("missing"));
    }

    @Test
    void deleteThrowsWhenRequestDoesNotExist() {
        when(repository.deleteById("missing")).thenReturn(false);

        assertThrows(LeaveNotFoundException.class, () -> service.delete("missing"));
        verify(repository).deleteById("missing");
    }

    private static LeaveRequestDto request() {
        return new LeaveRequestDto(
                "employee-2",
                LeaveType.SICK,
                LocalDate.parse("2026-12-10"),
                LocalDate.parse("2026-12-11"),
                "Medical appointment");
    }
}
