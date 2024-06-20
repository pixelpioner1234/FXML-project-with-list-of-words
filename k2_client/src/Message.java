import java.time.LocalTime;

public class Message implements Comparable<Message> {
    public  String text;
    public  LocalTime time;

    public Message(String text, LocalTime time) {
        this.text = text;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public int compareTo(Message other) {
        return this.text.compareTo(other.text);
    }
}

