package org.example.opendesk.service;

import lombok.RequiredArgsConstructor;
import org.example.opendesk.dto.BookingCreateRequest;
import org.example.opendesk.dto.BookingResponse;
import org.example.opendesk.exception.BookingConflictException;
import org.example.opendesk.exception.ResourceNotFoundException;
import org.example.opendesk.model.Booking;
import org.example.opendesk.model.BookingStatus;
import org.example.opendesk.model.Room;
import org.example.opendesk.model.User;
import org.example.opendesk.repository.BookingRepository;
import org.example.opendesk.repository.RoomRepository;
import org.example.opendesk.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new IllegalArgumentException("End time must be strictly after start time");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.roomId()));

        boolean isOverlapping = bookingRepository.existsOverlappingBooking(
                room.getId(),
                request.startTime(),
                request.endTime(),
                BookingStatus.CONFIRMED
        );

        if (isOverlapping) {
            throw new BookingConflictException("Room is already booked for this time period");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStartTime(request.startTime());
        booking.setEndTime(request.endTime());
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);
        return BookingResponse.fromEntity(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByRoom(Long roomId) {
        return bookingRepository.findAllByRoomId(roomId)
                .stream()
                .map(BookingResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        booking.setStatus(BookingStatus.CANCELLED);
    }
}