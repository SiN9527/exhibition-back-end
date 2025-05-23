package com.exhibition.wrapper;

import com.exhibition.dto.venue.VenueDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchNotificationWrapper {

    private MatchDto matchDto;

    private List<UserAccountDto> usersAccounts;

    private String organizerId;

    private String organizerName;

    private VenueDto venueDto;

    private String sportType;

    private LocalDate date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String note;

    private String organizerLine;

    private String courtDesc;

    private Integer courtAmount;

    private Integer fee;

}
