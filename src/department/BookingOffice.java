package department;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BookingOffice {
    private static final BigDecimal SINGLE_FEE = new BigDecimal(20);
    private static final BigDecimal PRICE_FOR_TRANSFER = new BigDecimal(5);

    private Map<LocalDate, BigDecimal> income = new HashMap<>();

    public void saleTicket(LocalDate date, int transferCount) {
        BigDecimal price = new BigDecimal(transferCount)
                .multiply(PRICE_FOR_TRANSFER)
                .add(SINGLE_FEE);
        income.put(date, income.getOrDefault(date, BigDecimal.ZERO).add(price));
    }

    public Map<LocalDate, BigDecimal> getIncome() {
        return new HashMap<>(income);
    }
}
