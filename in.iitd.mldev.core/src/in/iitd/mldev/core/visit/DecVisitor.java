package in.iitd.mldev.core.visit;

import in.iitd.mldev.core.parse.ast.AbstypeDec;
import in.iitd.mldev.core.parse.ast.DatatypeDec;
import in.iitd.mldev.core.parse.ast.Dec;
import in.iitd.mldev.core.parse.ast.ErrorDec;
import in.iitd.mldev.core.parse.ast.ExnDec;
import in.iitd.mldev.core.parse.ast.ExpDec;
import in.iitd.mldev.core.parse.ast.FctDec;
import in.iitd.mldev.core.parse.ast.FixityDec;
import in.iitd.mldev.core.parse.ast.FunDec;
import in.iitd.mldev.core.parse.ast.FunsigDec;
import in.iitd.mldev.core.parse.ast.LocalDec;
import in.iitd.mldev.core.parse.ast.OpenDec;
import in.iitd.mldev.core.parse.ast.RecValDec;
import in.iitd.mldev.core.parse.ast.SigDec;
import in.iitd.mldev.core.parse.ast.StrDec;
import in.iitd.mldev.core.parse.ast.TypeDec;
import in.iitd.mldev.core.parse.ast.ValDec;

public abstract class DecVisitor {

	public final void visit(SMLVisitor visitor, Dec dec) {
		if (dec instanceof AbstypeDec)visitAbstypeDec(visitor,(AbstypeDec)dec);
		if (dec instanceof DatatypeDec)visitDatatypeDec(visitor,(DatatypeDec)dec);
		if (dec instanceof ErrorDec)visitErrorDec(visitor,(ErrorDec)dec);
		if (dec instanceof ExnDec)visitExnDec(visitor,(ExnDec)dec);
		if (dec instanceof ExpDec)visitExpDec(visitor,(ExpDec)dec);
		if (dec instanceof FctDec)visitFctDec(visitor,(FctDec)dec);
		if (dec instanceof FixityDec)visitFixityDec(visitor,(FixityDec)dec);
		if (dec instanceof FunDec)visitFunDec(visitor,(FunDec)dec);
		if (dec instanceof FunsigDec)visitFunsigDec(visitor,(FunsigDec)dec);
		if (dec instanceof LocalDec)visitLocalDec(visitor,(LocalDec)dec);
		if (dec instanceof OpenDec)visitOpenDec(visitor,(OpenDec)dec);
		if (dec instanceof RecValDec)visitRecValDec(visitor,(RecValDec)dec);
		if (dec instanceof SigDec)visitSigDec(visitor,(SigDec)dec);
		if (dec instanceof StrDec)visitStrDec(visitor,(StrDec)dec);
		if (dec instanceof TypeDec)visitTypeDec(visitor,(TypeDec)dec);
		if (dec instanceof ValDec)visitValDec(visitor,(ValDec)dec);
	}
	
	public void visitAbstypeDec(SMLVisitor visitor,AbstypeDec dec){}
	public void visitDatatypeDec(SMLVisitor visitor,DatatypeDec dec){}
	public void visitErrorDec(SMLVisitor visitor,ErrorDec dec){}
	public void visitExnDec(SMLVisitor visitor,ExnDec dec){}
	public void visitExpDec(SMLVisitor visitor,ExpDec dec){}
	public void visitFctDec(SMLVisitor visitor,FctDec dec){}
	public void visitFixityDec(SMLVisitor visitor,FixityDec dec){}
	public void visitFunDec(SMLVisitor visitor,FunDec dec){}
	public void visitFunsigDec(SMLVisitor visitor,FunsigDec dec){}
	public void visitLocalDec(SMLVisitor visitor,LocalDec dec){}
	public void visitOpenDec(SMLVisitor visitor,OpenDec dec){}
	public void visitRecValDec(SMLVisitor visitor,RecValDec dec){}
	public void visitSigDec(SMLVisitor visitor,SigDec dec){}
	public void visitStrDec(SMLVisitor visitor,StrDec dec){}
	public void visitTypeDec(SMLVisitor visitor,TypeDec dec){}
	public void visitValDec(SMLVisitor visitor,ValDec dec){
		
	}
}
