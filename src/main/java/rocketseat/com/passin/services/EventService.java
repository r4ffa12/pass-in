package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.event.Event;
import rocketseat.com.passin.domain.event.exceptions.EventFullException;
import rocketseat.com.passin.domain.event.exceptions.EventNotFoundException;
import rocketseat.com.passin.dto.EventIdDTO;
import rocketseat.com.passin.dto.EventRequestDTO;
import rocketseat.com.passin.dto.EventResponseDTO;
import rocketseat.com.passin.dto.attendee.AttendeeIdDTO;
import rocketseat.com.passin.dto.attendee.AttendeeRequestDTO;
import rocketseat.com.passin.repositories.EventRepository;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId){
        Event event = this.getEventByID(eventId);
        List<Attendee> attendeesList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeesList.size());
    }
    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        Event newEvent = new Event();

        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.creatSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());

    }


    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(),eventId);

        Event event = this.getEventByID(eventId);
        List<Attendee> attendeesList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendeesList.size()) throw new EventFullException("Event is full");{
        }
        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }

    private Event getEventByID(String eventId){
      return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not fount with ID:" + eventId));
    }


    private String creatSlug(String text){
        String normalizer = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalizer.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]" ,"")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }
}
