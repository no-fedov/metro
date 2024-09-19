package metro;

import department.BookingOffice;
import department.SeasonTicket;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Станция метро
 */
public class Station {
    private final String name;
    private Station previous;
    private Station next;
    private Duration timeDrivingToNextStation;
    private final Line line;
    private final Metro metro;
    private Set<Station> transferStations;
    private final BookingOffice bookingOffice;

    public Station(String name,
                   Line line,
                   Metro metro,
                   Set<Station> transferStations) {
        this.name = name;
        this.line = line;
        this.metro = metro;
        this.setTransferStations(transferStations);
        this.bookingOffice = new BookingOffice(this, metro);
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

    public BookingOffice getBookingOffice() {
        return bookingOffice;
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

    /**
     * Продать разовую поездку (увеличивает доход в кассе)
     *
     * @param date             - дата продажи (не понятно зачем ее передавать (наверное для тестирования),
     *                         спросить у Юрия)
     * @param startStationName - станция отправления
     * @param endStationName   - конечная станция
     */
    public void sellTicket(LocalDate date, String startStationName, String endStationName) {
        int transferCount = metro.transferCountBetweenStations(startStationName, endStationName);
        bookingOffice.saleTicket(date, transferCount);
    }

    /**
     * Продать месячный абонемент на поездки (увеличивает доход в кассе)
     *
     * @param stationName - имя станции на которой продали
     * @param saleDate    - дата продажи
     * @return - возвращает проданный абонемент
     * (не понимаю для чего тут должны быть параметры у метода, я думал передавать текущее имя станции
     * и текущую дату, спросить у Юрия пункт 3.1)
     */
    public SeasonTicket sellSeasonTicket(String stationName, LocalDate saleDate) {
        this.metro.findStationByName(stationName);
        return this.bookingOffice.saleSeasonTicket(stationName, saleDate);
    }

    /**
     * Продлить абонемент (увеличивает доход в кассе)
     */
    public void extendSeasonTicket(String seasonTicketId, LocalDate saleDate) {
        bookingOffice.extendSeasonTicket(seasonTicketId, saleDate);
    }

    /**
     * Устанавливает для текущей станции станции перехода на другие линии
     */
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

    /**
     * Возвращает цвета линий/веток метро на которые можно перейти с текущей станции
     */
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