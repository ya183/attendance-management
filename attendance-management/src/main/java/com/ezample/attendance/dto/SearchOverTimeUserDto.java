package com.ezample.attendance.dto;

import java.math.BigDecimal;


public interface SearchOverTimeUserDto {
    Integer getUserId();
    String getUserName();
    BigDecimal getTotalOvertime();
}