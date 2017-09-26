package org.inferred.freebuilder.processor.excerpt;

import static org.inferred.freebuilder.processor.util.feature.FunctionPackage.FUNCTION_PACKAGE;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMultiset;
import com.google.common.collect.Multiset;

import org.inferred.freebuilder.processor.util.Excerpt;
import org.inferred.freebuilder.processor.util.ParameterizedType;
import org.inferred.freebuilder.processor.util.SourceBuilder;
import org.inferred.freebuilder.processor.util.LazyName;

import java.util.Collection;

import javax.annotation.Nullable;

/**
 * Excerpts defining a multiset implementation that delegates to a provided setCount method to
 * perform element validation and insertion into a backing multiset.
 */
public class CheckedMultiset extends Excerpt {

  public static final LazyName TYPE = new LazyName("CheckedMultiset", new CheckedMultiset());

  private CheckedMultiset() {}

  @Override
  public void addTo(SourceBuilder code) {
    ParameterizedType biConsumer = code.feature(FUNCTION_PACKAGE).biConsumer().get();
    code.addLine("")
        .addLine("/**")
        .addLine(" * A multiset implementation that delegates to a provided setCount method")
        .addLine(" * to perform element validation and insertion into a backing multiset.")
        .addLine(" */")
        .addLine("private static class %s<E> extends %s<E> {", TYPE, ForwardingMultiset.class)
        .addLine("")
        .addLine("  private final %s<E> multiset;", Multiset.class)
        .addLine("  private final %s<E, Integer> setCount;", biConsumer.getQualifiedName())
        .addLine("")
        .addLine("  %s(%s<E> multiset, %s<E, Integer> setCount) {",
            TYPE, Multiset.class, biConsumer.getQualifiedName())
        .addLine("    this.multiset = multiset;")
        .addLine("    this.setCount = setCount;")
        .addLine("  }")
        .addLine("")
        .addLine("  @Override protected %s<E> delegate() {", Multiset.class)
        .addLine("    return multiset;")
        .addLine("  }")
        .addLine("")
        .addLine("  @Override public boolean add(@%s E element) {", Nullable.class)
        .addLine("    return standardAdd(element);")
        .addLine("  }")
        .addLine("")
        .addLine("  @Override public int add(@%s E element, int occurrences) {",
            Nullable.class)
        .addLine("    %s.checkArgument(occurrences >= 0,", Preconditions.class)
        .addLine("        \"occurrences cannot be negative: %%s\", occurrences);")
        .addLine("    int oldCount = multiset.count(element);")
        .addLine("    if (occurrences > 0) {")
        .addLine("      long newCount = (long) oldCount + occurrences;")
        .addLine("      %s.checkArgument(newCount <= %s.MAX_VALUE,",
            Preconditions.class, Integer.class)
        .addLine("          \"too many occurrences: %%s\", newCount);")
        .addLine("      setCount.accept(element, (int) newCount);")
        .addLine("    }")
        .addLine("    return oldCount;")
        .addLine("  }")
        .addLine("")
        .addLine("  @Override public boolean addAll(%s<? extends E> elementsToAdd) {",
            Collection.class)
        .addLine("    return standardAddAll(elementsToAdd);")
        .addLine("  }")
        .addLine("")
        .addLine("  @Override public int setCount(@%s E element, int count) {",
            Nullable.class)
        .addLine("    return standardSetCount(element, count);")
        .addLine("  }")
        .addLine("")
        .addLine("  @Override public boolean setCount(")
        .addLine("      @%s E element, int oldCount, int newCount) {", Nullable.class)
        .addLine("    return standardSetCount(element, oldCount, newCount);")
        .addLine("  }")
        .addLine("}");
  }

  @Override
  protected void addFields(FieldReceiver fields) {}
}