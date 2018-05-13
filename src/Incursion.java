import org.json.JSONArray;

public class Incursion {
	private int constellationID;
	private boolean hasMom;
	private JSONArray systems;
	private double influence;
	private int stagingID;
	private String status;
	private String [] systemNames;
	
	public Incursion(int cid, boolean mom, JSONArray sys, double inf, int sid, String sta, String[] sysN) {
		constellationID = cid;
		hasMom = mom;
		systems = sys;
		influence = inf;
		stagingID = sid;
		status = sta;
		systemNames = sysN;
	}
	
	//Sets the new Influence
	public void setInfluence(double inf) {
		influence = inf;
	}
	
	//Sets the new mom status
	public void setMom(boolean m) {
		hasMom = m;
	}
	
	//Sets the new Incursion status
	public void setStatus(String s) {
		status = s;
	}
	
	//Sets the new system names
	public void setSystemNames(String[] n) {
		systemNames = n;
	}
	
	//Returns the constellationID
	public int getConstellationID() {
		return constellationID;
	}
	
	//Returns Mom Status
	public boolean getMomStatus() {
		return hasMom;
	}
	
	//Returns system ids
	public JSONArray getSystemIDs() {
		return systems;
	}
	
	//Returns staging system id
	public int getStagingID() {
		return stagingID;
	}
	
	//Returns the state of the incursion
	public String getStatus() {
		return status;
	}
	
	//Returns the influence
	public double getInfluence() {
		return influence;
	}
	
	//Returns the system 
	public String[] getSystemNames() {
		return systemNames;
	}
}