package com.tisson.demo.common.base;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailService {
	private final static Logger LOGGER = LoggerFactory.getLogger(MailService.class);
	@Autowired
	private JavaMailSender sender;
	@Value("${spring.mail.username}")
	private String from;
	
	/**
	 *  发送纯文本的简单邮件
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 * @throws Exception
	 */
	public void sendSimpleMail(String to, String subject, String content) throws Exception {
		SimpleMailMessage message = new SimpleMailMessage();
		String[] reciver = to.split(",");
		for (String rec : reciver) {
			message.setFrom(from);
			message.setTo(rec);
			message.setSubject(subject);
			message.setText(content);
			sender.send(message);
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("带纯文本邮件已经发送,from:[{}],to:[{}],subject:[{}],content:[{}],"
						+ "filePath:[{}]",from,to,subject,content);
			}
		}
	}
	
	/**
	 * 发送html格式的邮件
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 * @throws Exception
	 */
	public void sendHtmlMail(String to, String subject, String content) throws Exception {
		MimeMessage message = sender.createMimeMessage();
		String[] reciver = to.split(",");
		for (String rec : reciver) {
			// true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(rec);
			helper.setSubject(subject);
			helper.setText(content, true);
			sender.send(message);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("带html邮件已经发送,from:[{}],to:[{}],subject:[{}],content:[{}]," + "filePath:[{}]", from, to,
						subject, content);
			}
		}
	}
	
	
	/**
	 * 发送带附件的邮件
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 * @param filePath
	 * @throws Exception
	 */
	public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws Exception {
		MimeMessage message = sender.createMimeMessage();
		String[] reciver = to.split(",");
		for (String rec : reciver) {
			// true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(rec);
			helper.setSubject(subject);
			helper.setText(content, true);

			FileSystemResource file = new FileSystemResource(new File(filePath));
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
			helper.addAttachment(fileName, file);
			sender.send(message);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("带附件的邮件已经发送,from:[{}],to:[{}],subject:[{}],content:[{}]," + "filePath:[{}]", from, to,
						subject, content, filePath);
			}
		}
	}
	
	/**
	 * 发送嵌入静态资源（一般是图片）的邮件
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 * @param rscPath
	 * @param rscId
	 * @throws Exception
	 */
	public void sendInlineResourceMail(String to, String subject, String content, 
			String rscPath, String rscId)throws Exception {
		MimeMessage message = sender.createMimeMessage();
		String[] reciver = to.split(",");
		for (String rec : reciver) {
			// true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(rec);
			helper.setSubject(subject);
			helper.setText(content, true);
			FileSystemResource res = new FileSystemResource(new File(rscPath));
			helper.addInline(rscId, res);
			sender.send(message);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("嵌入静态资源的邮件已经发送,from:[{}],to:[{}],subject:[{}],content:[{}]," + "rscPath:[{}],rscId:[{}]",
						from, to, subject, content, rscPath, rscId);
			}
		}
	}
}
