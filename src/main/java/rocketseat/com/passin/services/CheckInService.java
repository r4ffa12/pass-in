package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.checkin.CheckIn;
import rocketseat.com.passin.domain.checkin.exception.CheckInAlreadyExistsException;
import rocketseat.com.passin.repositories.CheckInRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CheckInService {
    private final CheckInRepository checkInRepository;

    public void registerCheckIn(Attendee attendee){
        this.verifyCheckInExists(attendee.getId());
        CheckIn newcheckIn = new CheckIn();
        newcheckIn.setAttendee(attendee);
        newcheckIn.setCreatedAt(LocalDateTime.now());
        this.checkInRepository.save(newcheckIn);
    }
    private void verifyCheckInExists(String attendeeId){
        Optional<CheckIn> checkIn = this.getCheckIn(attendeeId);
        if(checkIn.isPresent()) throw new CheckInAlreadyExistsException("Attendee already checked in");

    }

    public Optional<CheckIn> getCheckIn(String attendeeId) {
        return this.checkInRepository.findByAttendeeId(attendeeId);
    }

}
