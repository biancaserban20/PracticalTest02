package ro.pub.cs.systems.eim.practicaltest02;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {
    private EditText serverPortEditText = null;
    private EditText cuvantEditText = null;

    private TextView listaAnagrameTextView = null;
    private ServerThread serverThread = null;
    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i(Constants.TAG, "[MAIN ACTIVITY] Server port is: " + serverPort);
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private final GetListaAnagrameClickListener getListaAnagrameClickListener = new GetListaAnagrameClickListener();
    private class GetListaAnagrameClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String cuvant = cuvantEditText.getText().toString();

            if (cuvant.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (cuvant) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            listaAnagrameTextView.setText(Constants.EMPTY_STRING);

            ClientThread clientThread = new ClientThread(
                    cuvant, listaAnagrameTextView
            );
            clientThread.start();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        cuvantEditText = (EditText)findViewById(R.id.cuvant_edit_text);
        Button connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        cuvantEditText = (EditText)findViewById(R.id.cuvant_edit_text);
        Button getListaAnagrameButton = (Button) findViewById(R.id.get_lista_anagrame_button);
        getListaAnagrameButton.setOnClickListener(getListaAnagrameClickListener);
        listaAnagrameTextView = (TextView)findViewById(R.id.anagrams_text_view);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}