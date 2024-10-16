package edu.austincollege.acvote.faculty;

import java.io.Serializable;
import java.util.Objects;

/**
 * This is the domain class for the faculty to be used in 
 * displaying, importing, etc.
 * @author Enrique Pineda and Jaidyn Vankirk
 *
 */
public class Faculty implements Serializable {
	
	private static final long serialVersionUID = -5727818089068725774L;
	
	private String acId;
	private String lastName, firstName, dept, div, rank, email, tenure;
	//tenure can be null, t for tenured, tt for tenured track
	private boolean voting;
	private boolean active;
	
	/**
	 * Default constructor for the Faculty object
	 */
	public Faculty() {
		this("","","","","","","","",false, true);
	}
	
	public Faculty(String adId ) {
		this(adId,"","","","","","","",true, true);
	}
	
	/**
	 * Constructor that allows for all of the information for a 
	 * faculty member.
	 * 
	 * @param acId
	 * @param lastName
	 * @param firstName
	 * @param dept
	 * @param div
	 * @param rank
	 * @param email
	 * @param tenure
	 * @param voting
	 */
	public Faculty(String acId, String lastName, String firstName, String dept, String div, String rank, String email,
			String tenure, boolean voting, boolean active) {
		// TODO Auto-generated constructor stub
		super();
		this.acId = acId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.dept = dept; 
		this.div = div;
		this.rank = rank;
		this.email = email;
		this.tenure = tenure; 
		this.voting = voting;
		this.active = active;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(acId, active, dept, div, email, firstName, lastName, rank, tenure, voting);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Faculty other = (Faculty) obj;
		return Objects.equals(acId, other.acId) && active == other.active && Objects.equals(dept, other.dept)
				&& Objects.equals(div, other.div) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(rank, other.rank) && Objects.equals(tenure, other.tenure) && voting == other.voting;
	}

	@Override
	public String toString() {
		return "Faculty [acId=" + acId + ", lastName=" + lastName + ", firstName=" + firstName + ", dept=" + dept
				+ ", div=" + div + ", rank=" + rank + ", email=" + email + ", tenure=" + tenure + ", voting=" + voting
				+ ", active=" + active + "]";
	}

	//----------Getters--and--Setters----------\\
	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDiv() {
		return div;
	}

	public void setDiv(String div) {
		this.div = div;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public boolean isVoting() {
		return voting;
	}

	public void setVoting(boolean voting) {
		this.voting = voting;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
