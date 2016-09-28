package lex;
import java_cup.runtime.*;

%%
%class Lexer

%cup
%line
%column
%ignorecase

L = [a-zA-Z_]
D = [0-9]
WHITE=[ \b\r\f\t\n]

%{
	public Symbol token(int simbolo){
		Lexema lexema = new Lexema( yytext() );
		compilador.setError("La cadena "+yytext()+" es invalida, se encontro en la linea "+(yyline+1)+", y en la columna "+(yycolumn+1));
		return new Symbol(simbolo,yyline,yycolumn,lexema);
	}
	public Symbol token(int simbolo,String componenteLexico){
		Cup.vars++;
		Lexema lexema = new Lexema( yytext() );
		return new Symbol(simbolo,yyline,yycolumn,lexema);
	}
%}
%%
<YYINITIAL>{ 
{WHITE} {/*Ignore*/}

("{"(.*)"}")
{}
("(*"."*)")
{}
("//".*)
{}

(":=")
{return token(sym.ASIG,"ASIG");}
("entero")
{return token(sym.ENTERO,"ENTERO");}
("escribirln(")
{return token(sym.WRITE,"WRITE");}



("verdadero")
{return token(sym.TRUE,"TRUE");}
("falso")
{return token(sym.FALSE,"FALSE");}


("real")
{return token(sym.REAL,"REAL");}
("leerln(")
{return token(sym.READ,"READ");}

("cadena")
{return token(sym.CAD,"CAD");}
("caracter")
{return token(sym.CARACTER,"CARACTER");}

("boleano")
{return token(sym.BOLEANO,"BOLEANO");}
("var")
{return token(sym.VAR,"VAR");}

(";")
{return token(sym.PUNTOYCOMA,"PUNTOYCOMA");}
(".")
{return token(sym.PUNTO,"PUNTO");}


(">=")
{return token(sym.MAYORIGUALQUE,"MAYORIGUALQUE");}











("y")
{return token(sym.AND,"AND");}


("no")
{return token(sym.NOT,"NOT");}

("o")
{return token(sym.OR,"OR");}
("<=")
{return token(sym.MENORIGUALQUE,"MENORIGUALQUE");}

("<")
{return token(sym.MENORQUE,"MENORQUE");}
(">")
{return token(sym.MAYORQUE,"MAYORQUE");}
("=")
{return token(sym.IGUAL,"IGUAL");}
("<>")
{return token(sym.DISTINTODE,"DISTINTODE");}
(",")
{return token(sym.COMA,"COMA");}
("and")
{return token(sym.Y,"Y");}
("or")
{return token(sym.O,"O");}
("no")
{return token(sym.NEGACION,"NEGACION");}
("si")
{return token(sym.IF,"IF");}
("repetir")
{return token(sym.REPEAT,"REPEAT");}
("hasta")
{return token(sym.UNTIL,"UNTIL");}

("inicio")
{return token(sym.BEGIN,"BEGIN");}
("fin")
{return token(sym.FIN,"FIN");}
("si_no")
{return token(sym.ELSE,"ELSE");}
("for")
{return token(sym.FOR,"FOR");}
("programa")
{return token(sym.PROGRAMA,"PROGRAMA");}
("hacer")
{return token(sym.DO,"DO");}
("mientras")
{return token(sym.WHILE,"WHILE");}
("procedimiento")
{return token(sym.PROCEDURE,"PROCEDURE");}
("function")
{return token(sym.FUNCTION,"FUNCTION");}
("(")
{return token(sym.PARENTESISIZQ,"PARENTESISIZQ");}
(")")
{return token(sym.PARENTESISDER,"PARENTESISDER");}
(":")
{return token(sym.DOSPUNTOS,"DOSPUNTOS");}
("+")
{return token(sym.SUMA,"SUMA");}

("mod")
{return token(sym.MODULO,"MODULO");}


("*")
{return token(sym.MULTIPLICACION,"MULTIPLICACION");}
("-")
{return token(sym.RESTA,"RESTA");}
("/")
{return token(sym.DIVISION,"DIVISION");}
(("'") [^"'"]* ("'"))        //"'
{return token(sym.CADENA,"CADENA");}


(-?{D}+)
{return token(sym.INTEGERNUM,"INTEGERNUM");}
(-?{D}+"."{D}+)
{return token(sym.REALNUM,"REALNUM");}
{L}({L}|{D})*
{return token(sym.ID,"ID");}
}
.
{return token(sym.ERROR);}











