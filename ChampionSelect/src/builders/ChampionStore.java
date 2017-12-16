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
	
	public ChampionStore(RiotApi api) throws RiotApiException {
		ChampionList championList = api.getDataChampionList(Platform.EUW);
		
		for(String champion:championList.getData().keySet()) champions.put(championList.getData().get(champion).getId(), championList.getData().get(champion));
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
}
