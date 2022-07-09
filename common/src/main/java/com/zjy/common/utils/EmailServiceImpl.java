package com.zjy.common.utils;

import com.zjy.baseframework.common.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.List;
import java.util.Properties;

@Service
public class EmailServiceImpl {

    @Autowired(required = false)
    private JavaMailSenderImpl mailSender;

    static {
        System.setProperty("mail.mime.splitlongparameters", Boolean.FALSE.toString());
        System.setProperty("mail.mime.charset", StandardCharsets.UTF_8.name());
    }

    /**
     * 简单文本邮件
     *
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(mailSender.getUsername());
        mailSender.send(message);
    }

    /**
     * 发送复杂邮件
     *
     * @param to            接收人邮箱
     * @param cc            抄送人邮箱
     * @param subject       邮件主题
     * @param content       邮件内容
     * @param isContentHtml 邮件内容是否是html格式
     * @param attachments   附件
     */
    public void sendEmail(List<String> to, List<String> cc, String subject, String content, boolean isContentHtml, List<String> attachments) {
        List<File> attachmentFiles = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(attachments)) {
            for (String attachment : attachments) {
                attachmentFiles.add(new File(attachment));
            }
        }
        sendEmailWithFile(to, cc, subject, content, isContentHtml, attachmentFiles);
    }

    /**
     * 发送复杂邮件
     *
     * @param to            接收人邮箱
     * @param cc            抄送人邮箱
     * @param subject       邮件主题
     * @param content       邮件内容
     * @param isContentHtml 邮件内容是否是html格式
     * @param attachments   附件
     */
    public void sendEmailWithFile(List<String> to, List<String> cc, String subject, String content, boolean isContentHtml, List<File> attachments) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        if(CollectionUtils.isEmpty(to)) {
            throw new ServiceException("邮件接收人不能为空！");
        }
        if(CollectionUtils.isEmpty(cc)) {
            cc = new ArrayList<>();
        }
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to.toArray(new String[to.size()]));
            helper.setCc(cc.toArray(new String[cc.size()]));
            helper.setSubject(subject);
            helper.setText(content, isContentHtml);
            helper.setFrom(mailSender.getUsername());

            if (CollectionUtils.isNotEmpty(attachments)) {
                for (File attachment : attachments) {
                    FileSystemResource file = new FileSystemResource(attachment);
                    String fileName = file.getFilename();
                    try {
                        fileName = MimeUtility.encodeText(fileName);
                    }catch (Exception e) {

                    }
                    helper.addAttachment(fileName, file);
                }
//            helper.addInline(rscId, file);
            }
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inlineImageWithCid() throws Exception {
//        String htmlContent = "<div style=\"background:url('https://college.dmallcdn.com/ann_year_2.jpg')no-repeat;height:500px;\"><span style='color:red;'>邮件内容</span></div>";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props);
        Message message = new MimeMessage(session);
        InternetAddress from = new InternetAddress("310510906@qq.com");
        from.setPersonal(MimeUtility.encodeText("佛系少年江流儿<310510906@qq.com>"));
        message.setFrom(from);
        /*抄送*/
        InternetAddress to = new InternetAddress("junyi.zeng@qq.com");
        message.setRecipient(Message.RecipientType.TO, to);
        message.setSubject(MimeUtility.encodeText("邮件主题！"));
        message.setSentDate(new Date());
        // 我就当这是一个消息包，类型是混杂的
        MimeMultipart msgMultipart = new MimeMultipart("mixed");// 指定为混合关系
        message.setContent(msgMultipart);
        //  显示图片必须为related，如果还需要添加附件必须为multi
        //  邮件内容
        MimeMultipart multipart = new MimeMultipart("related");
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<img src=\"cid:image\">" +
                "<img src=\"cid:image\">";
//        htmlText = "<div><img src=\"cid:image\"><span>来来来</span></div>" +
//                "<img src=\"cid:image\">";
        htmlText = "<div style='background:url(\"cid:image\")no-repeat;position:relative;'><img src=\"cid:image\" style='position:absolute;top:0;left:0;'><span style='position:absolute;top:0;left:0;'>来来来</span></div>" +
                "";
        //  必须明确指定字体为UTF-8，避免中文乱码
        messageBodyPart.setContent(htmlText, "text/html;charset=utf-8;");
        multipart.addBodyPart(messageBodyPart);
        //  添加图片
        MimeBodyPart imagePart = new MimeBodyPart();
        DataSource fds = new FileDataSource("/home/zjy/桌面/ann_year_2.jpg");
        DataSource fds1 = new FileDataSource("/home/zjy/桌面/ann_year_2.jpg");
        imagePart.setDataHandler(new DataHandler(fds));
        imagePart.setDataHandler(new DataHandler(fds1));
        //  设置ID
        imagePart.setHeader("Content-ID", "<image>");
        imagePart.setHeader("Content-Type", "image/jpg");
        imagePart.setDisposition(MimeBodyPart.INLINE);
        imagePart.setFileName("image.jpg");
        //  添加内容
        multipart.addBodyPart(imagePart);
        message.setContent(multipart);
        message.saveChanges();
        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.qq.com", 587, "310510906@qq.com", "qyjpwkjvixwicbbd");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        System.out.println("发送完毕");
    }
}
