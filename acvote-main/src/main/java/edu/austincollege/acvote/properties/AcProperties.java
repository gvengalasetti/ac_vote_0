package edu.austincollege.acvote.properties;

import java.io.Serializable;
import java.util.Objects;

/**
 * Purpose of class is to keep track of imported faculty list file name and date of import. 
 */
public class AcProperties implements Serializable{

private static final long serialVersionUID = 4716239980846784634L;
String propval;
String propkey;

public AcProperties() {
	this.propval="";
	this.propkey="";
}
public AcProperties(String propkey, String propval) {
	this.propval=propval;
	this.propkey=propkey;
}
@Override
public int hashCode() {
	return Objects.hash(propkey, propval);
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	AcProperties other = (AcProperties) obj;
	return Objects.equals(propkey, other.propkey) && Objects.equals(propval, other.propval);
}

@Override
public String toString() {
	return "property [propkey=" + propkey + ", =" + propval + "]";
}


public String getPropval() {
	return propval;
}
public void setPropval(String propval) {
	this.propval = propval;
}
public String getPropkey() {
	return propkey;
}
public void setPropkey(String propkey) {
	this.propkey = propkey;
}

}
