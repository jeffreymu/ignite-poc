package me.jeff.ignitepoc.queue.translator;

public class SimpleTranslator implements MessageConsumer {

    private MessageConsumer out;

    public SimpleTranslator(MessageConsumer out) {
        this.out = out;
    }

    @Override
    public void onMessage(String text) {
        System.out.println("translating " + text);
        text = text.replaceAll("hello", "salut");
        text = text.replaceAll("bye", "salut");
        System.out.println("... to: " + text);
        out.onMessage(text);
    }

}
