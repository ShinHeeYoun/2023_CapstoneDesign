package com.ozxexe.aceinvoice;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import animatefx.animation.*;
import javafx.scene.layout.AnchorPane;
import java.util.*;

public class LoginController implements Initializable {
    
    @FXML
    private Circle btnClose;
    @FXML
    private StackPane pnlStack;
    @FXML
    private Pane pnlSignUp;
    @FXML
    private ImageView btnBack;
    @FXML
    private Button btnSignUp;
    @FXML
    private Pane pnlSignIn;
    @FXML
    private AnchorPane archRoot;
    @FXML
    private ImageView btnclose;
    @FXML
    private Button btnSignIn;
    @FXML
    private TextField txtID;
    @FXML
    private PasswordField txtPW;
    @FXML
    private TextField txtMID;
    @FXML
    private PasswordField txtMPW;
    @FXML
    private PasswordField txtMPWC;
    @FXML
    private Button btnMem;
    @FXML
    private Button btnIDC;
    @FXML
    private ImageView btnclose2;
    
    MyDB db = new MyDB();
    Alert alert = new Alert(AlertType.CONFIRMATION);
    Alert alert2 = new Alert(AlertType.INFORMATION);
    
    public boolean dck() {
    	if(db.exists(txtMID.getText())) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource().equals(btnSignUp)) {
        	txtID.clear();
            txtPW.clear();
            new ZoomIn(pnlSignUp).play();
            pnlSignUp.toFront();
        }
        if(event.getSource().equals(btnSignIn)) {
        	
        	if(db.table.containsKey(txtID.getText()) && db.table.get(txtID.getText()).equals(txtPW.getText())) {
        		alert.setTitle("Login Success");
                alert.setHeaderText("로그인 성공");
                alert.setContentText("OK 버튼 클릭 시 프로그램이 종료됩니다.");
                
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                   try {
                         Process process = Runtime.getRuntime().exec("C:\\Users\\82103\\Desktop\\demo(2)\\green.exe"); 
                         System.exit(0);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                      
                }else if(result.get() == ButtonType.CANCEL) {
                   event.consume();
                }
                
        	} else if (txtID.getText().isEmpty() || txtPW.getText().isEmpty() ) { 
        		alert2.setTitle("Login Fail");
                alert2.setContentText("아이디 혹은 비밀번호를 입력하지 않았습니다!!");
                alert2.show();
                txtID.clear();
                txtPW.clear();
                txtID.requestFocus();
            } else {
        		alert2.setTitle("Login Fail");
                alert2.setContentText("아이디 혹은 비밀번호가 잘못되었습니다!!");
                alert2.show();
                txtID.clear();
                txtPW.clear();
                txtID.requestFocus();
            }
    		
        }
        if(event.getSource().equals(btnIDC)) {
        	if (txtMID.getText().isEmpty()) {
        		alert2.setTitle("ID Creation Fail");
                alert2.setContentText("생성할 아이디를 입력하지 않았습니다!!");
                alert2.show();
                txtMID.requestFocus();
        	}else if(this.dck()) {
        		alert2.setTitle("ID Creation Fail");
                alert2.setContentText("중복된 아이디 입니다!!");
                alert2.show();
                txtMID.clear();
                txtMID.requestFocus();
        	}else {
        		alert2.setTitle("ID Creation Success");
                alert2.setContentText("사용가능한 아이디 입니다!!");
                alert2.show();
        	}
        }
        if(event.getSource().equals(btnMem)) {
        	if (txtMID.getText().isEmpty() || txtMPW.getText().isEmpty()) {
        		alert2.setTitle("Join Membership Fail");
                alert2.setContentText("생성할 아이디, 또는 비밀번호를 입력하지 않았습니다!!");
                alert2.show();
        	}else if(this.dck()){
        		alert2.setTitle("Join Membership Fail");
                alert2.setContentText("생성할 아이디를 중복확인 해주세요!!");
                alert2.show();
        	}else if(txtMPW.getText().equals(txtMPWC.getText())){
        		db.put(txtMID.getText(), txtMPW.getText());
        		alert2.setTitle("Join Membership Success");
                alert2.setContentText("회원가입이 성공하였습니다!!");
                alert2.show();
                txtMID.clear();
                txtMPW.clear();
                txtMPWC.clear();
                new ZoomIn(pnlSignIn).play();
                pnlSignIn.toFront();
                
        	}else {
        		alert2.setTitle("Join Membership Fail");
                alert2.setContentText("비밀번호가 일치하지 않습니다!!");
                alert2.show();
                txtMPWC.clear();
                txtMPWC.requestFocus();
        	}
        }
        
    }
    
    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if(event.getSource() == btnClose) {
            new animatefx.animation.FadeOut(archRoot).play();
            System.exit(0);
        }
        
        if(event.getSource().equals(btnBack)) {
        	txtMID.clear();
            txtMPW.clear();
            txtMPWC.clear();
            new ZoomIn(pnlSignIn).play();
            pnlSignIn.toFront();
        }
        if(event.getSource().equals(btnclose)) {
    		alert.setTitle("program exit");
    		alert.setHeaderText("잠깐! 프로그램을 종료하시겠습니까?");
    		alert.setContentText("OK 버튼 클릭 시 프로그램이 종료됩니다.");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			System.exit(0);
    		}else if(result.get() == ButtonType.CANCEL) {
    			event.consume();
    		}
    		
        }
        if(event.getSource().equals(btnclose2)) {
    		alert.setTitle("program exit");
    		alert.setHeaderText("잠깐! 프로그램을 종료하시겠습니까?");
    		alert.setContentText("OK 버튼 클릭 시 프로그램이 종료됩니다.");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			System.exit(0);
    		}else if(result.get() == ButtonType.CANCEL) {
    			event.consume();
    		}
    		
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
