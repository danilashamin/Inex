package ru.messenger.inex_messenger.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wefika.flowlayout.FlowLayout;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import ru.messenger.inex_messenger.R;
import ru.messenger.inex_messenger.entities.ListItem;
import ru.messenger.inex_messenger.ui.XmppActivity;
import ru.messenger.inex_messenger.utils.InternalContact;
import ru.messenger.inex_messenger.utils.UIHelper;

public class ListItemAdapter extends ArrayAdapter<ListItem> {

	protected XmppActivity activity;
	protected boolean showDynamicTags = false;
	private View.OnClickListener onTagTvClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view instanceof  TextView && mOnTagClickedListener != null) {
				TextView tv = (TextView) view;
				final String tag = tv.getText().toString();
				mOnTagClickedListener.onTagClicked(tag);
			}
		}
	};
	private OnTagClickedListener mOnTagClickedListener = null;

	public ListItemAdapter(XmppActivity activity, List<ListItem> objects) {
		super(activity, 0, objects);
		this.activity = activity;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		this.showDynamicTags = preferences.getBoolean("show_dynamic_tags",true);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ListItem item = getItem(position);
		if (view == null) {
			view = inflater.inflate(R.layout.contact, parent, false);
		}
		RelativeLayout layoutFone = (RelativeLayout) view.findViewById(R.id.backgroundFon);
		TextView tvName = (TextView) view.findViewById(R.id.contact_display_name);
		TextView tvJid = (TextView) view.findViewById(R.id.contact_jid);
		ImageView picture = (ImageView) view.findViewById(R.id.contact_photo);
		FlowLayout tagLayout = (FlowLayout) view.findViewById(R.id.tags);

		List<ListItem.Tag> tags = item.getTags(activity);
		if(tags != null) {
			if (tags.size() == 0 || !this.showDynamicTags) {
				tagLayout.setVisibility(View.GONE);
			} else {
				tagLayout.setVisibility(View.VISIBLE);
				tagLayout.removeAllViewsInLayout();
				for (ListItem.Tag tag : tags) {
					TextView tv = (TextView) inflater.inflate(R.layout.list_item_tag, tagLayout, false);
					tv.setText(tag.getName());
					// KAA TODO Закоменчено
					//tv.setBackgroundColor(tag.getColor());
					tv.setTextColor(tag.getColor());
					tv.setTypeface(null, Typeface.ITALIC);
					tv.setOnClickListener(this.onTagTvClick);
					tagLayout.addView(tv);
				}
			}
		}
		final String jid = item.getDisplayJid();
		if (jid != null) {
			//KAA tvJid.setVisibility(View.VISIBLE);
			tvJid.setVisibility(View.GONE);
			tvJid.setText(jid);
		} else {
			tvJid.setVisibility(View.GONE);
		}
		tvName.setText(item.getDisplayName());
		// TODO: KAV отображение списка
		if(item.getClass() == InternalContact.class){
			Bitmap bm = ((InternalContact)item).getAvatar_btmp();
			if(bm != null) {
				picture.setImageBitmap(bm);
			}
			else{
				loadAvatar(item, picture);
			}
//			if(jid != null){
//				layoutFone.setBackgroundColor(0xFFe91e63);
//			}
		}
		else {
			loadAvatar(item, picture);
		}
		return view;
	}

	public void setOnTagClickedListener(OnTagClickedListener listener) {
		this.mOnTagClickedListener = listener;
	}

	public interface OnTagClickedListener {
		void onTagClicked(String tag);
	}

	class BitmapWorkerTask extends AsyncTask<ListItem, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		private ListItem item = null;

		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference<>(imageView);
		}

		@Override
		protected Bitmap doInBackground(ListItem... params) {
			return activity.avatarService().get(params[0], activity.getPixel(48), isCancelled());
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null && !isCancelled()) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
					imageView.setBackgroundColor(0x00000000);
				}
			}
		}
	}

	public void loadAvatar(ListItem item, ImageView imageView) {
		if (cancelPotentialWork(item, imageView)) {
			final Bitmap bm = activity.avatarService().get(item,activity.getPixel(48),true);
			if (bm != null) {
				cancelPotentialWork(item, imageView);
				imageView.setImageBitmap(bm);
                // TODO KAA Color
                // imageView.setBackgroundColor(0x00000000);
			} else {
                // TODO KAA Color
                // imageView.setBackgroundColor(UIHelper.getColorForName(item.getDisplayName()));
				imageView.setImageDrawable(null);
				final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
				final AsyncDrawable asyncDrawable = new AsyncDrawable(activity.getResources(), null, task);
				imageView.setImageDrawable(asyncDrawable);
				try {
					task.execute(item);
				} catch (final RejectedExecutionException ignored) {
		    	}
			}
		}
	}

	public static boolean cancelPotentialWork(ListItem item, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final ListItem oldItem = bitmapWorkerTask.item;
			if (oldItem == null || item != oldItem) {
				bitmapWorkerTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

}
