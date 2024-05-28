package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private final String address = Constants.clientAddress;
    private final int port = Constants.clientPort;
    private final String cuvant;
    private final TextView listaAnagrameTextView;

    private Socket socket;

    public ClientThread(String cuvant, TextView listaAnagrameTextView) {
        this.cuvant = cuvant;
        this.listaAnagrameTextView = listaAnagrameTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(cuvant);
            printWriter.flush();
            String listaAnagrame;
            while ((listaAnagrame = bufferedReader.readLine()) != null) {
                final String finalizedCurrencyInformation = listaAnagrame;
                listaAnagrameTextView.post(() -> listaAnagrameTextView.setText(finalizedCurrencyInformation));
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }

}
