// Generated from D:/code/github/debug-java-agent/src/main/antlr4/com/microsoft/azure/spring/condition/Condition.g4 by ANTLR 4.13.1
package com.microsoft.azure.spring.gen;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ConditionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		BOOLEAN=1, AND=2, OR=3, NOT=4, TRUE=5, FALSE=6, NULL=7, GT=8, GE=9, LT=10, 
		LE=11, EQ=12, NE=13, LPAREN=14, RPAREN=15, CHARACTER=16, NUMBER=17, STRING=18, 
		VARIABLE=19, PLACEHOLDER=20, WS=21;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"BOOLEAN", "AND", "OR", "NOT", "TRUE", "FALSE", "NULL", "GT", "GE", "LT", 
			"LE", "EQ", "NE", "LPAREN", "RPAREN", "CHARACTER", "NUMBER", "STRING", 
			"VARIABLE", "PLACEHOLDER", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'NOT'", "'true'", "'false'", "'null'", "'>'", 
			"'>='", "'<'", "'<='", "'=='", "'!='", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "BOOLEAN", "AND", "OR", "NOT", "TRUE", "FALSE", "NULL", "GT", "GE", 
			"LT", "LE", "EQ", "NE", "LPAREN", "RPAREN", "CHARACTER", "NUMBER", "STRING", 
			"VARIABLE", "PLACEHOLDER", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public ConditionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Condition.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0015\u009a\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0001\u0000\u0001\u0000\u0003\u0000.\b\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u00015\b\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002;\b\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\t\u0001"+
		"\t\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f"+
		"\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0003\u0010j\b\u0010\u0001"+
		"\u0010\u0004\u0010m\b\u0010\u000b\u0010\f\u0010n\u0001\u0010\u0001\u0010"+
		"\u0004\u0010s\b\u0010\u000b\u0010\f\u0010t\u0003\u0010w\b\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011}\b\u0011\n\u0011"+
		"\f\u0011\u0080\t\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0005\u0012\u0086\b\u0012\n\u0012\f\u0012\u0089\t\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0004\u0013\u008e\b\u0013\u000b\u0013\f\u0013\u008f"+
		"\u0001\u0013\u0001\u0013\u0001\u0014\u0004\u0014\u0095\b\u0014\u000b\u0014"+
		"\f\u0014\u0096\u0001\u0014\u0001\u0014\u0000\u0000\u0015\u0001\u0001\u0003"+
		"\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011"+
		"\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010"+
		"!\u0011#\u0012%\u0013\'\u0014)\u0015\u0001\u0000\u0006\u0001\u000009\u0004"+
		"\u0000\n\n\r\r\"\"\\\\\u0002\u0000\"\"\\\\\u0003\u0000AZ__az\u0005\u0000"+
		"..09AZ__az\u0003\u0000\t\n\f\r  \u00a5\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"+
		"\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013"+
		"\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017"+
		"\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"+
		"\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f"+
		"\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000"+
		"\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000"+
		"\u0000\u0000)\u0001\u0000\u0000\u0000\u0001-\u0001\u0000\u0000\u0000\u0003"+
		"4\u0001\u0000\u0000\u0000\u0005:\u0001\u0000\u0000\u0000\u0007<\u0001"+
		"\u0000\u0000\u0000\t@\u0001\u0000\u0000\u0000\u000bE\u0001\u0000\u0000"+
		"\u0000\rK\u0001\u0000\u0000\u0000\u000fP\u0001\u0000\u0000\u0000\u0011"+
		"R\u0001\u0000\u0000\u0000\u0013U\u0001\u0000\u0000\u0000\u0015W\u0001"+
		"\u0000\u0000\u0000\u0017Z\u0001\u0000\u0000\u0000\u0019]\u0001\u0000\u0000"+
		"\u0000\u001b`\u0001\u0000\u0000\u0000\u001db\u0001\u0000\u0000\u0000\u001f"+
		"d\u0001\u0000\u0000\u0000!i\u0001\u0000\u0000\u0000#x\u0001\u0000\u0000"+
		"\u0000%\u0083\u0001\u0000\u0000\u0000\'\u008a\u0001\u0000\u0000\u0000"+
		")\u0094\u0001\u0000\u0000\u0000+.\u0003\t\u0004\u0000,.\u0003\u000b\u0005"+
		"\u0000-+\u0001\u0000\u0000\u0000-,\u0001\u0000\u0000\u0000.\u0002\u0001"+
		"\u0000\u0000\u0000/0\u0005A\u0000\u000001\u0005N\u0000\u000015\u0005D"+
		"\u0000\u000023\u0005&\u0000\u000035\u0005&\u0000\u00004/\u0001\u0000\u0000"+
		"\u000042\u0001\u0000\u0000\u00005\u0004\u0001\u0000\u0000\u000067\u0005"+
		"O\u0000\u00007;\u0005R\u0000\u000089\u0005|\u0000\u00009;\u0005|\u0000"+
		"\u0000:6\u0001\u0000\u0000\u0000:8\u0001\u0000\u0000\u0000;\u0006\u0001"+
		"\u0000\u0000\u0000<=\u0005N\u0000\u0000=>\u0005O\u0000\u0000>?\u0005T"+
		"\u0000\u0000?\b\u0001\u0000\u0000\u0000@A\u0005t\u0000\u0000AB\u0005r"+
		"\u0000\u0000BC\u0005u\u0000\u0000CD\u0005e\u0000\u0000D\n\u0001\u0000"+
		"\u0000\u0000EF\u0005f\u0000\u0000FG\u0005a\u0000\u0000GH\u0005l\u0000"+
		"\u0000HI\u0005s\u0000\u0000IJ\u0005e\u0000\u0000J\f\u0001\u0000\u0000"+
		"\u0000KL\u0005n\u0000\u0000LM\u0005u\u0000\u0000MN\u0005l\u0000\u0000"+
		"NO\u0005l\u0000\u0000O\u000e\u0001\u0000\u0000\u0000PQ\u0005>\u0000\u0000"+
		"Q\u0010\u0001\u0000\u0000\u0000RS\u0005>\u0000\u0000ST\u0005=\u0000\u0000"+
		"T\u0012\u0001\u0000\u0000\u0000UV\u0005<\u0000\u0000V\u0014\u0001\u0000"+
		"\u0000\u0000WX\u0005<\u0000\u0000XY\u0005=\u0000\u0000Y\u0016\u0001\u0000"+
		"\u0000\u0000Z[\u0005=\u0000\u0000[\\\u0005=\u0000\u0000\\\u0018\u0001"+
		"\u0000\u0000\u0000]^\u0005!\u0000\u0000^_\u0005=\u0000\u0000_\u001a\u0001"+
		"\u0000\u0000\u0000`a\u0005(\u0000\u0000a\u001c\u0001\u0000\u0000\u0000"+
		"bc\u0005)\u0000\u0000c\u001e\u0001\u0000\u0000\u0000de\u0005\'\u0000\u0000"+
		"ef\t\u0000\u0000\u0000fg\u0005\'\u0000\u0000g \u0001\u0000\u0000\u0000"+
		"hj\u0005-\u0000\u0000ih\u0001\u0000\u0000\u0000ij\u0001\u0000\u0000\u0000"+
		"jl\u0001\u0000\u0000\u0000km\u0007\u0000\u0000\u0000lk\u0001\u0000\u0000"+
		"\u0000mn\u0001\u0000\u0000\u0000nl\u0001\u0000\u0000\u0000no\u0001\u0000"+
		"\u0000\u0000ov\u0001\u0000\u0000\u0000pr\u0005.\u0000\u0000qs\u0007\u0000"+
		"\u0000\u0000rq\u0001\u0000\u0000\u0000st\u0001\u0000\u0000\u0000tr\u0001"+
		"\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000uw\u0001\u0000\u0000\u0000"+
		"vp\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000w\"\u0001\u0000\u0000"+
		"\u0000x~\u0005\"\u0000\u0000y}\b\u0001\u0000\u0000z{\u0005\\\u0000\u0000"+
		"{}\u0007\u0002\u0000\u0000|y\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000"+
		"\u0000}\u0080\u0001\u0000\u0000\u0000~|\u0001\u0000\u0000\u0000~\u007f"+
		"\u0001\u0000\u0000\u0000\u007f\u0081\u0001\u0000\u0000\u0000\u0080~\u0001"+
		"\u0000\u0000\u0000\u0081\u0082\u0005\"\u0000\u0000\u0082$\u0001\u0000"+
		"\u0000\u0000\u0083\u0087\u0007\u0003\u0000\u0000\u0084\u0086\u0007\u0004"+
		"\u0000\u0000\u0085\u0084\u0001\u0000\u0000\u0000\u0086\u0089\u0001\u0000"+
		"\u0000\u0000\u0087\u0085\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000"+
		"\u0000\u0000\u0088&\u0001\u0000\u0000\u0000\u0089\u0087\u0001\u0000\u0000"+
		"\u0000\u008a\u008b\u0005$\u0000\u0000\u008b\u008d\u0005{\u0000\u0000\u008c"+
		"\u008e\u0007\u0004\u0000\u0000\u008d\u008c\u0001\u0000\u0000\u0000\u008e"+
		"\u008f\u0001\u0000\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000\u008f"+
		"\u0090\u0001\u0000\u0000\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u0091"+
		"\u0092\u0005}\u0000\u0000\u0092(\u0001\u0000\u0000\u0000\u0093\u0095\u0007"+
		"\u0005\u0000\u0000\u0094\u0093\u0001\u0000\u0000\u0000\u0095\u0096\u0001"+
		"\u0000\u0000\u0000\u0096\u0094\u0001\u0000\u0000\u0000\u0096\u0097\u0001"+
		"\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0099\u0006"+
		"\u0014\u0000\u0000\u0099*\u0001\u0000\u0000\u0000\r\u0000-4:intv|~\u0087"+
		"\u008f\u0096\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}