import java.util.*;

public class Emitter {
	    String name;    // 이름
	    String item; // 종류
	    int cost; //비용
	    String unit;    // 처리 날짜
	    Date date;
	    Emitter(String name, Date date, String item, int cost, String unit) {
	    	this.name = name; this.item = item;
	    	this.cost = cost; this.unit = unit;
	    	this.date = date;
	    }
	    
	    public String getName() {
	        return name;
	    }
	    
	    public void setName(String name) {
	        this.name = name;
	    }
	    
	    public String getItem() {
	        return item;
	    }
	    
	    public void setItem(String item) {
	        this.item = item;
	    }
	    
	    public String getUnit() {
	        return unit;
	    }
	    
	    public void setUnit(String unit) {
	        this.unit = unit;
	    }
	    
	    public int getCost() {
	    	return cost;
	    }
	    public void SetCost(int cost) {
	    	this.cost = cost;
	    }
	    public Date getDate() {
	        return date;
	    }
	    
	    public void setDate(Date date) {
	        this.date = date;
	    }
}
