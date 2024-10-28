import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class App {
    public static void main(String[] args) throws Exception {
        String username = "hiusnguyen201";
        URL url = new URL(String.format("https://api.github.com/users/%s/events", username));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            Type listType = TypeToken.getParameterized(ArrayList.class, Map.class).getType();
            List<Map<String, Object>> events = gson.fromJson(response.toString(), listType);

            System.out.println("\n========== EVENTS ==========\n");
            for (Map<String, Object> event : events) {
                Map<String, Object> payload = (Map<String, Object>) event.get("payload");
                Map<String, Object> repo = (Map<String, Object>) event.get("repo");
                List<Object> commits = (List<Object>) payload.get("commits");

                if (commits == null) {
                    commits = new ArrayList<Object>();
                }

                System.out
                        .println(String.format("%s %d commits to %s", event.get("type"), commits.size(),
                                repo.get("name")));
            }
            System.out.println("\n========== EVENTS ==========\n");
        }
    }
}
