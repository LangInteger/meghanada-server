package meghanada.analyze;

import static java.util.Objects.nonNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;
import meghanada.reflect.CandidateUnit;
import meghanada.reflect.FieldDescriptor;
import meghanada.reflect.MemberDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Variable implements Serializable, Symbol {

  private static final long serialVersionUID = 456932034908443221L;
  private static Logger log = LogManager.getLogger(Variable.class);

  public final String name;
  public final int pos;
  public final Range range;
  public String fqcn;
  public boolean isDef;
  public boolean isParameter;
  public boolean isField;
  public int argumentIndex = -1;
  public String modifier;
  public String declaringClass;

  public Variable(final String name, final int pos, final Range range) {
    this.name = name;
    this.pos = pos;
    this.range = range;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("modifier", modifier)
        .add("name", name)
        .add("fqcn", fqcn)
        .add("range", range)
        .add("isField", isField)
        .add("isDef", isDef)
        .add("isParameter", isParameter)
        .add("pos", pos)
        .toString();
  }

  public boolean isDecl() {
    return isDef;
  }

  public CandidateUnit toCandidateUnit() {
    return FieldDescriptor.createVar("", this.name, this.fqcn);
  }

  public Optional<MemberDescriptor> toMemberDescriptor() {
    if (this.isField && this.isDef && nonNull(this.modifier)) {
      FieldDescriptor descriptor =
          new FieldDescriptor(this.declaringClass, this.name, this.modifier, this.fqcn);
      descriptor.setTypeParameters(Collections.emptySet());
      return Optional.of(descriptor);
    }
    return Optional.empty();
  }

  @Override
  public String getFQCN() {
    return this.fqcn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Variable variable = (Variable) o;
    return pos == variable.pos
        && Objects.equal(name, variable.name)
        && Objects.equal(fqcn, variable.fqcn);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, pos, fqcn);
  }
}
