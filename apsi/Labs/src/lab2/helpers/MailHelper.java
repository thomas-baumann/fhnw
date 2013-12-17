package lab2.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class MailHelper {

	public static boolean sendMail(String email, String nickname, String password) {
		try {
			Properties properties = new Properties();
			// loading of property file in eclipse
			// BufferedInputStream stream = new BufferedInputStream(new FileInputStream("resources/config.properties"));
			// loading of property file in servlet
			InputStream stream = MailHelper.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(stream);
			stream.close();
			String smtpHost = properties.getProperty("smtp.host");
			String smtpPort = properties.getProperty("smtp.port");
			final String smtpUser = properties.getProperty("smtp.user");
			final String smtpPassword = properties.getProperty("smtp.password");
			String smtpUserName = properties.getProperty("smtp.user.name");
			String mailSubject = properties.getProperty("mail.subject");
			String mailText = properties.getProperty("mail.text");

			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.port", smtpPort);
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.tls", "true");
			props.put("mail.smtp.user", smtpUser);
			props.put("mail.password", smtpPassword);
			// props.put("mail.debug", "true");

			javax.mail.Authenticator auth = new javax.mail.Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(smtpUser, smtpPassword);
				}
			};

			Session session = Session.getDefaultInstance(props, auth);

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(smtpUser, smtpUserName));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject(mailSubject);
			msg.setText(MessageFormat.format(mailText, nickname, password));
			msg.saveChanges();
			Transport.send(msg);
			return true;
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
