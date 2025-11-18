package com.example.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.entity.Change_request;
import com.example.attendance.repository.ChangeRequestRepository;

@Service
public class ChangeRequestService {
	
	@Autowired
    private ChangeRequestRepository changeRequestRepository;

//    public List<Change_request> getRequestsByUser(int userId) {
//        return changeRequestRepository.findByIdUserIdOrderByApplication_dateDesc(userId);
//    }

}
