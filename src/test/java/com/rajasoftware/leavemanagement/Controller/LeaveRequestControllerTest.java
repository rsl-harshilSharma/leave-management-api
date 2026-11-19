package com.rajasoftware.leavemanagement.Controller;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import com.rajasoftware.leavemanagement.DTO.LeaveRequestResponse;
import com.rajasoftware.leavemanagement.Exception.GlobalExceptionHandler;
import com.rajasoftware.leavemanagement.Exception.LeaveNotFoundException;
import com.rajasoftware.leavemanagement.Model.LeaveStatus;
import com.rajasoftware.leavemanagement.Model.LeaveType;
import com.rajasoftware.leavemanagement.Service.LeaveRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LeaveRequestController.class)
@Import({GlobalExceptionHandler.class, ControllerTestConfiguration.class})
class LeaveRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaveRequestService service;

    @Test
    void createReturnsCreatedResourceAndLocation() throws Exception {
        when(service.create(org.mockito.ArgumentMatchers.any())).thenReturn(response("leave-1"));

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "employeeId": "employee-1",
                                  "leaveType": "ANNUAL",
                                  "startDate": "2026-12-10",
                                  "endDate": "2026-12-12",
                                  "reason": "Family trip"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/leaves/leave-1"))
                .andExpect(jsonPath("$.id").value("leave-1"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void invalidDateRangeReturnsBadRequestWithoutCallingService() throws Exception {
        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "employeeId": "employee-1",
                                  "leaveType": "ANNUAL",
                                  "startDate": "2026-12-12",
                                  "endDate": "2026-12-10",
                                  "reason": "Family trip"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.dateRangeValid")
                        .value("startDate must be on or before endDate"));
        verifyNoInteractions(service);
    }

    @Test
    void getByIdReturnsNotFoundForUnknownRequest() throws Exception {
        when(service.getById("missing")).thenThrow(new LeaveNotFoundException("missing"));

        mockMvc.perform(get("/leaves/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Leave request with id 'missing' was not found"));
    }

    @Test
    void getAllReturnsLeaveRequests() throws Exception {
        when(service.getAll()).thenReturn(List.of(response("leave-1")));

        mockMvc.perform(get("/leaves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("leave-1"));
    }

    @Test
    void updateReturnsUpdatedLeaveRequest() throws Exception {
        when(service.update(org.mockito.ArgumentMatchers.eq("leave-1"), org.mockito.ArgumentMatchers.any()))
                .thenReturn(response("leave-1"));

        mockMvc.perform(put("/leaves/leave-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "employeeId": "employee-1",
                                  "leaveType": "ANNUAL",
                                  "startDate": "2026-12-10",
                                  "endDate": "2026-12-12",
                                  "reason": "Family trip"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("leave-1"));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/leaves/leave-1"))
                .andExpect(status().isNoContent());
    }

    private static LeaveRequestResponse response(String id) {
        return new LeaveRequestResponse(
                id,
                "employee-1",
                LeaveType.ANNUAL,
                LocalDate.parse("2026-12-10"),
                LocalDate.parse("2026-12-12"),
                "Family trip",
                LeaveStatus.PENDING);
    }
}

@TestConfiguration
class ControllerTestConfiguration {

    @org.springframework.context.annotation.Bean
    Clock applicationClock() {
        return Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
    }
}
