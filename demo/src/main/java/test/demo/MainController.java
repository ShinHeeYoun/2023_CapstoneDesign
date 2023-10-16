package test.demo;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import test.demo.models.Emitters;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.FileOutputStream;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.prefs.Preferences;


import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.text.Element;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class MainController implements Initializable {

    /*@FXML
    private TableView<Member> tvMembers;

    @FXML
    private TableColumn<Member, String> tcName;

    @FXML
    private TableColumn<Member, String> tcMobile;

    @FXML
    private TableColumn<Member, String> tcEmail;

    @FXML
    private TableColumn<Member, String> tcAction;*/

    @FXML
    private TableView<Emitters> tvEmission;

    @FXML
    private TableColumn<Emitters, String> tcEmission;

    @FXML
    private TableColumn<Emitters, String> tcEmitter;

    @FXML
    private TableColumn<Emitters, String> tcDate;

    @FXML
    private HBox hboxMenu;

    @FXML
    private Tab tabChart;

    @FXML
    private Tab tabGraph;

    @FXML
    private Button btnResetLineChart;

    @FXML
    private Button dailyBtn, homeBtn, transactionBtn, emailBtn, btnWriteTransaction;



    @FXML
    private Pane dailyPane,transactionPane;

    @FXML
    private TextArea txtDragDrop;

    @FXML
    private AnchorPane emailPane;

    @FXML
    private AnchorPane anchorPaneAttach;

    @FXML
    private TabPane homePane;

    @FXML
    private LineChart<String,Number> Linechart;

    private ObservableList<XYChart.Series> sortedSeriesList;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private StackPane stackPane;
    @FXML
    private ComboBox<String> cbEmitter1;

    @FXML
    private TextField txtPW, txtRCV, txtID, txtTitle;

    @FXML
    private TextArea txtMsg;

    @FXML
    private ScrollPane paneAttachScroll;

    @FXML
    private Button btnAttach,btnSend;

    public static Stage pStage;

    private ObservableList<Emitters> data;

    private static final String LAST_FOLDER_KEY = "lastFolder";

    private PriorityQueue<LeafNode> maxHeap; // 배출자 정보 저장 heap

    private Set<String> uniqueValues;

    private List<String> columnValues;

    private List<String> columnValuesDate;

    private ArrayList<String> list;

    private XYChart.Series preSeries;
    String sheetName = "Sheet";
    int columnIndex = 6;

    private String selectedFolderPath;

    private File tmpFile;

    private List<File> tmpFileList;

    private Set<String> categories;

    @FXML
    private VBox vBoxAttach;

    @FXML
    private Button btnVBoxAttach;

    private String labelName;

    private String tmpTxt;
    static String smtp_host = "smtp.gmail.com";
    static final int smtp_port = 465;  // TLS : 587, SSL : 465

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        maxHeap = new PriorityQueue<>(Comparator.comparing(LeafNode::getDate));
        uniqueValues = new HashSet<>();
        columnValues = new ArrayList<>();
        columnValuesDate = new ArrayList<>();
        list = new ArrayList<>();
        categories = new LinkedHashSet<>();

        tmpTxt = "";

        tmpFileList = new ArrayList<>();

        tcEmitter.prefWidthProperty().bind(tvEmission.widthProperty().multiply(0.5));
        tcDate.prefWidthProperty().bind(tvEmission.widthProperty().multiply(0.25));
        tcEmission.prefWidthProperty().bind(tvEmission.widthProperty().multiply(0.25));


        Linechart.setTitle("파일 이름");

        xAxis.setTickLabelFont(Font.font(15));
        yAxis.setLabel("kg");
        yAxis.setTickLabelFont(Font.font(15));

        paneAttachScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        paneAttachScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        btnWriteTransaction.setOnAction(event -> {
            try {
                Desktop.getDesktop().open(new File("C:\\Users\\82103\\Desktop\\주찬결\\거래명세서 메크로.xlsm"));
            }catch (IOException ex){

            }
        });


        paneAttachScroll.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        vBoxAttach.setOnDragEntered(event -> {
        });


        paneAttachScroll.setOnDragDropped(event -> {

            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            labelName = "";

            if (event.getDragboard().hasFiles()) {
                event.getDragboard().getFiles().forEach(file -> {
                    setLabelName(file.getName());
                    System.out.println(file.getName());
                    vBoxAttachEvent();
                });
            }

            if (dragboard.hasFiles()) {
                List<File> files = dragboard.getFiles();
                for (File file : files) {
                    if (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")) {
                        // Perform actions with the Excel file
                        tmpFile = new File(file.getAbsolutePath());
                        tmpFileList.add(tmpFile);
                        //openExcelFile(tmpFile);
                    }
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void setLabelName(String name){
        labelName = name;
    }

    public static String decode(String maskedText) {
        StringBuilder decodedText = new StringBuilder();

        for (int i = 0; i < maskedText.length(); i++) {
            if (maskedText.charAt(i) == '*') {
                // Append the corresponding character from the original text
                decodedText.append(maskedText.charAt(i));
            } else {
                // Append the original character from the original text
                decodedText.append(maskedText.charAt(i));
            }
        }

        return decodedText.toString();
    }



    void resetHeap(){
        maxHeap = new PriorityQueue<>(Comparator.comparing(LeafNode::getDate));
    }

    @FXML
    void handleButtonClick(ActionEvent event) {
        if (event.getSource() == homeBtn) {
            //showAsDialog("import");
            homePane.toFront();
        } else if (event.getSource() == dailyBtn) {
            dailyPane.toFront();
        } else if (event.getSource() == transactionBtn) {
            transactionPane.toFront();
        } else if (event.getSource() == emailBtn) {
            emailPane.toFront();
        }

    }

    public void vBoxAttachEvent(){
        System.out.println(labelName+"추가됫읍니다");
        String myText = labelName;
        HBox hbox1 = new HBox();
        hbox1.setSpacing(5);
        hbox1.setAlignment(Pos.CENTER_LEFT);

        String imagePathExcel = "/test/demo/icons/xls.png";
        String imagePathCancel = "/test/demo/icons/cancel.png";
        ImageView imageView = new ImageView(new Image(getClass().getResource(imagePathExcel).toExternalForm()));
        imageView.setFitHeight(18);
        imageView.setPreserveRatio(true);

        Label label1 = new Label(labelName);
        label1.setFont(new Font(13));

        Button imageButton = new Button();
        imageButton.setStyle("-fx-background-color:white;");
        Image image = new Image(getClass().getResource(imagePathCancel).toExternalForm());
        ImageView imageView2 = new ImageView(image);
        imageView2.setFitHeight(10);
        imageView2.setPreserveRatio(true);
        imageButton.setGraphic(imageView2);
        imageButton.setOnAction(event -> {
            removeHBoxByLabelText(myText);
            //System.out.println(labelName);
        });

        hbox1.getChildren().addAll(imageView,label1,imageButton);

        vBoxAttach.getChildren().addAll(hbox1);
        paneAttachScroll.setContent(vBoxAttach);

        System.out.println(vBoxAttach.getHeight());
        if (vBoxAttach.getHeight() > 100.0){
            System.out.println(vBoxAttach.getHeight());
            paneAttachScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        }

    }

    private void removeHBoxByLabelText(String labelText) {
        System.out.println(labelText+"지울겁니다");
        for (javafx.scene.Node child : vBoxAttach.getChildren()) {
            if (child instanceof HBox) {
                HBox hbox = (HBox) child;
                Label label = (Label) hbox.getChildren().get(1);
                if (label.getText().equals(labelText)) {
                    vBoxAttach.getChildren().remove(child);
                    for (File file : tmpFileList){
                        if(file.getName().equals(labelText)){
                            tmpFileList.remove(file);
                            break;
                        }
                    }
                    //setLabelName("");
                    break; // Exit the loop after removing the child
                }
            }
        }
    }
    public void printAllHeapValues() {
        for (LeafNode leafNode : maxHeap) {
            System.out.println("배출자: "+leafNode.getName() + " 배출량: "+ leafNode.getNumber()
                    +" 날짜: "+leafNode.getDate());
        }
    }
    private void addLeafToHeap(String name, double emission, String date) {
        LeafNode leaf = new LeafNode(name, date, emission);
        maxHeap.add(leaf);
        //System.out.println("Added leaf: " + leaf);
        //System.out.println("Max leaf in heap: " + maxHeap.peek());
    }

    private void removeLeafFromHeap() {
        if (!maxHeap.isEmpty()) {
            LeafNode maxLeaf = maxHeap.poll();
            System.out.println("Removed leaf: " + maxLeaf);
            System.out.println("Max leaf in heap: " + maxHeap.peek());
        } else {
            System.out.println("Heap is empty");
        }
    }
    private static class LeafNode implements Comparable<LeafNode> {

        private String date;
        private String name;
        private double number;

        public LeafNode(String name, String date,double number) {
            this.name = name;
            this.date = date;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public double getNumber() {
            return number;
        }

        public String getDate(){
            return  date;
        }

        @Override
        public int compareTo(LeafNode other) {
            return Double.compare(this.number, other.number);
        }

        @Override
        public String toString() {
            return "(" + name + ", " + number + ")";
        }

        public void setNumber(double mergedNumber) {
            number = mergedNumber;
        }

    }

    private void showAsDialog(String fxml) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(fxml + ".fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setX(pStage.getX() + 50);
            stage.setY(pStage.getY() + 50);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // needed for new widow
    private Stage primaryStage;

    public void openDaily(Event e){
        try {
            Desktop.getDesktop().open(new File("C:\\Users\\82103\\Downloads\\Excel_VBA_Practice\\Excel VBA Practice\\RunVBA.xlsm")); //
            //updateMergedCellValue("E:\\Download\\GreenWaste\\VBA\\RunVBA.xlsm","Sheet1","D17", selectedFolderPath);
        }catch (IOException ex){

        }
    }

    private void updateMergedCellValue(String filePath, String sheetName, String cellAddress, String newValue)
            throws IOException {
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        CellRangeAddress mergedRegion = getMergedRegion1(sheet, cellAddress);

        if (mergedRegion != null) {
            int firstRow = mergedRegion.getFirstRow();
            int firstColumn = mergedRegion.getFirstColumn();
            Row row = sheet.getRow(firstRow);
            Cell cell = row.getCell(firstColumn);
            cell.setCellValue(newValue);

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
        }

        workbook.close();
        fis.close();
    }

    private CellRangeAddress getMergedRegion1(Sheet sheet, String cellAddress) {
        CellReference cellReference = new CellReference(cellAddress);
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(cellReference.getRow(), cellReference.getCol())) {
                return mergedRegion;
            }
        }
        return null;
    }

    private CellRangeAddress getMergedRegion(Sheet sheet, String cellAddress) {
        CellReference cellReference = new CellReference(cellAddress);
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(cellReference.getRow(), cellReference.getCol())) {
                return mergedRegion;
            }
        }
        return null;
    }

    public void selOpenFile(ActionEvent e) {
        //declare filechooser
        Preferences preferences = Preferences.userNodeForPackage(getClass());
        String initialDirectory = preferences.get(LAST_FOLDER_KEY, System.getProperty("user.home"));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(initialDirectory));

        // file choice option
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls", "*.xlsm"));

        //use primary stage for new window
        File file = fileChooser.showOpenDialog(primaryStage);

        Button clickedButton = (Button) e.getSource();
        String clickedButtonId = clickedButton.getId();
        hboxMenu.requestFocus();
        if(clickedButtonId.equals("btnWriteTransaction")){
            if (file != null) {
                openExcelFile(file);
                return;
            }
        }

        if(clickedButtonId.equals("btnAttach")){
            if (file != null) {
                tmpFile = new File(file.getAbsolutePath());
                tmpFileList.add(tmpFile);

                setLabelName(tmpFile.getName());
                vBoxAttachEvent();

                tmpTxt += tmpFile.getName();
                tmpTxt += "\n";
                return;
            }
        }
        if (file != null) {
            selectedFolderPath = file.getParent();
            preferences.put(LAST_FOLDER_KEY, selectedFolderPath); // remember last folder
            selectedFolderPath += "\\" + file.getName();
            //System.out.println((selectedFolderPath +"\\" + file.getName()));

            resetHeap();
            columnValues.clear();

            double columnGTotal = readColumnGTotal(file);
            System.out.println("Total of Column G: " + columnGTotal);

            readAndSaveUniqueColumnValues(file);
            columnValues.sort(Comparator.naturalOrder());

            tcEmitter.setCellValueFactory(new PropertyValueFactory<>("Name"));
            tcEmission.setCellValueFactory(new PropertyValueFactory<>("Emission"));
            tcDate.setCellValueFactory(new PropertyValueFactory<>("Date"));

            ObservableList<String> items = FXCollections.observableArrayList();

            int i = 0;

            processExcelFile(file);
            data = FXCollections.observableArrayList();
            for(LeafNode node : maxHeap){
                data.add(new Emitters(node.getName(),node.getNumber(),node.getDate()));
            }

            while (i<columnValues.size()){
                items.add(columnValues.get(i));
                i++;
            }

            XYChart.Series<String, Number> series = new XYChart.Series<>();

            // Add data points to the series
            Linechart.setTitle(file.getName());
            Linechart.getData().add(series);

            cbEmitter1.setItems(items);

            //printAllHeapValues();
            data.sort(Comparator.comparing(emitters -> emitters.getDate()));
            tvEmission.setItems(data);
            tvEmission.getSortOrder().add(tcEmitter);

            //openExcelFile(file);
        }
    }

    private void processExcelFile(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                Cell emitterCell = row.getCell(CellReference.convertColStringToIndex("L"));
                Cell dateCell = row.getCell(CellReference.convertColStringToIndex("M"));
                Cell emissionCell = row.getCell(CellReference.convertColStringToIndex("Q"));
                Cell unitCell = row.getCell(CellReference.convertColStringToIndex("P"));

                String emitter = formatter.formatCellValue(emitterCell);
                String date = formatter.formatCellValue(dateCell);
                String unit = formatter.formatCellValue(unitCell);
                double emission = 0.0;
                try {
                    emission = emissionCell.getNumericCellValue();
                } catch (IllegalStateException e) {
                    // Handle the exception, e.g., set a default value
                    emission = 0.0;
                }

                // Process the emitter, date, and emission values
                if(unit.equals("Ton")){
                    unit = "kg";
                    emission *= 1000;
                }
                if(!emitter.equals("배출자") && !date.equals("배출자인계일자")) {
                    LeafNode node = new LeafNode(emitter,date,emission);
                    insertOrUpdate(node);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertOrUpdate(LeafNode node) {

        for (LeafNode existingNode : maxHeap) {
            if (existingNode.getName().equals(node.getName()) && existingNode.getDate().equals(node.getDate())) {
                // Merge the numbers if name and date are the same
                double mergedNumber = existingNode.getNumber() + node.getNumber();
                existingNode.setNumber(mergedNumber);
                return;
            }
        }

        // No matching node found, insert as a new node
        maxHeap.offer(node);

    }
    private void readAndSaveUniqueColumnValues(File file) { // 배출자 중복 없애고 저장
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(file))) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet is to be read
            Set<String> uniqueValues = new HashSet<>();
            for (Row row : sheet) {
                Cell cell = row.getCell(11); // Column L is index-based, starting from 0
                if (cell != null && cell.getCellType() == CellType.STRING && !cell.getStringCellValue().equals("배출자")) {
                    String cellValue = cell.getStringCellValue();
                    if (!uniqueValues.contains(cellValue)) {
                        uniqueValues.add(cellValue);
                        columnValues.add(cellValue);
                    }
                }
            }
            System.out.println("Column L values: " + columnValues);
            System.out.println(columnValues.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void resetChart() {
        Linechart.getData().clear();
        // Perform any additional chart reset logic here
    }

    @FXML
    private void handleComboBoxAction(ActionEvent event) { // 콤보박스 아이템 클릭했을 때
        String selectedItem = cbEmitter1.getSelectionModel().getSelectedItem();
        String name = "";
        System.out.println("Clicked item: " + selectedItem);
        // Add your custom logic here
        //ArrayList<String> list = new ArrayList<>();
        XYChart.Series series = new XYChart.Series();
        for (LeafNode leafNode : maxHeap) { // getDate 최솟값 찾아야 한다.
            if(leafNode.getName().equals(selectedItem)){
                list.add(leafNode.getDate());
                //System.out.println(leafNode.getDate()); //Date를 array에 넣고 다시 sort
                name = leafNode.getName();
            }
        }
        list.sort(Comparator.naturalOrder());
        System.out.println(Arrays.deepToString(list.toArray()));
        for(int i=0; i< list.size(); i++){
            for (LeafNode leafNode : maxHeap) { // getDate 최솟값 찾아야 한다.
                if(leafNode.getDate().equals(list.get(i)) && leafNode.getName().equals(name)){
                    Linechart.getData().forEach(series1 ->
                            series1.getData().sort(Comparator.comparing(data -> String.valueOf(data.getXValue()))));
                    drawLineChart(leafNode.getName(),leafNode.getDate(),leafNode.getNumber(),series);
                }
            }
        }
        Linechart.getData().clear();
        Linechart.layout();
        series.setName(name); //leafNode.getName()
        Linechart.getData().addAll(series);

    }

    private void openExcelFile(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double readColumnGTotal(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming you want to read the first sheet
            double total = 0.0;

            for (Row row : sheet) {
                Cell cell = row.getCell(6); // Column G
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    total += cell.getNumericCellValue();
                }
            }

            return total;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public void drawLineChart(String name, String date, double emission, XYChart.Series series){
        series.getData().add(new XYChart.Data<>(date, emission));
    }

    public void SendFile(){
        try {
            Send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void Send() throws Exception {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtp_host);
        props.put("mail.smtp.port", smtp_port);
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.smtp.ssl.trust", smtp_host);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        System.out.println(decode(txtPW.getText()));
                        return new PasswordAuthentication(txtID.getText(),txtPW.getText());
                    }
                });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(txtID.getText()));

            // Recipient email
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(txtRCV.getText())
            );

            Multipart multipart = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(txtMsg.getText(), "UTF-8");

            for (File file : tmpFileList) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                attachmentBodyPart.setDataHandler(new DataHandler(source));

                // Set the character encoding for the file name
                String encodedFileName = MimeUtility.encodeText(file.getName(), "UTF-8", null);
                attachmentBodyPart.setFileName(encodedFileName);

                multipart.addBodyPart(attachmentBodyPart);
            }

            multipart.addBodyPart(textBodyPart);

            // Set the subject with proper character encoding
            message.setSubject(txtTitle.getText(), "UTF-8");

            // 발송
            message.setContent(multipart);
            Transport.send(message);


        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}