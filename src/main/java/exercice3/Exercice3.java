package exercice3;

import commun.Commun;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

/*

*/
public class Exercice3 {
    private static DatasFeedbacks DATAS_FEEDBACKS;
    static {
        try {
            DATAS_FEEDBACKS = new DatasFeedbacks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        chargerRedis();
    }

    public static void chargerRedis() {
        Commun redis = new Commun();
        System.out.println(DATAS_FEEDBACKS.getAsins().length);
        for (String asin : DATAS_FEEDBACKS.getAsins()) {
            Map<String, String> feedbacks = DATAS_FEEDBACKS.getFeedbacksByAsin(asin);
            String result = redis.setAsin(asin, DATAS_FEEDBACKS.getFeedbacksByAsin(asin)) > 0 ? "\033[0;92m[CREATED]\033[0m" : "\033[0;93m[UPDATED]\033[0m";
            feedbacks.entrySet().stream().forEach(entry -> System.out.printf("%s \033[0;96m[asin:%s] [personneId:%s]\033[0m%n", result, asin, entry.getKey()));
        }
        redis.shutdown();
    }
}
