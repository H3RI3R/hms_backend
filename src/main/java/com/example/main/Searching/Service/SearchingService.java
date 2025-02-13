package com.example.main.Searching.Service;



import com.example.main.Exception.ResponseClass;
import com.example.main.StaffManagement.Model.Role;
import com.example.main.StaffManagement.Model.Staff;
import com.example.main.StaffManagement.Repository.StaffRepo;
import com.example.main.Support.DateTimeWeekDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchingService {
        @Autowired
        private StaffRepo staffRepo;

        public ResponseEntity<?> searchStaffByEmail(String hotelId, String email) {
            List<Staff> staffs = staffRepo.findByHotelIdAndEmail(hotelId, email);

            if (staffs.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found with email: " + email);
            }

            List<StaffDTORecord> staffs1 = staffs.stream().map(staff -> new StaffDTORecord(
                    staff.getId(),
                    staff.getHotelId(),
                    staff.getName(),
                    staff.getUserName(),
                    staff.getEmail(),
                    staff.getRole(),
                    (staff.getStatus() == null ? "ENABLED" : staff.getStatus()),
                    new DateTimeWeekDay(staff.getCreatedAt() == null ? Instant.now() : staff.getCreatedAt()))).toList();

            return ResponseClass.responseSuccess("Staff found", "staffs", staffs1);
        }
    record StaffDTORecord(
            Long id,
            String hotelId,
            String name,
            String userName,
            String email,
            Role role,
            String status,
            DateTimeWeekDay createdAt
    ){}
}
