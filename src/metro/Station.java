package metro;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Станция метро
 */
public class Station {
    private String name;
    private Station previous;
    private Station next;
    private Duration timeDrivingToNextStation;
    private final Line line;
    private final Metro metro;
    private Set<Station> transferStations;

    public Station(String name,
                   Line line,
                   Metro metro,
                   Set<Station> transferStations) {
        this.name = name;
        this.line = line;
        this.metro = metro;
        this.transferStations = transferStations;
    }


    public String getName() {
        return name;
    }

    public Line getLine() {
        return line;
    }

    public Metro getMetro() {
        return metro;
    }

    public Station getPrevious() {
        return previous;
    }

    public Station getNext() {
        return next;
    }

    public void setNext(Station station, Duration timeDriving) {
        validStation(station);
        this.next = station;
        this.timeDrivingToNextStation = timeDriving;
    }

    public void setPrevious(Station previous) {
        validStation(previous);
        this.previous = previous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", changesLine=" + getTransferLines() +
                '}';
    }

    private String getTransferLines() {
        if (transferStations == null) {
            return null;
        }
        List<String> colorsLineTransferStation = transferStations.stream()
                .map(station -> station.getLine().getColor())
                .toList();
        return String.join(", ", colorsLineTransferStation);
    }

    private void validStation(Station station) {
        if (!station.getLine().equals(this.line)) {
            throw new RuntimeException("Нельзя добавить станцию от другой линии");
        }
        if (!station.getMetro().equals(this.metro)) {
            throw new RuntimeException("Нельзя добавить станцию от другой метро");
        }
    }
}
