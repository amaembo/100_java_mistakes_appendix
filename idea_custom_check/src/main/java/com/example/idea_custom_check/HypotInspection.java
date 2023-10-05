package com.example.idea_custom_check;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtil;
import com.siyeh.ig.callMatcher.CallMatcher;
import com.siyeh.ig.psiutils.EquivalenceChecker;

public class HypotInspection extends LocalInspectionTool {
  @Override
  public PsiElementVisitor buildVisitor
      (ProblemsHolder holder, boolean isOnTheFly) {
    return new Visitor(holder);
  }

  static boolean isSquare(PsiExpression expression) {
    expression = PsiUtil.skipParenthesizedExprDown(expression);
    if (!(expression instanceof PsiBinaryExpression mul))
      return false;
    IElementType token = mul.getOperationTokenType();
    if (!token.equals(JavaTokenType.ASTERISK)) return false;
    PsiExpression left = mul.getLOperand();
    PsiExpression right = mul.getROperand();
    var eq = EquivalenceChecker.getCanonicalPsiEquivalence();
    return eq.expressionsAreEquivalent(left, right);
  }

  private static class Visitor extends JavaElementVisitor {
    private static final CallMatcher SQRT_CALL =
        CallMatcher.staticCall("java.lang.Math", "sqrt")
            .parameterCount(1);

    private final ProblemsHolder holder;

    Visitor(ProblemsHolder holder) { this.holder = holder; }

    @Override
    public void visitMethodCallExpression
        (PsiMethodCallExpression call) {
      if (!SQRT_CALL.test(call)) return;
      var arg = call.getArgumentList().getExpressions()[0];
      arg = PsiUtil.skipParenthesizedExprDown(arg);
      if (!(arg instanceof PsiBinaryExpression sum)) return;
      IElementType token = sum.getOperationTokenType();
      if (!token.equals(JavaTokenType.PLUS)) return;
      if (isSquare(sum.getLOperand()) &&
          isSquare(sum.getROperand())) {
        holder.registerProblem(call, "Use Math.hypot()");
      }
    }
  }
}
