package in.iitd.mldev.core.visit;

import in.iitd.mldev.core.parse.ast.Bind;
import in.iitd.mldev.core.parse.ast.ConBind;
import in.iitd.mldev.core.parse.ast.DatatypeBind;
import in.iitd.mldev.core.parse.ast.ExnBind;
import in.iitd.mldev.core.parse.ast.FctBind;
import in.iitd.mldev.core.parse.ast.FunBind;
import in.iitd.mldev.core.parse.ast.FunsigBind;
import in.iitd.mldev.core.parse.ast.RecValBind;
import in.iitd.mldev.core.parse.ast.SigBind;
import in.iitd.mldev.core.parse.ast.StrBind;
import in.iitd.mldev.core.parse.ast.TypeBind;
import in.iitd.mldev.core.parse.ast.ValBind;

public class BindVisitor {

	public void visit(SMLVisitor visitor, Bind bind) {
		if (bind instanceof ConBind)visitConBind(visitor,(ConBind)bind);
		if (bind instanceof DatatypeBind)visitDatatypeBind(visitor,(DatatypeBind)bind);
		if (bind instanceof ExnBind)visitExnBind(visitor,(ExnBind)bind);
		if (bind instanceof FctBind)visitFctBind(visitor,(FctBind)bind);
		if (bind instanceof FunBind)visitFunBind(visitor,(FunBind)bind);
		if (bind instanceof FunsigBind)visitFunsigBind(visitor,(FunsigBind)bind);
		if (bind instanceof RecValBind)visitRecValBind(visitor,(RecValBind)bind);
		if (bind instanceof SigBind)visitSigBind(visitor,(SigBind)bind);
		if (bind instanceof StrBind)visitStrBind(visitor,(StrBind)bind);
		if (bind instanceof TypeBind)visitTypeBind(visitor,(TypeBind)bind);
		if (bind instanceof ValBind)visitValBind(visitor,(ValBind)bind);
	}
	
	public void visitConBind(SMLVisitor visitor, ConBind bind){}
	public void visitDatatypeBind(SMLVisitor visitor, DatatypeBind bind){}
	public void visitExnBind(SMLVisitor visitor, ExnBind bind){}
	public void visitFctBind(SMLVisitor visitor, FctBind bind){}
	public void visitFunBind(SMLVisitor visitor, FunBind bind){}
	public void visitFunsigBind(SMLVisitor visitor, FunsigBind bind){}
	public void visitRecValBind(SMLVisitor visitor, RecValBind bind){}
	public void visitSigBind(SMLVisitor visitor, SigBind bind){}
	public void visitStrBind(SMLVisitor visitor, StrBind bind){}
	public void visitTypeBind(SMLVisitor visitor, TypeBind bind){}
	public void visitValBind(SMLVisitor visitor, ValBind bind){}
}
