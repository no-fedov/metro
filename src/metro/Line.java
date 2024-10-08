package metro;

import exception.LineActionException;

import java.time.Duration;
import java.util.*;

/**
 * Ветка метро
 */
public class Line {
    private final String color;
    private final Metro metro;
    private Station firstStation;
    private Station lastStation;
    private final Map<String, Station> stations;

    public Line(String color, Metro metro) {
        if (color == null || color.isBlank() || metro == null) {
            throw new LineActionException("Ошибка инициализации объекта Line"
                    + " (параметры конструктора не должны быть пустыми)");
        }
        this.color = color;
        this.metro = metro;
        this.stations = new HashMap<>();
    }

    public Station getFirstStation() {
        return firstStation;
    }

    public Station getLastStation() {
        return lastStation;
    }

    public String getColor() {
        return color;
    }

    public void addStation(Station station, Duration timeDrivingFromPreviousStation) {
        if (firstStation == null) {
            station.setNext(null);
            station.setPrevious(null);
            firstStation = station;
        } else {
            lastStation.setNext(station);
            lastStation.setTimeDrivingToNextStation(timeDrivingFromPreviousStation);
            station.setPrevious(lastStation);
            station.setNext(null);
        }
        lastStation = station;
        stations.put(station.getName(), station);
    }

    public Station getStationByName(String stationName) {
        return stations.get(stationName);
    }

    public boolean hasStationWithName(String stationName) {
        return stations.containsKey(stationName);
    }

    public Map<String, Station> getStations() {
        return new HashMap<>(stations);
    }

    @Override
    public String toString() {
        return "Line{" +
                "color='" + color + '\'' +
                ", stations=" + getCorrectSequenceStations() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(color, line.color)
                && Objects.equals(metro, line.metro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, metro);
    }

    private List<Station> getCorrectSequenceStations() {
        if (stations.isEmpty()) {
            return List.of();
        }
        List<Station> stationsSequence = new ArrayList<>();
        Station currentStation = firstStation;
        while (currentStation.getNext() != null) {
            stationsSequence.add(currentStation);
            currentStation = currentStation.getNext();
        }
        stationsSequence.add(currentStation);
        return stationsSequence;
    }
}
