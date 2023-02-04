package Engine.var;

import java.util.HashMap;

public class Var {
    public static HashMap<String,VarType> ValueMap = new HashMap<String,VarType>();
    public static Boolean createVarType(String code) {
        try{
            String TMP = code;
            String VarSplit = code.substring(0,4);
            if (VarSplit.equals("var ")) {
                String varName = code.substring(code.indexOf("var ")+4,code.indexOf("="));
                varName = varName.replace(" ","");
                TMP = TMP.replace(" ","");
                String varValue = TMP.substring(TMP.indexOf("=")+1);

                VarType varType = new VarType();
                varType.setName(varName);
                varType.setValue(Var.getReal_str(varValue));

                Var.ValueMap.put(varName,varType);
                return true;
            }else {
                return false;
            }
        }catch (Exception exception){
            return false;
        }
    }
    public static String getReal_str(String str) {
        String type = VarType.getType(str);
        if (type.equals("string")) {
            return str.substring(str.indexOf("'")+1,str.lastIndexOf("'"));
        }else {
            return str;
        }
    }
    public static String DealVar(String str,HashMap<String,VarType> hashMap) {
        try{
            String TMP_str = str.replace("  ","");
            int s = TMP_str.indexOf("$");
            if (s != -1) {
                TMP_str = TMP_str.toLowerCase();
                String TMPNAME = TMP_str.substring(s);
                String VarName = TMPNAME.substring(0,Var.endVar(TMPNAME));
                String getVarValue = Var.ValueMap.get(VarName.substring(1)).getValue();
                if (getVarValue == null) {
                    System.out.println("[ERR] Can not know: "+VarName.substring(1));
                    return "/213/21/ 1/2/d1/2/3//d/sd/ddas/d/12/32 /213/21/3??sdsadsadsa wr2";
                }else {
                    return str.replace(VarName,getVarValue);
                }
            }else {
                return str;
            }
        }
        catch (Exception exception){
            return str;
        }
    }
    public static int endVar(String str) {
        String[] s = {
                "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y"
                ,"z"
        };
        int e = str.indexOf("$");
        int r = -1;
        if (e != -1) {
            String tmpVar = str.substring(e+1);
            for (int i = 0 ; i < tmpVar.length() ;i++) {
                String charset = tmpVar.substring(i,i+1);
                for (int j = 0 ; j < s.length ; j++) {
                    if (i < tmpVar.length() -1) {
                        r = i;
                        break;
                    }
                    if (!s[j].equals(charset)) {
                        r = i + 1;
                        break;
                    }
                }
            }
            return r;
        }else {
            return -1;
        }
    }
}
