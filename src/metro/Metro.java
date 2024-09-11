package metro;

import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Metro {
    private final String city;
    private final Set<Line> lines = new HashSet<>();

    public Metro(String city) {
        this.city = city;
    }

    /**
     * Создает линию/ветку метро, принимает в параметре 'ЦВЕТ' для новой ветки.
     * ЦВЕТ должен быть уникальным.
     */
    public void createMetroLine(String colorLine) {
        Line line = new Line(colorLine, this);
        if (!lines.add(line)) {
            throw new RuntimeException(String.format("Такой цвет линии '%s' уже есть", colorLine));
        }
    }

    /**
     * Создает в указанной линии первую станцию по её названию и станциям пересадки.
     * Имя станции должно быть уникальным для всех веток/линий.
     */
    public void createFirstStationToLine(String colorLine,
                                         String stationName,
                                         Set<Station> transferStations) {
        Line currentLine = getCurrentLine(colorLine);
        checkStationDuplicates(stationName);

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
        Line currentLine = getCurrentLine(colorLine);
        checkStationDuplicates(stationName);

        if (currentLine.getLastStation() != null && currentLine.getLastStation().getNext() != null) {
            throw new RuntimeException("Пердыдущая станция должна существовать и не должна иметь следующей станции");
        }

        if (timeDrivingFromPreviousStation == null || timeDrivingFromPreviousStation.compareTo(Duration.ZERO) <= 0) {
            throw new RuntimeException("Некорректно задано время перегона");
        }

        Station newStation = createStationToAdd(stationName, currentLine, transferStations);
        currentLine.addStation(newStation, timeDrivingFromPreviousStation);
    }

    /**
     * Возвращает станцию по названию.
     */
    public Station findStationByName(String stationName) {
        return lines.stream()
                .map(line -> line.getStationByName(stationName))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                                String.format("Станции с именем '%s' еще не существует", stationName)
                        )
                );
    }

    public Station getTransferStationBetweenLines(String startLineName,
                                                  String endLineName) {
        Line lineStartTrip = getCurrentLine(startLineName);
        Line lineEndTrip = getCurrentLine(endLineName);

        Station currentStation = lineStartTrip.getFirstStation();
        while (currentStation != null) {
            Set<Station> stations = currentStation.getTransferStations();
            if (stations != null) {
                //ищем станцию пересадки на endLine
                Optional<Station> foundTransferStation = stations.stream()
                        .filter(station -> station.getLine().equals(lineEndTrip))
                        .findFirst();
                //если станция существует, то возвращаем текущую станцию из первой ветки
                if (foundTransferStation.isPresent()) {
                    return currentStation;
                }
            }
            currentStation = currentStation.getNext();
        }
        throw new RuntimeException("Между ветками нет пересадки");
    }

    private Line getCurrentLine(String colorLine) {
        Line referenceLine = new Line(colorLine, this);
        return lines.stream()
                .filter(line -> line.equals(referenceLine))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Нет линии данного цвета: %s", colorLine)));
    }

    private void checkStationDuplicates(String stationName) {
        lines.forEach(line -> {
            if (line.hasStationWithName(stationName)) {
                throw new RuntimeException("Имя станции должно быть уникальным" +
                        " (включая названия станций в остальных ветках");
            }
        });
    }

    private Station createStationToAdd(String stationName,
                                       Line line,
                                       Set<Station> transferStations) {
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