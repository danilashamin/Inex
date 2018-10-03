package ru.messenger.inex_messenger.xmpp.jingle;

import ru.messenger.inex_messenger.entities.DownloadableFile;

public interface OnFileTransmissionStatusChanged {
	void onFileTransmitted(DownloadableFile file);

	void onFileTransferAborted();
}
