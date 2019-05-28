/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author zhichengfu
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author zhichengfu
 */
public class EmailSender {

    public static String ourEmail = "";

    public static Session session = null;
    //String to = "fuz@uwm.edu";
//    public static String from = "lovebaby.w@gmail.com";
//    public static String password = "NEWlife84185543";

    public static String from = "fuzhuzhuiss@gmail.com";
    public static String password = "Daodao!99";
    public static String TaskSubject = "Shangping Ren's Group";
    String toRestaurant = "";
    String toCustomer = "";

    public static void initSession(final String username, final String password) {
        if (session == null) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
        }
    }

    public static MimeMessage composeEmail(String from, String to, String subject, String htmlContent, boolean hasImage, boolean hasFax) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //message.setSubject(subject);
            if (!hasImage) {
                if (hasFax) {
//                    MimeMultipart multipart = new MimeMultipart("related");
//                    MimeBodyPart attachPart = new MimeBodyPart();
//                    //String attachFile = "/Users/zhichengfu/Desktop/orderTest.html";
//                    String attachFile = "/Users/zhichengfu/Desktop/orderTest.html";
//                    attachPart.attachFile(attachFile);
//                    multipart.addBodyPart(attachPart);
//                    message.setContent(multipart);

                } else {
                    message.setSubject(subject);
                    message.setContent(htmlContent, "text/html;charset=utf-8");
                }

                //message.setContent(htmlContent, "text/html");
            } else {
                message.setSubject(subject);
                message.setContent(htmlContent, "text/html;charset=utf-8");
                MimeMultipart multipart = new MimeMultipart("related");
                // first part (the html)
                BodyPart messageBodyPart = new MimeBodyPart();
//                 mbp.setContent(message,"text/html;charset=UTF-8");
                //String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
                messageBodyPart.setContent(htmlContent, "text/html;charset=UTF-8");
                

                // add it
                multipart.addBodyPart(messageBodyPart);

                // second part (the image)
                messageBodyPart = new MimeBodyPart();
                //DataSource fds = new FileDataSource(Paths.emailLogo);

                //messageBodyPart.setDataHandler(new DataHandler(fds));
                //messageBodyPart.setHeader("Content-ID", "<image>");
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);
            }

            return message;
        } catch (MessagingException e) {
            System.out.println("Compose email failed");
            throw new RuntimeException(e);

        }

    }

    public static void sendEmail(MimeMessage message) {
        try {
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException e) {
            System.out.println("Send email failed: error message: "+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendEmailToManagerByEmailInvitaion(String to,String link) {
        initSession(from, password);
        String content = "Hi " + ",<br/><br/> 欢迎注册Utopia 系统:<br/> " + link 
                + "<br/>请点击此链接开始注册<br/>"
                + "现在开始加入我们!<br/>"
                + "Utopia 团队";
        MimeMessage message = composeEmail(from, to, TaskSubject, content, false, false);
        sendEmail(message);
    }
    
    public void sendEmailToManagerByEmailInvitaionCode(String to,String code) {
        initSession(from, password);
        String content = "Hi " + ",<br/><br/> Welcome to register Recall :<br/> Your code is ：" + code 
                + "<br/>Plese use the code to finish the registration<br/>"
                + "Thanks!<br/>"
                + "Shangping Ren's Group";
        MimeMessage message = composeEmail(from, to, TaskSubject, content, false, false);
        sendEmail(message);
    }

    
    
    public void sendFileLinkToUserByEmail(String to_email, String uname, String fname, String link, String file_password) {
        initSession(from, password);
        String content = "亲爱的的用户你好，" + ",<br/><br/>用户"+uname+"向你分享了文件/文件夹 "+fname+"， 请通过以下链接进行访问:<br/>点击此链接：<a href='"+link+"'>" + link+"</a>";
        if(file_password!=null&&!file_password.equals(""))
        {
        	content+="<br/>访问密码："+file_password+"<br/>"
                    + "uDrive 团队";
        }
        else
        {
        	content+="uDrive 团队";
        }
        MimeMessage message = composeEmail(from, to_email, TaskSubject, content, false, false);
        sendEmail(message);
    }
    
    public void sendEmailReportByEmail(String to, String problem, String content) {
        String from1 = "service@asianfoodmap.com";
        String password1 = "service1982";
        initSession(from1, password1);
        MimeMessage message = composeEmail(from1, to, problem, content, false, false);
        sendEmail(message);
    }

//    public void sendEmailToRegistration(User user) {
//        String from1 = "service@asianfoodmap.com";
//        String password1 = "service1982";
//        initSession(from1, password1);
//        String content = "Hi " + user.userName + ",<br/><br/> Welcome to AsianFoodMap, your username is: " + user.userName + ", and password is: " + user.password
//                + "<br/>We help you to find all great and healthy Asian foods around you.<br/>"
//                + "Start to order your healthy food by clicking www.asianfoodmap.com now!<br/>"
//                + "Asianfoodmap Team";
//        MimeMessage message = composeEmail(from1, user.email, "Registration Confirmation From AsianFoodMap", content, false, false);
//        sendEmail(message);
//    }
//
//    public void sendEmailToRestaurantByFax(String to, Orders order, DeliverAddress da, CardInformation card) {
//        initSession(from, password);
//        this.toRestaurant = this.generateHTMLBodyForRestaurantRevisedFax(order, da, card);
//        //System.out.println(this.toRestaurant);
//        TestSQL.generateHTML(this.toRestaurant);
//        MimeMessage message = composeEmail(from, to, TaskSubject, toRestaurant, false, true);
//        //message.get
//        sendEmail(message);
//    }
//
//    public void sendEmailToCustomer(String to, Orders order, DeliverAddress da, CardInformation card) {
//        initSession(from, password);
//        this.toCustomer = this.generateHTMLBodyForCustomer(order, da, card);
//        //System.out.println(this.toCustomer);
//        MimeMessage message = composeEmail(from, to, TaskSubject + order.orderID, this.toCustomer, true, false);
//        sendEmail(message);
//    }
//
//    public String generateHTMLBodyForRestaurant(Orders order, DeliverAddress da, CardInformation card) {
//        Restaurant rest = null;
//        try {
//            rest = Restaurant.getRestaurantsByID(order.restID);
//        } catch (SQLException ex) {
//            Logger.getLogger(SendEmailOrder.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        LinkedList<OrderFoodComment> orderComments = null;
//        LinkedList<OrderFoodSide> orderSides = null;
//        if (order.foodComments != null) {
//            orderComments = (LinkedList<OrderFoodComment>) order.foodComments.clone();
//        }
//        if (order.foodSides != null) {
//            orderSides = (LinkedList<OrderFoodSide>) order.foodSides.clone();
//        }
//
//        String body = "<html><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
//                + "</head><body style=\"text-align:center;width:800px;\">";
//        body += "<div class=\"block\" style=\"margin-top:20px;display:inline-block;width:98%;\">\n"
//                + "           <table style=\"width:100%\">\n"
//                + "                <tr><td style=\"font-size: 20px;font-weight: bold; text-align: left;width:300px;\"><label style=\"background-color:lightgray;\">&nbsp;Restaurant Receipt&nbsp;</label></td>\n"
//                + "                    <td style=\"font-size: 44px;font-weight: bold; text-align: left\">";
//        if (order.orderType == 1) {
//            body += "DELIVERY";
//        } else {
//            body += "PICKUP";
//        }
//        body += "</td>";
//        if (order.orderNow == 2) {
//            body += "<td style=\"font-size: 28px;font-weight: bold;text-align: right;\">Future Order</td>";
//        }
//        body += "\n</tr>\n</table></div>\n"
//                + "<div class=\"block\" style=\"margin-top:10px;display:inline-block;width:98%;\">\n"
//                + "            <table style=\"width:100%;\">\n"
//                + "                <tr>\n"
//                + "            <td style=\"font-size: 18px; text-align: left\">Order #&nbsp;&nbsp;" + order.orderID + "</td>\n"
//                + "\n"
//                + "            \n"
//                + "            <td style=\"font-size: 18px;  text-align: right\">Order Time:&nbsp;&nbsp;" + InstanceClass.getDateOnly(order.orderTime) + "&nbsp;<font style=\"font-size: 24px;font-weight: bold;\">" + InstanceClass.changeTimeToUS(order.orderTime) + "</font></td>\n"
//                + "           \n"
//                + "                </tr>\n"
//                + "            </table>\n"
//                + "        </div>\n"
//                + "        <div class=\"block\" style=\"display:inline-block;width:98%; margin-top:0px;\">\n"
//                + "            <table style=\"width:100%;border-top:2px solid;border-bottom:1px solid;\">\n"
//                + "                <tr>\n"
//                + "            <td style=\"font-size:14px;font-weight: bold;width:70px;\">Restaurant:</td>\n"
//                + "            <td style=\"font-size:14px;text-align:left\">" + order.restName + " / " + rest.address + " " + rest.city + " " + rest.state + " " + rest.zipcode + " / " + InstanceClass.getFormattedPhoneNumber(rest.phone) + "</td>\n"
//                + "                </tr>\n"
//                + "                </table>\n"
//                + "        </div>";
//        body += "        <div class=\"block\" style=\"display:inline-block;width:98%;\">\n"
//                + "            <table class=\"tableUser\" style=\"font-size:20px;font-weight: bold;width:100%;margin-top:5px;margin-bottom: 5px;text-align: center;\">\n"
//                + "                <tr>\n"
//                + "                <td style=\"font-weight:bold\">" + order.userName + "<br>\n" + da.email + "\n"
//                + "                </td>\n"
//                + "                <td style=\"font-weight:bold\">\n" + da.address + "<br>\n" + da.city + ", " + da.state + ". " + da.zipcode + " \n"
//                + "                </td>\n"
//                + "                <td style=\"font-weight:bold\">\n" + InstanceClass.getFormattedPhoneNumber(da.phone) + "\n"
//                + "                </td>\n"
//                + "                </tr>\n"
//                + "            </table>\n"
//                + "        </div>\n"
//                + "        <div class=\"block\" style=\"display:inline-block;width:98%;\">\n"
//                + "<table style=\"width:100%;border-top:1px solid;border-bottom:1px solid;\">\n"
//                + "                <tr>\n";
//        if (card == null) {
//            body += "<td style=\"font-size:14px; font-weight: bold;text-align:left;\">Payment:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cash</td>";
//        } else {
//            body += "            <td style=\"font-size:14px; font-weight: bold;text-align:left;\">Payment&nbsp;&nbsp;" + card.cardNumber + "</td>\n"
//                    + "       <td style=\"font-size:14px; font-weight: bold;text-align:right\">Exp:&nbsp;" + card.exp + "&nbsp;&nbsp;&nbsp;&nbsp;CCV:&nbsp;" + card.ccv + "</td>  \n";
//        }
//        body += "                </tr>\n"
//                + "            </table>"
//                + "        </div>\n"
//                + "        <div class=\"block\" style=\"margin-top:5px;display:inline-block;width:98%;\">\n"
//                + "            <table style=\"width:100%;border-bottom:2px solid;\"><tr>\n"
//                + "            <td style=\"text-align: left;font-size:14px;font-weight: bold;background-color: lightgray;width:90px;height:18px;\">Order Note:&nbsp;</td>\n"
//                + "            <td style=\"font-size:14px; text-align: left\">" + order.orderComment + "</td>\n"
//                + "                </tr>\n"
//                + "            </table>\n"
//                + "        </div>";
//        body += " <div class=\"block\" style=\"text-align: center;margin-top:5px;display:inline-block;width:98%;\">\n"
//                + "            <label style=\"font-size:24px;font-weight: bold\">Order Details</label>\n"
//                + "            <table class=\"tableOrder\" style=\"width:100%;text-align:center;font-size:14px;margin-top:10px;border-top: 1px solid;border-collapse: collapse;\">\n"
//                + "                <tr>\n"
//                + "                <th style=\"background-color: lightgray; border-bottom: 2px solid;\">Category</th><th style=\"background-color: lightgray; border-bottom: 2px solid;\">Qty</th>"
//                + "<th style=\"background-color: lightgray; border-bottom: 2px solid;\">Item</th><th style=\"background-color: lightgray; border-bottom: 2px solid;\">Side Choice</th>"
//                + "<th style=\"background-color: lightgray; border-bottom: 2px solid;text-align:left;\">Price</th><th style=\"background-color: lightgray; border-bottom: 2px solid;text-align:center;\">Notes</th>\n"
//                + "                </tr>";
//        int tempIndex = 0;
//        LinkedList<CategoryOrderFood> cofs = CategoryOrderFood.getFoodOrder(order);
//        //LinkedList<CategoryOrderFood> cofs1=(LinkedList<CategoryOrderFood>) cofs.clone();
//        for (CategoryOrderFood cof : cofs) {
//
//            for (int i = 0; i < cof.fs.size(); i++) {
//                TempFood tf = cof.fs.get(i);
//                if (tempIndex != cofs.size() - 1) {
//                    if (i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 1px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 1px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 1px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 1px solid;text-align:center\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>\n";
//                    } else {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 1px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 1px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 1px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>";
//                    }
//                } else {
//                    if (i == cof.fs.size() - 1 && i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 2px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 2px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 2px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 2px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 2px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>\n";
//
//                    } else if (i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 2px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 1px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 1px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 1px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>\n";
//                    } else if (i == cof.fs.size() - 1) {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 2px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 2px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 2px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 2px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 2px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>";
//                    } else {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 1px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 1px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 1px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>";
//                    }
//                }
//            }
//
//            tempIndex++;
//
//        }
//
//        body += "<tr style=\"text-align:right\">\n"
//                + "                <td colspan=\"6\" >\n"
//                + "                    <table class=\"priceSummary\" style=\"float:right;margin-right:30px;\">\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Subtotal:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none;\">$" + order.subTotal + "</td>\n"
//                + "                        </tr>\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Coupon:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none;\">$-" + order.coupon + "</td>\n"
//                + "                        </tr>\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Tax(6.1%):</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none;\">$" + order.tax + "</td>\n"
//                + "                        </tr>\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">DELIVERY Fee:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none\">$" + order.dfee + "</td>\n"
//                + "                        </tr>\n"
//                + "                <tr><td style=\"border:none;text-align: right;\">Processing Fee:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none\">$" + order.processFee + "</td>\n"
//                + "                </tr>"
//                + "                <tr>\n<td style=\"border:none;font-weight:bold;text-align: right;\">Before Tips:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;font-weight:bold;border:none\">$" + order.beforeTip + "</td>\n"
//                + "                </tr>"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Tip:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none\">$" + order.tips + "</td>\n"
//                + "                    </tr>"
//                + "<tr></tr><tr></tr><tr></tr>"
//                + "<tr><td style=\"border:none;font-weight: bold;text-align: right;\">Total:</td><td style=\"border:none;font-weight: bold;text-align: left;\"></td><td style=\"text-align:left;font-weight: bold;border:none\">$" + order.totalPrice + "</td></tr>"
//                + "\n"
//                + "            </table>  \n"
//                + "        </td>\n"
//                + "        </tr>\n"
//                + "    </table>\n"
//                + "</div><hr style=\"margin-top:-28px;border:none;width:98%;height: 1px;background-color: black;\">";
//        body += "<div class=\"block\" style=\"margin-top:50px;display:inline-block;width:98%;\"><table style=\"width:100%\">\n"
//                + "    <tr><td style=\"font-size:20px;font-weight: bold;text-align:left\">To Chef</td>\n"
//                + "    <td style=\"font-size:18px;text-align:right;\">Order#:&nbsp;&nbsp;" + order.orderID + "</td></tr></table>\n"
//                + "            <table class=\"tableOrder\" style=\"width:100%;text-align:center;font-size:14px;margin-top: 10px;border-top: 1px solid;border-collapse: collapse;\">\n"
//                + "                <tr>\n"
//                + "                <th style=\"background-color: lightgray; border-bottom: 2px solid;\">Category</th><th style=\"background-color: lightgray; border-bottom: 2px solid;\">Qty</th>"
//                + "<th style=\"background-color: lightgray; border-bottom: 2px solid;\">Item</th><th style=\"background-color: lightgray; border-bottom: 2px solid;\">Side Choice</th>"
//                + "<th style=\"background-color: lightgray; border-bottom: 2px solid;text-align:left;\">Price</th><th style=\"background-color: lightgray; border-bottom: 2px solid;text-align:center;\">Notes</th>\n"
//                + "                </tr>";
//        tempIndex = 0;
//        order.foodComments = orderComments;
//        order.foodSides = orderSides;
//        for (CategoryOrderFood cof : cofs) {
//
//            for (int i = 0; i < cof.fs.size(); i++) {
//                TempFood tf = cof.fs.get(i);
//                if (tempIndex != cofs.size() - 1) {
//                    if (i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 1px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 1px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 1px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 1px solid;text-align:center\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>\n";
//                    } else {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 1px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 1px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 1px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>";
//                    }
//                } else {
//                    if (i == cof.fs.size() - 1 && i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 2px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 2px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 2px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 2px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 2px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>\n";
//
//                    } else if (i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 2px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 1px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 1px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 1px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>\n";
//                    } else if (i == cof.fs.size() - 1) {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 2px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 2px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 2px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 2px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 2px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>";
//                    } else {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 1px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 1px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 1px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>";
//                    }
//                }
//            }
//
//            tempIndex++;
//
//        }
//
//        body += "</table>\n</div>\n<br/><br/><br/>\n</body>\n</html>";
//        return body;
//    }
//
//    public String generateHTMLBodyForCustomer(Orders order, DeliverAddress da, CardInformation card) {
//
//        Restaurant rest = null;
//        try {
//            rest = Restaurant.getRestaurantsByID(order.restID);
//        } catch (SQLException ex) {
//            Logger.getLogger(SendEmailOrder.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        String body = "<html><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
//                + "</head><body style=\"text-align:center;width:800px;\">";
//        body += "<div class=\"block\" style=\"margin-bottom:4px;display:inline-block;width:98%;\"><img src=\"cid:image\"></div>"
//                + "<div class=\"block\" style=\"text-align:left;margin-top:0px;display:inline-block;width:98%;\">\n"
//                + "Thank you for ordering from AsianFoodMap.com.<br/>\n"
//                + "In case of any change, cancellation or unsatisfied with the food or delivery service, please contact us directly " + rest.phone + ".<br/>"
//                + "For any other questions, please send email to customer@asinafoodmap.com.</div>";
//        body += "<div class=\"block\" style=\"margin-top:20px;display:inline-block;width:98%;\">\n"
//                + "           <table style=\"width:100%\">\n"
//                + "                <tr><td style=\"font-size: 20px;font-weight: bold; text-align: left;width:300px;\"><label style=\"background-color:lightgray;\">&nbsp;Customer Receipt&nbsp;</label></td>\n"
//                + "                    <td style=\"font-size: 44px;font-weight: bold; text-align: left\">";
//        if (order.orderType == 1) {
//            body += "DELIVERY";
//        } else {
//            body += "PICKUP";
//        }
//        body += "</td>";
//        if (order.orderNow == 2) {
//            body += "<td style=\"font-size: 28px;font-weight: bold;text-align: right;\">Future Order</td>";
//        }
//        body += "\n</tr>\n</table></div>\n"
//                + "<div class=\"block\" style=\"margin-top:10px;display:inline-block;width:98%;\">\n"
//                + "            <table style=\"width:100%;\">\n"
//                + "                <tr>\n"
//                + "            <td style=\"font-size: 18px; text-align: left\">Order #&nbsp;&nbsp;" + order.orderID + "</td>\n"
//                + "\n"
//                + "            \n"
//                + "            <td style=\"font-size: 18px;  text-align: right\">Order Time:&nbsp;&nbsp;" + InstanceClass.getDateOnly(order.orderTime) + "&nbsp;<font style=\"font-size: 24px;font-weight: bold;\">" + InstanceClass.changeTimeToUS(order.orderTime) + "</font></td>\n"
//                + "           \n"
//                + "                </tr>\n"
//                + "            </table>\n"
//                + "        </div>\n"
//                + "        <div class=\"block\" style=\"display:inline-block;width:98%; margin-top:0px;\">\n"
//                + "            <table style=\"width:100%;border-top:2px solid;border-bottom:1px solid;\">\n"
//                + "                <tr>\n"
//                + "            <td style=\"font-size:14px;font-weight: bold;width:70px;\">Restaurant:</td>\n"
//                + "            <td style=\"font-size:14px;text-align:left\">" + order.restName + " / " + rest.address + " " + rest.city + " " + rest.state + " " + rest.zipcode + " / " + InstanceClass.getFormattedPhoneNumber(rest.phone) + "</td>\n"
//                + "                </tr>\n"
//                + "                </table>\n"
//                + "        </div>";
//        body += "        <div class=\"block\" style=\"display:inline-block;width:98%;\">\n"
//                + "            <table class=\"tableUser\" style=\"font-size:20px;font-weight: bold;width:100%;margin-top:5px;margin-bottom: 5px;text-align: center;\">\n"
//                + "                <tr>\n"
//                + "                <td style=\"font-weight:bold\">" + order.userName + "<br>\n" + da.email + "\n"
//                + "                </td>\n"
//                + "                <td style=\"font-weight:bold\">\n" + da.address + "<br>\n" + da.city + ", " + da.state + ". " + da.zipcode + " \n"
//                + "                </td>\n"
//                + "                <td style=\"font-weight:bold\">\n" + InstanceClass.getFormattedPhoneNumber(da.phone) + "\n"
//                + "                </td>\n"
//                + "                </tr>\n"
//                + "            </table>\n"
//                + "        </div>\n"
//                + "        <div class=\"block\" style=\"display:inline-block;width:98%;\">\n"
//                + "<table style=\"width:100%;border-top:1px solid;border-bottom:1px solid;\">\n"
//                + "                <tr>\n";
//        if (card == null) {
//            body += "<td style=\"font-size:14px; font-weight: bold;text-align:left;\">Payment:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cash</td>";
//        } else {
//            body += "            <td style=\"font-size:14px; font-weight: bold;text-align:left;\">Payment&nbsp;&nbsp;XXXXXXXXXXXX" + card.cardNumber.substring(12) + "</td>\n"
//                    + "       <td style=\"font-size:14px; font-weight: bold;text-align:right\">Exp:&nbsp;XX/XXXX" + "&nbsp;&nbsp;&nbsp;&nbsp;CCV:&nbsp;XXX" + "</td>  \n";
//        }
//        body += "                </tr>\n"
//                + "            </table>"
//                + "        </div>\n"
//                + "        <div class=\"block\" style=\"margin-top:5px;display:inline-block;width:98%;\">\n"
//                + "            <table style=\"width:100%;border-bottom:2px solid;\"><tr>\n"
//                + "            <td style=\"text-align: left;font-size:14px;font-weight: bold;background-color: lightgray;width:90px;height:18px;\">Order Note:&nbsp;</td>\n"
//                + "            <td style=\"font-size:14px; text-align: left\">" + order.orderComment + "</td>\n"
//                + "                </tr>\n"
//                + "            </table>\n"
//                + "        </div>";
//        body += " <div class=\"block\" style=\"text-align: center;margin-top:5px;display:inline-block;width:98%;\">\n"
//                + "            <label style=\"font-size:24px;font-weight: bold\">Order Details</label>\n"
//                + "            <table class=\"tableOrder\" style=\"width:100%;text-align:center;font-size:14px;margin-top:10px;border-top: 1px solid;border-collapse: collapse;\">\n"
//                + "                <tr>\n"
//                + "                <th style=\"background-color: lightgray; border-bottom: 2px solid;\">Category</th><th style=\"background-color: lightgray; border-bottom: 2px solid;\">Qty</th>"
//                + "<th style=\"background-color: lightgray; border-bottom: 2px solid;\">Item</th><th style=\"background-color: lightgray; border-bottom: 2px solid;\">Side Choice</th>"
//                + "<th style=\"background-color: lightgray; border-bottom: 2px solid;text-align:left;\">Price</th><th style=\"background-color: lightgray; border-bottom: 2px solid;text-align:center;\">Notes</th>\n"
//                + "                </tr>";
//        int tempIndex = 0;
//        LinkedList<CategoryOrderFood> cofs = CategoryOrderFood.getFoodOrder(order);
//        //LinkedList<CategoryOrderFood> cofs1=(LinkedList<CategoryOrderFood>) cofs.clone();
//        for (CategoryOrderFood cof : cofs) {
//
//            for (int i = 0; i < cof.fs.size(); i++) {
//                TempFood tf = cof.fs.get(i);
//                if (tempIndex != cofs.size() - 1) {
//                    if (i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 1px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 1px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 1px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 1px solid;text-align:center\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>\n";
//                    } else {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 1px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 1px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 1px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>";
//                    }
//                } else {
//                    if (i == cof.fs.size() - 1 && i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 2px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 2px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 2px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 2px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 2px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>\n";
//
//                    } else if (i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 2px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 1px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 1px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 1px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>\n";
//                    } else if (i == cof.fs.size() - 1) {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 2px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 2px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 2px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 2px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 2px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>";
//                    } else {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 1px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 1px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\">" + InstanceClass.getOrderSides(order.foodSides, tf.ID) + "</td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 1px solid;text-align:center;\">" + InstanceClass.getOrderComments(order.foodComments, tf.ID) + "</td>\n</tr>";
//                    }
//                }
//            }
//
//            tempIndex++;
//
//        }
//        body += "<tr style=\"text-align:right\">\n"
//                + "                <td colspan=\"6\" >\n"
//                + "                    <table class=\"priceSummary\" style=\"float:right;margin-right:30px;\">\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Subtotal:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none;\">$" + order.subTotal + "</td>\n"
//                + "                        </tr>\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Coupon:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none;\">$-" + order.coupon + "</td>\n"
//                + "                        </tr>\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Tax:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none;\">$" + order.tax + "</td>\n"
//                + "                        </tr>\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">DELIVERY Fee:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none\">$" + order.dfee + "</td>\n"
//                + "                        </tr>\n"
//                + "                <tr><td style=\"border:none;text-align: right;\">Processing Fee:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none\">$" + order.processFee + "</td>\n"
//                + "                </tr>"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Tip:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none\">$" + order.tips + "</td>\n"
//                + "                    </tr>"
//                + "<tr></tr><tr></tr><tr></tr>"
//                + "<tr><td style=\"border:none;font-weight: bold;text-align: right;\">Total:</td><td style=\"border:none;font-weight: bold;text-align: left;\"></td><td style=\"text-align:left;font-weight: bold;border:none\">$" + order.totalPrice + "</td></tr>"
//                + "\n"
//                + "            </table>  \n"
//                + "        </td>\n"
//                + "        </tr>\n"
//                + "</table></div><hr style=\"margin-top:-28px;border:none;width:98%;height: 1px;background-color: black;\">";
//        body += "<br/><br/><br/></body>\n</html>";
//        return body;
//    }
//
//    public String generateHTMLBodyForRestaurantRevisedFax(Orders order, DeliverAddress da, CardInformation card) {
//        Restaurant rest = null;
//        try {
//            rest = Restaurant.getRestaurantsByID(order.restID);
//        } catch (SQLException ex) {
//            Logger.getLogger(SendEmailOrder.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        String body = "<html><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
//                + "</head><body style=\"text-align:center;width:1200px;\">";
//        body += "<div class=\"block\" style=\"margin-top:40px;display:inline-block;width:98%;\">\n"
//                + "           <table style=\"width:100%\">\n"
//                + "                <tr><td style=\"font-size: 40px;font-weight: bold;text-align: left\"><label style=\"background-color:lightgray;\">&nbsp;Restaurant Receipt&nbsp;</label></td>\n"
//                + "                    <td style=\"font-size: 60px;font-weight: bold; text-align: left\">";
//        if (order.orderType == 1) {
//            body += "DELIVERY";
//        } else {
//            body += "PICKUP";
//        }
//        body += "</td><td style=\"font-size: 28px;text-align: left;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>\n</tr>\n</table></div>\n"
//                + "<div class=\"block\" style=\"margin-top:10px;display:inline-block;width:98%;\">\n"
//                + "            <table style=\"width:100%\">\n"
//                + "                <tr>\n"
//                + "            <td style=\"font-size: 40px; text-align: left\">Order #&nbsp;&nbsp;" + order.orderID + "</td>\n"
//                + "\n"
//                + "            \n"
//                + "            <td style=\"font-size: 40px;  text-align: right\">Order Time:&nbsp;&nbsp;" + InstanceClass.getDateOnly(order.orderTime) + "&nbsp;<font style=\"font-size: 24px;font-weight: bold;\">" + InstanceClass.changeTimeToUS(order.orderTime) + "</font></td>\n"
//                + "           \n"
//                + "                </tr>\n"
//                + "            </table>\n"
//                + "        </div>\n"
//                + "        <div class=\"block\" style=\"display:inline-block;width:98%; margin-top:0px;\">\n"
//                + "            <table style=\"width:100%;border-collapse:collapse;\">\n"
//                + "                <tr>\n"
//                + "            <td style=\"font-size:30px;border-top:2px solid;border-bottom:1px solid;font-weight: bold;width:70px;\">Restaurant:</td>\n"
//                + "            <td style=\"font-size:30px;text-align:left;border-bottom:1px solid;border-top:2px solid ; \">" + order.restName + " / " + rest.address + " " + rest.city + " " + rest.state + " " + rest.zipcode + " / " + InstanceClass.getFormattedPhoneNumber(rest.phone) + "</td>\n"
//                + "                </tr>\n"
//                + "                </table>\n"
//                + "        </div>";
//        body += "        <div class=\"block\" style=\"display:inline-block;width:98%;\">\n"
//                + "            <table class=\"tableUser\" style=\"font-size:40px;font-weight: bold;width:100%;margin-top:5px;margin-bottom: 5px;text-align: center;\">\n"
//                + "                <tr>\n"
//                + "                <td style=\"font-weight:bold;\">" + order.userName + "<br>\n" + da.email + "\n"
//                + "                </td>\n"
//                + "                <td style=\"font-weight:bold\">\n" + da.address + "<br>\n" + da.city + ", " + da.state + ". " + da.zipcode + " \n"
//                + "                </td>\n"
//                + "                <td style=\"font-weight:bold\">\n" + InstanceClass.getFormattedPhoneNumber(da.phone) + "\n"
//                + "                </td>\n"
//                + "                </tr>\n"
//                + "            </table>\n"
//                + "        </div>\n"
//                + "        <div class=\"block\" style=\"display:inline-block;width:98%;\">\n"
//                + "<table style=\"width:100%;border-collapse: collapse;\">\n"
//                + "                <tr>\n"
//                + "            <td style=\"font-size:30px; font-weight: bold;border-bottom:1px solid;border-top:1px solid;text-align:left;\">Payment&nbsp;&nbsp;" + card.cardNumber + "</td>\n"
//                + "       <td style=\"font-size:30px; font-weight: bold;border-bottom:1px solid;border-top:1px solid;text-align:right\">Exp:&nbsp;" + card.exp + "&nbsp;&nbsp;&nbsp;&nbsp;CCV:&nbsp;" + card.ccv + "</td>  \n"
//                + "                </tr>\n"
//                + "            </table>"
//                + "        </div>\n"
//                + "        <div class=\"block\" style=\"margin-top:5px;display:inline-block;width:98%;\">\n"
//                + "            <table style=\"width:100%;border-collapse: collapse;\"><tr>\n"
//                + "            <td style=\"text-align: left;font-size:30px;font-weight: bold;border-bottom:2px solid;background-color: lightgray;width:90px;height:40px;\">Order Note:&nbsp;</td>\n"
//                + "            <td style=\"font-size:30px; text-align: left;border-bottom:2px solid; \">" + order.orderComment + "</td>\n"
//                + "                </tr>\n"
//                + "            </table>\n"
//                + "        </div>";
//        body += " <div class=\"block\" style=\"text-align: center;margin-top:5px;display:inline-block;width:98%;\">\n"
//                + "            <label style=\"font-size:40px;font-weight: bold\">Order Details</label>\n"
//                + "            <table class=\"tableOrder\" style=\"width:100%;text-align:center;font-size:30px;margin-top:10px;border-top: 1px solid;border-collapse: collapse;\">\n"
//                + "                <tr>\n"
//                + "                <th style=\"background-color: lightgray; border-bottom: 2px solid;\">Category</th><th style=\"background-color: lightgray; border-bottom: 2px solid;\">Qty</th>"
//                + "<th style=\"background-color: lightgray; border-bottom: 2px solid;\">Item</th><th style=\"background-color: lightgray; border-bottom: 2px solid;\">Side Choice</th>"
//                + "<th style=\"background-color: lightgray; border-bottom: 2px solid;text-align:left;\">Price</th><th style=\"background-color: lightgray; border-bottom: 2px solid;text-align:left;\">Instructions</th>\n"
//                + "                </tr>";
//        int tempIndex = 0;
//        LinkedList<CategoryOrderFood> cofs = CategoryOrderFood.getFoodOrder(order);
//        for (CategoryOrderFood cof : cofs) {
//
//            for (int i = 0; i < cof.fs.size(); i++) {
//                TempFood tf = cof.fs.get(i);
//                if (tempIndex != cofs.size() - 1) {
//                    if (i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 1px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 1px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 1px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\"></td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 1px solid;\"></td>\n</tr>\n";
//                    } else {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 1px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 1px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\"></td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 1px solid;text-align:left;\"></td>\n</tr>";
//                    }
//                } else {
//                    if (i == cof.fs.size() - 1 && i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 2px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 2px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 2px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 2px solid;\"></td>\n<td style=\"border-bottom: 2px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 2px solid;\"></td>\n</tr>\n";
//
//                    } else if (i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 1px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 1px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 1px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\"></td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 1px solid;\"></td>\n</tr>\n";
//                    } else if (i == cof.fs.size() - 1) {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 2px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 2px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 2px solid;\"></td>\n<td style=\"border-bottom: 2px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 2px solid;text-align:left;\"></td>\n</tr>";
//                    } else {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 1px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 1px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\"></td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 1px solid;text-align:left;\"></td>\n</tr>";
//                    }
//                }
//            }
//
//            tempIndex++;
//
//        }
//
//        body += "<tr style=\"text-align:right\">\n"
//                + "                <td colspan=\"6\" >\n"
//                + "                    <table class=\"priceSummary\" style=\"float:right;margin-right:30px;\">\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Subtotal:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none;\">$" + order.subTotal + "</td>\n"
//                + "                        </tr>\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Coupon:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none;\">$-" + order.coupon + "</td>\n"
//                + "                        </tr>\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Tax:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none;\">$" + order.tax + "</td>\n"
//                + "                        </tr>\n"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">DELIVERY Fee:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none\">$" + order.dfee + "</td>\n"
//                + "                        </tr>\n"
//                + "                <tr><td style=\"border:none;text-align: right;\">Processing Fee:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none\">$" + order.processFee + "</td>\n"
//                + "                </tr>"
//                + "                <tr><td style=\"border:none;font-weight:bold; text-align: right;\">Before Tips:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;font-weight:bold; border:none\">$" + order.beforeTip + "</td>\n"
//                + "                </tr>"
//                + "                        <tr>\n"
//                + "                        <td style=\"border:none;text-align: right;\">Tip:</td><td style=\"border:none;text-align: left;\"></td><td style=\"text-align: left;border:none\">$" + order.tips + "</td>\n"
//                + "                    </tr>"
//                + "<tr></tr><tr></tr><tr></tr>"
//                + "<tr><td style=\"border:none;font-weight: bold;text-align: right;\">Total:</td><td style=\"border:none;font-weight: bold;text-align: left;\"></td><td style=\"text-align:left;font-weight: bold;border:none\">$" + order.totalPrice + "</td></tr>"
//                + "\n"
//                + "            </table>  \n"
//                + "        </td>\n"
//                + "        </tr>\n"
//                + "    </table>\n"
//                + "</div><hr style=\"margin-top:-44px;border:none;width:98%;height: 1px;background-color: black;\">";
//        body += "<div class=\"block\" style=\"margin-top:50px;display:inline-block;width:98%;\"><table style=\"width:100%\">\n"
//                + "    <tr><td style=\"font-size:40px;font-weight: bold;text-align:left\">To Chef</td>\n"
//                + "    <td style=\"font-size:40px;text-align:right;\">Order#:&nbsp;&nbsp;" + order.orderID + "</td></tr></table>\n"
//                + "            <table class=\"tableOrder\" style=\"width:100%;text-align:center;font-size:30px;margin-top: 10px;border-top: 1px solid;border-collapse: collapse;\">\n"
//                + "                <tr>\n"
//                + "                <th style=\"background-color: lightgray; border-bottom: 2px solid;\">Category</th><th style=\"background-color: lightgray; border-bottom: 2px solid;\">Qty</th>"
//                + "<th style=\"background-color: lightgray; border-bottom: 2px solid;\">Item</th><th style=\"background-color: lightgray; border-bottom: 2px solid;\">Side Choice</th>"
//                + "<th style=\"background-color: lightgray; border-bottom: 2px solid;text-align:left;\">Price</th><th style=\"background-color: lightgray; border-bottom: 2px solid;text-align:left;\">Instructions</th>\n"
//                + "                </tr>";
//        tempIndex = 0;
//
//        for (CategoryOrderFood cof : cofs) {
//
//            for (int i = 0; i < cof.fs.size(); i++) {
//                TempFood tf = cof.fs.get(i);
//                if (tempIndex != cofs.size() - 1) {
//                    if (i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 1px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 1px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 1px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\"></td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 1px solid;\"></td>\n</tr>\n";
//                    } else {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 1px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 1px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\"></td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 1px solid;text-align:left;\"></td>\n</tr>";
//                    }
//                } else {
//                    if (i == cof.fs.size() - 1 && i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 2px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 2px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 2px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 2px solid;\"></td>\n<td style=\"border-bottom: 2px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 2px solid;\"></td>\n</tr>\n";
//
//                    } else if (i == 0) {
//                        body += "<tr>\n<td style=\"border-bottom: 1px solid; border-right: 1px solid;\" rowspan=\"" + cof.number + "\" class=\"nameT\">" + cof.catergory + "</td><td style=\"border-bottom: 1px solid; border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td>"
//                                + "<td style=\"border-bottom: 1px solid;text-align:left;\">&nbsp;&nbsp;"
//                                + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\"></td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + Tools.getShortNumber(tf.price * tf.number) + "</td><td style=\"border-bottom: 1px solid;\"></td>\n</tr>\n";
//                    } else if (i == cof.fs.size() - 1) {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 2px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 2px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 2px solid;\"></td>\n<td style=\"border-bottom: 2px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 2px solid;text-align:left;\"></td>\n</tr>";
//                    } else {
//                        body += "<tr>\n"
//                                + "<td style=\"border-bottom: 1px solid;border-right: 1px solid;\" class=\"nameT\">" + tf.number + "</td><td style=\"border-bottom: 1px solid;text-align:left;margin-left:10px;\">&nbsp;&nbsp;" + tf.ID + " . " + tf.name + "</td><td style=\"border-bottom: 1px solid;\"></td>\n<td style=\"border-bottom: 1px solid;text-align:left;\">$" + tf.price + "</td><td style=\"border-bottom: 1px solid;text-align:left;\"></td>\n</tr>";
//                    }
//                }
//            }
//
//            tempIndex++;
//        }
//
//        body += "</table>\n</div>\n<br/><br/><br/>\n</body>\n</html>";
//        return body;
//    }

    public void sendEmailToForgetPassword(String email, String pass) {
        initSession(from, password);
        String content = "Hi " + ",<br/><br/> Your temporary password is: " + pass + ", Please log in as soon as possible to create new password;";
        MimeMessage message = composeEmail(from, email, "Password Information From Utopia", content, false, false);
        sendEmail(message); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public void sendEmailToForgetPassword_CN(String email, String code, String link) {
        initSession(from, password);
        String content = "尊敬的用户您好: " + ",<br/><br/> 请在1个小时内点击以下链接进行密码重置<br/>" + link  + "<br/>您的激活码是 "+code+"<br/>谢谢您的使用!<br/>"
                + "Utopia 团队";
        MimeMessage message = composeEmail(from, email, "Utopia重新设置密码", content, false, false);
        sendEmail(message); //To change body of generated methods, choose Tools | Templates.
    }

	public void sendEmailForShareInfo(String email, String info) {
		// TODO Auto-generated method stub
		
		initSession(from, password);
        String content = "亲爱的的用户你好，" + ",<br/><br/>"+info+"<br/><br/>";
        content+="Transtopia 团队";
        MimeMessage message = composeEmail(from, email, TaskSubject, content, false, false);
        sendEmail(message);
		
	}
}

