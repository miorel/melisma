/*
 * Copyright (C) 2009-2010 Miorel-Lucian Palii
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */

package util.net.spoj;

public enum Language {
	ADA("Ada"),
	ASSEMBLY("ASM", "assembly language"),
	BASH("BAS", "bash"),
	BRAINFUCK("BF", "brainfuck"),
	C("C"),
	C_SHARP("C#", "C#"),
	C_PLUS_PLUS("C++", "C++"),
	C99("C99"),
	OCAML("CAM", "OCaml"),
	CLIPS("CLPS", "Clips"),
	D("D"),
	ERLANG("ERL", "Erlang"),
	FORTRAN("FOR", "Fortran"),
	HASKELL("HAS", "Haskell"),
	INTERCAL("ICK", "Intercal"),
	ICON("ICO", "Icon"),
	JAR("jar format"),
	JAVA("JAV", "Java"),
	JAVASCRIPT("JS", "JavaScript"),
	LISP("LIS", "Common Lisp"),
	LUA("Lua"),
	NEMERLE("NEM", "Nemerle"),
	NICE("NIC", "Nice"),
	PASCAL("PAS", "Pascal"),
	PERL("PER", "Perl"),
	PHP("PHP"),
	PIKE("Pike"),
//	PROLOG("PRLG", "Prolog"), ?
	PYTHON("PYT", "Python"),
	RUBY("RUB", "Ruby"),
//	SCALA("SCAL", "Scala"), ?
	SCHEME("SCM", "Scheme"),
	SMALLTALK("ST", "Smalltalk"),
	TCL("Tcl"),
//	TECS("TECS"), ?
	PLAIN_TEXT("TEX", "plain text"),
	WHITESPACE("WSP", "Whitespace");
	
	private final String spojName;
	private final String name;	

	private Language(String spojName) {
		this(spojName, spojName);
	}
	
	private Language(String spojName, String name) {
		this.spojName = spojName;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getSpojName() {
		return spojName;
	}
	
	public String toString() {
		return getName();
	}
	
	public static Language forSpojName(String spojName) {
		Language ret = null;
		for(Language lang: Language.values())
			if(lang.spojName.equals(spojName)) {
				ret = lang;
				break;
			}
		return ret;
	}
}
