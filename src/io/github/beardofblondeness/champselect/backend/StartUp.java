package io.github.beardofblondeness.champselect.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;
import java.util.prefs.Preferences;
import io.github.beardofblondeness.champselect.gui.Frame;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

public class StartUp {

	public static final Scanner scanner = new Scanner(System.in);
	static RiotApi api;
	private static String apiKey;
	static ApiConfig config;
	static Account acc;
	public static ChampionStore CS;
	static int teamPosition;
	static Summoner summoner;
	
	public static void main(String[] args) {
		Frame f = new Frame();
		new Thread(new Runnable() {
		    @Override
		    public void run() {
				try {
					apiSetUp();
					beginCrawling();    
				} catch (RiotApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }  
		}).start();
	}

	public static void apiSetUp() throws RiotApiException {
		loadPreferences();
		config = new ApiConfig().setKey(StartUp.apiKey);
		api = new RiotApi(config);
		//CS = new ChampionStore(api);
		setChampionStore();
		acc = new Account(api.getSummonerByName(Platform.EUW, "Ghaster").getId());

		// First we need to request the summoner because we will need it's account ID
		summoner = api.getSummonerByName(Platform.EUW, "Ghaster");
	}
	
	public static void beginCrawling() throws RiotApiException {
		long now = Instant.now().toEpochMilli();
		long beginCrawlTime = now - 63113852000l;

		// Then we can use the account ID to request the summoner's match list
		MatchList matchList = api.getMatchListByAccountId(Platform.EUW, summoner.getAccountId(), null, null, null, -1l, -1l, 0, 100);

		for(int i = 100; matchList.getMatches().get(i).getTimestamp() > beginCrawlTime; i+=100) {	
			System.out.println(matchList.getStartIndex());
			System.out.println("Total Games in requested match list: " + matchList.getTotalGames() + " " + matchList.getEndIndex());

			// We can now iterate over the match list to access the data
			if (matchList.getMatches() != null) {
				for (MatchReference match : matchList.getMatches()) {
					Match mat = api.getMatch(Platform.EUW, match.getGameId());
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {}
					System.out.println(getOtherChampions(mat, summoner.getName(), summoner.getAccountId(), match));
				}
			}
			matchList = api.getMatchListByAccountId(Platform.EUW, summoner.getAccountId(), null, null, null, -1l, -1l, i, i+100);
		}
	}
	private static void loadPreferences() {
		Preferences preferences = Preferences.userNodeForPackage(StartUp.class);
		String key = preferences.get("api_key", null);
		if (key==null){
			System.out.println("No API key found. Please enter a key to use:");
			key = scanner.nextLine();
			System.out.println("Should the key be saved? (Y/n)");
			String response = "";
			while (!response.equals("n") && !response.equals("y") && !response.equals("")) response=scanner.nextLine().toLowerCase();
			if (!response.equals("n")) preferences.put("api_key", key);
		}
		else System.out.println("API key successfully loaded");
		StartUp.apiKey=key;
	}

	public static String getOtherChampions(Match tempMatch, String summonerID, Long accountID, MatchReference match) throws RiotApiException {
		teamPosition = -1;
		System.out.println("Ghaster played " + match.getChampion());
		System.out.println(match.getTimestamp() + " " + match.getSeason() + " " + tempMatch.getGameCreation());
		List participants = tempMatch.getParticipantIdentities();

		/*
		 * Initial Loop to calculate which player the account of interest is
		 */
		for(int i = 1; i < 11; i++) {
			System.out.println(CS.get(api.getMatch(Platform.EUW, tempMatch.getGameId()).getParticipantByParticipantId(i).getChampionId()));
			//System.out.println(api.getMatch(Platform.EUW, matchID).getParticipantByParticipantId(i).getStats().isWin());
			//System.out.println(api.getMatch(Platform.EUW, matchID).getParticipantByParticipantId(i).getTeamId() + " is ghasters team");

			if(tempMatch.getParticipantByParticipantId(i).equals(tempMatch.getParticipantByAccountId(accountID))) {
				teamPosition = i;
				break;
			}
		}
		System.out.println(teamPosition);
		return "\n";		
	}

	public static void setChampionStore() throws RiotApiException {
		String loadFile = "res/Current_Champion_Data.CSTORE";
		File f = new File(loadFile);
		if(f.exists() && !f.isDirectory()) {
			CS = (ChampionStore) deserializeObject(CS, loadFile);
			System.out.println("Loading champion data");
		}
		else {
			CS = new ChampionStore(api);
			CS.saveChampData();
			System.out.println("Saving champion data");
		}
	}
	
	/*
	 * Decided to make this one general purpose so that we could use it for account as well 
	 * TODO move this into some form of builder class or background class, SartUp is way too clusterfucked
	 */
	static Object deserializeObject(Object obj, String loadFile) {
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try {
			fin = new FileInputStream(loadFile);
			ois = new ObjectInputStream(fin);
			obj = ois.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}
}