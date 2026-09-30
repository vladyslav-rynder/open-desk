package org.example.opendesk.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record BookingCreateRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Room ID is required")
        Long roomId,

        @NotNull(message = "Start time is required")
        @Future(message = "Start time must be in the future")
        LocalDateTime startTime,

        @NotNull(message = "End time is required")
        @Future(message = "End time must be in the future")
        LocalDateTime endTime
) {}