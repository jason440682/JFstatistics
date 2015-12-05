package association;

import utility.SequenceList;

public class AssociationRule {
	private SequenceList front;
	private SequenceList back;
	public AssociationRule(SequenceList front,SequenceList back){
		this.front=front;
		this.back=back;
	}
	
	public SequenceList getFront(){
		return this.front;
	}
    
	public SequenceList getBack(){
		return this.back;
	}
}
