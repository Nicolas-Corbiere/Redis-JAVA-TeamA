package exercice3;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DatasFeedbacks {
    public static final String PATH_FILE = "src/ressources/csv/";
    public static final String NAME_FILE = "Feedback";

    public static final String SEPARATOR = Pattern.quote("|");

    private InputStream inputStreamFile;
    private Map<String, Map<String, String>> hashFeedbacks = new HashMap<>();


    DatasFeedbacks() throws IOException {
        this.reset();
    }

    private void initInputStream() throws IOException {
        try {
            this.inputStreamFile = new FileInputStream(PATH_FILE+NAME_FILE+".csv");
        } catch (FileNotFoundException e) {
            this.unzipFile();
            this.inputStreamFile = new FileInputStream(PATH_FILE+NAME_FILE+".csv");
        }
    }

    private void unzipFile() throws IOException {
        byte[] buffer = new byte[1024];
        File destDir = new File(PATH_FILE);
        ZipInputStream zis = new ZipInputStream(new FileInputStream(PATH_FILE+NAME_FILE+".zip"));
        ZipEntry zipEntry = zis.getNextEntry();
        if (zipEntry != null) {
            File unZipFile = new File(PATH_FILE+NAME_FILE+".csv");
            FileOutputStream fos = new FileOutputStream(unZipFile);
            int len;
            while((len = zis.read(buffer)) > 0) fos.write(buffer, 0, len);
            fos.close();
        }
        zis.closeEntry();
        zis.close();
    }

    private void fillInHashFeedbacks() throws IOException {
        if (this.inputStreamFile != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStreamFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(SEPARATOR);
                    if (values.length >= 3) {
                        String asin = values[0];
                        String personneId = values[1];
                        String feedback = values[2];
                        if (this.hashFeedbacks.containsKey(asin)) this.hashFeedbacks.get(asin).put(personneId, feedback);
                        else {
                            Map<String, String> feedbacks = new HashMap<>();
                            feedbacks.put(personneId, feedback);
                            this.hashFeedbacks.put(asin, feedbacks);
                        }
                    }
                }
            }
        }
    }

    public void reset() throws IOException {
        this.initInputStream();
        this.fillInHashFeedbacks();
    }

    public String[] getAsins() {
        int size = this.hashFeedbacks.size();
        return this.hashFeedbacks.keySet().toArray(new String[size]);
    }

    public Map<String, String> getFeedbacksByAsin(final String asin) {
        Map<String, String> map = this.hashFeedbacks.get(asin);
        return map != null ? new HashMap<>(map) : new HashMap<>();
    }
}
