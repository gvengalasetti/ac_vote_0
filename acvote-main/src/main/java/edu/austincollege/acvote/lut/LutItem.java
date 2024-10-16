package edu.austincollege.acvote.lut;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * An instance of this class encapsulates lookup item
 * information.  Lookup items are useful to populate 
 * combo-boxes and other user interface widgets. The
 * items will be sourced from out database allowing us 
 * to modify the valid options for some faculty fields
 * by changing our database (not hard-coded into our html
 * or into our code).  
 * <p>
 * Controllers can use the LutDao to fetch the list of items
 * for a particular field.  For example, we may need the list
 * of divisions (CW, HU, SS, SC) for populating a combobox.
 * </p>
 * <p>
 * We may also need the list of Lut Items when during test or
 * auditing the faculty import.
 * </p>
 * @author mahiggs
 *
 */
public class LutItem implements Serializable {
	

	private static final long serialVersionUID = -2678115693334276705L;
	
	private String code;    // text code must uniquely identify the item
	private String label;   // label provides reader friendly version

	
	public LutItem() {
		super();
	}
	
	public LutItem(String code, String label) {
		super();
		this.code = code;
		this.label = label;
	}

	public String toString() {
		return String.format("(%s,%s)", this.code,this.label);
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
