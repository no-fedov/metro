package department;

import exception.SeasonTicketActionException;

import java.util.regex.Pattern;

public class SeasonTicketUtil {
    private static int generatorId = 0;
    private static final int MAX_TICKET_NUMBER = 9999;
    private static final String ID_FORMAT = "a%04d";
    private static final String ID_PATTERN = "^a\\d{4}$";
    private static final int START_INDEX_FOR_NUMBER = 1;

    private SeasonTicketUtil() {
    }

    /**
     * Генерирует новый номер билета
     */
    public static String generateId() {
        if (generatorId > MAX_TICKET_NUMBER) {
            throw new SeasonTicketActionException("Все абонементы проданы");
        }
        String id = String.format(ID_FORMAT, generatorId);
        generatorId++;
        return id;
    }

    /**
     * Ивлекает порядковый номер абонемента из строки
     */
    public static int excludeNumberFromId(String seasonTicketId) {
        if (Pattern.matches(ID_PATTERN, seasonTicketId)) {
            return Integer.parseInt(seasonTicketId.substring(START_INDEX_FOR_NUMBER));
        }
        throw new SeasonTicketActionException("Передан не номер абонемента");
    }
}