package builders;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

public class StartUp {
	static RiotApi api;
	static ApiConfig config;
	public static void main(String[] args) throws RiotApiException {
		config = new ApiConfig().setKey("RGAPI-27ff443f-2c2f-4ba2-b03c-1f9adeb00a32");
		api = new RiotApi(config);

		// First we need to request the summoner because we will need it's account ID
		Summoner summoner = api.getSummonerByName(Platform.EUW, "Ghaster");

		// Then we can use the account ID to request the summoner's match list
		MatchList matchList = api.getMatchListByAccountId(Platform.EUW, summoner.getAccountId());
		
		
		System.out.println("Total Games in requested match list: " + matchList.getTotalGames());

		// We can now iterate over the match list to access the data
		if (matchList.getMatches() != null) {
			for (MatchReference match : matchList.getMatches()) {
				Match mat = api.getMatch(Platform.EUW, match.getGameId());
				System.out.println(getOtherChampions(mat.getGameId(), summoner.getName(), summoner.getAccountId(), match));
				
			}
		}
	}
	
	public static String getOtherChampions(Long matchID, String summonerID, Long accountID, MatchReference match) throws RiotApiException {
		System.out.println("Ghaster played " + match.getChampion());
		Match tempMatch = api.getMatch(Platform.EUW, matchID);
		for(int i = 0; i < 4; i++) {
			
		}
		return "";		
	}
}
