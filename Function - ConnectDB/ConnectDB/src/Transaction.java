import java.util.*;

public class Transaction {
		String Transaction_num;
	    Date e_date;
	    Double e_amount;
	    String emitter;
	    String dealer;
	    String hand_over;
	    String car_num;
	    String waste;
	    String unit;
	    
	    Transaction(String num, String waste, String emit, Double amount, String unit, Date date, String dealer, String hand, String car) {
	    	this.Transaction_num = num; this.e_date = date; this.e_amount = amount;
	    	this.emitter = emit; this.dealer = dealer; this.hand_over = hand; 
	    	this.car_num = car; this.waste = waste; this.unit = unit;
	    }
	    
	    public String getTrans() {
	    	return Transaction_num;
	    }
	    public void setTrans(String trans) {
	    	Transaction_num = trans;
	    }
	    public Date getDate() {
	    	return e_date;
	    }
	    public void setDate(Date date) {
	    	this.e_date = date;
	    }
	    public Double getAmount() {
	    	return e_amount;
	    }
	    public void setAmount(Double amount) {
	    	this.e_amount = amount;
	    }
	    public String getEmitter() {
	    	return emitter;
	    }
	    public void setEmitter(String emit) {
	    	emitter = emit;
	    }
	    public String getDealer() {
	    	return dealer;
	    }
	    public void setDealer(String deal) {
	    	dealer = deal;
	    }
	    public String getCar() {
	    	return car_num;
	    }
	    public void setCar(String car) {
	    	this.car_num = car;
	    }
	    public String getHand() {
	    	return hand_over;
	    }
	    public void setHand(String hand) {
	    	this.hand_over = hand;
	    }
	    public String getWaste() {
	    	return waste;
	    }
	    public void setWaste(String waste) {
	    	this.waste = waste;
	    }
	    public String getUnit() {
	    	return unit;
	    }
	    public void setUnit(String unit) {
	    	this.unit = unit;
	    }
	
}
