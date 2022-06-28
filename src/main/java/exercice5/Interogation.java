package exercice5;

import commun.Commun;
import java.io.IOException;

import java.util.Map;

public class Interogation {


    public static void main(String[] args) throws IOException {
        Commun commun = new Commun();
        Map<String,String> getFeedback = commun.getMap("asin:B002AQRJZQ");
        // commun.printMap(getFeedback);

        Map<String,String> getFeedbacksOfOneUser = commun.getAllFeedbackFromPersonId("15393162791624");
        commun.printMap(getFeedbacksOfOneUser);
        commun.printMap(commun.getAllFeedbackFromPersonId("15393162791624"));
        commun.shutdown();
    }

}
