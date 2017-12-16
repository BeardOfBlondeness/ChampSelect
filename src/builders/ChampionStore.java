package builders;

import java.util.ArrayList;
import java.util.HashMap;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.static_data.dto.Champion;
import net.rithms.riot.api.endpoints.static_data.dto.ChampionList;
import net.rithms.riot.constant.Platform;

public class ChampionStore {
	private HashMap<Integer, Champion> champions;
	private HashMap<Integer, Integer> indexes;
	
	public ChampionStore(RiotApi api) throws RiotApiException {
		champions = new HashMap<Integer, Champion>();
		indexes = new HashMap<Integer, Integer>();
		ChampionList championList = api.getDataChampionList(Platform.EUW);
		
		int i = 0;
		for(String champion:championList.getData().keySet()) {
			champions.put(championList.getData().get(champion).getId(), championList.getData().get(champion));
			indexes.put(championList.getData().get(champion).getId(), i);
			i++;
		}
	}
	
	public HashMap<Integer, Champion> getChampionMap() {
		return new HashMap<>(champions);
	}
	
	public ArrayList<Champion> getChampions() {
		return new ArrayList<>(champions.values());
	}
	
	public ArrayList<Integer> getIds() {
		return new ArrayList<>(champions.keySet());
	}
	
	public Champion get(int id) {
		return champions.get(id);
	}
	
	public int getIndex(int id) {
		return indexes.get(id);
	}
}
