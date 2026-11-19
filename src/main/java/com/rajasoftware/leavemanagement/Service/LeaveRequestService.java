package com.rajasoftware.leavemanagement.Service;

import java.util.List;
import java.util.UUID;

import com.rajasoftware.leavemanagement.DTO.LeaveRequestDto;
import com.rajasoftware.leavemanagement.DTO.LeaveRequestResponse;
import com.rajasoftware.leavemanagement.Exception.LeaveNotFoundException;
import com.rajasoftware.leavemanagement.Model.LeaveRequest;
import com.rajasoftware.leavemanagement.Model.LeaveStatus;
import com.rajasoftware.leavemanagement.Repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository repository;

    public LeaveRequestService(LeaveRequestRepository repository) {
        this.repository = repository;
    }

    public List<LeaveRequestResponse> getAll() {
        return repository.findAll().stream()
                .map(LeaveRequestService::toResponse)
                .toList();
    }

    public LeaveRequestResponse getById(String id) {
        return repository.findById(id)
                .map(LeaveRequestService::toResponse)
                .orElseThrow(() -> new LeaveNotFoundException(id));
    }

    public LeaveRequestResponse create(LeaveRequestDto request) {
        LeaveRequest leaveRequest = new LeaveRequest(
                UUID.randomUUID().toString(),
                request.employeeId(),
                request.leaveType(),
                request.startDate(),
                request.endDate(),
                request.reason(),
                LeaveStatus.PENDING);
        return toResponse(repository.save(leaveRequest));
    }

    public LeaveRequestResponse update(String id, LeaveRequestDto request) {
        LeaveRequest existing = repository.findById(id)
                .orElseThrow(() -> new LeaveNotFoundException(id));
        LeaveRequest updated = new LeaveRequest(
                existing.id(),
                request.employeeId(),
                request.leaveType(),
                request.startDate(),
                request.endDate(),
                request.reason(),
                existing.status());
        return toResponse(repository.save(updated));
    }

    public void delete(String id) {
        if (!repository.deleteById(id)) {
            throw new LeaveNotFoundException(id);
        }
    }

    private static LeaveRequestResponse toResponse(LeaveRequest leaveRequest) {
        return new LeaveRequestResponse(
                leaveRequest.id(),
                leaveRequest.employeeId(),
                leaveRequest.leaveType(),
                leaveRequest.startDate(),
                leaveRequest.endDate(),
                leaveRequest.reason(),
                leaveRequest.status());
    }
}
