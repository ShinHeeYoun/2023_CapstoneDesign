Attribute VB_Name = "Module2"
Private myString3 As String
Public FormFile As String
Public NewFile As String
Public targetF As String
Public myString As String
Sub CopyExcelFile()
    
    Dim sourceWorkbook As Workbook
    Dim destinationWorkbook As Workbook
    Dim sourceWorksheet As Worksheet
    Dim destinationWorksheet As Worksheet
    Dim sourceRange As Range
    Dim destinationRange As Range
    
    FormFile = "C:\Users\82103\Desktop\������\�ŷ�����.xlsx"
    NewFile = "C:\Users\82103\Desktop\������\�ŷ����� �ۼ���.xlsx"
    
    ' ���� ��� ����
    Set sourceWorkbook = Workbooks.Open(FormFile)
    Set destinationWorkbook = Workbooks.Open(NewFile)
    
    Set sourceWorksheet = sourceWorkbook.Sheets(1)
    Set destinationWorksheet = destinationWorkbook.Sheets(1)
    
    ' ������ ���� ����
    Set sourceRange = sourceWorksheet.UsedRange
    Set destinationRange = destinationWorksheet.Range("A1:T578")
    
    ' ���� ���� ���Ͽ��� ��� ������, ����, ĭ�� �ʺ�, ĭ�� ���� ����
    sourceWorksheet.Cells.Copy
    destinationWorksheet.Cells.PasteSpecial Paste:=xlPasteAll
    
    ' Ŭ������ �����
    Application.CutCopyMode = False
    
    ' ���� ���� ������ �ݽ��ϴ�.
    sourceWorkbook.Close SaveChanges:=False
    
End Sub

Sub GetMySQLData()
    ' MySQL ���� ����
    Dim conn As New ADODB.Connection
    conn.ConnectionString = "DRIVER={MySQL ODBC 8.0 Unicode Driver};SERVER=localhost;DATABASE=db;UID=root;PWD=159cksruf!;"
    conn.Open

    Dim CurrentWorkbook As Workbook
    Set CurrentWorkbook = ThisWorkbook
    Dim CWs As Worksheet
    Set CWs = CurrentWorkbook.Sheets("Sheet1")
    
    ' ������ ��������
    Dim myString1 As String
    myString1 = Module2.myString
    
    ' ó���ܰ� ��������
    myString3 = CWs.Range("E13").value
    
    If myString1 = "" Or myString3 = "" Then
        MsgBox "�����ڿ� ó���ܰ��� �Է����ּ���.", vbInformation
        Exit Sub
    End If
    
    ' ������ ��������
    Dim rs As New ADODB.Recordset
    rs.Open "SELECT * FROM Transaction WHERE emitter = '" & myString1 & "' AND e_date LIKE '%" & Module2.myString2 & "%' ORDER BY e_date ASC", conn

    If rs.EOF Then
        MsgBox "�ش��ϴ� �����Ͱ� �����ϴ�.", vbInformation
        rs.Close
        conn.Close
        Exit Sub
    End If
    
    CopyExcelFile
    
    targetF = NewFile
    Dim target As Workbook
    Set target = Workbooks.Open(targetF)
    Dim targetWs As Worksheet
    Set targetWs = target.Sheets("Sheet1")
    
    SetDateValue Module2.year, Module2.month, targetWs
    targetWs.Range("A3").value = myString1
    
    Dim unit As String
    Dim amount As Double
    
    ' ������ �� ��ŭ ���� �÷����� ������ ����
    Dim row As Long
    row = 9
    While Not rs.EOF
        
        unit = rs.Fields("unit")
        amount = rs.Fields("e_amount")
        
        If unit = "kg" Then
            amount = amount / 1000
            unit = "��"
        Else
            unit = "��"
        End If
    
        targetWs.Range("A9:P9").Copy Destination:=targetWs.Range("A" & row)
        ' �׵θ� ���� ���� �� ����
        Dim sourceRange As Range
        Set sourceRange = targetWs.Range("A9:P9")
        
        Dim targetRange As Range
        Set targetRange = targetWs.Range("A" & row)
        
        sourceRange.Copy
        targetRange.PasteSpecial xlPasteFormats
        
        Application.CutCopyMode = False
        
        targetWs.Range("B" & row).value = rs.Fields("waste")
        targetWs.Range("E" & row).value = myString3
        targetWs.Range("N" & row).value = rs.Fields("dealer")
        targetWs.Range("O" & row).value = rs.Fields("e_date")
        targetWs.Range("A" & row).value = dateValue(Mid(rs.Fields("e_date"), 6))
        
        ' Check if there is data in A:row+1 and move the cells down one row if true
        If targetWs.Range("A" & row + 1).value <> "" And Not rs.EOF Then
            targetWs.Range("A" & row + 1 & ":P" & row + 1).Copy
            targetWs.Range("A" & row + 2).Insert Shift:=xlDown
            targetWs.Range("A" & row + 1 & ":P" & row + 1).ClearContents
        End If
            
        targetWs.Range("C" & row).value = unit
        targetWs.Range("P" & row).value = amount
        targetWs.Range("G" & row & ":H" & row).Merge
        targetWs.Range("I" & row & ":K" & row).Merge
            
        ' ������ ���� �� ���� �ʺ� �ڵ� ����
        targetWs.Range("A" & row & ":P" & row).EntireColumn.AutoFit
        
        ' ������ ���� �� ���� ���� �ڵ� ����
        targetWs.Range("A" & row & ":P" & row).EntireRow.AutoFit
            
        row = row + 1
        rs.MoveNext
        
    Wend
    
    targetWs.Range("A" & row & ":P" & row).Delete Shift:=xlShiftUp
    targetWs.Range("D" & row).value = WorksheetFunction.sum(targetWs.Range("D9:D" & row - 1))
    targetWs.Range("F" & row).value = WorksheetFunction.sum(targetWs.Range("F9:F" & row - 1))
    targetWs.Range("G" & row).value = WorksheetFunction.sum(targetWs.Range("G9:H" & row - 1))
    targetWs.Range("I" & row).value = WorksheetFunction.sum(targetWs.Range("I9:K" & row - 1))
    targetWs.Range("D7").value = "��" & Format(targetWs.Range("I" & row).value, "#,##0")

    ' ������ ���� �� ���� �ʺ� �ڵ� ����
    targetWs.Range("A" & row & ":P" & row).EntireColumn.AutoFit
        
    ' ������ ���� �� ���� ���� �ڵ� ����
    targetWs.Range("A" & row & ":P" & row).EntireRow.AutoFit

    ' A:9���� K:row-1������ ��� �׵θ��� �⺻���� ����
    targetWs.Range("A9:K" & row).Borders.LineStyle = xlContinuous
    targetWs.Range("A9:K" & row).Borders.Weight = xlThin
    ' A:9���� A:row-1������ ���� �׵θ��� ���� �β��� ����
    targetWs.Range("A9:A" & row - 1).Borders(xlEdgeLeft).Weight = xlMedium
    ' K:9���� K:row-1������ ������ �׵θ��� ���� �β��� ����
    targetWs.Range("K9:K" & row - 1).Borders(xlEdgeRight).Weight = xlMedium

    
    ' A:row, B:row, C:row�� ���յ� A:row ĭ �׵θ��� ���� �β��� ����
    targetWs.Range("A" & row & ":C" & row).Borders(xlEdgeTop).Weight = xlMedium
    targetWs.Range("A" & row & ":C" & row).Borders(xlEdgeBottom).Weight = xlMedium
    targetWs.Range("A" & row & ":C" & row).Borders(xlEdgeLeft).Weight = xlMedium
    targetWs.Range("A" & row & ":C" & row).Borders(xlEdgeRight).Weight = xlMedium

    ' D:row���� K:row���� �ٱ��� �׵θ��� ���� �β��� ����
    targetWs.Range("D" & row & ":K" & row).Borders(xlEdgeTop).Weight = xlMedium
    targetWs.Range("D" & row & ":K" & row).Borders(xlEdgeBottom).Weight = xlMedium
    targetWs.Range("D" & row & ":K" & row).Borders(xlEdgeLeft).Weight = xlMedium
    targetWs.Range("D" & row & ":K" & row).Borders(xlEdgeRight).Weight = xlMedium

    InsertPictureToMergedCells
    
    Dim printRange As Range
    Set printRange = targetWs.Range("A1:K" & row)
    
    With ActiveSheet.PageSetup
        .PrintArea = printRange.Address
    End With

    CWs.Range("E13").value = 0
    ' ���� ����
    rs.Close
    conn.Close

End Sub

Sub InsertPictureToMergedCells()
    
    Dim ws As Worksheet
    Set ws = ActiveSheet
    
    Dim mergedRange As Range
    Set mergedRange = ws.Range("J2:K3")
    
    Dim picFilePath As String
    picFilePath = "C:\Users\82103\Desktop\������\�ŷ����� �׸�.png"
    
    Dim picLeft As Double
    picLeft = mergedRange.Left
    
    Dim picTop As Double
    picTop = mergedRange.Top
    
    Dim picWidth As Double
    picWidth = mergedRange.Width
    
    Dim picHeight As Double
    picHeight = mergedRange.Height
    
    ws.Shapes.AddPicture Filename:=picFilePath, LinkToFile:=msoFalse, _
        SaveWithDocument:=msoTrue, Left:=picLeft, Top:=picTop, _
        Width:=picWidth, Height:=picHeight
        
End Sub

Sub SetDateValue(yValue As Integer, mValue As Integer, ws As Worksheet)
    Dim yearValue As String
    Dim monthValue As String
    Dim daysInMonth As Variant
    Dim days As Integer
    
    ' ���� ��������
    yearValue = yValue
    
    ' �� ��������
    monthValue = mValue
    
    ' ���� �ϼ��� �迭�� ����
    daysInMonth = Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    
    ' ���� ���� �ϼ��� �����ͼ� A2 ���� �Է�
    If monthValue >= 1 And monthValue <= 12 Then
        days = daysInMonth(monthValue - 1)
        
        ' ������ ��� 2�� �ϼ� ����
        If monthValue = 2 And IsLeapYear(yearValue) Then
            days = 29
        End If
        
        ' A2 ���� ��� �Է�
        ws.Range("A2").value = yearValue & "�� " & monthValue & "�� " & days & "��"
    Else
        MsgBox "�߸��� �� ���Դϴ�."
    End If
End Sub

Function IsLeapYear(ByVal year As Integer) As Boolean
    ' �������� Ȯ���ϴ� �Լ�
    If year Mod 4 = 0 Then
        If year Mod 100 <> 0 Or year Mod 400 = 0 Then
            IsLeapYear = True
        End If
    End If
End Function

------------------------------------------------------------------------

Module2

Option Explicit

Public myString As String
Public myString2 As Variant
Public year As Integer
Public month As Integer
Dim ComboBoxHandler As ComboBoxEventHandler

Sub PopulateComboBoxFromMySQL()
    Dim conn As Object
    Dim rs As Object
    Dim strSQL As String
    Dim emitterComboBox As OLEObject
    Dim myComboBox As Object
    Dim value As Variant
    Dim ws As Worksheet
    Set ws = ThisWorkbook.Sheets("Sheet1")
    
    ' Check if the ComboBox already exists and delete it if it does
    On Error Resume Next
    Set emitterComboBox = ws.OLEObjects("ComboBox1")
    On Error GoTo 0
    
    If Not emitterComboBox Is Nothing Then
        emitterComboBox.Delete
    End If
    
    ' Specify the position of the ComboBox
    Set emitterComboBox = ws.OLEObjects.Add(ClassType:="Forms.ComboBox.1", _
        Left:=210, Top:=135, Width:=285, Height:=20)
    
    Set myComboBox = emitterComboBox.Object
    
    year = ws.Range("E5").value
    month = ws.Range("F5").value
    myString2 = ws.Range("G5").value

    If Not IsDate(myString2) Or month < 1 Or month > 12 Then
        MsgBox "�ùٸ� ��¥ ������ �ƴմϴ�.", vbInformation
        Exit Sub
    End If
    
    myString2 = Format(myString2, "yy-mm")
    
    ' Connect to MySQL database
    Set conn = CreateObject("ADODB.Connection")
    conn.Open "DRIVER={MySQL ODBC 8.0 Unicode Driver};SERVER=localhost;DATABASE=db;UID=root;PWD=159cksruf!;"
    
    ' Prepare the SQL query
    strSQL = "SELECT DISTINCT Emitter FROM Transaction WHERE e_date LIKE '%" & myString2 & "%'"
    
    ' Execute the query
    Set rs = conn.Execute(strSQL)
    
    ' Check if there are any records returned
    Dim hasData As Boolean
    hasData = Not rs.EOF
    
    ' Allow screen updating before showing the message box
    DoEvents
    
    ' Check if there are any records returned
    If Not hasData Then
        MsgBox "�ش��ϴ� ��¥�� �����Ͱ� �����ϴ�.", vbInformation, "�˸�"
        Exit Sub
    End If
    
    ' Add values to the ComboBox
    With myComboBox
        Do Until rs.EOF
            value = rs.Fields(0).value
            .AddItem value
            rs.MoveNext
        Loop
    End With
    
    ' Create an instance of the ComboBoxEventHandler class
    Set ComboBoxHandler = New ComboBoxEventHandler
    ' Set the ComboBox reference to the class instance
    Set ComboBoxHandler.myComboBox = myComboBox
    
    ' Clean up resources
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
    
End Sub

-----------------------------------------------------------------------

ComboBoxEventHandler

Option Explicit

Public WithEvents myComboBox As MSForms.ComboBox

Public Sub MyComboBox_Change()
    Dim ComboBox As Object
    Set ComboBox = Sheets("Sheet1").OLEObjects("ComboBox1").Object
    
    ' ������ ���� �����ɴϴ�.
    Module2.myString = ComboBox.value
End Sub

