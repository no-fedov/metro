package metro;

import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

        if (!currentLine.isEmpty()) {
            throw new RuntimeException("Нельзя дважды добавить первую станцию");
        }

        currentLine.addStation(new Station(stationName, currentLine, this, transferStations),
                Duration.ZERO);
    }

    public void createLastStationToLine(String colorLine,
                                        String stationName,
                                        Duration timeDrivingFromPreviousStation,
                                        Set<Station> transferStations) {
        if (timeDrivingFromPreviousStation.compareTo(Duration.ZERO) <= 0) {
            throw new RuntimeException("Время перегона должно быть больше 0");
        }

        Line currentLine = getCurrentLine(colorLine, stationName);
        currentLine.addStation(new Station(stationName, currentLine, this, transferStations),
                timeDrivingFromPreviousStation);
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
