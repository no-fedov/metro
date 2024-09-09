package metro;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

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
        this.color = color;
        this.metro = metro;
        this.stations = new TreeMap<>();
    }

    public void addStation(Station station, Duration timeDrivingFromPreviousStation) {
        validStation(station);

        if (firstStation == null) {
            firstStation = station;
            lastStation = station;
        } else {
            lastStation.setNext(station, timeDrivingFromPreviousStation);
            station.setPrevious(lastStation);
            lastStation = station;
        }
        stations.put(station.getName(), station);
    }

    public String getColor() {
        return color;
    }

    public boolean hasStationWithName(String stationName) {
        return stations.containsKey(stationName);
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    private String getStationsForPrint() {
        Station currentStation = firstStation;
        String result = "{";
        result = currentStation.toString();
        while (currentStation.getNext() != null) {
            result += currentStation.getNext().toString() + ",";
            currentStation = currentStation.getNext();
        }
        result = result.substring(0, result.length() - 1);
        return result + "}";
    }

    @Override
    public String toString() {
        return "Line{" +
                "color='" + color + '\'' +
                ", stations=" + getStationsForPrint() +
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

    private void validStation(Station station) {
        if (stations.containsKey(station.getName())) {
            throw new RuntimeException("Нельзя добавить станцию с одинаковым именем");
        }
        if (!station.getLine().equals(this)) {
            throw new RuntimeException("Нельзя добавить станцию от другой линии");
        }
        if (!station.getMetro().equals(this.metro)) {
            throw new RuntimeException("Нельзя добавить станцию от другого метро");
        }
    }
}
