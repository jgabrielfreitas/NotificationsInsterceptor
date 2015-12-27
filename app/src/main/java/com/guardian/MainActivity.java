package com.guardian;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.guardian.notifications.DeadNotification;
import com.guardian.notifications.NotificationInterceptorService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button notificationButton;
    Button settingsButton;
    TextView statusTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this);

        instanceViews();
    }

    private void instanceViews() {

        notificationButton = (Button)   findViewById(R.id.notificationButton);
        settingsButton     = (Button)   findViewById(R.id.settingsButton);
        statusTextView     = (TextView) findViewById(R.id.statusTextView);

        notificationButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.notificationButton:
                DeadNotification.notify(this);
                Intent bindService = new Intent(this, NotificationInterceptorService.class);
                startService(bindService);
                break;

            case R.id.settingsButton:
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    protected void onResume() {
        super.onResume();
        boolean notificationListenerIsEnabled = Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners").contains(getApplicationContext().getPackageName());
        if (notificationListenerIsEnabled) {
            statusTextView.setText("Permitido - OK");
            settingsButton.setVisibility(View.INVISIBLE);
        } else {
            statusTextView.setText("Sem permissão. Para o funcionando correto deste aplicativo, aceite o acesso das notificações.");
            settingsButton.setVisibility(View.VISIBLE);
        }
    }
}
