package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InterfaceMembersVisitor extends WebIDLBaseVisitor<List<InterfaceMember>> {

    private final String containingType;

    public InterfaceMembersVisitor(String containingType){
        this.containingType=containingType;
    }
    @Override
    public List<InterfaceMember> visitInterfaceMembers(WebIDLParser.InterfaceMembersContext ctx) {
        if (ctx.isEmpty()){
            return Collections.emptyList();
        }
        List<InterfaceMember> interfaceMembers=new ArrayList<>();
        for (WebIDLParser.InterfaceMembersContext members=ctx;members!=null;members=members.interfaceMembers()){
            InterfaceMember member=null;
            if (members.interfaceMember()!=null) {
                member = members.interfaceMember().accept(new InterfaceMemberVisitor(containingType));
            }
            //FIXME add misisng types
            if (member!=null) {
                interfaceMembers.add(member);
            }
        }
        return interfaceMembers;
    }
}
