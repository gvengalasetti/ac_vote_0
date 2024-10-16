package edu.austincollege.acvote.faculty;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class will be utilized to read from the csv 
 * containing all faculty information for a import csv
 * function
 * 
 * @author Enrique Pineda and Jaidyn Vankirk
 *
 */
public class FacultyIO {
	protected ArrayList<Faculty> facultyList;
	private HashMap<String,Integer> keywords;
	protected FileReader fr;
	protected BufferedReader br = null;
	
	{
		facultyList = new ArrayList<Faculty>();
		keywords = new HashMap<String,Integer>();
		
		keywords.put("Faculty Id", null);	
		keywords.put("Last Name", null);
		keywords.put("First Name", null);
		keywords.put("Rank", null);
		keywords.put("Fac Depts", null);
		keywords.put("Fac Divisions", null);
		keywords.put("Tenure", null);
		keywords.put("Vote Status", null);
		keywords.put("AC Email", null);
	}
	
	/**
	 * Default Constructor for the FacultyIO class
	 */
	public FacultyIO() {
		
	}
	
/**
 * Will return a list of Faculty that is read from the FileName parameter. Skips faculty that is missing an ACID or Last Name.
 * @param FileStream
 * @return
 */
	public ArrayList<Faculty> readFacultyFile(InputStream FileStream) {
		try {
			InputStreamReader ir = new InputStreamReader(FileStream);
			br = new BufferedReader(ir);
			String str = br.readLine();
			String[] temp = str.split(",");
			//Write code looking at the title of the columns and deciphering which ones we need
			// Sets the columns
			/*
			 * Sets everything in the hash map to the matching column name.
			 */
			int length = temp.length;
			for(int i=0;i<temp.length;i++) {
				temp[i] = temp[i].trim();
				if(keywords.containsKey(temp[i])) {
					keywords.replace(temp[i], i);
				}
			}
			
			str = br.readLine();
			while(str != null) {
				// Input the faculty detail in list
				temp = str.split(",", length);
				if((temp[keywords.get("Last Name")] == "") || (temp[keywords.get("Faculty Id")] == "0")) {
					str = br.readLine();
				} else {
					boolean votingStatus = false;
					if(temp[keywords.get("Vote Status")].equals("VT")) {
						votingStatus = true;
					}
					Faculty hold = new Faculty(temp[keywords.get("Faculty Id")], temp[keywords.get("Last Name")], temp[keywords.get("First Name")], temp[keywords.get("Fac Depts")], temp[keywords.get("Fac Divisions")], temp[keywords.get("Rank")], temp[keywords.get("AC Email")], temp[keywords.get("Tenure")], votingStatus, true);
					facultyList.add(hold);
					//Read next line
					str = br.readLine();
				}
			}
		} catch(IOException ex) {
			//Deal with exception
			System.err.println("File error: " + (String)ex.getMessage());
		} finally {
			//Attempt to close the file
			try{
				if(br != null) {
					br.close();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}
		
		
		return facultyList;
	}

	//----------Getters-And-Setters----------\\
	public ArrayList<Faculty> getFacultyList() {
		return facultyList;
	}

	public void setFacultyList(ArrayList<Faculty> facultyList) {
		this.facultyList = facultyList;
	}

	public HashMap<String, Integer> getKeywords() {
		return keywords;
	}

	public void setKeywords(HashMap<String, Integer> keywords) {
		this.keywords = keywords;
	}

	public FileReader getFr() {
		return fr;
	}

	public void setFr(FileReader fr) {
		this.fr = fr;
	}

	public BufferedReader getBr() {
		return br;
	}

	public void setBr(BufferedReader br) {
		this.br = br;
	}
}
