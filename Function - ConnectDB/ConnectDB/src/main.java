import java.io.*;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.*;
//출력 성공
public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "C:\\Users\\82103\\Desktop\\주찬결\\";
		String fileName = "23년 04월 올바로.xlsx";
		DB db = new DB();
		try {
	
			db.readExcel(path + fileName);
			db.wrtieExcel(path + "임시.xlsx");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	
	
}
