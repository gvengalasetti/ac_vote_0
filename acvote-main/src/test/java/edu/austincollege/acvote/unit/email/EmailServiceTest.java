package edu.austincollege.acvote.unit.email;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.email.EmailService;
import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.faculty.dao.FacultyDAO;
import edu.austincollege.acvote.vote.VoteToken;

@RunWith(MockitoJUnitRunner.class)
class EmailServiceTest {
	private static Logger log = LoggerFactory.getLogger(EmailServiceTest.class);

	@Rule
	public MockitoRule initRule = MockitoJUnit.rule();

	@Mock
	private JavaMailSender mockEmailSender;

	@Mock
	private FreeMarkerConfigurer mockFreemarkerConfigurer;

	@Mock
	private FacultyDAO mockFacultyDao;

	@InjectMocks
	private EmailService emailService = new EmailService();

//    private Faculty f1;
//	private Faculty f2;

	private Faculty f1 = new Faculty("624682", "Vankirk", "Jaidyn", "CS", "SC", "STU", "jvankirk20@austincollege.edu",
			"", false, true);
	private Faculty f2 = new Faculty("613412", "Pineda", "Enrique", "CS", "SC", "STU", "epineda@austincollege.edu", "",
			false, true);

	private VoteToken t1;
	private VoteToken t2;

	private Ballot b1;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
//		f1 = new Faculty("624682", "Vankirk", "Jaidyn", "CS", "SC", "STU", "jvankirk20@austincollege.edu", "", false, true);
//		f2 = new Faculty("613412", "Pineda", "Enrique", "CS", "SC", "STU", "epineda@austincollege.edu", "", false, true);
//		
		t1 = VoteToken.newToken(1, "624682");
		t2 = VoteToken.newToken(1, "613412");

		b1 = new Ballot(1, "Coolest CS Professor", "Please drag and drop these candidates around!",
				"This is a description for ballot 1!", true, null, "IRV", 2, LocalDateTime.now().plusDays(10),
				LocalDateTime.now().plusDays(20), "CW", 124);
	}

	/**
	 * Test creating a map with a ballot and a token for the email template
	 */
	@Test
	void getVoteEmailTemplateTest() {
		Map<String, Object> templateModel = new HashMap<String, Object>();

		templateModel.put("text", "");
		templateModel.put("endTime", b1.getEndTime());
		templateModel.put("title", b1.getTitle());
		templateModel.put("description", b1.getDescription());
		templateModel.put("instructions", b1.getInstructions());
		templateModel.put("link",
				"http://localhost:8080/acvote/voter/vote?bid=" + t1.getBid() + "&token=" + t1.getToken());

		assertEquals(templateModel, emailService.getVoteEmailTemplate(b1, t1));
	}

	/**
	 * Test creating a map with a ballot, token, and text for the email template
	 */
	@Test
	void getVoteEmailTemplateWithTextTest() {
		Map<String, Object> templateModel = new HashMap<String, Object>();

		templateModel.put("text", "Test Message");
		templateModel.put("endTime", b1.getEndTime());
		templateModel.put("title", b1.getTitle());
		templateModel.put("description", b1.getDescription());
		templateModel.put("instructions", b1.getInstructions());
		templateModel.put("link",
				"http://localhost:8080/acvote/voter/vote?bid=" + t1.getBid() + "&token=" + t1.getToken());

		String text = "Test Message";
		assertEquals(templateModel, emailService.getVoteEmailTemplate(b1, t1, text));
	}

	/**
	 * Test sending a simple text email
	 * 
	 * @throws Exception
	 */
	@Test
	void sendSimpleEmailTest() throws Exception {
		// mockFacultyDao = (FacultyDAO) Mockito.mock(FacultyDAO.class);
		log.debug("started");
		Mockito.when(mockFacultyDao.findFacultyByID("624682")).thenReturn(f1);

		// create the simple message to edit and send
		SimpleMailMessage message = new SimpleMailMessage();
		// set the from field
		message.setFrom("ACVote@austincollege.edu");

		// Currently only sending to bhill20@austincollege.edu
		message.setTo("bhill20@austincollege.edu");
		// message.setTo("brandon.hill2@verizon.net");

		// set the subject and actual text message
		message.setSubject("test subject");
		message.setText("test message");

		emailService.sendSimpleEmail("test subject", "test message", t1);

		// Mockito.when(mockEmailSender.send(message));
		Mockito.verify(mockEmailSender).send(message);
	}

}
