public class Main {
    public static void main(String[] args) {
        WordBag wordBag = new WordBag();
        wordBag.populate();
        Server server = new Server(8081, wordBag);
        server.start();
        server.startSending();
    }
}