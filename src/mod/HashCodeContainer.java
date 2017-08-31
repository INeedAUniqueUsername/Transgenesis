package mod;

public class HashCodeContainer {
//Stores hash codes invisibly (i.e. without interfering changing the parent's hashcode)
	int lastSaveCode;
	int lastBindCode;
	public HashCodeContainer() {
		setLastSaveCode(0);
		setLastBindCode(0);
	}
	public int getLastSaveCode() {
		return lastSaveCode;
	}
	public int getLastBindCode() {
		return lastBindCode;
	}
	public void setLastSaveCode(int lastSavingCode) {
		this.lastSaveCode = lastSavingCode;
	}
	public void setLastBindCode(int lastBindingCode) {
		this.lastBindCode = lastBindingCode;
	}
	public boolean equals(Object o) {
		return o instanceof HashCodeContainer;
	}
	public int hashCode() {
		return 0;
	}
}
