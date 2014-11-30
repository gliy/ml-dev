package in.iitd.mldev.core.visit;

import in.iitd.mldev.core.parse.ast.Bind;

import java.util.List;

public interface SMLVisitor {

	void visit(List<Bind> bindings);
}
