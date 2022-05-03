//include other classes

import java.io.Serializable;

public class Message implements Serializable {
    protected String type;
    protected String status;
    protected Member mem;
    protected String who;
    
    //can be used as debit pin or employee ID
    protected String login;
    protected String EmpPass;

    public Message(){
        this.type = "Undefined";
        this.status = "Undefined";
        //this.emp = null;
        this.who = "NoOne";
    }

    //login employee
    public Message(String type, String status, String login, String EmpPass, String who){
        setType(type);
        setStatus(status);
        setWho(who);
        
    }
    
  //create account
    public Message(String type, String status, Member mem, String who){
        setType(type);
        setStatus(status);
        setWho(who);
    }
    
    private void setType(String type){
    	this.type = type;
    }

    public void setStatus(String status){
    	this.status = status;
    }

    public void setMember(Member mem){
    	this.mem = mem;
    }
    
    public Member getMember(){
    	return mem;
    }
    
    public void setWho(String who){
    	this.who = who;
    }
    

    public String getType(){
    	return type;
    }

    public String getStatus(){
    	return status;
    }
    
    public String getEmpPassword(){
    	return EmpPass;
    }
    
    public String getWho(){
    	return who;
    }

}
