import metro.Metro;
import metro.Station;

import java.time.Duration;
import java.util.Set;

public class Runner {
    public static void main(String[] args) {
//        Мы захотели построить метро в городе Пермь. Было принятно решение о строении двух линий метро:
//        Красная, со станциями:
//                -Спортивная
//                | Перегон 2 минуты 21 секуда
//                -Медведковская
//                | Перегон 1 минута 58 секунд
//                -Молодежная
//                | Перегон 3 минуты
//                -Пермь 1
//                | Перегон 2 минуты 10 секунд
//                -Пермь 2
//                | Перегон 4 минуты 26 секунд
//                -Дворец Культуры
//
//        Синяя, со станциями:
//                -Пацанская
//                | Перегон 1 минута 30 секунд
//                -Улица Кирова
//                | Перегон 1 минута 47 секунд
//                -Тяжмаш
//                | Перегон 3 минуты 19 секунд
//                -Нижнекамская
//                | Перегон 1 минута 48 секунд
//                -Соборная

//        Переход между линиями будет происходить на станции Тяжмаш/Пермь 1.

        Metro metro = new Metro("Пермь");

        metro.createMetroLine("Красная");
        //создаем первую станцию
        metro.createFirstStationToLine("Красная", "Спортивная", null);
        //создаем последующие станции
        metro.createLastStationToLine("Красная", "Медведковская",
                Duration.ofMinutes(2).plusSeconds(21), null);
        metro.createLastStationToLine("Красная", "Молодежная",
                Duration.ofMinutes(1).plusSeconds(58), null);
        metro.createLastStationToLine("Красная", "Пермь 1",
                Duration.ofMinutes(3), null);
        metro.createLastStationToLine("Красная", "Пермь 2",
                Duration.ofMinutes(2).plusSeconds(10), null);
        metro.createLastStationToLine("Красная", "Дворец Культуры",
                Duration.ofMinutes(4).plusSeconds(26), null);

        metro.createMetroLine("Синяя");
        //создаем первую станцию
        metro.createFirstStationToLine("Синяя", "Пацанская", null);
        //создаем последующие станции
        metro.createLastStationToLine("Синяя", "Улица Кирова",
                Duration.ofMinutes(1).plusSeconds(30), null);
        metro.createLastStationToLine("Синяя", "Тяжмаш",
                Duration.ofMinutes(1).plusSeconds(47), Set.of(metro.findStationByName("Пермь 1")));
        metro.createLastStationToLine("Синяя", "Нижнекамская",
                Duration.ofMinutes(3).plusSeconds(19), null);
        metro.createLastStationToLine("Синяя", "Соборная",
                Duration.ofMinutes(1).plusSeconds(48), null);

        System.out.println(metro);

        Station stationBetweenLines = metro.getTransferStationBetweenLines("Красная", "Синяя");

        System.out.println(stationBetweenLines);
    }
}