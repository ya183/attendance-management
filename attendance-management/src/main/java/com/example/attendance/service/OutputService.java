package com.example.attendance.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.repository.AttendanceInformationRepository;
import com.example.attendance.repository.OutputRepository;
import com.ezample.attendance.dto.OutputSearchDto;

@Service
public class OutputService {
	@Autowired
	private OutputRepository repository;

	public List<OutputSearchDto> search(String yearMonth, String username) {
		
		YearMonth ym = YearMonth.parse(yearMonth);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        
		return repository.outputSearch(start, end, username);

	}
}
