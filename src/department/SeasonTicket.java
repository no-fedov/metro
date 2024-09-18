package department;

import java.time.LocalDate;

/**
 * Абонемент
 */
public class SeasonTicket {
    private static int VALIDITY_PERIOD = 1;
    private final String id;
    private final String stationName;
    private LocalDate saleDate;

    public SeasonTicket(String id, String stationName, LocalDate saleDate) {
        this.id = id;
        this.stationName = stationName;
        this.saleDate = saleDate;
    }

    public String getId() {
        return id;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public String getStationName() {
        return stationName;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public boolean isActive(LocalDate dateVerification) {
        LocalDate endDate = saleDate.plusMonths(VALIDITY_PERIOD);
        return !endDate.isAfter(dateVerification);
    }
}
