package io.github.beardofblondeness;

import java.util.HashMap;

import net.rithms.riot.api.endpoints.static_data.dto.Champion;

public class Account {
	
	private Long summonerID; //The id of the summoner in the account you have created
	private HashMap<Champion, WinRate> champWinLoss; //will hold the wins/losses for each champion you have played
	private HashMap<Champion, HashMap<Champion, WinRate>> opponentChampionCombination; //will hold the wins and losses for each champion you have played against, as each champion you have played
	private HashMap<Champion, HashMap<Champion, WinRate>> teamChampionCombination; //will hold the wins and losses for each champion you have played with, as each champion you have played
	ChampionStore cs; //Uses the ChampionStore to calculate the max number of champions in the game, as well as indexing each champion based on their ID
	
	public Account(Long summonerID) {
		this.summonerID = summonerID;
	}
	
	public Long getId() {
		return summonerID;
	}
	
	/*
	 * Adds the win or loss count for the champion you played against, corresponding with the champion you played
	 */
	public void addOpponent(int yourId, int oppId, boolean win) {
		Champion you = cs.get(yourId);
		Champion opp = cs.get(oppId);
		
		if (opponentChampionCombination.get(you)==null) opponentChampionCombination.put(you, new HashMap<Champion, WinRate>());
		if (opponentChampionCombination.get(you).get(opp)==null) opponentChampionCombination.get(you).put(opp, new WinRate());
		
		if(win) opponentChampionCombination.get(you).get(opp).addWin();
		else opponentChampionCombination.get(you).get(opp).addLoss();
	}
	
	/*
	 * adds a win/loss for the champion you just played
	 */
	public void addGame(int yourId, boolean win) {
		Champion champion = cs.get(yourId);
		
		if (champWinLoss.get(champion)==null) champWinLoss.put(champion, new WinRate());
		
		if(win) champWinLoss.get(champion).addWin();
		else champWinLoss.get(champion).addLoss();
	}
	
	/*
	 * Adds a win/loss for the champion you played with, corresponding with the champion you played
	 */
	public void addSynergy(int yourId, int synId, boolean win) {
		Champion you = cs.get(yourId);
		Champion syn = cs.get(synId);
		
		if (teamChampionCombination.get(you)==null) teamChampionCombination.put(you, new HashMap<Champion, WinRate>());
		if (teamChampionCombination.get(you).get(syn)==null) teamChampionCombination.get(you).put(syn, new WinRate());
		
		if(win) teamChampionCombination.get(you).get(syn).addWin();
		else teamChampionCombination.get(you).get(syn).addLoss();
	}
}
