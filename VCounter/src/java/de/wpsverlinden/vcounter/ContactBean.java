/**
 *
 * VCounter - Der Besucherzähler ohne JavaScript!
 * Copyright (C) 2013 Oliver Verlinden (http://wps-verlinden.de)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */

package de.wpsverlinden.vcounter;

import com.sun.mail.smtp.SMTPMessage;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

@ManagedBean
@ViewScoped
public class ContactBean {

    private String name;
    private String email;
    private String subject;
    private String message;
    private String result;
    private boolean disableSubmit = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isDisableSubmit() {
        return disableSubmit;
    }

    public void setDisableSubmit(boolean disableSubmit) {
        this.disableSubmit = disableSubmit;
    }
    
    

    public String submit() {
        
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "localhost");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");

        Session mailSession = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("web2", "a23aq324es");
                    }
                });
        Message msg = new SMTPMessage(mailSession);
        try {
            msg.setSubject(getSubject());
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress("info@vcounter.de"));
            msg.setFrom(new InternetAddress(getName() + "<" + getEmail() + ">"));
            msg.setText(getMessage());
            msg.saveChanges();
            Transport.send(msg);
            result = "Deine Nachricht erfolgreich versendet.";
        } catch (MessagingException e) {
            result = "Das Senden der Nachricht ist fehlgeschlagen. Bitte sende deine Anfrage direkt per Email an info@vcounter.de.";
        }

        disableSubmit = true;
        return "";
    }
}
