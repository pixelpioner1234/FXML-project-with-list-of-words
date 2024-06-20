import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    Socket socket;
    BufferedReader input;
    int counterOfWords = 0;
    List<Message> messages = new ArrayList<>();

    @FXML
    TextArea textArea;
    @FXML
    Label wordCountLabel;
    @FXML
    TextField filterField;
    @FXML
    Button connect;
    @FXML
    Button disconnect;


    public void onConnect(ActionEvent actionEvent) {
        connect.setDisable(true);
        disconnect.setDisable(false);

        Thread thread = new Thread(()->{
            try {
                socket = new Socket("localhost", 8081);

                //socket.close();
                this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg;

                while((msg = input.readLine()) != null) {
                    counterOfWords++;
                    String finalMsg = msg;


                    /*LocalTime now = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String time = now.format(formatter);*/

                    LocalTime now = LocalTime.now();
                    messages.add(new Message(finalMsg,now));
                    Collections.sort(messages);

                    Platform.runLater(()->{
                        onFilterChange();
                    });
                    //System.out.println("Received/new: " + msg );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void disconnect(){
        try {
            socket.close();
            disconnect.setDisable(true);
            connect.setDisable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onFilterChange(){
        String filter = filterField.getText();
        List<Message> filteredMessages = messages.stream()
                .filter((message)->{
                    boolean isOk ;
                    isOk = message.getText().contains(filter);
                    return isOk;
                })
                .collect(Collectors.toList());

        textArea.clear();
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (Message message : filteredMessages) {
            String time = message.getTime().format(formatter);
            stringBuilder.append(time).append("  ").append(message.getText()).append("\n");
        }
        textArea.setText(stringBuilder.toString());
        wordCountLabel.setText("Total words: " + filteredMessages.size());
    }








}
