package com.example.main.Dashboard.Service;

import com.example.main.Exception.ResponseClass;
import com.example.main.GuestManagement.Model.Guest;
import com.example.main.GuestManagement.Repository.GuestRepo;
import com.example.main.Hotel.Entity.Booking;
import com.example.main.Hotel.Repo.BookingRepo;
import com.example.main.Payment.Repo.PaymentRepo;
import com.example.main.Payment.entity.PaymentClass;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminDashboardService {

    private final GuestRepo guestRepo;
    private final BookingRepo bookingRepo;
    private final PaymentRepo paymentRepo;

    public ResponseEntity<?> getDashboardMetrics(String hotelId) {
        try {
            long totalPageViews = 442236;

            List<Guest> guests = guestRepo.findAllByHotelId(hotelId);
            if (guests == null) {
                return ResponseClass.responseFailure("No guests found for hotel ID: " + hotelId);
            }
            long totalGuests = guests.size();

            List<Booking> bookings = bookingRepo.findByHotelId(hotelId);
            if (bookings == null) {
                return ResponseClass.responseFailure("No bookings found for hotel ID: " + hotelId);
            }
            long totalBookings = bookings.size();

            List<PaymentClass> payments = paymentRepo.findAllByHotelId(hotelId);
            if (payments == null) {
                return ResponseClass.responseFailure("No payments found for hotel ID: " + hotelId);
            }
            double totalSales = payments.stream()
                    .mapToDouble(PaymentClass::getTotalPaid)
                    .sum();

            Map<String, Object> metrics = new HashMap<>();
            metrics.put("totalPageViews", totalPageViews);
            metrics.put("totalGuests", totalGuests);
            metrics.put("totalBookings", totalBookings);
            metrics.put("totalSales", totalSales);

            return ResponseClass.responseSuccess("Dashboard metrics retrieved successfully", "metrics", metrics);
        } catch (Exception e) {
            return ResponseClass.responseFailure("Error retrieving dashboard metrics: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getBookingReport(String hotelId, String startDate, String endDate) {
        try {
            LocalDate start;
            LocalDate end;

            if (startDate == null || endDate == null) {
                start = LocalDate.of(2024, 9, 1);
                end = LocalDate.of(2024, 12, 31);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    start = LocalDate.parse(startDate, formatter);
                    end = LocalDate.parse(endDate, formatter);
                } catch (Exception e) {
                    return ResponseClass.responseFailure("Invalid date format. Use yyyy-MM-dd.");
                }
            }

            if (end.isBefore(start)) {
                return ResponseClass.responseFailure("End date cannot be before start date.");
            }

            List<Booking> bookings = bookingRepo.findByCheckInDateBetweenAndHotelId(

                    start.atStartOfDay(),
                    end.atTime(23, 59, 59),hotelId
            );



            Map<String, Double> bookingReport = new LinkedHashMap<>();
            String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

            for (String day : daysOfWeek) {
                bookingReport.put(day, 0.0);
            }

            for (Booking booking : bookings) {
                LocalDate checkInDate = booking.getCheckInDate().toLocalDate();
                if (!checkInDate.isBefore(start) && !checkInDate.isAfter(end)) {
                    String dayOfWeek = daysOfWeek[checkInDate.getDayOfWeek().getValue() - 1];
                    double revenue = booking.getTotalAmount();
                    bookingReport.put(dayOfWeek, bookingReport.get(dayOfWeek) + revenue);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("labels", Arrays.asList(daysOfWeek));
            response.put("data", bookingReport.values());
            response.put("startDate", start.toString());
            response.put("endDate", end.toString());

            return ResponseClass.responseSuccess("Booking report retrieved successfully", "bookingReport", response);
        } catch (Exception e) {
            return ResponseClass.responseFailure("Error retrieving booking report: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getPaymentReport(String hotelId, Integer startYear, Integer endYear) {
        try {
            int start = (startYear != null) ? startYear : 2016;
            int end = (endYear != null) ? endYear : 2021;

            if (end < start) {
                return ResponseClass.responseFailure("End year cannot be less than start year.");
            }

            List<PaymentClass> payments = paymentRepo.findAllByHotelId(hotelId);
            if (payments == null) {
                return ResponseClass.responseFailure("No payments found for hotel ID: " + hotelId);
            }

            Map<Integer, Double> paymentReport = new TreeMap<>();
            List<Integer> years = new ArrayList<>();

            for (int year = start; year <= end; year++) {
                paymentReport.put(year, 0.0);
                years.add(year);
            }

            for (PaymentClass payment : payments) {
                int paymentYear = payment.getPaymentDate().getYear();
                if (paymentYear >= start && paymentYear <= end) {
                    double revenue = payment.getTotalPaid();
                    paymentReport.put(paymentYear, paymentReport.get(paymentYear) + revenue);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("labels", years);
            response.put("data", paymentReport.values());
            response.put("startYear", start);
            response.put("endYear", end);

            return ResponseClass.responseSuccess("Payment report retrieved successfully", "paymentReport", response);
        } catch (Exception e) {
            return ResponseClass.responseFailure("Error retrieving payment report: " + e.getMessage());
        }
    }
}