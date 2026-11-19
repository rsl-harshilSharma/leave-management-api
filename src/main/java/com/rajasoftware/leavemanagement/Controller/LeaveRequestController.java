package com.rajasoftware.leavemanagement.Controller;

import java.net.URI;
import java.util.List;

import com.rajasoftware.leavemanagement.DTO.LeaveRequestDto;
import com.rajasoftware.leavemanagement.DTO.LeaveRequestResponse;
import com.rajasoftware.leavemanagement.Service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leaves")
public class LeaveRequestController {

    private final LeaveRequestService service;

    public LeaveRequestController(LeaveRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<LeaveRequestResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public LeaveRequestResponse getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<LeaveRequestResponse> create(@Valid @RequestBody LeaveRequestDto request) {
        LeaveRequestResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/leaves/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public LeaveRequestResponse update(
            @PathVariable String id,
            @Valid @RequestBody LeaveRequestDto request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
