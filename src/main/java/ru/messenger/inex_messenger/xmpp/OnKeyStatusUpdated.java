package ru.messenger.inex_messenger.xmpp;

import ru.messenger.inex_messenger.crypto.axolotl.AxolotlService;

public interface OnKeyStatusUpdated {
	public void onKeyStatusUpdated(AxolotlService.FetchStatus report);
}
