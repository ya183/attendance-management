package com.example.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.repository.RequestStatusMasterRepository;

@Service
public class RequestStatusMasterService {
	
	@Autowired
	private RequestStatusMasterRepository requestStatusMasterRepository;

}
