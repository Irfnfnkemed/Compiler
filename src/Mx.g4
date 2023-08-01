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
    | ';'
    ;

selectStatement
    : IF '(' expression ')' statement ( ELSE statement )?
    ;

loopStatement
    : WHILE '(' expression ')' statement                            # WhileLoop
    | FOR '(' ( parallelExp | variableDef )? ';'
      condition=expression? ';' step=expression? ')' statement      # ForLoop
    ;

jumpStatement
    : RETURN expression? ';'     # ReturnStmt
    | BREAK ';'                  # BreakStmt
    | CONTINUE ';'               # ContinueStmt
    ;

variableDef
    : typeName initVariable ( ',' initVariable )*
    ;

initVariable
    : Identifier ( '=' expression )?
    ;

typeName
    : ( BOOL | INT | STRING | VOID )    # FundationType
    | Identifier                        # ClassType
    | typeName brackets+                # ArrayType
    ;

expression
    : '(' expression ')'                                        # PrimaryExp
    | expression '.' Identifier                                 # ClassMemberLhsExp
    | expression '.' Identifier '(' parallelExp? ')'            # ClassMemFunctionLhsExp
    | expression '[' expression ']'                             # ArrayElementLhsExp
    | Identifier '(' parallelExp? ')'                           # FunctionCallLhsExp
    | expression op=( '++' | '--' )                             # PostfixExp
    | op=( '++' | '--' ) expression                             # PrefixLhsExp
    | op=( '-' | '!' | '~' ) expression                         # UnaryExp
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
    | <assoc=right> expression '=' expression                   # AssignExp
    | NEW typeName ( '(' ')' )?                                 # NewClassExp
    | NEW typeName ( '[' expression ']' )+ ( brackets )*        # NewArrayExp
    | Identifier                                                # VariableLhsExp
    | THIS                                                      # ThisPointerExp
    | DecNumber                                                 # NumberExp
    | String                                                    # StringExp
    | ( TRUE | FALSE )                                          # BoolExp
    | NULL                                                      # NullExp
    ;

parallelExp
    : expression ( ',' expression )*
    ;

brackets
    : '['  ']'
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