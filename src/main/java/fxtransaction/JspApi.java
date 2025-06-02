package fxtransaction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.*;

public class JspApi {
	public JsonArray fetchExchangeRate() {
		try {
            long timestamp = System.currentTimeMillis(); // 產生時間戳
            String url = "https://www.bankchb.com/frontend/jsp/getG0100_query.jsp?v=" + timestamp;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET() // 用get取得資料（post也可以）
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray datas = obj.getAsJsonArray("datas"); // 一次取得所有 Json 物件陣列
            return datas;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
}
