package ru.messenger.inex_messenger.entities;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;

import ru.messenger.inex_messenger.utils.InternalContact;

public class AdressBook extends AbstractEntity {


	private Context context = null;

	public AdressBook(Context context){
		this.context = context;
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues values = new ContentValues();
//		values.put(UUID, uuid);
//		values.put(USERNAME, jid.getLocalpart());
//		values.put(SERVER, jid.getDomainpart());
//		values.put(PASSWORD, password);
//		values.put(OPTIONS, options);
//		synchronized (this.keys) {
//			values.put(KEYS, this.keys.toString());
//		}
//		values.put(ROSTERVERSION, rosterVersion);
//		values.put(AVATAR, avatar);
//		values.put(DISPLAY_NAME, displayName);
//		values.put(HOSTNAME, hostname);
//		values.put(PORT, port);
//		values.put(STATUS, presenceStatus.toShowString());
//		values.put(STATUS_MESSAGE, presenceStatusMessage);
		return values;
	}
	private class GrabURL extends AsyncTask<String, Void, ArrayList<InternalContact>> {
		private Context context = null;
		public GrabURL(Context context){
			this.context = context;
		}

		protected void onPreExecute() {
		}

		protected ArrayList<InternalContact> doInBackground(String... urls) {
			ArrayList<InternalContact> internalContacts = load_from_phonebook(context);
			return internalContacts;
		}

		protected void onCancelled() {
            Toast toast = Toast.makeText(context,
                    "Ошибка при загрузке адресной книги", Toast.LENGTH_LONG);
            toast.show();
		}

		protected void onPostExecute(String[] content) {
			//set_list_items(content);
		}
	}
	// TODO: load_from_phonebook
	public ArrayList<InternalContact> load_from_phonebook(Context context){
		ArrayList<InternalContact> internalContactList = new ArrayList<>();
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			String imName = "";
			int imType = 0;
			Cursor imCur;
			String phone = null;
			do {
				InternalContact contact = new InternalContact();

				// get the contact's information
				String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				imCur = cr.query(
						ContactsContract.Data.CONTENT_URI,
						null,
						ContactsContract.Data.CONTACT_ID
								+ " = ? AND "+ ContactsContract.CommonDataKinds.Im.PROTOCOL +" in (" + ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER + ")", new String[] { id }, null);
				if (imCur != null) {
					while (imCur.moveToNext()) {
						imName = imCur
								.getString(imCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
						imType = imCur
								.getInt(imCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));

						contact.setJabberid(imType, imName);
					}
					imCur.close();
				}

				// get the user's phone number
				if (hasPhone > 0) {
					imCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
					if (imCur != null) {
						while (imCur.moveToNext()) {
							phone = imCur.getString(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							contact.addPhone(phone);
						}
						imCur.close();
					}
				}
				contact.setName(name);

				internalContactList.add(contact);

			} while (cursor.moveToNext());
			// clean up cursor
			cursor.close();
		}
		return internalContactList;
	}
}
