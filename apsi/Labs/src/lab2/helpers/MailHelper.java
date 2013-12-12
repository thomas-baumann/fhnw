package lab2.helpers;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHelper {

	public static boolean sendMail(String email, String nickname, String password) {
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.tls", "true");
			props.put("mail.smtp.user", "eaf.htnw@gmail.com");
			props.put("mail.password", "hel2world");
			// props.put("mail.debug", "true");

			javax.mail.Authenticator auth = new javax.mail.Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("eaf.htnw@gmail.com", "hel2world");
				}
			};

			Session session = Session.getDefaultInstance(props, auth);

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("eaf.htnw@gmail.com", "APSI Lab 2"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject("SUBJECT");
			msg.setText("Hallo\n\nBenutzername: " + nickname + "\nPasswort: " + password);
			msg.saveChanges();
			Transport.send(msg);
			return true;
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
