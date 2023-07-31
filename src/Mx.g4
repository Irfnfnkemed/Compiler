grammar Mx;

program
    : ( classDef | functionDef | variableDef ';' )* mainDef
      ( classDef | functionDef | variableDef ';' )* EOF
    ;

mainDef
    : INT 'main' '(' ')' suite
    ;

classDef
    : CLASS Identifier '{' ( variableDef ';' | functionDef )* '}' ';'
    ;

functionDef
    : typeName Identifier '(' ( typeName Identifier ( ',' typeName Identifier )* )? ')' suite
    ;

suite
    : '{' statement* '}'
    ;

statement
    : suite
    | selectStatement
    | loopStatement
    | jumpStatement
    | variableDef ';'
    | parallelExp ';'
    ;

selectStatement
    : IF '(' expression ')' ( suite | statement ) ( ELSE ( suite | statement ) )?
    ;

loopStatement
    : WHILE '(' expression ')' ( suite | statement )
    | FOR '(' ( parallelExp | variableDef )? ';' expression? ';' expression? ')' ( suite | statement )
    ;

jumpStatement
    : RETURN expression? ';'     # ReturnStmt
    | BREAK ';'                  # BreakStmt
    | CONTINUE ';'               # ContinueStmt
    ;

variableDef
    : typeName Identifier ( '=' expression )? ( ',' Identifier ( '=' expression )? )*
    ;

typeName
    : ( BOOL | INT | STRING | VOID )    # FundationType
    | Identifier                        # ClassType
    | typeName ( '[' ']' )+             # ArrayType
    ;

expression
    : expressionLhs                                             # LhsExp
    | '(' expression ')'                                        # PrimaryExp
    | op=( '-' | '!' | '~' ) expression                         # UnaryExp
    | expressionLhs ( '++' | '--' )                             # PostfixExp
    | expression op=( '*' | '/' | '%' ) expression              # BinaryExp
    | expression op=( '+' | '-' ) expression                    # BinaryExp
    | expression op=( '<<' | '>>' ) expression                  # BinaryExp
    | expression op=( '<' | '>' | '<=' | '>=' ) expression      # BinaryExp
    | expression op=( '==' | '!=' ) expression                  # BinaryExp
    | expression op='&' expression                              # BinaryExp
    | expression op='^' expression                              # BinaryExp
    | expression op='|' expression                              # BinaryExp
    | expression op='&&' expression                             # BinaryExp
    | expression op='||' expression                             # BinaryExp
    | expression '?' expression ':' expression                  # TernaryExp
    | <assoc=right> expressionLhs '=' expression                # AssignExp
    | NEW typeName ( '(' ')' )?                                 # NewClassExp
    | NEW typeName ( '[' expression ']' )+ ( '[' ']' )*         # NewArrayExp
    | THIS                                                      # ThisPointerExp
    | DecNumber                                                 # NumberExp
    | String                                                    # StringExp
    | ( TRUE | FALSE )                                          # BoolExp
    | NULL                                                      # NullExp
    ;

expressionLhs
    : '(' expressionLhs ')'                             # Primary
    | Identifier                                        # Variable
    | expressionLhs '.' Identifier                      # ClassMember
    | expressionLhs '.' Identifier functionCallList     # ClassMemFunction
    | expressionLhs '[' expression ']'                  # ArrayElement
    | Identifier functionCallList                       # FunctionCall
    | op=( '++' | '--' ) expressionLhs                  # Prefix
    ;

functionCallList
    : '(' ( expression ( ',' expression )* )? ')'
    ;

parallelExp
    : expression ( ',' expression )*
    ;

VOID : 'void' ;
BOOL : 'bool' ;
INT : 'int' ;
STRING : 'string' ;
NEW : 'new' ;
CLASS : 'class' ;
NULL : 'null' ;
TRUE : 'true' ;
FALSE : 'false' ;
THIS : 'this' ;
IF : 'if' ;
ELSE : 'else' ;
FOR : 'for' ;
WHILE : 'while' ;
BREAK : 'break' ;
CONTINUE : 'continue' ;
RETURN : 'return' ;

Identifier
    : [a-zA-Z][a-zA-Z0-9_]*
    ;

WhiteSpace
    : [ \r\t\n]+ -> skip
    ;

Comments
    :
    ( '//' .*? '\n'
    | '/*' .*? '*/'
    ) -> skip
    ;

DecNumber
    : [1-9][0-9]*
    | '0'
    ;

String
    : '"' StringCharacter* '"'
    ;

fragment StringCharacter
    : '\\n'
    | '\\\\'
    | '\\"'
    | ~[\u0000-\u001F\u007F"\\]
    ;