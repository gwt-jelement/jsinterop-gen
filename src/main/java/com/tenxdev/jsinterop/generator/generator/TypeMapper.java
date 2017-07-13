package com.tenxdev.jsinterop.generator.generator;

public enum TypeMapper {

    INSTANCE;

    public String map(String idlType) {
        if (idlType.startsWith("sequence<")){
            String type=idlType.replace("sequence<","").replace(">","");
            return map(type)+"[]";
        }
        switch (idlType) {
            case "unrestricteddouble":
            case "unrestricted double":
                return "double";
            case "unrestrictedfloat":
            case "unrestricted float":
                return "float";
            case "unsignedlong":
                return "long";
            case "unsignedshort":
                return "short";
            case "DOMString":
                return "String";
            case "DOMTimeStamp":
                return "long";
            case "DOMHighResTimeStamp":
                return "double";
            default:
                return idlType;
        }
    }

}
