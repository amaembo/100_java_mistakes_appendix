package com.example;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.matchers.Matcher;
import com.google.errorprone.matchers.Matchers;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

@AutoService(BugChecker.class)
@BugPattern(name = "MathHypotCanBeUsed",
        summary = "Math.hypot() can be used instead",
        severity = BugPattern.SeverityLevel.WARNING)
public class HypotChecker extends BugChecker implements BugChecker.MethodInvocationTreeMatcher {
  // Matches expression like x * x
  static final Matcher<ExpressionTree> SQUARE_MATCHER =
    Matchers.ignoreParens(Matchers.allOf(Matchers.kindIs(Tree.Kind.MULTIPLY),
      (expr, state) -> ASTHelpers.sameVariable(
        ((BinaryTree) expr).getLeftOperand(), ((BinaryTree) expr).getRightOperand())));

  // Matches expression like x * x + y * y
  static final Matcher<ExpressionTree> SUM_OF_SQUARES_MATCHER =
    Matchers.ignoreParens(Matchers.allOf(Matchers.kindIs(Tree.Kind.PLUS),
      (expr, state) -> SQUARE_MATCHER.matches(((BinaryTree) expr).getLeftOperand(), state),
      (expr, state) -> SQUARE_MATCHER.matches(((BinaryTree) expr).getRightOperand(), state)));

  // Matches expression like Math.sqrt(x * x + y * y)
  static final Matcher<MethodInvocationTree> SQRT_MATCHER = Matchers.allOf(
    Matchers.staticMethod().onClass("java.lang.Math").named("sqrt"),
    Matchers.argument(0, SUM_OF_SQUARES_MATCHER));

  @Override
  public Description matchMethodInvocation(MethodInvocationTree tree, VisitorState state) {
    return SQRT_MATCHER.matches(tree, state) ?
            describeMatch(tree) :
            Description.NO_MATCH;
  }
}
