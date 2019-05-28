/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;
import constant.UPath;
/**
 *
 * @author zhichengfu
 */
public class EmailThread extends Thread {

    public String to;
    public String info;
    public int option;

    public EmailThread(String to,String info, int option) {
        this.to = to;
        this.info=info;
        this.option=option;
    }

    @Override
    public void run() {
        try {
            EmailSender es = new EmailSender();
            if(option==1)// inviate registration
            {
            String domain=UPath.domain+"index.jsp?email="+to+"&info="+info;
            es.sendEmailToManagerByEmailInvitaion(to, domain);
            }
            else if(option==2) // get inviate code
            {
                String domain=info;
                es.sendEmailToManagerByEmailInvitaionCode(to, domain);
            }
           
        } catch (Exception e) {
        }
    }
}
