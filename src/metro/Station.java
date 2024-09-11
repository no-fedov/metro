package metro;

import java.time.Duration;
import java.util.HashSet;
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
        this.setTransferStations(transferStations);
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

    public Set<Station> getTransferStations() {
        return transferStations;
    }

    public void setPrevious(Station station) {
        this.previous = station;
    }

    public void setNext(Station station) {
        this.next = station;
    }

    public void setTimeDrivingToNextStation(Duration timeDrivingToNextStation) {
        this.timeDrivingToNextStation = timeDrivingToNextStation;
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

    private void setTransferStations(Set<Station> transferStations) {
        this.transferStations = transferStations;
        if (transferStations == null) {
            return;
        }
        transferStations.forEach(station -> {
            if (station.transferStations == null) {
                station.transferStations = new HashSet<>();
            }
            station.transferStations.add(this);
        });
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
}