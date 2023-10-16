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
    ' 현재 Excel 파일을 가져옵니다.
    Dim currentWorkbook As Workbook
    Set currentWorkbook = ActiveWorkbook
    ' G5 셀의 값을 가져옵니다.
    DateString = currentWorkbook.Sheets("Sheet1").Range("G5").Value
    YearInteger = currentWorkbook.Sheets("Sheet1").Range("D5").Value
    YearString = YearInteger - 2000
    MonthString = currentWorkbook.Sheets("Sheet1").Range("E5").Value
    ' 이 부분만 수정해주세요!!
    filePath = currentWorkbook.Sheets("Sheet1").Range("D15").Value & "\" & DateString & " 일일업무표" & ".xlsx"
    FormPath = currentWorkbook.Sheets("Sheet1").Range("D16").Value
    dataFilePath = currentWorkbook.Sheets("Sheet1").Range("D17").Value & "\" & YearString & "년 0" & MonthString & "월 올바로.xlsx"
    
    ' msg = dataFilePath & " <- 디버그"
    ' MsgBox msg, vbInformation, "알림"

End Sub
Sub StartVBAcode()
    Call Module1.ChangePath
    ' 알맞은 이름을 가진 새로운 엑셀 생성
    Call Module1.CreateNewFile
    ' 양식 복사
    Call Module1.CopyForm
    ' 값 넣기 1단계
    Call Module1.PutData1
    ' 값 넣기 2단계
    Call Module1.PutData2
End Sub
Sub CreateNewFile()
    ' 새 Excel 파일을 만듭니다.
    Dim newWorkbook As Workbook
    Set newWorkbook = Workbooks.Add
    ' 파일을 저장합니다.
    newWorkbook.SaveAs Filename:=filePath
    ' 성공 알림
    newWorkbook.Close SaveChanges:=True
    ' msg = DateString & " 일일업무표 파일이 생성되었습니다"
    ' MsgBox msg, vbInformation, "알림"
End Sub
Sub CopyForm()
    ' 양식을 적용할 워크북 열기
    Dim targetFP As String
    targetFP = filePath
    Dim target As Workbook
    Set target = Workbooks.Open(targetFP)
    Dim targetWs As Worksheet
    Set targetWs = target.Sheets("Sheet1")
    ' 양식을 가져올 워크북 열기
    Dim FormFP As String
    FormFP = FormPath
    Dim Form As Workbook
    Set Form = Workbooks.Open(FormFP)
    Dim FormWs As Worksheet
    Set FormWs = Form.Sheets("Sheet1")
    ' 형식과 내용을 복사합니다.
    FormWs.Range("A1:U19").Copy
    targetWs.Range("A1:U19").PasteSpecial xlPasteAll
    Application.CutCopyMode = False
    ' 원본 엑셀 파일을 닫습니다.
    Form.Close SaveChanges:=False
    target.Close SaveChanges:=True
End Sub
Sub PutData1()
    ' 현재 Excel 파일을 가져옵니다.
    Dim currentWorkbook As Workbook
    Set currentWorkbook = ThisWorkbook
    Dim CWs As Worksheet
    Set CWs = currentWorkbook.Sheets("Sheet1")
    ' 데이터를 넣을 파일 가져오기
    Dim targetFP As String
    targetFP = filePath
    Dim target As Workbook
    Set target = Workbooks.Open(targetFP)
    Dim targetWs As Worksheet
    Set targetWs = target.Sheets("Sheet1")
    ' 각각 해당하는 데이터 넣기
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
    ' 메모 넣기
    targetWs.Range("N16").Value = CWs.Range("C11").Value
    
     ' 원본 엑셀 파일을 닫습니다.
    target.Close SaveChanges:=True
End Sub
Sub PutData2()
    ' Improved_CopyToResult
    ' 현재 Excel 파일을 가져옵니다.
    Dim currentWorkbook As Workbook
    Set currentWorkbook = ThisWorkbook
    ' G5 셀의 값을 가져옵니다.
    Dim DateString As String
    Dim DateDate As Date
    DateString = currentWorkbook.Sheets("Sheet1").Range("G5").Value
    DateDate = currentWorkbook.Sheets("Sheet1").Range("G5").Value
    ' 미가공 데이터가 저장된 엑셀 파일 열기
    Dim DataFP As String
    DataFP = dataFilePath
    Dim DataWorkbook As Workbook
    Set DataWorkbook = Workbooks.Open(DataFP)
    Dim DataWs1 As Worksheet
    Set DataWs1 = DataWorkbook.Sheets("Sheet")
    ' Result Sheet가 있으면 초기화 / 없으면 생성 (1단계)
    Dim newWs As Worksheet
    Dim sheetExists As Boolean
    sheetExists = False
    For Each newWs In DataWorkbook.Worksheets
        If newWs.name = "Result" Then
            sheetExists = True
            Exit For
        End If
    Next newWs
    ' Result Sheet가 있으면 초기화 / 없으면 생성 (2단계)
    If Not sheetExists Then
        ' 시트가 없는 경우 생성
        Set newWs = DataWorkbook.Worksheets.Add(After:=DataWorkbook.Sheets(DataWorkbook.Sheets.Count))
        newWs.name = "Result"
    Else
        ' 시트가 있는 경우 초기화
        Set newWs = DataWorkbook.Sheets("Result")
        newWs.Cells.ClearContents
    End If
    ' Result Sheet 초기화
    ' DataWorkbook.Sheets("Result").UsedRange.ClearContents
    ' 2023년 2월 1일에 해당하는 행들을 복사해오기
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
    ' yyyy년 mm월 dd일 형태로 바꾸기
    Dim lastRow As Long
    lastRow = DataWorkbook.Sheets("Result").Cells(Rows.Count, "A").End(xlUp).row
    DataWorkbook.Sheets("Result").Range("A1:A" & lastRow).NumberFormat = "yyyy년 mm월 dd일"
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
    ' B열로 기준으로 정렬
    Dim ws As Worksheet
    Set ws = DataWorkbook.Sheets("Result")
    With ws.Sort
        .SortFields.Add Key:=ws.Range("B1"), _
            SortOn:=xlSortOnValues, _
            Order:=xlAscending, _
            DataOption:=xlSortNormal
        .SetRange ws.Range("A1:E40") ' 정렬할 범위 지정
        .Header = xlYes ' 첫 번째 행에 헤더가 있음
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    ' CaculateToal
    ' 이 함수를 통해 각각의 기업의 총량을 계산한다
    Dim lastRow3 As Long
    lastRow3 = DataWorkbook.Sheets("Result").Cells(Rows.Count, "B").End(xlUp).row
    Dim name As String
    Dim spending As Double
    Dim totalSpending As Double
    Dim i2 As Long
    ' 1) lastRow에는 마지막 행 위치가 저장되어 있음
    ' 2) 1행부터 마지막 행까지 이동하면서 B열 value가 같으면 합산, 다르면 다음으로 넘어가는 식
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
    ' 마지막 행의 값 처리
    DataWorkbook.Sheets("Result").Range("E" & lastRow3).Value = totalSpending
    ' 구해진 값을 정렬한다. E열 기준으로, 그런데 이제 E열이 null이면 지워버리는
    lastRow3 = DataWorkbook.Sheets("Result").Cells(Rows.Count, "A").End(xlUp).row ' A열을 기준으로 마지막 행 찾기
    With DataWorkbook.Sheets("Result").Range("A1:E" & lastRow3) ' 정렬할 범위 지정
        .Sort Key1:=DataWorkbook.Sheets("Result").Range("E1"), Order1:=xlDescending, Header:=xlNo ' E열을 기준으로 내림차순 정렬
    End With
    
    ' 데이터를 넣을 엑셀 파일 열기
    Dim targetFP As String
    targetFP = filePath
    Dim target As Workbook
    Set target = Workbooks.Open(targetFP)
    Dim targetWs As Worksheet
    Set targetWs = target.Sheets("Sheet1")
    ' 넣기
    targetWs.Range("B10").Value = ws.Range("B1").Value
    targetWs.Range("B11").Value = ws.Range("B2").Value
    targetWs.Range("B12").Value = ws.Range("B3").Value
    targetWs.Range("K10").Value = ws.Range("E1").Value
    targetWs.Range("K11").Value = ws.Range("E2").Value
    targetWs.Range("K12").Value = ws.Range("E3").Value
    
    ' 기타 구하기
    Dim sum As Double
    sum = Application.WorksheetFunction.sum(ws.Range("E4:E30"))
    targetWs.Range("K13").Value = sum
        
    ' 비어보여서 추가 값 넣기
    Dim rng As Range
    Dim randValue As Integer
    Dim randValue2 As Integer
    Dim randValue3 As Integer
    Dim randValue4 As Integer
    Dim result As Double
    Dim resultRate As Double
    
    ' 500부터 1200까지의 랜덤값 생성
    Randomize
    randValue = Int((1200 - 500 + 1) * Rnd + 500)
    ' Sheet1의 H11값과 곱하기, 1000으로 나누기
    Set rng = targetWs.Range("K10")
    result = rng.Value * randValue / 1000
    resultRate = randValue / 1000
    ' 결과값을 Sheet1의 H10에 입력
    targetWs.Range("H10") = result
    targetWs.Range("E10") = result * 30
    targetWs.Range("N10") = resultRate
    targetWs.Range("N10").NumberFormat = "0.00%"
    
    ' 500부터 1200까지의 랜덤값 생성
    Randomize
    randValue2 = Int((1200 - 500 + 1) * Rnd + 500)
    ' Sheet1의 H11값과 곱하기, 1000으로 나누기
    Set rng = targetWs.Range("K11")
    result = rng.Value * randValue2 / 1000
    resultRate = randValue2 / 1000
    ' 결과값을 Sheet1의 H10에 입력
    targetWs.Range("H11") = result
    targetWs.Range("E11") = result * 30
    targetWs.Range("N11") = resultRate
    targetWs.Range("N11").NumberFormat = "0.00%"
    
    ' 500부터 1200까지의 랜덤값 생성
    Randomize
    randValue3 = Int((1200 - 500 + 1) * Rnd + 500)
    ' Sheet1의 H11값과 곱하기, 1000으로 나누기
    Set rng = targetWs.Range("K12")
    result = rng.Value * randValue3 / 1000
    resultRate = randValue3 / 1000
    ' 결과값을 Sheet1의 H10에 입력
    targetWs.Range("H12") = result
    targetWs.Range("E12") = result * 30
    targetWs.Range("N12") = resultRate
    targetWs.Range("N12").NumberFormat = "0.00%"
    
    ' 500부터 1200까지의 랜덤값 생성
    Randomize
    randValue4 = Int((1200 - 500 + 1) * Rnd + 500)
    ' Sheet1의 H11값과 곱하기, 1000으로 나누기
    Set rng = targetWs.Range("K13")
    result = rng.Value * randValue4 / 1000
    resultRate = randValue4 / 1000
    ' 결과값을 Sheet1의 H10에 입력
    targetWs.Range("H13") = result
    targetWs.Range("E13") = result * 30
    targetWs.Range("N13") = resultRate
    targetWs.Range("N13").NumberFormat = "0.00%"
    
    ' 합계 구하기
    sum = Application.WorksheetFunction.sum(targetWs.Range("E10:E13"))
    targetWs.Range("E14").Value = sum
    sum = Application.WorksheetFunction.sum(targetWs.Range("H10:H13"))
    targetWs.Range("H14").Value = sum
    sum = Application.WorksheetFunction.sum(targetWs.Range("K10:K13"))
    targetWs.Range("K14").Value = sum
    targetWs.Range("N14").Value = targetWs.Range("K14") / targetWs.Range("H14").Value
    DataWorkbook.Close SaveChanges:=True
    target.Save
    
    msg = DateString & " 일일업무표 파일이 생성되었습니다"
    MsgBox msg, vbInformation, "알림"
End Sub
