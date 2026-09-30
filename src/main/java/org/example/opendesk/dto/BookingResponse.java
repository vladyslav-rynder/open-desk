package org.example.opendesk.dto;

import org.example.opendesk.model.Booking;
import org.example.opendesk.model.BookingStatus;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long userId,
        String userEmail,
        Long roomId,
        String roomName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BookingStatus status
) {
    public static BookingResponse fromEntity(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getUser().getEmail(),
                booking.getRoom().getId(),
                booking.getRoom().getName(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus()
        );
    }
}