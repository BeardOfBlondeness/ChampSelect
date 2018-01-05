package io.github.beardofblondeness;

public class WinRate {
	int wins;
	int losses;
	
	public WinRate() {
		this.wins=0;
		this.losses=0;
	}
	
	public void addWin() {
		this.wins++;
	}
	
	public void addLoss() {
		this.losses++;
	}
}
