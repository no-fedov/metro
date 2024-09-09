import metro.Metro;

import java.time.Duration;

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


        Metro metro = new Metro("Пермь");
        metro.createMetroLine("Красная");
        metro.createFirstStationToLine("Красная", "Спортивная", null);
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

        System.out.println(metro);

    }
}