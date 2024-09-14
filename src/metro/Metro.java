package metro;

import java.time.Duration;
import java.util.*;

public class Metro {
    private final String city;
    private final Set<Line> lines = new HashSet<>();

    public Metro(String city) {
        this.city = city;
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

//    /**
//     * Считает прибыль по всем станциям
//     */
//    public void calculateIncome() {
//        Map<LocalDate, BigDecimal> totalIncome = new HashMap<>();
//        lines.stream().map()
//    }

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

    /**
     * Создает последнюю станцию в линии
     */
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

    /**
     * 2.1
     * Возвращает станцию пересадки между двумя ПЕРЕСЕКАЮЩИМИСЯ линями,
     * если линии не пересекаются - выбрасывается исключение
     */
    public Station getTransferStationBetweenLines(String startLineName,
                                                  String endLineName) {
        Line lineStart = getCurrentLine(startLineName);
        Line lineEnd = getCurrentLine(endLineName);

        Station currentStation = lineStart.getFirstStation();
        while (currentStation != null) {
            Set<Station> stations = currentStation.getTransferStations();
            if (stations != null) {
                Optional<Station> foundTransferStation = stations.stream()
                        .filter(station -> station.getLine().equals(lineEnd))
                        .findFirst();
                if (foundTransferStation.isPresent()) {
                    return currentStation;
                }
            }
            currentStation = currentStation.getNext();
        }
        throw new RuntimeException(String.format("Между линиями '%s'-'%s' нет пересадки", startLineName, endLineName));
    }

    //    2.5
    public int transferCountBetweenStations(String startStationName, String endStationName) {
        Station startStation = findStationByName(startStationName);
        Station endStation = findStationByName(endStationName);

        if (startStation.equals(endStation)) {
            throw new RuntimeException("Начальная и конечная станции совпадают.");
        }

        if (startStation.getLine().equals(endStation.getLine())) {
            return transferCountBetweenStationsOnTheLine(startStation, endStation);
        }

        return calculateTransfers(startStation, endStation);
    }

    /**
     * Ищет количество пересадок если станции находятся на разных линиях
     */
    private int calculateTransfers(Station startStation, Station endStation) {
        Queue<Station> aroundStationMetro = new LinkedList<>();
        Set<Station> visited = new HashSet<>();
        Map<Station, Integer> stationTransferCount = new HashMap<>();

        aroundStationMetro.add(startStation);
        stationTransferCount.put(startStation, 0);
        visited.add(startStation);

        while (!aroundStationMetro.isEmpty()) {
            Station currentStation = aroundStationMetro.remove();
            int currentRides = stationTransferCount.get(currentStation);

            if (currentStation.getNext() != null && !visited.contains(currentStation.getNext())) {
                aroundStationMetro.add(currentStation.getNext());
                visited.add(currentStation.getNext());
                stationTransferCount.put(currentStation.getNext(), currentRides + 1);
            }
            if (currentStation.getPrevious() != null && !visited.contains(currentStation.getPrevious())) {
                aroundStationMetro.add(currentStation.getPrevious());
                visited.add(currentStation.getPrevious());
                stationTransferCount.put(currentStation.getPrevious(), currentRides + 1);
            }

            Set<Station> transferStations = currentStation.getTransferStations();
            if (transferStations != null) {
                for (Station transferStation : transferStations) {
                    if (!visited.contains(transferStation)) {
                        aroundStationMetro.add(transferStation);
                        visited.add(transferStation);
                        stationTransferCount.put(transferStation, currentRides);
                    }
                }
            }

            if (currentStation.equals(endStation)) {
                return stationTransferCount.get(currentStation);
            }
        }

        throw new RuntimeException(String.format("Путь между станциями '%s' и '%s' не найден",
                startStation.getName(),
                endStation.getName()));
    }

    /**
     * 2.4
     * Ищет количество перегонов между станциями на одной линии
     */
    private int transferCountBetweenStationsOnTheLine(Station startStation, Station endStation) {
        int transferCountStraight = transferCountBetweenStationsStraight(startStation, endStation);
        if (transferCountStraight != -1) {
            return transferCountStraight;
        }

        int transferCountBack = transferCountBetweenStationsBack(startStation, endStation);
        if (transferCountBack != -1) {
            return transferCountBack;
        }

        throw new RuntimeException(String.format("Нет пути между станциями '%s' и '%s'",
                startStation.getName(),
                endStation.getName()));
    }

    /**
     * 2.2
     * Ищет количество перегонов между станциями на одной линии (обход прямо)
     */
    private int transferCountBetweenStationsStraight(Station startStation, Station endStation) {
        int transferCounter = 0;
        while (startStation.getNext() != null) {
            transferCounter++;
            if (startStation.getNext().equals(endStation)) {
                return transferCounter;
            }
            startStation = startStation.getNext();
        }
        return -1;
    }

    /**
     * 2.3
     * Ищет количество перегонов между станциями на одной линии (обход назад)
     */
    private int transferCountBetweenStationsBack(Station startStation, Station endStation) {
        int transferCounter = 0;
        while (startStation.getPrevious() != null) {
            transferCounter++;
            if (startStation.getPrevious().equals(endStation)) {
                return transferCounter;
            }
            startStation = startStation.getPrevious();
        }
        return -1;
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
}