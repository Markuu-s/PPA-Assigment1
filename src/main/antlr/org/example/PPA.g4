grammar PPA;

program
    : line ';\n' program
    | line ';' EOF
    ;

line
    : number ' ' command
    ;

number
    : INT
    ;

command
    : assign_ident ':=' expr
    ;

expr
    : func '(' exprs ')'
    | ident
    | INT
    ;

exprs
    : expr ',' exprs
    | expr
    ;

assign_ident
    : VAR
    ;

ident
    : VAR
    ;

func
    : SUM
    | RANDOM_CHOICE
    ;

SUM : 'sum' ;
RANDOM_CHOICE : 'random_choice' ;

VAR
    : [a-zA-Z]+
    ;

INT
    : [0-9]+
    ;
