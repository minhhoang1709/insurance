package com.ninelives.insurance.api.model;

public class Version implements Comparable<Version> {
	Integer major, minor, patch;

	public Version(Integer major, Integer minor, Integer patch) {
		super();
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	public Integer getMajor() {
		return major;
	}

	public void setMajor(Integer major) {
		this.major = major;
	}

	public Integer getMinor() {
		return minor;
	}

	public void setMinor(Integer minor) {
		this.minor = minor;
	}

	public Integer getPatch() {
		return patch;
	}

	public void setPatch(Integer patch) {
		this.patch = patch;
	}

	@Override
	public int compareTo (Version o) {
	    if (this == o)
	        return 0;
	    int result = major.compareTo (o.major);
	    if (result == 0) {
	        result = minor.compareTo (o.minor);
	        if (result == 0){
	            result = patch.compareTo (o.patch);	            
	        }
	    }
	    return result;
	}

	@Override
	public String toString() {
		return "Version [major=" + major + ", minor=" + minor + ", patch=" + patch + "]";
	}
}
