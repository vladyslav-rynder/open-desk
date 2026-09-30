package org.example.opendesk.repository;

import org.example.opendesk.model.Booking;
import org.example.opendesk.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserId(Long userId);

    List<Booking> findAllByRoomId(Long roomId);

    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.room.id = :roomId
          AND b.status = :status
          AND b.startTime < :endTime
          AND b.endTime > :startTime
    """)
    boolean existsOverlappingBooking(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") BookingStatus status
    );
}