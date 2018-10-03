package ru.messenger.inex_messenger.ui;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import ru.messenger.inex_messenger.R;
import ru.messenger.inex_messenger.entities.Message;

public class MysqlFragment extends Fragment {

	protected ListView messagesView;

	public Context getRoot_context() {
		return root_context;
	}

	public void setRoot_context(Context root_context) {
		this.root_context = root_context;
	}
	final View view = null;
	private   Context  root_context = null;

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessagesView(ListView messagesView) {
		if(this.messagesView != null) {
			this.messagesView = messagesView;
		}
	}

	final protected List<Message> messageList = new ArrayList<>();

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_mysql, container, false);

		messagesView = (ListView) view.findViewById(R.id.messages_view);

		//view.setOnClickListener(null);
		return view;
	}

	void set_list_items(String[] messagesList){
//		String[] lstFiles = new String[paths_string_array_files.size()];
//		for (int i = 0; i < paths_string_array_files.size(); i++) {
//			lstFiles[i] = paths_string_array_files.get(i);
//		}
		try {
			//view = inflater.inflate(R.layout.fragment_mysql, container, false);

			//messagesView = (ListView) view.findViewById(R.id.messages_view);
			//Activity actyvity = getActivity();
			ArrayAdapter<String> adapter = null; //android.R.layout.simple_list_item_1

			adapter = new ArrayAdapter<String>(this.getActivity().getBaseContext(), R.layout.my_item_1, messagesList);


			// присваиваем адаптер списку
			messagesView.setAdapter(adapter);
		}
		catch(Exception e){
			Log.d("error: ", e.toString());
		}
	}

}
