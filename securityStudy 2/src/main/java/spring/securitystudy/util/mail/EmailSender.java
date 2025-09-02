package spring.securitystudy.util.mail;

public interface EmailSender {
    public void send(String to,  String subject, String text);
}
