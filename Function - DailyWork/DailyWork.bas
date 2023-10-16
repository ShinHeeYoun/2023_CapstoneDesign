Attribute VB_Name = "Module1"
Public filePath As String
Public FormPath As String
Public dataFilePath As String
Public RunVBAPath As String
Public YearInt As Integer
Public YearString As String
Public MonthString As String
Public DateString As String
Sub ChangePath()
    ' ���� Excel ������ �����ɴϴ�.
    Dim currentWorkbook As Workbook
    Set currentWorkbook = ActiveWorkbook
    ' G5 ���� ���� �����ɴϴ�.
    DateString = currentWorkbook.Sheets("Sheet1").Range("G5").Value
    YearInteger = currentWorkbook.Sheets("Sheet1").Range("D5").Value
    YearString = YearInteger - 2000
    MonthString = currentWorkbook.Sheets("Sheet1").Range("E5").Value
    ' �� �κи� �������ּ���!!
    filePath = currentWorkbook.Sheets("Sheet1").Range("D15").Value & "\" & DateString & " ���Ͼ���ǥ" & ".xlsx"
    FormPath = currentWorkbook.Sheets("Sheet1").Range("D16").Value
    dataFilePath = currentWorkbook.Sheets("Sheet1").Range("D17").Value & "\" & YearString & "�� 0" & MonthString & "�� �ùٷ�.xlsx"
    
    ' msg = dataFilePath & " <- �����"
    ' MsgBox msg, vbInformation, "�˸�"

End Sub
Sub StartVBAcode()
    Call Module1.ChangePath
    ' �˸��� �̸��� ���� ���ο� ���� ����
    Call Module1.CreateNewFile
    ' ��� ����
    Call Module1.CopyForm
    ' �� �ֱ� 1�ܰ�
    Call Module1.PutData1
    ' �� �ֱ� 2�ܰ�
    Call Module1.PutData2
End Sub
Sub CreateNewFile()
    ' �� Excel ������ ����ϴ�.
    Dim newWorkbook As Workbook
    Set newWorkbook = Workbooks.Add
    ' ������ �����մϴ�.
    newWorkbook.SaveAs Filename:=filePath
    ' ���� �˸�
    newWorkbook.Close SaveChanges:=True
    ' msg = DateString & " ���Ͼ���ǥ ������ �����Ǿ����ϴ�"
    ' MsgBox msg, vbInformation, "�˸�"
End Sub
Sub CopyForm()
    ' ����� ������ ��ũ�� ����
    Dim targetFP As String
    targetFP = filePath
    Dim target As Workbook
    Set target = Workbooks.Open(targetFP)
    Dim targetWs As Worksheet
    Set targetWs = target.Sheets("Sheet1")
    ' ����� ������ ��ũ�� ����
    Dim FormFP As String
    FormFP = FormPath
    Dim Form As Workbook
    Set Form = Workbooks.Open(FormFP)
    Dim FormWs As Worksheet
    Set FormWs = Form.Sheets("Sheet1")
    ' ���İ� ������ �����մϴ�.
    FormWs.Range("A1:U19").Copy
    targetWs.Range("A1:U19").PasteSpecial xlPasteAll
    Application.CutCopyMode = False
    ' ���� ���� ������ �ݽ��ϴ�.
    Form.Close SaveChanges:=False
    target.Close SaveChanges:=True
End Sub
Sub PutData1()
    ' ���� Excel ������ �����ɴϴ�.
    Dim currentWorkbook As Workbook
    Set currentWorkbook = ThisWorkbook
    Dim CWs As Worksheet
    Set CWs = currentWorkbook.Sheets("Sheet1")
    ' �����͸� ���� ���� ��������
    Dim targetFP As String
    targetFP = filePath
    Dim target As Workbook
    Set target = Workbooks.Open(targetFP)
    Dim targetWs As Worksheet
    Set targetWs = target.Sheets("Sheet1")
    ' ���� �ش��ϴ� ������ �ֱ�
    ' G5 --> A7
    targetWs.Range("A7").Value = CWs.Range("G5").Value
    targetWs.Range("A7").Select
    Selection.NumberFormatLocal = "[$-x-sysdate]dddd, mmmm dd, yyyy"
    ' D7 --> E9
    targetWs.Range("E9").Value = CWs.Range("D7").Value
    ' D8 --> H9
    targetWs.Range("H9").Value = CWs.Range("D8").Value
    ' D9 --> K9
    targetWs.Range("K9").Value = CWs.Range("D9").Value
    ' G8 --> N9
    targetWs.Range("N9").Value = CWs.Range("G8").Value
    ' �޸� �ֱ�
    targetWs.Range("N16").Value = CWs.Range("C11").Value
    
     ' ���� ���� ������ �ݽ��ϴ�.
    target.Close SaveChanges:=True
End Sub
Sub PutData2()
    ' Improved_CopyToResult
    ' ���� Excel ������ �����ɴϴ�.
    Dim currentWorkbook As Workbook
    Set currentWorkbook = ThisWorkbook
    ' G5 ���� ���� �����ɴϴ�.
    Dim DateString As String
    Dim DateDate As Date
    DateString = currentWorkbook.Sheets("Sheet1").Range("G5").Value
    DateDate = currentWorkbook.Sheets("Sheet1").Range("G5").Value
    ' �̰��� �����Ͱ� ����� ���� ���� ����
    Dim DataFP As String
    DataFP = dataFilePath
    Dim DataWorkbook As Workbook
    Set DataWorkbook = Workbooks.Open(DataFP)
    Dim DataWs1 As Worksheet
    Set DataWs1 = DataWorkbook.Sheets("Sheet")
    ' Result Sheet�� ������ �ʱ�ȭ / ������ ���� (1�ܰ�)
    Dim newWs As Worksheet
    Dim sheetExists As Boolean
    sheetExists = False
    For Each newWs In DataWorkbook.Worksheets
        If newWs.name = "Result" Then
            sheetExists = True
            Exit For
        End If
    Next newWs
    ' Result Sheet�� ������ �ʱ�ȭ / ������ ���� (2�ܰ�)
    If Not sheetExists Then
        ' ��Ʈ�� ���� ��� ����
        Set newWs = DataWorkbook.Worksheets.Add(After:=DataWorkbook.Sheets(DataWorkbook.Sheets.Count))
        newWs.name = "Result"
    Else
        ' ��Ʈ�� �ִ� ��� �ʱ�ȭ
        Set newWs = DataWorkbook.Sheets("Result")
        newWs.Cells.ClearContents
    End If
    ' Result Sheet �ʱ�ȭ
    ' DataWorkbook.Sheets("Result").UsedRange.ClearContents
    ' 2023�� 2�� 1�Ͽ� �ش��ϴ� ����� �����ؿ���
    Dim dateValue As Date
    dateValue = DateDate
    Dim destRow As Long
    destRow = 1
    For i = 1 To DataWorkbook.Sheets("Sheet").UsedRange.Rows.Count
        If DataWorkbook.Sheets("Sheet").Cells(i, "V").Value = dateValue Then
            DataWorkbook.Sheets("Result").Cells(destRow, "A").Value = DataWorkbook.Sheets("Sheet").Cells(i, "V").Value
            DataWorkbook.Sheets("Result").Cells(destRow, "B").Value = DataWorkbook.Sheets("Sheet").Cells(i, "AD").Value
            DataWorkbook.Sheets("Result").Cells(destRow, "C").Value = DataWorkbook.Sheets("Sheet").Cells(i, "AN").Value
            DataWorkbook.Sheets("Result").Cells(destRow, "D").Value = DataWorkbook.Sheets("Sheet").Cells(i, "AO").Value
            destRow = destRow + 1
        End If
    Next i
    ' yyyy�� mm�� dd�� ���·� �ٲٱ�
    Dim lastRow As Long
    lastRow = DataWorkbook.Sheets("Result").Cells(Rows.Count, "A").End(xlUp).row
    DataWorkbook.Sheets("Result").Range("A1:A" & lastRow).NumberFormat = "yyyy�� mm�� dd��"
    ' Ton --> kg
    Dim lastRow2 As Long
    lastRow2 = DataWorkbook.Sheets("Result").Cells(Rows.Count, "A").End(xlUp).row
    Dim row As Long
    For row = 2 To lastRow2
        If DataWorkbook.Sheets("Result").Range("D" & row).Value = "Ton" Then
            DataWorkbook.Sheets("Result").Range("C" & row).Value = DataWorkbook.Sheets("Result").Range("C" & row).Value * 1000
            DataWorkbook.Sheets("Result").Range("D" & row).Value = "kg"
        End If
    Next row
    ' B���� �������� ����
    Dim ws As Worksheet
    Set ws = DataWorkbook.Sheets("Result")
    With ws.Sort
        .SortFields.Add Key:=ws.Range("B1"), _
            SortOn:=xlSortOnValues, _
            Order:=xlAscending, _
            DataOption:=xlSortNormal
        .SetRange ws.Range("A1:E40") ' ������ ���� ����
        .Header = xlYes ' ù ��° �࿡ ����� ����
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    ' CaculateToal
    ' �� �Լ��� ���� ������ ����� �ѷ��� ����Ѵ�
    Dim lastRow3 As Long
    lastRow3 = DataWorkbook.Sheets("Result").Cells(Rows.Count, "B").End(xlUp).row
    Dim name As String
    Dim spending As Double
    Dim totalSpending As Double
    Dim i2 As Long
    ' 1) lastRow���� ������ �� ��ġ�� ����Ǿ� ����
    ' 2) 1����� ������ ����� �̵��ϸ鼭 B�� value�� ������ �ջ�, �ٸ��� �������� �Ѿ�� ��
    For i2 = 1 To lastRow3
        If name <> DataWorkbook.Sheets("Result").Cells(i2, "B").Value Then
            If name <> "" Then
                DataWorkbook.Sheets("Result").Range("E" & (i2 - 1)).Value = totalSpending
                totalSpending = 0
            End If
            name = DataWorkbook.Sheets("Result").Cells(i2, "B").Value
        End If
        spending = DataWorkbook.Sheets("Result").Cells(i2, "C").Value
        totalSpending = totalSpending + spending
    Next i2
    ' ������ ���� �� ó��
    DataWorkbook.Sheets("Result").Range("E" & lastRow3).Value = totalSpending
    ' ������ ���� �����Ѵ�. E�� ��������, �׷��� ���� E���� null�̸� ����������
    lastRow3 = DataWorkbook.Sheets("Result").Cells(Rows.Count, "A").End(xlUp).row ' A���� �������� ������ �� ã��
    With DataWorkbook.Sheets("Result").Range("A1:E" & lastRow3) ' ������ ���� ����
        .Sort Key1:=DataWorkbook.Sheets("Result").Range("E1"), Order1:=xlDescending, Header:=xlNo ' E���� �������� �������� ����
    End With
    
    ' �����͸� ���� ���� ���� ����
    Dim targetFP As String
    targetFP = filePath
    Dim target As Workbook
    Set target = Workbooks.Open(targetFP)
    Dim targetWs As Worksheet
    Set targetWs = target.Sheets("Sheet1")
    ' �ֱ�
    targetWs.Range("B10").Value = ws.Range("B1").Value
    targetWs.Range("B11").Value = ws.Range("B2").Value
    targetWs.Range("B12").Value = ws.Range("B3").Value
    targetWs.Range("K10").Value = ws.Range("E1").Value
    targetWs.Range("K11").Value = ws.Range("E2").Value
    targetWs.Range("K12").Value = ws.Range("E3").Value
    
    ' ��Ÿ ���ϱ�
    Dim sum As Double
    sum = Application.WorksheetFunction.sum(ws.Range("E4:E30"))
    targetWs.Range("K13").Value = sum
        
    ' ������ �߰� �� �ֱ�
    Dim rng As Range
    Dim randValue As Integer
    Dim randValue2 As Integer
    Dim randValue3 As Integer
    Dim randValue4 As Integer
    Dim result As Double
    Dim resultRate As Double
    
    ' 500���� 1200������ ������ ����
    Randomize
    randValue = Int((1200 - 500 + 1) * Rnd + 500)
    ' Sheet1�� H11���� ���ϱ�, 1000���� ������
    Set rng = targetWs.Range("K10")
    result = rng.Value * randValue / 1000
    resultRate = randValue / 1000
    ' ������� Sheet1�� H10�� �Է�
    targetWs.Range("H10") = result
    targetWs.Range("E10") = result * 30
    targetWs.Range("N10") = resultRate
    targetWs.Range("N10").NumberFormat = "0.00%"
    
    ' 500���� 1200������ ������ ����
    Randomize
    randValue2 = Int((1200 - 500 + 1) * Rnd + 500)
    ' Sheet1�� H11���� ���ϱ�, 1000���� ������
    Set rng = targetWs.Range("K11")
    result = rng.Value * randValue2 / 1000
    resultRate = randValue2 / 1000
    ' ������� Sheet1�� H10�� �Է�
    targetWs.Range("H11") = result
    targetWs.Range("E11") = result * 30
    targetWs.Range("N11") = resultRate
    targetWs.Range("N11").NumberFormat = "0.00%"
    
    ' 500���� 1200������ ������ ����
    Randomize
    randValue3 = Int((1200 - 500 + 1) * Rnd + 500)
    ' Sheet1�� H11���� ���ϱ�, 1000���� ������
    Set rng = targetWs.Range("K12")
    result = rng.Value * randValue3 / 1000
    resultRate = randValue3 / 1000
    ' ������� Sheet1�� H10�� �Է�
    targetWs.Range("H12") = result
    targetWs.Range("E12") = result * 30
    targetWs.Range("N12") = resultRate
    targetWs.Range("N12").NumberFormat = "0.00%"
    
    ' 500���� 1200������ ������ ����
    Randomize
    randValue4 = Int((1200 - 500 + 1) * Rnd + 500)
    ' Sheet1�� H11���� ���ϱ�, 1000���� ������
    Set rng = targetWs.Range("K13")
    result = rng.Value * randValue4 / 1000
    resultRate = randValue4 / 1000
    ' ������� Sheet1�� H10�� �Է�
    targetWs.Range("H13") = result
    targetWs.Range("E13") = result * 30
    targetWs.Range("N13") = resultRate
    targetWs.Range("N13").NumberFormat = "0.00%"
    
    ' �հ� ���ϱ�
    sum = Application.WorksheetFunction.sum(targetWs.Range("E10:E13"))
    targetWs.Range("E14").Value = sum
    sum = Application.WorksheetFunction.sum(targetWs.Range("H10:H13"))
    targetWs.Range("H14").Value = sum
    sum = Application.WorksheetFunction.sum(targetWs.Range("K10:K13"))
    targetWs.Range("K14").Value = sum
    targetWs.Range("N14").Value = targetWs.Range("K14") / targetWs.Range("H14").Value
    DataWorkbook.Close SaveChanges:=True
    target.Save
    
    msg = DateString & " ���Ͼ���ǥ ������ �����Ǿ����ϴ�"
    MsgBox msg, vbInformation, "�˸�"
End Sub
