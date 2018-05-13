

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.UniverseApi;
import net.troja.eve.esi.model.UniverseNamesResponse;

public class test {

	protected static final String DATASOURCE = "tranquility";
	protected static final int CHARACTER_ID_CHRIBBA = 196379789;
	private final static UniverseApi api = new UniverseApi();

	public static void main(String[] args) throws Exception {

		URL url = new URL("https://esi.evetech.net/latest/sovereignty/map/");
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String inputLine;
		String string = "";
		while ((inputLine = in.readLine()) != null) {
			string = string + inputLine;
		}
		
		in.close();
		
		int start = 1;
		int count = 0;
		String[] out = new String[999999];
		for (int i = 0; i < string.length(); i++) {
			if (string.substring(i, i + 1).equals("}")) {
				out[count] = string.substring(start, i + 1);
				start = i + 2;
				count++;
			}
		}
		
		
    }
}