// Generated from D:/code/github/debug-java-agent/src/main/antlr4/com/microsoft/azure/spring/condition/Condition.g4 by ANTLR 4.13.1
package com.microsoft.azure.spring.gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ConditionParser}.
 */
public interface ConditionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ConditionParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(ConditionParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConditionParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(ConditionParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpression}
	 * labeled alternative in {@link ConditionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpression(ConditionParser.BinaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpression}
	 * labeled alternative in {@link ConditionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpression(ConditionParser.BinaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link ConditionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenExpression(ConditionParser.ParenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link ConditionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenExpression(ConditionParser.ParenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comparatorExpression}
	 * labeled alternative in {@link ConditionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterComparatorExpression(ConditionParser.ComparatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comparatorExpression}
	 * labeled alternative in {@link ConditionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitComparatorExpression(ConditionParser.ComparatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConditionParser#comparator}.
	 * @param ctx the parse tree
	 */
	void enterComparator(ConditionParser.ComparatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConditionParser#comparator}.
	 * @param ctx the parse tree
	 */
	void exitComparator(ConditionParser.ComparatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConditionParser#binary}.
	 * @param ctx the parse tree
	 */
	void enterBinary(ConditionParser.BinaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConditionParser#binary}.
	 * @param ctx the parse tree
	 */
	void exitBinary(ConditionParser.BinaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConditionParser#operand}.
	 * @param ctx the parse tree
	 */
	void enterOperand(ConditionParser.OperandContext ctx) throws Exception;
	/**
	 * Exit a parse tree produced by {@link ConditionParser#operand}.
	 * @param ctx the parse tree
	 */
	void exitOperand(ConditionParser.OperandContext ctx);
}