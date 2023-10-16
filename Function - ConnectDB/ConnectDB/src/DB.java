import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.text.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class DB {
	   String dbDriver ="com.mysql.cj.jdbc.Driver";  
	   String dbUrl    = "jdbc:mysql://localhost:3306/db?characterEncoding=UTF-8 & serverTimezone=UTC"; //db변경
	   String dbId     = "root";
	   String dbPw     = "159cksruf!";
	   Connection con = null;
	   PreparedStatement pstmt = null;  
	   Statement stmt = null;
	   DB(){
	    try{
	        Class.forName(dbDriver);
	        
	        try {
	        	this.con = DriverManager.getConnection(dbUrl, dbId, dbPw);
	        	 DatabaseMetaData metaData = con.getMetaData();
	             ResultSet resultSet = metaData.getTables(null, null, "Transaction", null);
	             
	             if (resultSet.next()) {
	                 System.out.println("Table exists");
	             } else {
	                 System.out.println("Table does not exist");
	                 //테이블 생성
	                 String createTableQuery = "CREATE TABLE IF NOT EXISTS `Emitter`(\r\n"
	                 		+ "\r\n"
	                 		+ "	`emitter`    VARCHAR(80) NOT NULL COMMENT 'emitter',\r\n"
	                 		+ "\r\n"
	                 		+ "	`waste`    VARCHAR(200) NOT NULL COMMENT '처리물 종류',\r\n"
	                 		+ "\r\n"
	                 		+ "	`e_date`    DATE NOT NULL COMMENT 'e_date',\r\n"
	                 		+ "\r\n"
	                 		+ "	`unit`    VARCHAR(10) NOT NULL COMMENT '단위',\r\n"
	                 		+ "\r\n"
	                 		+ "	`cost`    INTEGER COMMENT '비용',\r\n"
	                 		+ "\r\n"
	                 		+ "	PRIMARY KEY ( `emitter`,`waste`, `e_date`)\r\n"
	                 		+ "\r\n"
	                 		+ "	) COMMENT = 'Emitter';\r\n"
	                 		+ "	                 		\r\n"
	                 		+ "\r\n"
	                 		+ "CREATE TABLE IF NOT EXISTS `Transaction`(\r\n"
	                 		+ "\r\n"
	                 		+ "		`Transaction_num`    VARCHAR(80) NOT NULL COMMENT 'Transaction_num',\r\n"
	                 		+ "\r\n"
	                 		+ "		`e_date`    DATE NOT NULL COMMENT 'e_date',\r\n"
	                 		+ "\r\n"
	                 		+ "		`e_amount`    decimal(8, 3) NOT NULL COMMENT 'e_amount',\r\n"
	                 		+ "\r\n"
	                 		+ "		`emitter`    VARCHAR(80) NOT NULL COMMENT 'emitter',\r\n"
	                 		+ "		`dealer`    VARCHAR(80) NOT NULL COMMENT 'dealer',\r\n"
	                 		+ "		`hand_over`    VARCHAR(20) NOT NULL COMMENT 'hand_over',\r\n"
	                 		+ "		`car_num`    VARCHAR(80) NOT NULL COMMENT 'car_num',\r\n"
	                 		+ "		`waste`    VARCHAR(200) NOT NULL COMMENT '처리물 종류',\r\n"
	                 		+ "		`unit`    VARCHAR(10) NOT NULL COMMENT '단위',\r\n"
	                 		+ "		PRIMARY KEY ( `Transaction_num` ),\r\n"
	                 		+ "		FOREIGN KEY (`emitter`, `waste`, `e_date`) REFERENCES `Emitter` (`emitter`, `waste`, `e_date`)\r\n"
	                 		+ "		) COMMENT = 'Transaction'";
	                 	 stmt = con.createStatement();
	                     stmt.executeUpdate(createTableQuery);
	                     
	                     System.out.println("Table created successfully");
	             }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        System.out.println("success");
	    }catch(Exception e) {
	        System.out.println(e);
	    } 
	   }
	   
	   //엑셀을 읽어 데이터베이스에 추가하는 함수
	   public void readExcel(String path) throws FileNotFoundException, IOException, SQLException, InvalidFormatException {
		   String Emittersql = null;
		   String TranSql = null;
		   ResultSet rs = null;
		   Emittersql = "INSERT INTO EMITTER (waste, emitter, unit, cost, e_date)"
		   		+ " values( ?, ?, ?, ?, ?)";
		   TranSql = "INSERT INTO TRANSACTION (`Transaction_num`, `e_date`, `waste`," 
		   		+ " `emitter`, `e_amount` , `unit`, `car_num` , `hand_over` , `dealer`)"
		   		+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		   
		   String SelectSql = "SELECT EMITTER, WASTE, UNIT" 
		   		+ " FROM TRANSACTION";
		   
		  /* //utf-8 인코딩
		   File file = new File(path);

		   // create workbook with default encoding
		   Workbook tempworkbook = WorkbookFactory.create(file);

			// create temporary file with UTF-8 encoding
			File tempFile = File.createTempFile(path.substring(0, path.lastIndexOf(".")), ".xlsx");
			tempFile.deleteOnExit();
			OutputStream outputStream = new FileOutputStream(tempFile);
			tempworkbook.write(outputStream);

			// overwrite original file with UTF-8 encoded file
			Files.copy(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING); 
			*/
		   //file select추가 필요
		   try(FileInputStream inp = new FileInputStream(path)) {
			    Workbook workbook;
				
			    workbook = new XSSFWorkbook(OPCPackage.open(inp));
				workbook.setMissingCellPolicy(Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
				Sheet datatypeSheet = workbook.getSheetAt(0);
				
				Map<String, Integer> columns = new HashMap<>();
				columns.put("인계서번호", 1); columns.put("인수일자(*)", 1); columns.put("폐기물종류", 1); columns.put("배출자", 1);
				columns.put("위탁량", 14); columns.put("단위", 15); columns.put("차량번호(*)", 1); columns.put("인계자명(*)", 1);
				columns.put("처리자", 1);
				
				//필요 칼럼 선택
				Row columnsRow = datatypeSheet.getRow(0);
				for (Cell cell : columnsRow) {
					String str = cell.getStringCellValue();
					if(columns.containsKey(str)) {
						if(str.equals("위탁량")) continue;
						columns.put(str, cell.getColumnIndex());
					}
				}
				
				Map<String, Integer> emit = new HashMap<>();
				emit.put("폐기물종류", columns.get("폐기물종류")); emit.put("배출자", columns.get("배출자")); emit.put("단위", columns.get("단위"));
				
				List<Transaction> transList = new ArrayList<>();
				List<Emitter> emitList = new ArrayList<>();
				
				
				
				//엑셀을 읽고 리스트에 추가
				try {
				for (int i = 2; i < datatypeSheet.getPhysicalNumberOfRows(); i++) { 
					List<Object> list = new ArrayList<>();
					Row row = datatypeSheet.getRow(i);
					Integer j = 1; //stmt용 
					for (Integer index : columns.values()) {
						Cell cell = row.getCell(index);
						//타입별 데이터 추가
						switch (cell.getCellType()) {
		        		case STRING:
		        			//배출자, 폐기물 종류, 단위
		        			list.add(cell.getStringCellValue());
		        			
		        			System.out.println(j.toString() + " String " + cell.getStringCellValue());
		        			j++;
		        			
		        			
		        			continue;
		        		case NUMERIC:
		        			if(DateUtil.isCellDateFormatted(cell)) {
		        				//String date = dateFormat.format(cell.getDateCellValue());
		        				//java.sql.Date Date = new java.sql.Date(dateFormat.parse(date).getTime());
		        				list.add(cell.getDateCellValue());
		        				//System.out.println(j.toString() + " Date: " + DataFormat.getFormat(cell.getDateCellValue()) );
		        				
		        				j++;
		        				continue;
		        			}
		        			list.add(cell.getNumericCellValue());
		        			
		        			System.out.println(j.toString() + " Integer: "  + cell.getNumericCellValue());
		        			j++;
		        			
		        			continue;
		        		case BOOLEAN:
		        			list.add(cell.getBooleanCellValue());
		        			
		        			System.out.println(j.toString() + " Boolean " + cell.getBooleanCellValue());
		        			j++;
		        			
		        			continue;
		        		default:
		        			System.out.print(" " + "\t");
		        		}
					}
					//list에 넣을 객체 생성
					Transaction tran = new Transaction((String)list.get(0), (String)list.get(1), (String)list.get(2), (Double)list.get(3), (String)list.get(4),
							(java.util.Date)list.get(5), (String)list.get(6), (String)list.get(7), (String)list.get(8));
					Emitter emitter = new Emitter((String)list.get(2), (java.util.Date)list.get(5), (String)list.get(1), 0, (String)list.get(4));
					//객체 추가
					transList.add(tran);
					//중복 확인
					boolean dupBool = false;
					for(Emitter k : emitList) { //중복
						if (emitList.size() !=0 && k.getItem().equals(emitter.getItem()) && k.getName().equals(emitter.getName()) &&
						k.getUnit().equals(emitter.getUnit()) && k.getDate().equals(emitter.getDate())) {
							dupBool = true;
							break;
						}
					}
					if(!dupBool) {
						emitList.add(emitter);
					}
			       
			        System.out.println();
			      }
		   }catch (Exception e) {
			      e.printStackTrace();
		   }
		 
		  //data load
		   for(Emitter emitter: emitList) {
			   System.out.println(emitList.indexOf(emitter));
			   pstmt = con.prepareStatement(Emittersql);
			   
			   String item = emitter.getItem(); String name = emitter.getName(); String unit = emitter.getUnit();
			   Integer cost = emitter.getCost(); java.sql.Date e_date = new java.sql.Date(emitter.getDate().getTime());
			   pstmt.setString(1, item); pstmt.setString(2, name); pstmt.setString(3, unit);
			   pstmt.setInt(4, cost); pstmt.setDate(5,  e_date);
			   pstmt.executeUpdate();
			  
		   }
		   //data load
		   for(Transaction trans: transList) {
			   pstmt = con.prepareStatement(TranSql);
			   
			   String trans_num = trans.getTrans(); java.sql.Date date = new java.sql.Date(trans.getDate().getTime()); String waste = trans.getWaste();
			   String emitter = trans.getEmitter(); Double amount = trans.getAmount(); String unit = trans.getUnit();
			   String car = trans.getCar(); String hand = trans.getHand(); String dealer = trans.getDealer();
			   System.out.println(date); 
			   //prepare
			   pstmt.setString(1, trans_num); pstmt.setDate(2, date); pstmt.setString(3, waste);
			   pstmt.setString(4, emitter); pstmt.setDouble(5, amount); pstmt.setString(6,unit);
			   pstmt.setString(7, car); pstmt.setString(8, hand); pstmt.setString(9, dealer);
			   pstmt.executeUpdate();
			  
		   }
		  
	   }
	   }
	   public void wrtieExcel(String path) {
		   try {
	            Statement statement = con.createStatement();
	            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + "Transaction");

	            XSSFWorkbook workbook = new XSSFWorkbook();
	            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Transaction");

	            int rowNum = 0;
	            int arr[] = { 2, 4, 8, 3, 9}; //특정 칼럼
	            //수정
	            while (resultSet.next()) {
	                Row row = sheet.createRow(rowNum++);
	                //int columnCount = resultSet.getMetaData().getColumnCount();
	                int num = 0;
	                for (int i : arr) {
	                	Cell cell = row.createCell(num++);
	                	if(i == 3) { //amount
	                		String str = resultSet.getString(i);
	                		String unit = resultSet.getString(9);
	                		Double amount = Double.parseDouble(str);
	                		if(unit.equals("Ton")) {
	                			System.out.println(amount);
	                			amount *= 1000;
	                			System.out.println(amount);
	                			
	                		
	                		}
	                		cell.setCellValue(amount);
	                	}
	                	else if (i == 9) {// unit
	                		cell.setCellValue("kg");
	                	}
	                	else {
	                		
	                		cell.setCellValue(resultSet.getString(i));
	                	}
	                }
	            }

	            try (FileOutputStream outputStream = new FileOutputStream(path)) {
	                workbook.write(outputStream);
	                System.out.println("Excel file created successfully.");
	            }
	        } catch (SQLException | IOException e) {
	            e.printStackTrace();
	        }
	    }
}