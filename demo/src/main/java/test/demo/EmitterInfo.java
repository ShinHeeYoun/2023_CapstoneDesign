package test.demo;

public class EmitterInfo {

    String emitterName = "";
    float emitMonth1 = 0;
    String emitMonth1Value = "";
    float emitMonth2 = 0;
    String emitMonth2Value = "";
    float emitMonth3 = 0;
    String emitMonth3Value = "";
    float emitMonth4 = 0;
    String emitMonth4Value = "";
    float emitMonth5 = 0;
    String emitMonth5Value = "";

    public EmitterInfo(String name, float month1,String month1Value,float month2,String month2Value,float month3,
                       String month3Value,float month4,String month4Value,float month5,String month5Value){
        setValue(name,month1,month1Value,month2,month2Value,month3,month3Value,month4,month4Value,month5,month5Value);
    }

    public void setValue(String name, float month1,String month1Value,float month2,String month2Value,float month3,
                         String month3Value,float month4,String month4Value,float month5,String month5Value){

        emitterName = name;
        emitMonth1 = month1;
        emitMonth1Value = month1Value;
        emitMonth2 = month2;
        emitMonth2Value = month2Value;
        emitMonth3 = month3;
        emitMonth3Value = month3Value;
        emitMonth4 = month4;
        emitMonth4Value = month4Value;
        emitMonth5 = month5;
        emitMonth5Value = month5Value;
    }
    public String getEmitterName(){
        return emitterName;
    }
    public String getEmitMonth1Value(){
        return emitMonth1Value;
    }
    public float getEmitMonth1(){
        return emitMonth1;
    }
    public String getEmitMonth2Value(){
        return emitMonth2Value;
    }
    public float getEmitMonth2(){
        return emitMonth2;
    }
    public String getEmitMonth3Value(){
        return emitMonth3Value;
    }
    public float getEmitMonth3(){
        return emitMonth3;
    }
    public String getEmitMonth4Value(){
        return emitMonth4Value;
    }
    public float getEmitMonth4(){
        return emitMonth4;
    }
    public String getEmitMonth5Value(){
        return emitMonth5Value;
    }
    public float getEmitMonth5(){
        return emitMonth5;
    }

}
