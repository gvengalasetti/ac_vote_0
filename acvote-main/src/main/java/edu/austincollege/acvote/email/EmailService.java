package edu.austincollege.acvote.email;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.mail.SimpleMailMessage;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.faculty.dao.FacultyDAO;
import edu.austincollege.acvote.vote.VoteToken;

import jakarta.mail.internet.MimeMessage;

import freemarker.template.Template;




@Service
public class EmailService{
	
	private static Logger log = LoggerFactory.getLogger(EmailService.class);
	
	private static final String FROM = "ACVote@austincollege.edu";

    @Autowired
    JavaMailSender emailSender;
    
    @Autowired
    FreeMarkerConfigurer freemarkerConfigurer;
    
    @Autowired
    FacultyDAO facultyDao;

    /**
     * create a map with the vote email template
     * @param bal
     * @param token
     * @return
     */
    public Map<String, Object> getVoteEmailTemplate(Ballot bal, VoteToken token)
    {
    	Map<String, Object> templateModel = new HashMap<String, Object>();
    	
    	templateModel.put("text", "");
    	templateModel.put("endTime", bal.getEndTime());
    	templateModel.put("title", bal.getTitle());
    	templateModel.put("description", bal.getDescription());
    	templateModel.put("instructions", bal.getInstructions());
    	templateModel.put("link", "http://localhost:8080/acvote/voter/vote?bid="+token.getBid()+"&token="+token.getToken());
    	
    	return templateModel;
    }
    
    /**
     * Create a map that can have additional text above vote email template
     * @param bal
     * @param token
     * @param text
     * @return
     */
    public Map<String, Object> getVoteEmailTemplate(Ballot bal, VoteToken token, String text)
    {
    	Map<String, Object> templateModel = new HashMap<String, Object>();
    	
    	templateModel.put("text", text);
    	templateModel.put("endTime", bal.getEndTime());
    	templateModel.put("title", bal.getTitle());
    	templateModel.put("description", bal.getDescription());
    	templateModel.put("instructions", bal.getInstructions());
    	templateModel.put("link", "http://localhost:8080/acvote/voter/vote?bid="+token.getBid()+"&token="+token.getToken());
    	
    	return templateModel;
    }
    
    /**
     * Method will send just simple text to the desired email with the set subject
     * @param to
     * @param subject
     * @param text
     * @throws Exception 
     */
    public void sendSimpleEmail(String subject, String text, VoteToken token) throws Exception
    {
    	//for token get the corresponding faculty member
    	Faculty fac = facultyDao.findFacultyByID(token.getAcId());
    	
    	log.info("Sending email to: " + fac.getEmail());
    	
        //create the simple message to edit and send
        SimpleMailMessage message = new SimpleMailMessage(); 
        //set the from field
        message.setFrom(FROM);
        
        //Currently only sending to bhill20@austincollege.edu
        message.setTo("bhill20@austincollege.edu"); 
        //message.setTo("brandon.hill2@verizon.net"); 
        
        //set the subject and actual text message
        message.setSubject(subject); 
        message.setText(text);
        
        //send message
        emailSender.send(message);
        
    }
    
    /**
     * Method uses the getVoteEmailTemplate to get the current template we are using for the voter email  and sends it with a subject
     * @param subject
     * @param bal
     * @param token
     * @throws Exception 
     */
    public MimeMessage sendVoteTemplateEmail(String subject, Ballot bal, VoteToken token) throws Exception
    {
    	//for token get the corresponding faculty member
		Faculty fac = facultyDao.findFacultyByID(token.getAcId());
		
    	log.info("Sending email to: " + fac.getEmail());
    	
    	//create the message to edit and send
    	MimeMessage message = emailSender.createMimeMessage();
        
    	//this sets the from and subject fields
		message.setFrom(FROM);
		message.setSubject(subject);
		
		//we set who we are sending to (right now we only send to bhill20@austincollege.edu)
		message.setRecipients(MimeMessage.RecipientType.TO, "bhill20@austincollege.edu");
        
        //get pre-made template
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("voteEmailTemplate.html");
        	
        //create a map and fill in the variables
        Map<String, Object> templateModel = getVoteEmailTemplate(bal, token);
        	
        //combine the two and convert it into a string to be emailed
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        	
        //set the content to be the string we converted
        message.setContent(htmlBody, "text/html; charset=utf-8");
        	  
        //send updated message
        emailSender.send(message);
        
        return message;
    	
    }
    
    /**
     * Method will send a list of tokens their voting emails with a set subject and a link to that ballot
     * @param subject
     * @param bal
     * @param tokens
     * @throws Exception
     */
    public List<MimeMessage> sendVoteTemplateEmailToList(String subject, Ballot bal, List<VoteToken> tokens) throws Exception
    {
    	List<MimeMessage> messages = new ArrayList<MimeMessage>();
    	
    	//This is the message we will be editing
    	MimeMessage message = emailSender.createMimeMessage(); 
        
    	//this is going to fill the from and subject fields
		message.setFrom(FROM);
		message.setSubject(subject);
		
		//loop through all the tokens
		for(int i = 0; i < tokens.size(); i++)
		{
			//for each token get the corresponding faculty member
			Faculty fac = facultyDao.findFacultyByID(tokens.get(i).getAcId());
			
			//use the faculty email as the to field (for now we will only send to bhill20@austincollege.edu
			message.setRecipients(MimeMessage.RecipientType.TO, "bhill20@austincollege.edu");
        	
        	//get the pre-made template
        	Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("voteEmailTemplate.html");
        	
        	//create a map and fill in the variables
        	Map<String, Object> templateModel = getVoteEmailTemplate(bal, tokens.get(i));
        	
        	//combine the two and convert it into a string to be emailed
        	String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        	
        	//set the content to be the string we converted
        	message.setContent(htmlBody, "text/html; charset=utf-8");
        	  
        	log.info("Sending email to: " + fac.getEmail());
        	
        	//send updated message
        	emailSender.send(message);
        	
        	messages.add(message);
        	
        	//wait 2 seconds inbetween because there is a limit to how fast they can be sent
        	Thread.sleep(2000);
		}
		return messages;
    	
    }
    
    /**
     * Method uses the getVoteEmailTemplate to get the current template we are using for the voter email  and sends it with a subject
     * @param subject
     * @param bal
     * @param token
     * @throws Exception 
     */
    public MimeMessage sendVoteTemplateEmailWithText(String text, String subject, Ballot bal, VoteToken token) throws Exception
    {
    	//for token get the corresponding faculty member
		Faculty fac = facultyDao.findFacultyByID(token.getAcId());
		
    	log.info("Sending email to: " + fac.getEmail());
    	
    	//create the message to edit and send
    	MimeMessage message = emailSender.createMimeMessage();
        
    	//this sets the from and subject fields
		message.setFrom(FROM);
		message.setSubject(subject);
		
		//we set who we are sending to (right now we only send to bhill20@austincollege.edu)
		message.setRecipients(MimeMessage.RecipientType.TO, "bhill20@austincollege.edu");
        
        //get pre-made template
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("voteEmailTemplate.html");
        	
        //create a map and fill in the variables
        Map<String, Object> templateModel = getVoteEmailTemplate(bal, token, text);
        	
        //combine the two and convert it into a string to be emailed
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        	
        //set the content to be the string we converted
        message.setContent(htmlBody, "text/html; charset=utf-8");
        	  
        //send updated message
        emailSender.send(message);
        
        return message;
    	
    }
    
    /**
     * Method uses the getVoteEmailTemplate to get the current template we are using for the 
     * voter email  and sends it with a subject to the given list
     * 
     * @param subject
     * @param bal
     * @param token
     * @throws Exception 
     */
    public List<MimeMessage> sendVoteTemplateEmailWithTextToList(String text, String subject, Ballot bal, List<VoteToken> tokens) throws Exception
    {
    	List<MimeMessage> messages = new ArrayList<MimeMessage>();
    	
    	//create the message to edit and send
    	MimeMessage message = emailSender.createMimeMessage();
        
    	//this sets the from and subject fields
		message.setFrom(FROM);
		message.setSubject(subject);
		
		for(int i = 0; i < tokens.size(); i++)
		{
		
		//for token get the corresponding faculty member
				Faculty fac = facultyDao.findFacultyByID(tokens.get(i).getAcId());
				
		    	log.info("Sending email to: " + fac.getEmail());
		
		//we set who we are sending to (right now we only send to bhill20@austincollege.edu)
		message.setRecipients(MimeMessage.RecipientType.TO, "bhill20@austincollege.edu");
        
        //get pre-made template
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("voteEmailTemplate.html");
        	
        //create a map and fill in the variables
        Map<String, Object> templateModel = getVoteEmailTemplate(bal, tokens.get(i), text);
        	
        //combine the two and convert it into a string to be emailed
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        	
        //set the content to be the string we converted
        message.setContent(htmlBody, "text/html; charset=utf-8");
        	  
        //send updated message
        emailSender.send(message);
        
        messages.add(message);
        
        Thread.sleep(2000);
		}
		
		return messages;
    }
    
}
