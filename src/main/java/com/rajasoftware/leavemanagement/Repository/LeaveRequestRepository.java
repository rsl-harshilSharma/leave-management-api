package com.rajasoftware.leavemanagement.Repository;

import java.util.List;
import java.util.Optional;

import com.rajasoftware.leavemanagement.Model.LeaveRequest;

public interface LeaveRequestRepository {

    List<LeaveRequest> findAll();

    Optional<LeaveRequest> findById(String id);

    LeaveRequest save(LeaveRequest leaveRequest);

    boolean deleteById(String id);
}
