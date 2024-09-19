package department;

import metro.Metro;
import metro.Station;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Касса
 */
public class BookingOffice {
    private static final BigDecimal SINGLE_FEE = new BigDecimal(20);
    private static final BigDecimal PRICE_FOR_TRANSFER = new BigDecimal(5);
    private static final BigDecimal PRICE_SEASON_TICKET = new BigDecimal(3000);
    private static final BigDecimal PRICE_RENEWAL_SEASON_TICKET = new BigDecimal(3000);

    private final Metro metro;
    private final Station station;
    private final Map<LocalDate, BigDecimal> income = new HashMap<>();

    public BookingOffice(Station station, Metro metro) {
        this.station = station;
        this.metro = metro;
    }

    public Map<LocalDate, BigDecimal> getIncome() {
        return new HashMap<>(income);
    }

    /**
     * Продать билет на поездку (увеличивает доход в кассе)
     *
     * @param saleDate          - дата продажи
     * @param transferCount - количество перегонов между станциями
     */
    public void saleTicket(LocalDate saleDate, int transferCount) {
        BigDecimal price = new BigDecimal(transferCount)
                .multiply(PRICE_FOR_TRANSFER)
                .add(SINGLE_FEE);
        addIncome(saleDate, price);
    }

    /**
     * Продать абонемент на месячные поездки (увеличивает доход в кассе)
     *
     * @param stationName - имя станции на которой производилась покупка
     * @param saleDate    - дата продажи
     * @return - возвращает абонемент
     */
    public SeasonTicket saleSeasonTicket(String stationName, LocalDate saleDate) {
        String ticketId = SeasonTicketUtil.generateId();
        SeasonTicket seasonTicket = new SeasonTicket(ticketId, stationName, saleDate);
        this.metro.addSeasonTicket(seasonTicket);
        addIncome(saleDate, PRICE_SEASON_TICKET);
        return seasonTicket;
    }

    /**
     * Продлить срок действия абонемента (увеличивает доход в касссе)
     *
     * @param seasonTicketId - номер абонемента
     * @param saleDate     - дата покупки продления
     */
    public void extendSeasonTicket(String seasonTicketId, LocalDate saleDate) {
        SeasonTicket currentSeasonTicket = this.metro.getSeasonTicketById(seasonTicketId);
        currentSeasonTicket.setSaleDate(saleDate);
        addIncome(saleDate, PRICE_RENEWAL_SEASON_TICKET);
    }

    /**
     * Добавляет доход в дату покупки
     *
     * @param saleDate - дата покупки
     * @param price    - величина дохода
     */
    private void addIncome(LocalDate saleDate, BigDecimal price) {
        income.put(saleDate, income.getOrDefault(saleDate, BigDecimal.ZERO).add(price));
    }
}