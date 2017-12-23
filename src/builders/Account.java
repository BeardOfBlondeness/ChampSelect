package builders;

public class Account {
	
	private Long summonerID; //The id of the summoner in the account you have created
	private int[][] champWinLoss; //will hold the wins/losses for each champion you have played
	private int[][][] opponentChampionCombination; //will hold the wins and losses for each champion you have played against, as each champion you have played
	private int[][][] teamChampionCombination; //will hold the wins and losses for each champion you have played with, as each champion you have played
	ChampionStore cs; //Uses the ChampionStore to calculate the max number of champions in the game, as well as indexing each champion based on their ID
	
	public Account(Long summonerID) {
		this.summonerID = summonerID;
		fillChampions();
	}
	
	public Long getId() {
		return summonerID;
	}
	
	/*
	 * Creates the size of each array depending on how many champ's are currently in the game!
	 */
	private void fillChampions() {
		cs = StartUp.CS;
		int numOfChamps = cs.getIds().size();
		
		champWinLoss = new int[numOfChamps][2];
		opponentChampionCombination = new int[numOfChamps][numOfChamps][2];
		teamChampionCombination = new int[numOfChamps][numOfChamps][2];
	}
	
	/*
	 * Adds the win or loss count for the champion you played against, corresponding with the champion you played
	 */
	public void addOpponent(int yourId, int oppId, boolean win) {
		int yourI = cs.getIndex(yourId);
		int oppI = cs.getIndex(oppId);
		
		if(win) opponentChampionCombination[yourI][oppI][0]++;
		else opponentChampionCombination[yourI][oppI][1]++;
	}
	
	/*
	 * adds a win/loss for the champion you just played
	 */
	public void addGame(int yourId, boolean win) {
		int yourI = cs.getIndex(yourId);
		
		if(win) champWinLoss[yourI][0]++;
		else champWinLoss[yourI][1]++;
	}
	
	/*
	 * Adds a win/loss for the champion you played with, corresponding with the champion you played
	 */
	public void addSynergy(int yourId, int synId, boolean win) {
		int yourI = cs.getIndex(yourId);
		int synI = cs.getIndex(synId);
		
		if(win) teamChampionCombination[yourI][synI][0]++;
		else teamChampionCombination[yourI][synI][1]++;
	}
}
