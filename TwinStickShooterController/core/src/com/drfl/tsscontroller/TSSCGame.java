package com.drfl.tsscontroller;

import com.badlogic.gdx.Game;
import com.drfl.tsscontroller.Network.TSSCClient;
import com.drfl.tsscontroller.Screens.IPScreen;

public class TSSCGame extends Game {

	private TSSCClient client;

	@Override
	public void create () {
		setScreen(new IPScreen(this));
	}


	public TSSCClient getClient() {
		return client;
	}

	public void setClient(TSSCClient client) {
		this.client = client;
	}
}
