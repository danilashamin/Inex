package ru.messenger.inex_messenger.ui;

import android.os.Bundle;
import ru.messenger.inex_messenger.R;

/**
 * Created by Алексей on 16.02.2017.
 */

public class VoteActivity extends XmppActivity {

    @Override
    protected void refreshUiReal() {

    }

    @Override
    void onBackendConnected() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysql_test);
    }

}
