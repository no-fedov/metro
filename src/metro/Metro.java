package metro;

import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Metro {
    private final String city;
    private final Set<Line> lines = new HashSet<>();

    public Metro(String city) {
        this.city = city;
    }

    public void createMetroLine(String colorLine) {
        Line line = new Line(colorLine, this);
        if (!lines.add(line)) {
            throw new RuntimeException("Такой цвет линии уже есть");
        }
    }

    public void createFirstStationToLine(String colorLine,
                                         String stationName,
                                         Set<Station> transferStations) {
        Line currentLine = getCurrentLine(colorLine, stationName);

        if (currentLine.getFirstStation() != null) {
            throw new RuntimeException("Линия уже содержит первую станцию");
        }

        Station newStation = createStationToAdd(stationName, currentLine, transferStations);
        currentLine.addStation(newStation, null);
    }

    public void createLastStationToLine(String colorLine,
                                        String stationName,
                                        Duration timeDrivingFromPreviousStation,
                                        Set<Station> transferStations) {
        Line currentLine = getCurrentLine(colorLine, stationName);

        if (currentLine.getLastStation() != null && currentLine.getLastStation().getNext() != null) {
            throw new RuntimeException("Пердыдущая станция должна существовать и не должна иметь следующей станции");
        }

        if (timeDrivingFromPreviousStation == null || timeDrivingFromPreviousStation.compareTo(Duration.ZERO) <= 0) {
            throw new RuntimeException("Некорректно задано время перегона");
        }

        Station newStation = createStationToAdd(stationName, currentLine, transferStations);
        currentLine.addStation(newStation, timeDrivingFromPreviousStation);
    }

    public Station findStationByName(String stationName) {
        return lines.stream()
                .map(line -> line.getStationByName(stationName))
                .filter(Objects::nonNull)
                .findFirst().orElseThrow(() -> new RuntimeException("Такой станции еще не существует"));
    }

    private Line getCurrentLine(String colorLine, String stationName) {
        Line referenceLine = new Line(colorLine, this);

        Line currentLine = lines.stream()
                .filter(line -> line.equals(referenceLine))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Нет линии данного цвета"));

        lines.forEach(line -> {
            if (line.hasStationWithName(stationName)) {
                throw new RuntimeException("Имя станции должно быть уникальным" +
                        " (включая названия станций в остальных ветках");
            }
        });
        return currentLine;
    }

    private Station createStationToAdd(String stationName, Line line, Set<Station> transferStations) {
        return new Station(stationName, line, this, transferStations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Metro metro = (Metro) o;
        return Objects.equals(city, metro.city);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(city);
    }

    @Override
    public String toString() {
        return "Metro{" +
                "city='" + city + '\'' +
                ", lines=" + lines +
                "}";
    }
}