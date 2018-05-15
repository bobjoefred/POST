import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.api.UniverseApi;
import net.troja.eve.esi.model.UniverseNamesResponse;


public class MainBack {
	
	final static int INITSIZE = 10;
	final static String INCURSIONESI = "https://esi.tech.ccp.is/latest/incursions/?datasource=tranquility";
	final static String SOVESI = "https://esi.evetech.net/latest/sovereignty/map/";
	final static String FACTIONESI ="https://esi.evetech.net/latest/universe/factions/";
	final static String ALLIANCEESI = "https://esi.evetech.net/latest/alliances/";
	final static String CONSTELLATIONESI = "https://esi.evetech.net/latest/universe/constellations/";
	final static int MAXINCURSIONSPAWNS = 7;
	protected static final String DATASOURCE = "tranquility";
	protected static final int CHARACTER_ID_CHRIBBA = 196379789;
	private final static UniverseApi api = new UniverseApi();
	static int CURRENTNUMINCURSIONS;
	
	public static Incursion[] fillIncursions(Incursion inc[]) throws Exception {
		Incursion [] incursions = inc;
		String [] parsedIncursions = new String[MAXINCURSIONSPAWNS];
		int [] constellations = new int[MAXINCURSIONSPAWNS];
		boolean [] momStatus = new boolean[MAXINCURSIONSPAWNS];
		JSONArray [] systems = new JSONArray[MAXINCURSIONSPAWNS];
		double [] influence = new double[MAXINCURSIONSPAWNS];
		String [] state = new String[MAXINCURSIONSPAWNS];
		int [] staging = new int[MAXINCURSIONSPAWNS];
		String [] esiSovMap = new String[999];
		String [] esiFaction = new String[23];
		String esiConstellation = "";
		
		parsedIncursions = incursionParse(INCURSIONESI);
		constellations = getConstellationID(parsedIncursions);
		momStatus = getMomStatus(parsedIncursions);
		systems = getSystems(parsedIncursions);
		influence = getInfluence(parsedIncursions);
		state = getState(parsedIncursions);
		staging = getStaging(parsedIncursions);
		esiSovMap = esiParse(SOVESI, 8045);
		esiFaction = esiParse(FACTIONESI, 23);
		
		//System.out.println(getFaction(esiSovMap)[0]);
		for(int i = 0; i < MAXINCURSIONSPAWNS; i++) {
			if(constellations[i] == 0) {
				CURRENTNUMINCURSIONS = i;
				break;
			} else {
				esiConstellation = esiParse(CONSTELLATIONESI, 1, constellations[i])[0];
				incursions[i] = new Incursion(
						constellations[i], 
						momStatus[i],  
						systems[i],
						influence[i],
						staging[i],
						state[i],
						null,
						getFactionID(esiSovMap, staging[i], esiFaction),
						null,
						postUniverseNames(new JSONArray("[" + constellations[i] + "]"))[0],
						postUniverseNames(new JSONArray("[" + new JSONObject(esiConstellation).get("region_id") + "]"))[0]);
			}
		}
		for(int i = 0; i < MAXINCURSIONSPAWNS; i++) {
			if(incursions[i] == null) {
				break;
			} else {
				incursions[i].setSystemNames(postUniverseNames(incursions[i].getSystemIDs()));
			}
		}
		
		return incursions;
	}
	
	public static int[] getStaging(String p[]) throws JSONException{
		String [] out = p;
		int[] staging = new int[MAXINCURSIONSPAWNS];

		for (int i = 0; i < 7; i++) {
			if (out[i] == null) {
				break;
			}
			JSONObject jsonObject = new JSONObject(out[i]);
			staging[i] = (int) jsonObject.get("staging_solar_system_id");
		}
		return staging;
	}
	
	public static String[] getState(String p[]) throws JSONException{
		String [] out = p;
		String[] state = new String[MAXINCURSIONSPAWNS];

		for (int i = 0; i < 7; i++) {
			if (out[i] == null) {
				break;
			}
			JSONObject jsonObject = new JSONObject(out[i]);
			state[i] = (String) jsonObject.get("state");
		}
		return state;
	}
	
	public static double[] getInfluence(String p[]) throws JSONException{
		String [] out = p;
		double[] influence = new double[MAXINCURSIONSPAWNS];

		for (int i = 0; i < 7; i++) {
			if (out[i] == null) {
				break;
			}
			JSONObject jsonObject = new JSONObject(out[i]);
			influence[i] = (double) jsonObject.get("influence");
		}
		return influence;
	}
	
	public static boolean[] getMomStatus(String p[]) throws JSONException{
		String [] out = p;
		boolean[] mStatus = new boolean[MAXINCURSIONSPAWNS];

		for (int i = 0; i < 7; i++) {
			if (out[i] == null) {
				break;
			}
			JSONObject jsonObject = new JSONObject(out[i]);
			mStatus[i] = (boolean) jsonObject.get("has_boss");
		}
		return mStatus;
	}
	
	public static JSONArray[] getSystems(String p[]) throws JSONException {
		String [] out = p;
		JSONArray[] systems = new JSONArray[MAXINCURSIONSPAWNS];

		for (int i = 0; i < 7; i++) {
			if (out[i] == null) {
				break;
			}
			JSONObject jsonObject = new JSONObject(out[i]);
			systems[i] = (JSONArray) jsonObject.get("infested_solar_systems");
		}
		return systems;
	}
	
	public static int[] getConstellationID(String p[]) throws JSONException {
		String [] out = p;
		int[] constellation = new int[MAXINCURSIONSPAWNS]; 

		for (int i = 0; i < 7; i++) {
			if (out[i] == null) {
				break;
			}
			JSONObject jsonObject = new JSONObject(out[i]);
			constellation[i] = (int) jsonObject.get("constellation_id");
		}
		return constellation;
	}
	
	public static String[] incursionParse(String u) throws Exception{
		URL url = new URL(u);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String inputLine;
		String string = "";
		while ((inputLine = in.readLine()) != null) {
			string = string + inputLine;
		}
		
		in.close();
		
		int start = 1;
		int count = 0;
		String[] out = new String[10];
		for (int i = 0; i < string.length(); i++) {
			if (string.substring(i, i + 1).equals("}")) {
				out[count] = string.substring(start, i + 1);
				start = i + 2;
				count++;
			}
		}
		
		return out;
	}
	
	public static String[] esiParse(String u, int s) throws Exception{
		URL url = new URL(u);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		int size = s;
		
		String inputLine;
		String string = "";
		while ((inputLine = in.readLine()) != null) {
			string = string + inputLine;
		}
		
		in.close();
		
		int start = 1;
		int count = 0;
		String[] out = new String[size];
		for (int i = 0; i < string.length(); i++) {
			if (string.substring(i, i + 1).equals("}")) {
				out[count] = string.substring(start, i + 1);
				start = i + 2;
				count++;
			}
		}
		
		return out;
	}
	
	public static String[] esiParse(String u, int s, int a) throws Exception{
		String concatenate = u + a + "/";
		URL url = new URL(concatenate);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		int size = s;
		
		String inputLine;
		String string = "";
		while ((inputLine = in.readLine()) != null) {
			string = string + inputLine;
		}
		
		in.close();
		
		int start = 1;
		int count = 0;
		String[] out = new String[size];
		out[0] = string;
		
		return out;
	}
	
	public static String[] postUniverseNames(JSONArray jsonArray) throws ApiException, JSONException {
        final List<Integer> ids = new ArrayList<>();
        int [] systems = parseSystems(jsonArray);

        for(int i = 0; i<jsonArray.length(); i++) {
        	ids.add(systems[i]);
        }
        
        final List<UniverseNamesResponse> response = api.postUniverseNames(ids, DATASOURCE, null, null);

        final String [] result = new String[jsonArray.length()];
        
        for(int i = 0; i<jsonArray.length(); i++) {
        	result[i] = response.get(i).getName();
        }
        
        return result;
        
    }
	
	public static int[] parseSystems(JSONArray j) throws JSONException {
		int [] parsed = new int[10];

		for(int i = 0; i<j.length(); i++) {
			parsed[i] = j.getInt(i);
		}

		return parsed;
	}
	
	public static String getFactionName(int l, String p[], int c) throws JSONException {
		String [] out = p;
		String faction = "errorrrr";
		int look = l;
		int factionid = 0; 
		int change = c;
	
		for(int i = 0; i<23; i++) {
			System.out.println(out[i]);
			JSONObject jsonObject = new JSONObject(out[i]);
			factionid = (int) jsonObject.get("faction_id");

			if(factionid == look) {
				faction = jsonObject.getString("name");
				break;
			}
		}
		
		return faction;
	}
	
	public static String getFactionID(String p[], int l, String o[]) throws Exception {
		String [] out = p;
		String [] through = o;
		int look = l;
		int faction = 0;
		int system;
		String factionName = "error";
		
		for (int i = 0; i < 8045; i++) {
			if (out[i] == null) {
				break;
			}
			JSONObject jsonObject = new JSONObject(out[i]);
			system = (int) jsonObject.get("system_id");
			if(system == look) {
				if(jsonObject.has("faction_id") == false) {
					faction = (int) jsonObject.get("alliance_id");
					jsonObject = new JSONObject(esiParse(ALLIANCEESI, 1, faction)[0]);
					factionName = (String) jsonObject.get("name");
				} else {
					faction = (int) jsonObject.get("faction_id");
					factionName = getFactionName(faction, through, 1);
				}
			}
		}
		
		System.out.println("faction name: " + faction);
		return factionName;
	}
	
	public static void main(String[] args) throws Exception {
		Incursion [] incursions = new Incursion[MAXINCURSIONSPAWNS];
		String [] systems = new String[9];
		
		incursions = fillIncursions(incursions);
		
		for(int i = 0; i < MAXINCURSIONSPAWNS; i++) {
			if(incursions[i] == null) {
				break;
			} else {
				System.out.println("-----INCURSION " + i + "-------");
				System.out.println(incursions[i].getConstellationID());
				System.out.println(incursions[i].getMomStatus());
				System.out.println(incursions[i].getStagingID());
				System.out.println(incursions[i].getStatus());
				System.out.println(incursions[i].getSystemIDs());
				for(int j = 0; j<incursions[i].getSystemNames().length; j++) {
					System.out.println(incursions[i].getSystemNames()[j]);
				}
				System.out.println(incursions[i].getInfluence());
				System.out.println(incursions[i].getFaction());
				System.out.println(incursions[i].getConstellationName());
				System.out.println(incursions[i].getRegion());
			}
		}
		
		//System.out.println(esiParse("https://esi.evetech.net/latest/sovereignty/map/"));
		//System.out.println(postUniverseNames(incursions[0].getSystems()));
	}

}