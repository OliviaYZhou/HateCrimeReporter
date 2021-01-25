package com.example.hatecrimereporter;//package com.myapp.android.model.service;


import android.os.AsyncTask;

import java.util.Calendar;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class SendMail{
    // public static final String MAIL_SERVER = "localhost";

    private String toList;
    private String ccList;
    private String bccList;
    private String subject;
//    final private static String SMTP_SERVER = DataService.getSetting(DataService.SETTING_SMTP_SERVER);
    private String from;
    private String txtBody;
    private String htmlBody;
    private String replyToList;
//    private ArrayList<Attachment> attachments;
    private boolean authenticationRequired = false;

    public SendMail(String from, String toList, String subject, String txtBody, String htmlBody)
    { //,Attachment attachment
        this.txtBody = txtBody;
        this.htmlBody = htmlBody;
        this.subject = subject;
        this.from = from;
        this.toList = toList;
        this.ccList = null;
        this.bccList = null;
        this.replyToList = null;
        this.authenticationRequired = true;

//        this.attachments = new ArrayList<Attachment>();
//        if (attachment != null) {
//            this.attachments.add(attachment);
//        }
    }
//
//    public SendMail(String from, String toList, String subject, String txtBody, String htmlBody
//                       ) {//ArrayList<Attachment> attachments
//        this.txtBody = txtBody;
//        this.htmlBody = htmlBody;
//        this.subject = subject;
//        this.from = from;
//        this.toList = toList;
//        this.ccList = null;
//        this.bccList = null;
//        this.replyToList = null;
//        this.authenticationRequired = true;
////        this.attachments = attachments == null ? new ArrayList<Attachment>()
////                : attachments;
//    }

    public void sendAuthenticated() throws AddressException, MessagingException {
        authenticationRequired = true;
        send();
    }

    /**
     * Send an e-mail
     *
     * @throws MessagingException
     * @throws AddressException
     */
    public void send() throws AddressException, MessagingException {
        Properties props = new Properties();

        // set the host smtp address
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");  // needed for gmail
        props.put("mail.smtp.auth", "true"); // needed for gmail
        props.put("mail.smtp.port", "587");  // gmail smtp port

        props.put("mail.user", from);

        /*Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("mobile@mydomain.com", "mypassword");
            }
        };*/

//        Session session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("throwawayreporteremail@gmail.com", "adorious");
//            }
//        })
        Session session;

        if (authenticationRequired) {
            Authenticator auth = new SMTPAuthenticator();
            props.put("mail.smtp.auth", "true");
            session = Session.getDefaultInstance(props, auth);
        } else {
            session = Session.getDefaultInstance(props, null);
        }

        // get the default session
        session.setDebug(true);

        // create message
        Message msg = new javax.mail.internet.MimeMessage(session);

        // set from and to address
        try {
            msg.setFrom(new InternetAddress(from, from));
            msg.setReplyTo(new InternetAddress[]{new InternetAddress(from, from)});
        } catch (Exception e) {
            msg.setFrom(new InternetAddress(from));
            msg.setReplyTo(new InternetAddress[]{new InternetAddress(from)});
        }

        // set send date
        msg.setSentDate(Calendar.getInstance().getTime());

        // parse the recipients TO address
        java.util.StringTokenizer st = new java.util.StringTokenizer(toList, ",");
        int numberOfRecipients = st.countTokens();

        javax.mail.internet.InternetAddress[] addressTo = new javax.mail.internet.InternetAddress[numberOfRecipients];

        int i = 0;
        while (st.hasMoreTokens()) {
            addressTo[i++] = new javax.mail.internet.InternetAddress(st
                    .nextToken());
        }
        msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);

        // parse the replyTo addresses
        if (replyToList != null && !"".equals(replyToList)) {
            st = new java.util.StringTokenizer(replyToList, ",");
            int numberOfReplyTos = st.countTokens();
            javax.mail.internet.InternetAddress[] addressReplyTo = new javax.mail.internet.InternetAddress[numberOfReplyTos];
            i = 0;
            while (st.hasMoreTokens()) {
                addressReplyTo[i++] = new javax.mail.internet.InternetAddress(
                        st.nextToken());
            }
            msg.setReplyTo(addressReplyTo);
        }

        // parse the recipients CC address
        if (ccList != null && !"".equals(ccList)) {
            st = new java.util.StringTokenizer(ccList, ",");
            int numberOfCCRecipients = st.countTokens();

            javax.mail.internet.InternetAddress[] addressCC = new javax.mail.internet.InternetAddress[numberOfCCRecipients];

            i = 0;
            while (st.hasMoreTokens()) {
                addressCC[i++] = new javax.mail.internet.InternetAddress(st
                        .nextToken());
            }

            msg.setRecipients(javax.mail.Message.RecipientType.CC, addressCC);
        }

        // parse the recipients BCC address
        if (bccList != null && !"".equals(bccList)) {
            st = new java.util.StringTokenizer(bccList, ",");
            int numberOfBCCRecipients = st.countTokens();

            javax.mail.internet.InternetAddress[] addressBCC = new javax.mail.internet.InternetAddress[numberOfBCCRecipients];

            i = 0;
            while (st.hasMoreTokens()) {
                addressBCC[i++] = new javax.mail.internet.InternetAddress(st
                        .nextToken());
            }

            msg.setRecipients(javax.mail.Message.RecipientType.BCC, addressBCC);
        }

        // set header
        msg.addHeader("X-Mailer", "MyAppMailer");
        msg.addHeader("Precedence", "bulk");
        // setting the subject and content type
        msg.setSubject(subject);

        Multipart mp = new MimeMultipart("related");

        // set body message
        MimeBodyPart bodyMsg = new MimeBodyPart();
        bodyMsg.setText(txtBody, "iso-8859-1");
//        if (attachments.size()>0) htmlBody = htmlBody.replaceAll("#filename#",attachments.get(0).getFilename());
//        if (htmlBody.indexOf("#header#")>=0) htmlBody = htmlBody.replaceAll("#header#",attachments.get(1).getFilename());
//        if (htmlBody.indexOf("#footer#")>=0) htmlBody = htmlBody.replaceAll("#footer#",attachments.get(2).getFilename());

        bodyMsg.setContent(htmlBody, "text/html");
        mp.addBodyPart(bodyMsg);

//        // set attachements if any
//        if (attachments != null && attachments.size() > 0) {
//            for (i = 0; i < attachments.size(); i++) {
//                Attachment a = attachments.get(i);
//                BodyPart att = new MimeBodyPart();
//                att.setDataHandler(new DataHandler(a.getDataSource()));
//                att.setFileName( a.getFilename() );
//                att.setHeader("Content-ID", "<" + a.getFilename() + ">");
//                mp.addBodyPart(att);
//            }
//        }
        msg.setContent(mp);

        // send it
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
        Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );
        try {
            javax.mail.Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * SimpleAuthenticator is used to do simple authentication when the SMTP
     * server requires it.
     */
    private static class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {

            String username = "throwawayreporteremail@gmail.com";
            String password = "strongpassword";

            return new PasswordAuthentication(username, password);
        }
    }

    public String getToList() {
        return toList;
    }

    public void setToList(String toList) {
        this.toList = toList;
    }

    public String getCcList() {
        return ccList;
    }

    public void setCcList(String ccList) {
        this.ccList = ccList;
    }

    public String getBccList() {
        return bccList;
    }

    public void setBccList(String bccList) {
        this.bccList = bccList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTxtBody(String body) {
        this.txtBody = body;
    }

    public void setHtmlBody(String body) {
        this.htmlBody = body;
    }

    public String getReplyToList() {
        return replyToList;
    }

    public void setReplyToList(String replyToList) {
        this.replyToList = replyToList;
    }

    public boolean isAuthenticationRequired() {
        return authenticationRequired;
    }

    public void setAuthenticationRequired(boolean authenticationRequired) {
        this.authenticationRequired = authenticationRequired;
    }

}

//String message = "s";
//String string = "m";
//public class A extends AsyncTask<message, string, string>