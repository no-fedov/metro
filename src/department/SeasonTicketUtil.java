package department;

import java.util.regex.Pattern;

public class SeasonTicketUtil {
    private static int generatorId = 0;
    private static final int MAX_COUNT_TICKET = 9999;
    private static final String ID_FORMAT = "a%04d";
    private static final String ID_PATTERN = "^a\\d{4}$";

    private SeasonTicketUtil() {
    }

    /**
     * Генерирует новый номер билета
     */
    public static String generateId() {
        if (generatorId >= MAX_COUNT_TICKET) {
            throw new RuntimeException("Все абонементы проданы");
        }
        String id = String.format(ID_FORMAT, generatorId);
        generatorId++;
        return id;
    }

    public static int excludeNumberFromId(String seasonTicketId) {
        if (Pattern.matches(ID_PATTERN, seasonTicketId)) {
            return Integer.parseInt(seasonTicketId.substring(1));
        }
        throw new RuntimeException("Передан не номер абонемента");
    }
}