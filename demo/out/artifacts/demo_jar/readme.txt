221 거래명세서 경로
456줄 일일업무표 경로

서버 연결 오류나면 
1.C:\Program Files\Java\jdk-20\conf\security\java.security 열고

2. jdk.tls.disabledAlgorithms=SSLv3, TLSv1, TLSv1.1, RC4, DES, MD5withRSA, \
     DH keySize < 1024, EC keySize < 224, 3DES_EDE_CBC, anon, NULL, \
     include jdk.disabled.namedCurves
에서 TLSv1, TLSv1.1 지우기
