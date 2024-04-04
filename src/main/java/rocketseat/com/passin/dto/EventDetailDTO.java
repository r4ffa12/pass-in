package rocketseat.com.passin.dto;

public record EventDetailDTO
        (String id,
         String title,
         String details,
         String slug,
         Integer maximumAttendees,
         Integer attendeesAmount ) {
}

