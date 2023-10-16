package com.ozxexe.aceinvoice;

import java.util.HashMap;

public class MyDB {
	public HashMap<String, String> table;
	
	public MyDB() {
		table = new HashMap<>();
		table.put("admin", "admin1234");
		table.put("green", "1234");
	}
	
	public boolean exists(String id) {    // 회원정보가 존재하는지 검사한다.
		return table.get(id) != null;
	}
	
	public void put(String id, String pw) {    // 회원정보를 저장
		if(id != null && pw != null)
			table.put(id, pw);
	}
}
