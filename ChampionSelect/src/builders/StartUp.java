package builders;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;



public class StartUp {

	public static void main(String[] args) throws RiotApiException {
		ApiConfig config = new ApiConfig().setKey("THIS IS A KEY");
		RiotApi api = new RiotApi(config);

		// First we need to request the summoner because we will need it's account ID
		Summoner summoner = api.getSummonerByName(Platform.EUW, "Ghaster");

		// Then we can use the account ID to request the summoner's match list
		MatchList matchList = api.getMatchListByAccountId(Platform.EUW, summoner.getAccountId());

		System.out.println("Total Games in requested match list: " + matchList.getTotalGames());

		// We can now iterate over the match list to access the data
		if (matchList.getMatches() != null) {
			for (MatchReference match : matchList.getMatches()) {
				System.out.println("GameID: " + match.getGameId());
				System.out.println(match.getChampion() + " was the champ they played");
				System.out.println(match.getRole() + " was the role they played");
				System.out.println(match.getLane() + " was the lane they played");
				System.out.println(match.toString() + "\n\n\n");
			}
		}
	}
}
