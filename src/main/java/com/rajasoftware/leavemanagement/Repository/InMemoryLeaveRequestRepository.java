package com.rajasoftware.leavemanagement.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.rajasoftware.leavemanagement.Model.LeaveRequest;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLeaveRequestRepository implements LeaveRequestRepository {

    private final ConcurrentMap<String, LeaveRequest> leaveRequests = new ConcurrentHashMap<>();

    @Override
    public List<LeaveRequest> findAll() {
        return leaveRequests.values().stream()
                .sorted(Comparator.comparing(LeaveRequest::id))
                .toList();
    }

    @Override
    public Optional<LeaveRequest> findById(String id) {
        return Optional.ofNullable(leaveRequests.get(id));
    }

    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
        leaveRequests.put(leaveRequest.id(), leaveRequest);
        return leaveRequest;
    }

    @Override
    public boolean deleteById(String id) {
        return leaveRequests.remove(id) != null;
    }
}
