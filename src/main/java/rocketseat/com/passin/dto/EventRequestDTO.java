package rocketseat.com.passin.dto;

public record EventRequestDTO(
        String title,
        String details,
        Integer maximumAttendees ) {
}
