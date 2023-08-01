// Generated from D:/IntelliJ IDEA 2023.1.3/Compiler/src\Mx.g4 by ANTLR 4.12.0
package src.paser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class MxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, VOID=36, BOOL=37, INT=38, STRING=39, 
		NEW=40, CLASS=41, NULL=42, TRUE=43, FALSE=44, THIS=45, IF=46, ELSE=47, 
		FOR=48, WHILE=49, BREAK=50, CONTINUE=51, RETURN=52, Identifier=53, WhiteSpace=54, 
		Comments=55, DecNumber=56, String=57;
	public static final int
		RULE_program = 0, RULE_mainDef = 1, RULE_classDef = 2, RULE_functionDef = 3, 
		RULE_suite = 4, RULE_statement = 5, RULE_selectStatement = 6, RULE_loopStatement = 7, 
		RULE_jumpStatement = 8, RULE_variableDef = 9, RULE_initVariable = 10, 
		RULE_typeName = 11, RULE_expression = 12, RULE_parallelExp = 13, RULE_brackets = 14;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "mainDef", "classDef", "functionDef", "suite", "statement", 
			"selectStatement", "loopStatement", "jumpStatement", "variableDef", "initVariable", 
			"typeName", "expression", "parallelExp", "brackets"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'main'", "'('", "')'", "'{'", "'}'", "','", "'='", "'.'", 
			"'['", "']'", "'++'", "'--'", "'-'", "'!'", "'~'", "'*'", "'/'", "'%'", 
			"'+'", "'<<'", "'>>'", "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", 
			"'&'", "'^'", "'|'", "'&&'", "'||'", "'?'", "':'", "'void'", "'bool'", 
			"'int'", "'string'", "'new'", "'class'", "'null'", "'true'", "'false'", 
			"'this'", "'if'", "'else'", "'for'", "'while'", "'break'", "'continue'", 
			"'return'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			"VOID", "BOOL", "INT", "STRING", "NEW", "CLASS", "NULL", "TRUE", "FALSE", 
			"THIS", "IF", "ELSE", "FOR", "WHILE", "BREAK", "CONTINUE", "RETURN", 
			"Identifier", "WhiteSpace", "Comments", "DecNumber", "String"
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

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public MainDefContext mainDef() {
			return getRuleContext(MainDefContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MxParser.EOF, 0); }
		public List<ClassDefContext> classDef() {
			return getRuleContexts(ClassDefContext.class);
		}
		public ClassDefContext classDef(int i) {
			return getRuleContext(ClassDefContext.class,i);
		}
		public List<FunctionDefContext> functionDef() {
			return getRuleContexts(FunctionDefContext.class);
		}
		public FunctionDefContext functionDef(int i) {
			return getRuleContext(FunctionDefContext.class,i);
		}
		public List<VariableDefContext> variableDef() {
			return getRuleContexts(VariableDefContext.class);
		}
		public VariableDefContext variableDef(int i) {
			return getRuleContext(VariableDefContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(35);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
					case 1:
						{
						setState(30);
						classDef();
						}
						break;
					case 2:
						{
						setState(31);
						functionDef();
						}
						break;
					case 3:
						{
						setState(32);
						variableDef();
						setState(33);
						match(T__0);
						}
						break;
					}
					} 
				}
				setState(39);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			setState(40);
			mainDef();
			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 9010429070147584L) != 0)) {
				{
				setState(46);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(41);
					classDef();
					}
					break;
				case 2:
					{
					setState(42);
					functionDef();
					}
					break;
				case 3:
					{
					setState(43);
					variableDef();
					setState(44);
					match(T__0);
					}
					break;
				}
				}
				setState(50);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(51);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MainDefContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(MxParser.INT, 0); }
		public SuiteContext suite() {
			return getRuleContext(SuiteContext.class,0);
		}
		public MainDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mainDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterMainDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitMainDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitMainDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MainDefContext mainDef() throws RecognitionException {
		MainDefContext _localctx = new MainDefContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_mainDef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53);
			match(INT);
			setState(54);
			match(T__1);
			setState(55);
			match(T__2);
			setState(56);
			match(T__3);
			setState(57);
			suite();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassDefContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(MxParser.CLASS, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public List<VariableDefContext> variableDef() {
			return getRuleContexts(VariableDefContext.class);
		}
		public VariableDefContext variableDef(int i) {
			return getRuleContext(VariableDefContext.class,i);
		}
		public List<FunctionDefContext> functionDef() {
			return getRuleContexts(FunctionDefContext.class);
		}
		public FunctionDefContext functionDef(int i) {
			return getRuleContext(FunctionDefContext.class,i);
		}
		public ClassDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterClassDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitClassDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDefContext classDef() throws RecognitionException {
		ClassDefContext _localctx = new ClassDefContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_classDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(CLASS);
			setState(60);
			match(Identifier);
			setState(61);
			match(T__4);
			setState(68);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 9008230046892032L) != 0)) {
				{
				setState(66);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(62);
					variableDef();
					setState(63);
					match(T__0);
					}
					break;
				case 2:
					{
					setState(65);
					functionDef();
					}
					break;
				}
				}
				setState(70);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(71);
			match(T__5);
			setState(72);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDefContext extends ParserRuleContext {
		public List<TypeNameContext> typeName() {
			return getRuleContexts(TypeNameContext.class);
		}
		public TypeNameContext typeName(int i) {
			return getRuleContext(TypeNameContext.class,i);
		}
		public List<TerminalNode> Identifier() { return getTokens(MxParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(MxParser.Identifier, i);
		}
		public SuiteContext suite() {
			return getRuleContext(SuiteContext.class,0);
		}
		public FunctionDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterFunctionDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitFunctionDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDefContext functionDef() throws RecognitionException {
		FunctionDefContext _localctx = new FunctionDefContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_functionDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			typeName(0);
			setState(75);
			match(Identifier);
			setState(76);
			match(T__2);
			setState(88);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 9008230046892032L) != 0)) {
				{
				setState(77);
				typeName(0);
				setState(78);
				match(Identifier);
				setState(85);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__6) {
					{
					{
					setState(79);
					match(T__6);
					setState(80);
					typeName(0);
					setState(81);
					match(Identifier);
					}
					}
					setState(87);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(90);
			match(T__3);
			setState(91);
			suite();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SuiteContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public SuiteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_suite; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterSuite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitSuite(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSuite(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SuiteContext suite() throws RecognitionException {
		SuiteContext _localctx = new SuiteContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_suite);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			match(T__4);
			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 234044175392305194L) != 0)) {
				{
				{
				setState(94);
				statement();
				}
				}
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(100);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public SuiteContext suite() {
			return getRuleContext(SuiteContext.class,0);
		}
		public SelectStatementContext selectStatement() {
			return getRuleContext(SelectStatementContext.class,0);
		}
		public LoopStatementContext loopStatement() {
			return getRuleContext(LoopStatementContext.class,0);
		}
		public JumpStatementContext jumpStatement() {
			return getRuleContext(JumpStatementContext.class,0);
		}
		public VariableDefContext variableDef() {
			return getRuleContext(VariableDefContext.class,0);
		}
		public ParallelExpContext parallelExp() {
			return getRuleContext(ParallelExpContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_statement);
		try {
			setState(113);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(102);
				suite();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(103);
				selectStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(104);
				loopStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(105);
				jumpStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(106);
				variableDef();
				setState(107);
				match(T__0);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(109);
				parallelExp();
				setState(110);
				match(T__0);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(112);
				match(T__0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectStatementContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(MxParser.IF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(MxParser.ELSE, 0); }
		public SelectStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterSelectStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitSelectStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSelectStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectStatementContext selectStatement() throws RecognitionException {
		SelectStatementContext _localctx = new SelectStatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_selectStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			match(IF);
			setState(116);
			match(T__2);
			setState(117);
			expression(0);
			setState(118);
			match(T__3);
			setState(119);
			statement();
			setState(122);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(120);
				match(ELSE);
				setState(121);
				statement();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LoopStatementContext extends ParserRuleContext {
		public LoopStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopStatement; }
	 
		public LoopStatementContext() { }
		public void copyFrom(LoopStatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForLoopContext extends LoopStatementContext {
		public ExpressionContext condition;
		public ExpressionContext step;
		public TerminalNode FOR() { return getToken(MxParser.FOR, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ParallelExpContext parallelExp() {
			return getRuleContext(ParallelExpContext.class,0);
		}
		public VariableDefContext variableDef() {
			return getRuleContext(VariableDefContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ForLoopContext(LoopStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterForLoop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitForLoop(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitForLoop(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileLoopContext extends LoopStatementContext {
		public TerminalNode WHILE() { return getToken(MxParser.WHILE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public WhileLoopContext(LoopStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterWhileLoop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitWhileLoop(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitWhileLoop(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopStatementContext loopStatement() throws RecognitionException {
		LoopStatementContext _localctx = new LoopStatementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_loopStatement);
		int _la;
		try {
			setState(146);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case WHILE:
				_localctx = new WhileLoopContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(124);
				match(WHILE);
				setState(125);
				match(T__2);
				setState(126);
				expression(0);
				setState(127);
				match(T__3);
				setState(128);
				statement();
				}
				break;
			case FOR:
				_localctx = new ForLoopContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(130);
				match(FOR);
				setState(131);
				match(T__2);
				setState(134);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(132);
					parallelExp();
					}
					break;
				case 2:
					{
					setState(133);
					variableDef();
					}
					break;
				}
				setState(136);
				match(T__0);
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 225247051577946120L) != 0)) {
					{
					setState(137);
					((ForLoopContext)_localctx).condition = expression(0);
					}
				}

				setState(140);
				match(T__0);
				setState(142);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 225247051577946120L) != 0)) {
					{
					setState(141);
					((ForLoopContext)_localctx).step = expression(0);
					}
				}

				setState(144);
				match(T__3);
				setState(145);
				statement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JumpStatementContext extends ParserRuleContext {
		public JumpStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jumpStatement; }
	 
		public JumpStatementContext() { }
		public void copyFrom(JumpStatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStmtContext extends JumpStatementContext {
		public TerminalNode CONTINUE() { return getToken(MxParser.CONTINUE, 0); }
		public ContinueStmtContext(JumpStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterContinueStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitContinueStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitContinueStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BreakStmtContext extends JumpStatementContext {
		public TerminalNode BREAK() { return getToken(MxParser.BREAK, 0); }
		public BreakStmtContext(JumpStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterBreakStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitBreakStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBreakStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStmtContext extends JumpStatementContext {
		public TerminalNode RETURN() { return getToken(MxParser.RETURN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnStmtContext(JumpStatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterReturnStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitReturnStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitReturnStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JumpStatementContext jumpStatement() throws RecognitionException {
		JumpStatementContext _localctx = new JumpStatementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_jumpStatement);
		int _la;
		try {
			setState(157);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RETURN:
				_localctx = new ReturnStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(148);
				match(RETURN);
				setState(150);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 225247051577946120L) != 0)) {
					{
					setState(149);
					expression(0);
					}
				}

				setState(152);
				match(T__0);
				}
				break;
			case BREAK:
				_localctx = new BreakStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(153);
				match(BREAK);
				setState(154);
				match(T__0);
				}
				break;
			case CONTINUE:
				_localctx = new ContinueStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(155);
				match(CONTINUE);
				setState(156);
				match(T__0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDefContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public List<InitVariableContext> initVariable() {
			return getRuleContexts(InitVariableContext.class);
		}
		public InitVariableContext initVariable(int i) {
			return getRuleContext(InitVariableContext.class,i);
		}
		public VariableDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterVariableDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitVariableDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDefContext variableDef() throws RecognitionException {
		VariableDefContext _localctx = new VariableDefContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_variableDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			typeName(0);
			setState(160);
			initVariable();
			setState(165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(161);
				match(T__6);
				setState(162);
				initVariable();
				}
				}
				setState(167);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InitVariableContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public InitVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initVariable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterInitVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitInitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitInitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitVariableContext initVariable() throws RecognitionException {
		InitVariableContext _localctx = new InitVariableContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_initVariable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			match(Identifier);
			setState(171);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(169);
				match(T__7);
				setState(170);
				expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeNameContext extends ParserRuleContext {
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
	 
		public TypeNameContext() { }
		public void copyFrom(TypeNameContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayTypeContext extends TypeNameContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public List<BracketsContext> brackets() {
			return getRuleContexts(BracketsContext.class);
		}
		public BracketsContext brackets(int i) {
			return getRuleContext(BracketsContext.class,i);
		}
		public ArrayTypeContext(TypeNameContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterArrayType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitArrayType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArrayType(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FundationTypeContext extends TypeNameContext {
		public TerminalNode BOOL() { return getToken(MxParser.BOOL, 0); }
		public TerminalNode INT() { return getToken(MxParser.INT, 0); }
		public TerminalNode STRING() { return getToken(MxParser.STRING, 0); }
		public TerminalNode VOID() { return getToken(MxParser.VOID, 0); }
		public FundationTypeContext(TypeNameContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterFundationType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitFundationType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFundationType(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ClassTypeContext extends TypeNameContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ClassTypeContext(TypeNameContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterClassType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitClassType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		return typeName(0);
	}

	private TypeNameContext typeName(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeNameContext _localctx = new TypeNameContext(_ctx, _parentState);
		TypeNameContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_typeName, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VOID:
			case BOOL:
			case INT:
			case STRING:
				{
				_localctx = new FundationTypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(174);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1030792151040L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case Identifier:
				{
				_localctx = new ClassTypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(175);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(186);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ArrayTypeContext(new TypeNameContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_typeName);
					setState(178);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(180); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(179);
							brackets();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(182); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					} 
				}
				setState(188);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallLhsExpContext extends ExpressionContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ParallelExpContext parallelExp() {
			return getRuleContext(ParallelExpContext.class,0);
		}
		public FunctionCallLhsExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterFunctionCallLhsExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitFunctionCallLhsExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionCallLhsExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BoolExpContext extends ExpressionContext {
		public TerminalNode TRUE() { return getToken(MxParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MxParser.FALSE, 0); }
		public BoolExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterBoolExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitBoolExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBoolExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ClassMemberLhsExpContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ClassMemberLhsExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterClassMemberLhsExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitClassMemberLhsExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassMemberLhsExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NewArrayExpContext extends ExpressionContext {
		public TerminalNode NEW() { return getToken(MxParser.NEW, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<BracketsContext> brackets() {
			return getRuleContexts(BracketsContext.class);
		}
		public BracketsContext brackets(int i) {
			return getRuleContext(BracketsContext.class,i);
		}
		public NewArrayExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterNewArrayExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitNewArrayExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNewArrayExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringExpContext extends ExpressionContext {
		public TerminalNode String() { return getToken(MxParser.String, 0); }
		public StringExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterStringExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitStringExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitStringExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ThisPointerExpContext extends ExpressionContext {
		public TerminalNode THIS() { return getToken(MxParser.THIS, 0); }
		public ThisPointerExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterThisPointerExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitThisPointerExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitThisPointerExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrefixLhsExpContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PrefixLhsExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterPrefixLhsExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitPrefixLhsExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPrefixLhsExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullExpContext extends ExpressionContext {
		public TerminalNode NULL() { return getToken(MxParser.NULL, 0); }
		public NullExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterNullExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitNullExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNullExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VariableLhsExpContext extends ExpressionContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public VariableLhsExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterVariableLhsExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitVariableLhsExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableLhsExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ClassMemFunctionLhsExpContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ParallelExpContext parallelExp() {
			return getRuleContext(ParallelExpContext.class,0);
		}
		public ClassMemFunctionLhsExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterClassMemFunctionLhsExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitClassMemFunctionLhsExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassMemFunctionLhsExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BinaryExpContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BinaryExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterBinaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitBinaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBinaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryExpContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PrimaryExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterPrimaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitPrimaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPrimaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PostfixExpContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PostfixExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterPostfixExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitPostfixExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPostfixExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssignExpContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AssignExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterAssignExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitAssignExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitAssignExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NewClassExpContext extends ExpressionContext {
		public TerminalNode NEW() { return getToken(MxParser.NEW, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public NewClassExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterNewClassExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitNewClassExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNewClassExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TernaryExpContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TernaryExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterTernaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitTernaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitTernaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayElementLhsExpContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayElementLhsExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterArrayElementLhsExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitArrayElementLhsExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArrayElementLhsExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryExpContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterUnaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitUnaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitUnaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumberExpContext extends ExpressionContext {
		public TerminalNode DecNumber() { return getToken(MxParser.DecNumber, 0); }
		public NumberExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterNumberExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitNumberExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNumberExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				_localctx = new PrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(190);
				match(T__2);
				setState(191);
				expression(0);
				setState(192);
				match(T__3);
				}
				break;
			case 2:
				{
				_localctx = new FunctionCallLhsExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(194);
				match(Identifier);
				setState(195);
				match(T__2);
				setState(197);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 225247051577946120L) != 0)) {
					{
					setState(196);
					parallelExp();
					}
				}

				setState(199);
				match(T__3);
				}
				break;
			case 3:
				{
				_localctx = new PrefixLhsExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(200);
				((PrefixLhsExpContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__11 || _la==T__12) ) {
					((PrefixLhsExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(201);
				expression(22);
				}
				break;
			case 4:
				{
				_localctx = new UnaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(202);
				((UnaryExpContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 114688L) != 0)) ) {
					((UnaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(203);
				expression(21);
				}
				break;
			case 5:
				{
				_localctx = new NewClassExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(204);
				match(NEW);
				setState(205);
				typeName(0);
				setState(208);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
				case 1:
					{
					setState(206);
					match(T__2);
					setState(207);
					match(T__3);
					}
					break;
				}
				}
				break;
			case 6:
				{
				_localctx = new NewArrayExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(210);
				match(NEW);
				setState(211);
				typeName(0);
				setState(216); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(212);
						match(T__9);
						setState(213);
						expression(0);
						setState(214);
						match(T__10);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(218); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(223);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(220);
						brackets();
						}
						} 
					}
					setState(225);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				}
				}
				break;
			case 7:
				{
				_localctx = new VariableLhsExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(226);
				match(Identifier);
				}
				break;
			case 8:
				{
				_localctx = new ThisPointerExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(227);
				match(THIS);
				}
				break;
			case 9:
				{
				_localctx = new NumberExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(228);
				match(DecNumber);
				}
				break;
			case 10:
				{
				_localctx = new StringExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(229);
				match(String);
				}
				break;
			case 11:
				{
				_localctx = new BoolExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(230);
				_la = _input.LA(1);
				if ( !(_la==TRUE || _la==FALSE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 12:
				{
				_localctx = new NullExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(231);
				match(NULL);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(293);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(291);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(234);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(235);
						((BinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 917504L) != 0)) ) {
							((BinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(236);
						expression(21);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(237);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(238);
						((BinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__13 || _la==T__19) ) {
							((BinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(239);
						expression(20);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(240);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(241);
						((BinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__20 || _la==T__21) ) {
							((BinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(242);
						expression(19);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(243);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(244);
						((BinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 125829120L) != 0)) ) {
							((BinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(245);
						expression(18);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(246);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(247);
						((BinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__26 || _la==T__27) ) {
							((BinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(248);
						expression(17);
						}
						break;
					case 6:
						{
						_localctx = new BinaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(249);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(250);
						((BinaryExpContext)_localctx).op = match(T__28);
						setState(251);
						expression(16);
						}
						break;
					case 7:
						{
						_localctx = new BinaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(252);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(253);
						((BinaryExpContext)_localctx).op = match(T__29);
						setState(254);
						expression(15);
						}
						break;
					case 8:
						{
						_localctx = new BinaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(255);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(256);
						((BinaryExpContext)_localctx).op = match(T__30);
						setState(257);
						expression(14);
						}
						break;
					case 9:
						{
						_localctx = new BinaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(258);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(259);
						((BinaryExpContext)_localctx).op = match(T__31);
						setState(260);
						expression(13);
						}
						break;
					case 10:
						{
						_localctx = new BinaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(261);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(262);
						((BinaryExpContext)_localctx).op = match(T__32);
						setState(263);
						expression(12);
						}
						break;
					case 11:
						{
						_localctx = new TernaryExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(264);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(265);
						match(T__33);
						setState(266);
						expression(0);
						setState(267);
						match(T__34);
						setState(268);
						expression(11);
						}
						break;
					case 12:
						{
						_localctx = new AssignExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(270);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(271);
						match(T__7);
						setState(272);
						expression(9);
						}
						break;
					case 13:
						{
						_localctx = new ClassMemberLhsExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(273);
						if (!(precpred(_ctx, 27))) throw new FailedPredicateException(this, "precpred(_ctx, 27)");
						setState(274);
						match(T__8);
						setState(275);
						match(Identifier);
						}
						break;
					case 14:
						{
						_localctx = new ClassMemFunctionLhsExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(276);
						if (!(precpred(_ctx, 26))) throw new FailedPredicateException(this, "precpred(_ctx, 26)");
						setState(277);
						match(T__8);
						setState(278);
						match(Identifier);
						setState(279);
						match(T__2);
						setState(281);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 225247051577946120L) != 0)) {
							{
							setState(280);
							parallelExp();
							}
						}

						setState(283);
						match(T__3);
						}
						break;
					case 15:
						{
						_localctx = new ArrayElementLhsExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(284);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(285);
						match(T__9);
						setState(286);
						expression(0);
						setState(287);
						match(T__10);
						}
						break;
					case 16:
						{
						_localctx = new PostfixExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(289);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(290);
						((PostfixExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__11 || _la==T__12) ) {
							((PostfixExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(295);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParallelExpContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ParallelExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parallelExp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterParallelExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitParallelExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParallelExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParallelExpContext parallelExp() throws RecognitionException {
		ParallelExpContext _localctx = new ParallelExpContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_parallelExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			expression(0);
			setState(301);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(297);
				match(T__6);
				setState(298);
				expression(0);
				}
				}
				setState(303);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BracketsContext extends ParserRuleContext {
		public BracketsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_brackets; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterBrackets(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitBrackets(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBrackets(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BracketsContext brackets() throws RecognitionException {
		BracketsContext _localctx = new BracketsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_brackets);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(304);
			match(T__9);
			setState(305);
			match(T__10);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 11:
			return typeName_sempred((TypeNameContext)_localctx, predIndex);
		case 12:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean typeName_sempred(TypeNameContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 20);
		case 2:
			return precpred(_ctx, 19);
		case 3:
			return precpred(_ctx, 18);
		case 4:
			return precpred(_ctx, 17);
		case 5:
			return precpred(_ctx, 16);
		case 6:
			return precpred(_ctx, 15);
		case 7:
			return precpred(_ctx, 14);
		case 8:
			return precpred(_ctx, 13);
		case 9:
			return precpred(_ctx, 12);
		case 10:
			return precpred(_ctx, 11);
		case 11:
			return precpred(_ctx, 10);
		case 12:
			return precpred(_ctx, 9);
		case 13:
			return precpred(_ctx, 27);
		case 14:
			return precpred(_ctx, 26);
		case 15:
			return precpred(_ctx, 25);
		case 16:
			return precpred(_ctx, 23);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u00019\u0134\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000$\b\u0000\n\u0000\f\u0000"+
		"\'\t\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0005\u0000/\b\u0000\n\u0000\f\u00002\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0005\u0002C\b\u0002\n\u0002\f\u0002F\t\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0005\u0003T\b\u0003\n\u0003\f\u0003W\t\u0003\u0003\u0003Y\b\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0005\u0004`\b"+
		"\u0004\n\u0004\f\u0004c\t\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005r\b\u0005\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0003\u0006{\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0003\u0007\u0087\b\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u008b"+
		"\b\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u008f\b\u0007\u0001\u0007"+
		"\u0001\u0007\u0003\u0007\u0093\b\u0007\u0001\b\u0001\b\u0003\b\u0097\b"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u009e\b\b\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0005\t\u00a4\b\t\n\t\f\t\u00a7\t\t\u0001\n\u0001\n"+
		"\u0001\n\u0003\n\u00ac\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b"+
		"\u00b1\b\u000b\u0001\u000b\u0001\u000b\u0004\u000b\u00b5\b\u000b\u000b"+
		"\u000b\f\u000b\u00b6\u0005\u000b\u00b9\b\u000b\n\u000b\f\u000b\u00bc\t"+
		"\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0003\f\u00c6\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0003\f\u00d1\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0004\f\u00d9\b\f\u000b\f\f\f\u00da\u0001\f\u0005\f\u00de\b"+
		"\f\n\f\f\f\u00e1\t\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003"+
		"\f\u00e9\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0003\f\u011a\b\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u0124\b\f\n\f\f\f\u0127\t\f"+
		"\u0001\r\u0001\r\u0001\r\u0005\r\u012c\b\r\n\r\f\r\u012f\t\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0000\u0002\u0016\u0018\u000f\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u0000\t\u0001\u0000$\'\u0001\u0000\f\r\u0001\u0000\u000e\u0010\u0001"+
		"\u0000+,\u0001\u0000\u0011\u0013\u0002\u0000\u000e\u000e\u0014\u0014\u0001"+
		"\u0000\u0015\u0016\u0001\u0000\u0017\u001a\u0001\u0000\u001b\u001c\u0164"+
		"\u0000%\u0001\u0000\u0000\u0000\u00025\u0001\u0000\u0000\u0000\u0004;"+
		"\u0001\u0000\u0000\u0000\u0006J\u0001\u0000\u0000\u0000\b]\u0001\u0000"+
		"\u0000\u0000\nq\u0001\u0000\u0000\u0000\fs\u0001\u0000\u0000\u0000\u000e"+
		"\u0092\u0001\u0000\u0000\u0000\u0010\u009d\u0001\u0000\u0000\u0000\u0012"+
		"\u009f\u0001\u0000\u0000\u0000\u0014\u00a8\u0001\u0000\u0000\u0000\u0016"+
		"\u00b0\u0001\u0000\u0000\u0000\u0018\u00e8\u0001\u0000\u0000\u0000\u001a"+
		"\u0128\u0001\u0000\u0000\u0000\u001c\u0130\u0001\u0000\u0000\u0000\u001e"+
		"$\u0003\u0004\u0002\u0000\u001f$\u0003\u0006\u0003\u0000 !\u0003\u0012"+
		"\t\u0000!\"\u0005\u0001\u0000\u0000\"$\u0001\u0000\u0000\u0000#\u001e"+
		"\u0001\u0000\u0000\u0000#\u001f\u0001\u0000\u0000\u0000# \u0001\u0000"+
		"\u0000\u0000$\'\u0001\u0000\u0000\u0000%#\u0001\u0000\u0000\u0000%&\u0001"+
		"\u0000\u0000\u0000&(\u0001\u0000\u0000\u0000\'%\u0001\u0000\u0000\u0000"+
		"(0\u0003\u0002\u0001\u0000)/\u0003\u0004\u0002\u0000*/\u0003\u0006\u0003"+
		"\u0000+,\u0003\u0012\t\u0000,-\u0005\u0001\u0000\u0000-/\u0001\u0000\u0000"+
		"\u0000.)\u0001\u0000\u0000\u0000.*\u0001\u0000\u0000\u0000.+\u0001\u0000"+
		"\u0000\u0000/2\u0001\u0000\u0000\u00000.\u0001\u0000\u0000\u000001\u0001"+
		"\u0000\u0000\u000013\u0001\u0000\u0000\u000020\u0001\u0000\u0000\u0000"+
		"34\u0005\u0000\u0000\u00014\u0001\u0001\u0000\u0000\u000056\u0005&\u0000"+
		"\u000067\u0005\u0002\u0000\u000078\u0005\u0003\u0000\u000089\u0005\u0004"+
		"\u0000\u00009:\u0003\b\u0004\u0000:\u0003\u0001\u0000\u0000\u0000;<\u0005"+
		")\u0000\u0000<=\u00055\u0000\u0000=D\u0005\u0005\u0000\u0000>?\u0003\u0012"+
		"\t\u0000?@\u0005\u0001\u0000\u0000@C\u0001\u0000\u0000\u0000AC\u0003\u0006"+
		"\u0003\u0000B>\u0001\u0000\u0000\u0000BA\u0001\u0000\u0000\u0000CF\u0001"+
		"\u0000\u0000\u0000DB\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000"+
		"EG\u0001\u0000\u0000\u0000FD\u0001\u0000\u0000\u0000GH\u0005\u0006\u0000"+
		"\u0000HI\u0005\u0001\u0000\u0000I\u0005\u0001\u0000\u0000\u0000JK\u0003"+
		"\u0016\u000b\u0000KL\u00055\u0000\u0000LX\u0005\u0003\u0000\u0000MN\u0003"+
		"\u0016\u000b\u0000NU\u00055\u0000\u0000OP\u0005\u0007\u0000\u0000PQ\u0003"+
		"\u0016\u000b\u0000QR\u00055\u0000\u0000RT\u0001\u0000\u0000\u0000SO\u0001"+
		"\u0000\u0000\u0000TW\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000"+
		"UV\u0001\u0000\u0000\u0000VY\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000"+
		"\u0000XM\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000YZ\u0001\u0000"+
		"\u0000\u0000Z[\u0005\u0004\u0000\u0000[\\\u0003\b\u0004\u0000\\\u0007"+
		"\u0001\u0000\u0000\u0000]a\u0005\u0005\u0000\u0000^`\u0003\n\u0005\u0000"+
		"_^\u0001\u0000\u0000\u0000`c\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000"+
		"\u0000ab\u0001\u0000\u0000\u0000bd\u0001\u0000\u0000\u0000ca\u0001\u0000"+
		"\u0000\u0000de\u0005\u0006\u0000\u0000e\t\u0001\u0000\u0000\u0000fr\u0003"+
		"\b\u0004\u0000gr\u0003\f\u0006\u0000hr\u0003\u000e\u0007\u0000ir\u0003"+
		"\u0010\b\u0000jk\u0003\u0012\t\u0000kl\u0005\u0001\u0000\u0000lr\u0001"+
		"\u0000\u0000\u0000mn\u0003\u001a\r\u0000no\u0005\u0001\u0000\u0000or\u0001"+
		"\u0000\u0000\u0000pr\u0005\u0001\u0000\u0000qf\u0001\u0000\u0000\u0000"+
		"qg\u0001\u0000\u0000\u0000qh\u0001\u0000\u0000\u0000qi\u0001\u0000\u0000"+
		"\u0000qj\u0001\u0000\u0000\u0000qm\u0001\u0000\u0000\u0000qp\u0001\u0000"+
		"\u0000\u0000r\u000b\u0001\u0000\u0000\u0000st\u0005.\u0000\u0000tu\u0005"+
		"\u0003\u0000\u0000uv\u0003\u0018\f\u0000vw\u0005\u0004\u0000\u0000wz\u0003"+
		"\n\u0005\u0000xy\u0005/\u0000\u0000y{\u0003\n\u0005\u0000zx\u0001\u0000"+
		"\u0000\u0000z{\u0001\u0000\u0000\u0000{\r\u0001\u0000\u0000\u0000|}\u0005"+
		"1\u0000\u0000}~\u0005\u0003\u0000\u0000~\u007f\u0003\u0018\f\u0000\u007f"+
		"\u0080\u0005\u0004\u0000\u0000\u0080\u0081\u0003\n\u0005\u0000\u0081\u0093"+
		"\u0001\u0000\u0000\u0000\u0082\u0083\u00050\u0000\u0000\u0083\u0086\u0005"+
		"\u0003\u0000\u0000\u0084\u0087\u0003\u001a\r\u0000\u0085\u0087\u0003\u0012"+
		"\t\u0000\u0086\u0084\u0001\u0000\u0000\u0000\u0086\u0085\u0001\u0000\u0000"+
		"\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000"+
		"\u0000\u0088\u008a\u0005\u0001\u0000\u0000\u0089\u008b\u0003\u0018\f\u0000"+
		"\u008a\u0089\u0001\u0000\u0000\u0000\u008a\u008b\u0001\u0000\u0000\u0000"+
		"\u008b\u008c\u0001\u0000\u0000\u0000\u008c\u008e\u0005\u0001\u0000\u0000"+
		"\u008d\u008f\u0003\u0018\f\u0000\u008e\u008d\u0001\u0000\u0000\u0000\u008e"+
		"\u008f\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000\u0000\u0000\u0090"+
		"\u0091\u0005\u0004\u0000\u0000\u0091\u0093\u0003\n\u0005\u0000\u0092|"+
		"\u0001\u0000\u0000\u0000\u0092\u0082\u0001\u0000\u0000\u0000\u0093\u000f"+
		"\u0001\u0000\u0000\u0000\u0094\u0096\u00054\u0000\u0000\u0095\u0097\u0003"+
		"\u0018\f\u0000\u0096\u0095\u0001\u0000\u0000\u0000\u0096\u0097\u0001\u0000"+
		"\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u009e\u0005\u0001"+
		"\u0000\u0000\u0099\u009a\u00052\u0000\u0000\u009a\u009e\u0005\u0001\u0000"+
		"\u0000\u009b\u009c\u00053\u0000\u0000\u009c\u009e\u0005\u0001\u0000\u0000"+
		"\u009d\u0094\u0001\u0000\u0000\u0000\u009d\u0099\u0001\u0000\u0000\u0000"+
		"\u009d\u009b\u0001\u0000\u0000\u0000\u009e\u0011\u0001\u0000\u0000\u0000"+
		"\u009f\u00a0\u0003\u0016\u000b\u0000\u00a0\u00a5\u0003\u0014\n\u0000\u00a1"+
		"\u00a2\u0005\u0007\u0000\u0000\u00a2\u00a4\u0003\u0014\n\u0000\u00a3\u00a1"+
		"\u0001\u0000\u0000\u0000\u00a4\u00a7\u0001\u0000\u0000\u0000\u00a5\u00a3"+
		"\u0001\u0000\u0000\u0000\u00a5\u00a6\u0001\u0000\u0000\u0000\u00a6\u0013"+
		"\u0001\u0000\u0000\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a8\u00ab"+
		"\u00055\u0000\u0000\u00a9\u00aa\u0005\b\u0000\u0000\u00aa\u00ac\u0003"+
		"\u0018\f\u0000\u00ab\u00a9\u0001\u0000\u0000\u0000\u00ab\u00ac\u0001\u0000"+
		"\u0000\u0000\u00ac\u0015\u0001\u0000\u0000\u0000\u00ad\u00ae\u0006\u000b"+
		"\uffff\uffff\u0000\u00ae\u00b1\u0007\u0000\u0000\u0000\u00af\u00b1\u0005"+
		"5\u0000\u0000\u00b0\u00ad\u0001\u0000\u0000\u0000\u00b0\u00af\u0001\u0000"+
		"\u0000\u0000\u00b1\u00ba\u0001\u0000\u0000\u0000\u00b2\u00b4\n\u0001\u0000"+
		"\u0000\u00b3\u00b5\u0003\u001c\u000e\u0000\u00b4\u00b3\u0001\u0000\u0000"+
		"\u0000\u00b5\u00b6\u0001\u0000\u0000\u0000\u00b6\u00b4\u0001\u0000\u0000"+
		"\u0000\u00b6\u00b7\u0001\u0000\u0000\u0000\u00b7\u00b9\u0001\u0000\u0000"+
		"\u0000\u00b8\u00b2\u0001\u0000\u0000\u0000\u00b9\u00bc\u0001\u0000\u0000"+
		"\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001\u0000\u0000"+
		"\u0000\u00bb\u0017\u0001\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000"+
		"\u0000\u00bd\u00be\u0006\f\uffff\uffff\u0000\u00be\u00bf\u0005\u0003\u0000"+
		"\u0000\u00bf\u00c0\u0003\u0018\f\u0000\u00c0\u00c1\u0005\u0004\u0000\u0000"+
		"\u00c1\u00e9\u0001\u0000\u0000\u0000\u00c2\u00c3\u00055\u0000\u0000\u00c3"+
		"\u00c5\u0005\u0003\u0000\u0000\u00c4\u00c6\u0003\u001a\r\u0000\u00c5\u00c4"+
		"\u0001\u0000\u0000\u0000\u00c5\u00c6\u0001\u0000\u0000\u0000\u00c6\u00c7"+
		"\u0001\u0000\u0000\u0000\u00c7\u00e9\u0005\u0004\u0000\u0000\u00c8\u00c9"+
		"\u0007\u0001\u0000\u0000\u00c9\u00e9\u0003\u0018\f\u0016\u00ca\u00cb\u0007"+
		"\u0002\u0000\u0000\u00cb\u00e9\u0003\u0018\f\u0015\u00cc\u00cd\u0005("+
		"\u0000\u0000\u00cd\u00d0\u0003\u0016\u000b\u0000\u00ce\u00cf\u0005\u0003"+
		"\u0000\u0000\u00cf\u00d1\u0005\u0004\u0000\u0000\u00d0\u00ce\u0001\u0000"+
		"\u0000\u0000\u00d0\u00d1\u0001\u0000\u0000\u0000\u00d1\u00e9\u0001\u0000"+
		"\u0000\u0000\u00d2\u00d3\u0005(\u0000\u0000\u00d3\u00d8\u0003\u0016\u000b"+
		"\u0000\u00d4\u00d5\u0005\n\u0000\u0000\u00d5\u00d6\u0003\u0018\f\u0000"+
		"\u00d6\u00d7\u0005\u000b\u0000\u0000\u00d7\u00d9\u0001\u0000\u0000\u0000"+
		"\u00d8\u00d4\u0001\u0000\u0000\u0000\u00d9\u00da\u0001\u0000\u0000\u0000"+
		"\u00da\u00d8\u0001\u0000\u0000\u0000\u00da\u00db\u0001\u0000\u0000\u0000"+
		"\u00db\u00df\u0001\u0000\u0000\u0000\u00dc\u00de\u0003\u001c\u000e\u0000"+
		"\u00dd\u00dc\u0001\u0000\u0000\u0000\u00de\u00e1\u0001\u0000\u0000\u0000"+
		"\u00df\u00dd\u0001\u0000\u0000\u0000\u00df\u00e0\u0001\u0000\u0000\u0000"+
		"\u00e0\u00e9\u0001\u0000\u0000\u0000\u00e1\u00df\u0001\u0000\u0000\u0000"+
		"\u00e2\u00e9\u00055\u0000\u0000\u00e3\u00e9\u0005-\u0000\u0000\u00e4\u00e9"+
		"\u00058\u0000\u0000\u00e5\u00e9\u00059\u0000\u0000\u00e6\u00e9\u0007\u0003"+
		"\u0000\u0000\u00e7\u00e9\u0005*\u0000\u0000\u00e8\u00bd\u0001\u0000\u0000"+
		"\u0000\u00e8\u00c2\u0001\u0000\u0000\u0000\u00e8\u00c8\u0001\u0000\u0000"+
		"\u0000\u00e8\u00ca\u0001\u0000\u0000\u0000\u00e8\u00cc\u0001\u0000\u0000"+
		"\u0000\u00e8\u00d2\u0001\u0000\u0000\u0000\u00e8\u00e2\u0001\u0000\u0000"+
		"\u0000\u00e8\u00e3\u0001\u0000\u0000\u0000\u00e8\u00e4\u0001\u0000\u0000"+
		"\u0000\u00e8\u00e5\u0001\u0000\u0000\u0000\u00e8\u00e6\u0001\u0000\u0000"+
		"\u0000\u00e8\u00e7\u0001\u0000\u0000\u0000\u00e9\u0125\u0001\u0000\u0000"+
		"\u0000\u00ea\u00eb\n\u0014\u0000\u0000\u00eb\u00ec\u0007\u0004\u0000\u0000"+
		"\u00ec\u0124\u0003\u0018\f\u0015\u00ed\u00ee\n\u0013\u0000\u0000\u00ee"+
		"\u00ef\u0007\u0005\u0000\u0000\u00ef\u0124\u0003\u0018\f\u0014\u00f0\u00f1"+
		"\n\u0012\u0000\u0000\u00f1\u00f2\u0007\u0006\u0000\u0000\u00f2\u0124\u0003"+
		"\u0018\f\u0013\u00f3\u00f4\n\u0011\u0000\u0000\u00f4\u00f5\u0007\u0007"+
		"\u0000\u0000\u00f5\u0124\u0003\u0018\f\u0012\u00f6\u00f7\n\u0010\u0000"+
		"\u0000\u00f7\u00f8\u0007\b\u0000\u0000\u00f8\u0124\u0003\u0018\f\u0011"+
		"\u00f9\u00fa\n\u000f\u0000\u0000\u00fa\u00fb\u0005\u001d\u0000\u0000\u00fb"+
		"\u0124\u0003\u0018\f\u0010\u00fc\u00fd\n\u000e\u0000\u0000\u00fd\u00fe"+
		"\u0005\u001e\u0000\u0000\u00fe\u0124\u0003\u0018\f\u000f\u00ff\u0100\n"+
		"\r\u0000\u0000\u0100\u0101\u0005\u001f\u0000\u0000\u0101\u0124\u0003\u0018"+
		"\f\u000e\u0102\u0103\n\f\u0000\u0000\u0103\u0104\u0005 \u0000\u0000\u0104"+
		"\u0124\u0003\u0018\f\r\u0105\u0106\n\u000b\u0000\u0000\u0106\u0107\u0005"+
		"!\u0000\u0000\u0107\u0124\u0003\u0018\f\f\u0108\u0109\n\n\u0000\u0000"+
		"\u0109\u010a\u0005\"\u0000\u0000\u010a\u010b\u0003\u0018\f\u0000\u010b"+
		"\u010c\u0005#\u0000\u0000\u010c\u010d\u0003\u0018\f\u000b\u010d\u0124"+
		"\u0001\u0000\u0000\u0000\u010e\u010f\n\t\u0000\u0000\u010f\u0110\u0005"+
		"\b\u0000\u0000\u0110\u0124\u0003\u0018\f\t\u0111\u0112\n\u001b\u0000\u0000"+
		"\u0112\u0113\u0005\t\u0000\u0000\u0113\u0124\u00055\u0000\u0000\u0114"+
		"\u0115\n\u001a\u0000\u0000\u0115\u0116\u0005\t\u0000\u0000\u0116\u0117"+
		"\u00055\u0000\u0000\u0117\u0119\u0005\u0003\u0000\u0000\u0118\u011a\u0003"+
		"\u001a\r\u0000\u0119\u0118\u0001\u0000\u0000\u0000\u0119\u011a\u0001\u0000"+
		"\u0000\u0000\u011a\u011b\u0001\u0000\u0000\u0000\u011b\u0124\u0005\u0004"+
		"\u0000\u0000\u011c\u011d\n\u0019\u0000\u0000\u011d\u011e\u0005\n\u0000"+
		"\u0000\u011e\u011f\u0003\u0018\f\u0000\u011f\u0120\u0005\u000b\u0000\u0000"+
		"\u0120\u0124\u0001\u0000\u0000\u0000\u0121\u0122\n\u0017\u0000\u0000\u0122"+
		"\u0124\u0007\u0001\u0000\u0000\u0123\u00ea\u0001\u0000\u0000\u0000\u0123"+
		"\u00ed\u0001\u0000\u0000\u0000\u0123\u00f0\u0001\u0000\u0000\u0000\u0123"+
		"\u00f3\u0001\u0000\u0000\u0000\u0123\u00f6\u0001\u0000\u0000\u0000\u0123"+
		"\u00f9\u0001\u0000\u0000\u0000\u0123\u00fc\u0001\u0000\u0000\u0000\u0123"+
		"\u00ff\u0001\u0000\u0000\u0000\u0123\u0102\u0001\u0000\u0000\u0000\u0123"+
		"\u0105\u0001\u0000\u0000\u0000\u0123\u0108\u0001\u0000\u0000\u0000\u0123"+
		"\u010e\u0001\u0000\u0000\u0000\u0123\u0111\u0001\u0000\u0000\u0000\u0123"+
		"\u0114\u0001\u0000\u0000\u0000\u0123\u011c\u0001\u0000\u0000\u0000\u0123"+
		"\u0121\u0001\u0000\u0000\u0000\u0124\u0127\u0001\u0000\u0000\u0000\u0125"+
		"\u0123\u0001\u0000\u0000\u0000\u0125\u0126\u0001\u0000\u0000\u0000\u0126"+
		"\u0019\u0001\u0000\u0000\u0000\u0127\u0125\u0001\u0000\u0000\u0000\u0128"+
		"\u012d\u0003\u0018\f\u0000\u0129\u012a\u0005\u0007\u0000\u0000\u012a\u012c"+
		"\u0003\u0018\f\u0000\u012b\u0129\u0001\u0000\u0000\u0000\u012c\u012f\u0001"+
		"\u0000\u0000\u0000\u012d\u012b\u0001\u0000\u0000\u0000\u012d\u012e\u0001"+
		"\u0000\u0000\u0000\u012e\u001b\u0001\u0000\u0000\u0000\u012f\u012d\u0001"+
		"\u0000\u0000\u0000\u0130\u0131\u0005\n\u0000\u0000\u0131\u0132\u0005\u000b"+
		"\u0000\u0000\u0132\u001d\u0001\u0000\u0000\u0000\u001f#%.0BDUXaqz\u0086"+
		"\u008a\u008e\u0092\u0096\u009d\u00a5\u00ab\u00b0\u00b6\u00ba\u00c5\u00d0"+
		"\u00da\u00df\u00e8\u0119\u0123\u0125\u012d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}