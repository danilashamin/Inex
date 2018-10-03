package ru.messenger.inex_messenger.xmpp;

import ru.messenger.inex_messenger.entities.Account;
import ru.messenger.inex_messenger.xmpp.stanzas.PresencePacket;

public interface OnPresencePacketReceived extends PacketReceived {
	public void onPresencePacketReceived(Account account, PresencePacket packet);
}
