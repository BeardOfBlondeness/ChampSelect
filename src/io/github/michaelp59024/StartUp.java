package io.github.michaelp59024;

import java.util.List;
import java.util.Scanner;
import java.util.prefs.Preferences;

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
	
	public static void main(String[] args) throws RiotApiException {
		loadPreferences();
		config = new ApiConfig().setKey(StartUp.apiKey);
		api = new RiotApi(config);
		CS = new ChampionStore(api);
		acc = new Account(api.getSummonerByName(Platform.EUW, "Ghaster").getId());
		
		// First we need to request the summoner because we will need it's account ID
		Summoner summoner = api.getSummonerByName(Platform.EUW, "Ghaster");
		
		// Then we can use the account ID to request the summoner's match list
		MatchList matchList = api.getMatchListByAccountId(Platform.EUW, summoner.getAccountId(), null, null, null, -1, 1496327958, -1, -1);
		
		System.out.println("Total Games in requested match list: " + matchList.getTotalGames());

		// We can now iterate over the match list to access the data
		if (matchList.getMatches() != null) {
			for (MatchReference match : matchList.getMatches()) {
				Match mat = api.getMatch(Platform.EUW, match.getGameId());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				System.out.println(getOtherChampions(mat.getGameId(), summoner.getName(), summoner.getAccountId(), match));
			}
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
	
	public static String getOtherChampions(Long matchID, String summonerID, Long accountID, MatchReference match) throws RiotApiException {
		teamPosition = -1;
		System.out.println("Ghaster played " + match.getChampion());
		Match tempMatch = api.getMatch(Platform.EUW, matchID);
		List participants = tempMatch.getParticipantIdentities();
		
		/*
		 * Initial Loop to calculate which player the account of interest is
		 */
		for(int i = 1; i < 11; i++) {
			System.out.println(CS.get(api.getMatch(Platform.EUW, matchID).getParticipantByParticipantId(i).getChampionId()));
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
}
