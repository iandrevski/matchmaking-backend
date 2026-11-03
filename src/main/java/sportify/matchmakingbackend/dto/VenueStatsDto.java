package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.Venue;
import sportify.matchmakingbackend.model.enums.MatchStatus;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// статистика за еден терен, само за неговиот сопственик
public record VenueStatsDto(
        int totalMatches,
        int upcomingMatches,
        int finishedMatches,
        int cancelledMatches,
        int totalPlayers,
        int uniquePlayers,
        int fillRatePercent,
        double bookedHours,
        Integer bookedValue,
        List<CountDto> bySport,
        List<CountDto> byHour,
        List<CountDto> byWeekday
) {

    public record CountDto(String label, long count) {
    }

    public static VenueStatsDto from(Venue venue, List<Match> matches) {
        // откажаните не се бројат во ништо освен cancelledMatches
        List<Match> active = matches.stream()
                .filter(m -> m.getStatus() != MatchStatus.CANCELLED)
                .toList();
        LocalDateTime now = LocalDateTime.now();

        int totalPlayers = active.stream().mapToInt(m -> m.getParticipants().size()).sum();
        int capacity = active.stream().mapToInt(m -> m.getSport().getNumberOfPlayers()).sum();
        long minutes = active.stream()
                .mapToLong(m -> Duration.between(m.getDateFrom(), m.getDateTo()).toMinutes())
                .sum();
        double hours = minutes / 60.0;

        List<CountDto> bySport = active.stream()
                .collect(Collectors.groupingBy(m -> m.getSport().getName(), Collectors.counting()))
                .entrySet().stream()
                .map(e -> new CountDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(CountDto::count).reversed())
                .toList();

        Map<Integer, Long> hourCounts = active.stream()
                .collect(Collectors.groupingBy(m -> m.getDateFrom().getHour(), Collectors.counting()));
        List<CountDto> byHour = IntStream.rangeClosed(8, 22)
                .mapToObj(h -> new CountDto(String.format("%02d", h), hourCounts.getOrDefault(h, 0L)))
                .toList();

        Map<DayOfWeek, Long> dayCounts = active.stream()
                .collect(Collectors.groupingBy(m -> m.getDateFrom().getDayOfWeek(), Collectors.counting()));
        List<CountDto> byWeekday = Arrays.stream(DayOfWeek.values())
                .map(d -> new CountDto(d.getDisplayName(TextStyle.SHORT, Locale.ENGLISH), dayCounts.getOrDefault(d, 0L)))
                .toList();

        return new VenueStatsDto(
                active.size(),
                (int) active.stream().filter(m -> m.getDateFrom().isAfter(now)).count(),
                (int) active.stream().filter(m -> m.getStatus() == MatchStatus.FINISHED).count(),
                matches.size() - active.size(),
                totalPlayers,
                (int) active.stream()
                        .flatMap(m -> m.getParticipants().stream())
                        .map(Player::getId)
                        .distinct()
                        .count(),
                capacity == 0 ? 0 : Math.round(100f * totalPlayers / capacity),
                Math.round(hours * 10) / 10.0,
                venue.getPricePerHour() == null ? null : (int) Math.round(hours * venue.getPricePerHour()),
                bySport,
                byHour,
                byWeekday
        );
    }
}