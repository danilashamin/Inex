package ru.messenger.inex_messenger.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;


import ru.messenger.inex_messenger.R;

/**
 * Created by Алексей on 16.02.2017.
 */

public class MysqlLstActivity extends XmppActivity {

    private Button mCancelButton;
    private Button mSaveButton;
    private MysqlFragment myfragment = null;

    private ListView messagesView = null;

    @Override
    protected void refreshUiReal() {

    }

    @Override
    void onBackendConnected() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mysql_lst);


        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        // инициализация
        tabHost.setup();

//        tabSpec = tabHost.newTabSpec("tag1");
//        tabSpec.setIndicator("Вкладка 1");
//        tabSpec.setContent(R.id.tab1);
//        tabHost.addTab(tabSpec);

//        tabSpec = tabHost.newTabSpec("tag2");
//        tabSpec.setIndicator("Вкладка 2");
//        tabSpec.setContent(R.id.tab2);
//        tabHost.addTab(tabSpec);

//        tabSpec = tabHost.newTabSpec("tag3");
//        tabSpec.setIndicator("Вкладка 3");
//        tabSpec.setContent(R.id.tab3);
//        tabHost.addTab(tabSpec);

        // вторая вкладка по умолчанию активна
//        tabHost.setCurrentTabByTag("tag2");

        // логгируем переключение вкладок
//        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            public void onTabChanged(String tabId) {
//                Log.d(LOG_TAG, "tabId = " + tabId);

//            }

//        this.mSaveButton = (Button) findViewById(R.id.save_button);
//        this.mCancelButton = (Button) findViewById(R.id.cancel_button);
//        this.messagesView = (ListView) this.findViewById(R.id.mysql_mess_content);

    }

}
