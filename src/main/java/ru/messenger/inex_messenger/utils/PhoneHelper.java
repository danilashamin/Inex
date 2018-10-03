package ru.messenger.inex_messenger.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Profile;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import ru.messenger.inex_messenger.entities.Contact;

import static java.security.AccessController.getContext;
import static ru.messenger.inex_messenger.R.string.contacts;

public class PhoneHelper {

	public void setInternalContactList(InternalContact contact){
		internalContactList.add(contact);
	}

	private List<InternalContact> internalContactList;

	public void load_from_phonebook(Context context){
//		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//		String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//				ContactsContract.CommonDataKinds.Phone.NUMBER};
//
//		Cursor people = context.getContentResolver().query(uri, projection, null, null, null);
//
//		int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//		int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//
//			if(people.moveToFirst())
//
//		{
//			do {
//				String name = people.getString(indexName);
//				String number = people.getString(indexNumber);
//				// Do work...
//			} while (people.moveToNext());
//		}
	}
	// TODO:
	//static
	public static void  loadPhoneContacts(Context context, final OnPhoneContactsLoadedListener listener) {
		final List<Bundle> phoneContacts = new ArrayList<>();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& context.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			listener.onPhoneContactsLoaded(phoneContacts);
			return;
		}

//		load_from_phonebook(context);

		final String[] PROJECTION = new String[]{ContactsContract.Data._ID,
				ContactsContract.Data.DISPLAY_NAME,
				ContactsContract.Data.PHOTO_URI,
				ContactsContract.Data.LOOKUP_KEY,
				ContactsContract.CommonDataKinds.Im.DATA};

		final String SELECTION = "(" + ContactsContract.Data.MIMETYPE + "=\""
				+ ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE
				+ "\") AND (" + ContactsContract.CommonDataKinds.Im.PROTOCOL
				+ "=\"" + ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER
				+ "\")";

		CursorLoader mCursorLoader = new NotThrowCursorLoader(context,
				ContactsContract.Data.CONTENT_URI, PROJECTION, SELECTION, null,
				null);
		mCursorLoader.registerListener(0, new OnLoadCompleteListener<Cursor>() {

			@Override
			public void onLoadComplete(Loader<Cursor> arg0, Cursor cursor) {
				if (cursor != null) {
					while (cursor.moveToNext()) {
						Bundle contact = new Bundle();
						contact.putInt("phoneid", cursor.getInt(cursor
								.getColumnIndex(ContactsContract.Data._ID)));
						contact.putString(
								"displayname",
								cursor.getString(cursor
										.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)));
//						contact.putString(
//								"phone",
//								cursor.getString(cursor
//										.getColumnIndex(ContactsContract.PhoneLookup.NUMBER)));
						contact.putString("photouri", cursor.getString(cursor
								.getColumnIndex(ContactsContract.Data.PHOTO_URI)));
						contact.putString("lookup", cursor.getString(cursor
								.getColumnIndex(ContactsContract.Data.LOOKUP_KEY)));

						contact.putString(
								"jid",
								cursor.getString(cursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA)));
						phoneContacts.add(contact);
					}
					cursor.close();
				}

				if (listener != null) {
					listener.onPhoneContactsLoaded(phoneContacts);
				}
			}
		});
		try {
			mCursorLoader.startLoading();
		} catch (RejectedExecutionException e) {
			if (listener != null) {
				listener.onPhoneContactsLoaded(phoneContacts);
			}
		}
	}

	public List<InternalContact> getInternalContactList() {
		return internalContactList;
	}

	public void setInternalContactList(List<InternalContact> internalContactList) {
		this.internalContactList = internalContactList;
	}

	private static class NotThrowCursorLoaderKAV extends CursorLoader {

		public NotThrowCursorLoaderKAV(Context c, Uri u, String[] p, String s, String[] sa, String so) {
			super(c, u, p, s, sa, so);
		}

		@Override
		public Cursor loadInBackground() {

			try {
				return (super.loadInBackground());
			} catch (Throwable e) {
				return(null);
			}
		}

	}

	private static class NotThrowCursorLoader extends CursorLoader {

		public NotThrowCursorLoader(Context c, Uri u, String[] p, String s, String[] sa, String so) {
			super(c, u, p, s, sa, so);
		}

		@Override
		public Cursor loadInBackground() {

			try {
				return (super.loadInBackground());
			} catch (Throwable e) {
				return(null);
			}
		}

	}

	public static Uri getSelfiUri(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& context.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			return null;
		}
		String[] mProjection = new String[]{Profile._ID, Profile.PHOTO_URI};
		Cursor mProfileCursor = context.getContentResolver().query(
				Profile.CONTENT_URI, mProjection, null, null, null);

		if (mProfileCursor == null || mProfileCursor.getCount() == 0) {
			return null;
		} else {
			mProfileCursor.moveToFirst();
			String uri = mProfileCursor.getString(1);
			mProfileCursor.close();
			if (uri == null) {
				return null;
			} else {
				return Uri.parse(uri);
			}
		}
	}

	public static String getVersionName(Context context) {
		final String packageName = context == null ? null : context.getPackageName();
		if (packageName != null) {
			try {
				return context.getPackageManager().getPackageInfo(packageName, 0).versionName;
			} catch (final PackageManager.NameNotFoundException | RuntimeException e) {
				return "unknown";
			}
		} else {
			return "unknown";
		}
	}
}
