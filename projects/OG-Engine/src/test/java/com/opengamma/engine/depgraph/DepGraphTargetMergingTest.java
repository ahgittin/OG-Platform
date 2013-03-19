/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.depgraph;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.AbstractFunction;
import com.opengamma.engine.function.CompiledFunctionDefinition;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionInvoker;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.id.UniqueId;
import com.opengamma.util.test.TestGroup;

/**
 * Tests the dependency graph building when two targets can be merged to create a composite equivalent to both.
 */
@Test(groups = TestGroup.UNIT)
public class DepGraphTargetMergingTest extends AbstractDependencyGraphBuilderTest {

  private static final Logger s_logger = LoggerFactory.getLogger(DepGraphTargetMergingTest.class);

  private static final class MergeableFunction extends AbstractFunction.NonCompiled {

    private final ValueRequirement _req1;
    private final ValueRequirement _req2;

    public MergeableFunction(final ValueRequirement req1, final ValueRequirement req2) {
      _req1 = req1;
      _req2 = req2;
    }

    @Override
    public ComputationTargetType getTargetType() {
      return ComputationTargetType.PRIMITIVE;
    }

    @Override
    public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target) {
      return Collections.singleton(new ValueSpecification("Foo", target.toSpecification(), createValueProperties().get()));
    }

    @Override
    public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
      if (target.getUniqueId().getValue().startsWith("0")) {
        return Collections.emptySet();
      } else if (target.getUniqueId().getValue().startsWith("1")) {
        return Collections.singleton(_req1);
      } else if (target.getUniqueId().getValue().startsWith("2")) {
        return Collections.singleton(_req2);
      } else if (target.getUniqueId().getValue().startsWith("3")) {
        return ImmutableSet.of(_req1, _req2);
      } else {
        throw new IllegalStateException();
      }
    }

    @Override
    public FunctionInvoker getFunctionInvoker() {
      return null;
    }

  }

  private static final class ConsumerFunction extends AbstractFunction.NonCompiled {

    @Override
    public ComputationTargetType getTargetType() {
      return ComputationTargetType.PRIMITIVE;
    }

    @Override
    public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target) {
      return Collections.singleton(new ValueSpecification("Bar", target.toSpecification(), createValueProperties().get()));
    }

    @Override
    public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
      return Collections.singleton(new ValueRequirement("Foo", target.toSpecification()));
    }

    @Override
    public FunctionInvoker getFunctionInvoker() {
      return null;
    }

  }

  @Override
  protected DepGraphTestHelper helper() {
    final DepGraphTestHelper helper = super.helper();
    helper.addFunctionProducing2();
    helper.addFunctionRequiring2Producing1();
    helper.getFunctionRepository().addFunction(new MergeableFunction(helper.getRequirement1(), helper.getRequirement2()));
    helper.getFunctionRepository().addFunction(new ConsumerFunction());
    return helper;
  }

  protected ComputationTargetCollapser collapser() {
    final DefaultComputationTargetCollapser collapser = new DefaultComputationTargetCollapser();
    collapser.addCollapser(ComputationTargetType.PRIMITIVE, new ComputationTargetCollapser() {

      private void add(final List<String> ids, final ComputationTargetSpecification target) {
        final String v = target.getUniqueId().getValue();
        for (int i = 0; i < v.length(); i += 2) {
          ids.add(v.substring(i, i + 2));
        }
      }

      @Override
      public boolean canApplyTo(final ComputationTargetSpecification a) {
        return "Test".equals(a.getUniqueId().getScheme());
      }

      @Override
      public ComputationTargetSpecification collapse(final CompiledFunctionDefinition function, final ComputationTargetSpecification a, final ComputationTargetSpecification b) {
        s_logger.debug("Collapse {} on {} + {}", new Object[] {function, a, b });
        if (function instanceof MergeableFunction) {
          final List<String> ids = new ArrayList<String>();
          add(ids, a);
          add(ids, b);
          Collections.sort(ids);
          final StringBuilder sb = new StringBuilder();
          for (final String id : ids) {
            sb.append(id);
          }
          return a.replaceIdentifier(UniqueId.of("Test", sb.toString()));
        } else {
          return null;
        }
      }

    });
    return collapser;
  }

  private Set<String> getTargets(final DependencyGraph graph) {
    final Collection<ComputationTargetSpecification> targets = graph.getAllComputationTargets();
    final Set<String> identifiers = Sets.newHashSetWithExpectedSize(targets.size());
    for (final ComputationTargetSpecification target : targets) {
      identifiers.add(target.getUniqueId().getValue());
    }
    return identifiers;
  }

  public void testNoInputsNoOutputs() {
    s_logger.info("testNoInputsNoOutputs");
    final DependencyGraphBuilder builder = helper().createBuilder(null);
    builder.setComputationTargetCollapser(collapser());
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "0A"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "0B"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "0C"))));
    final DependencyGraph graph = builder.getDependencyGraph();
    //graph.dumpStructureASCII(System.out);
    assertEquals(graph.getDependencyNodes().size(), 1); // Foo(0A0B0C)
    assertEquals(getTargets(graph), ImmutableSet.of("0A0B0C"));
  }

  public void testNoInputs() {
    s_logger.info("testNoInputs");
    final DepGraphTestHelper helper = helper();
    final DependencyGraphBuilder builder = helper.createBuilder(null);
    builder.setComputationTargetCollapser(collapser());
    builder.addTarget(new ValueRequirement("Bar", ComputationTargetSpecification.of(UniqueId.of("Test", "0A"))));
    builder.addTarget(new ValueRequirement("Bar", ComputationTargetSpecification.of(UniqueId.of("Test", "0B"))));
    builder.addTarget(new ValueRequirement("Bar", ComputationTargetSpecification.of(UniqueId.of("Test", "0C"))));
    final DependencyGraph graph = builder.getDependencyGraph();
    //graph.dumpStructureASCII(System.out);
    assertEquals(graph.getDependencyNodes().size(), 4); // Foo(0A0B0C) -> { Bar(0A), Bar(0B), Bar(0C) }
    assertEquals(getTargets(graph), ImmutableSet.of("0A0B0C", "0A", "0B", "0C"));
  }

  public void testNoOutputs() {
    s_logger.info("testNoOutputs");
    final DepGraphTestHelper helper = helper();
    final DependencyGraphBuilder builder = helper.createBuilder(null);
    builder.setComputationTargetCollapser(collapser());
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "0A"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "0B"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "0C"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "1A"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "1B"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "1C"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "2A"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "2B"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "2C"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "3A"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "3B"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "3C"))));
    final DependencyGraph graph = builder.getDependencyGraph();
    //graph.dumpStructureASCII(System.out);
    assertEquals(graph.getDependencyNodes().size(), 6); // Foo(0A0B0C), Req1 -> Foo(1A1B1C), Req2 -> Foo(2A2B2C), { Req1, Req2 } -> Foo(3A3B3C)
    assertEquals(getTargets(graph), ImmutableSet.of("0A0B0C", "1A1B1C", "2A2B2C", "3A3B3C", helper.getTarget().toSpecification().getUniqueId().getValue()));
  }

  public void testFull() {
    s_logger.info("testFull");
    final DepGraphTestHelper helper = helper();
    final DependencyGraphBuilder builder = helper.createBuilder(null);
    builder.setComputationTargetCollapser(collapser());
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "0A"))));
    builder.addTarget(new ValueRequirement("Bar", ComputationTargetSpecification.of(UniqueId.of("Test", "0B"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "0C"))));
    builder.addTarget(new ValueRequirement("Bar", ComputationTargetSpecification.of(UniqueId.of("Test", "1A"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "1B"))));
    builder.addTarget(new ValueRequirement("Bar", ComputationTargetSpecification.of(UniqueId.of("Test", "1C"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "2A"))));
    builder.addTarget(new ValueRequirement("Bar", ComputationTargetSpecification.of(UniqueId.of("Test", "2B"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "2C"))));
    builder.addTarget(new ValueRequirement("Bar", ComputationTargetSpecification.of(UniqueId.of("Test", "3A"))));
    builder.addTarget(new ValueRequirement("Foo", ComputationTargetSpecification.of(UniqueId.of("Test", "3B"))));
    builder.addTarget(new ValueRequirement("Bar", ComputationTargetSpecification.of(UniqueId.of("Test", "3C"))));
    final DependencyGraph graph = builder.getDependencyGraph();
    //graph.dumpStructureASCII(System.out);
    assertEquals(graph.getDependencyNodes().size(), 12); // Foo(0A0B0C) -> Bar(0B), Req1 -> Foo(1A1B1C) -> { Bar(1A), Bar(1C) }, Req2 -> Foo(2A2B2C) -> Bar(2B), { Req1, Req2 } -> Foo(3A3B3C) -> { Bar(3A), Bar (3C) }
    assertEquals(getTargets(graph),
        ImmutableSet.of("0A0B0C", "1A1B1C", "2A2B2C", "3A3B3C", helper.getTarget().toSpecification().getUniqueId().getValue(), "0B", "1A", "1C", "2B", "3A", "3C"));
  }
}
