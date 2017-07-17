grammar Types;

@header {
package org.antlr4.webidl;
}

type
    : isArray=type '[' ']'
    | 'sequence' '<' isArray=type '>'
    | 'FrozenArray' '<' isArray=type '>'
    | baseType='Promise' promiseRest
    | baseType='record' promiseRest2
    | baseType='Dictionary' promiseRest2
    | '(' unionType=type unionTypeRest
    | simpleType=TYPENAME
;

promiseRest
    : ('<' type '>')?
;

promiseRest2
    : ('<' type ',' type '>')?
;

unionTypeRest
    : OR type unionTypeRest
    | ')'
;

TYPENAME
    : [A-Z_a-z][0-9A-Z_a-z]*
;

OR
    : [Oo][Rr]
;

LT
    : '<'
;
GT
    : '>'
;
COMMA
    : ','
;
LPAREN
    : '('
;
RPAREN
    : ')'
;

WHITESPACE
	: [\t\n\r ]+ -> channel(HIDDEN)
;