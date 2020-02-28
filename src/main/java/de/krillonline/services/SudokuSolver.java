package de.krillonline.services;
import java.util.ArrayList;
import java.util.List;

import de.krillonline.entities.SudokuCell;
import de.krillonline.entities.SudokuField;

public class SudokuSolver {

	static int rekursionszaehler = 0;

	public static SudokuCell[][] loeseFeld(SudokuCell[][] arbeitsFeld) {

		FeldPosition betrachtetePos = sucheNaechstesFreiFeld(arbeitsFeld);

		rekursionszaehler++;

		if (betrachtetePos == null) {
			System.out.println("Im " + (rekursionszaehler-1) + ". Aufruf wurde das Feld geloest!\n");
			zeigeFeld(arbeitsFeld);
			return arbeitsFeld;
		}

		List<Integer> optionenFuerPos = ermittleMoeglicheZiffernFuerFeld(betrachtetePos, arbeitsFeld);
		if (optionenFuerPos != null ) {

			for (Integer option : optionenFuerPos) {
				arbeitsFeld[betrachtetePos.getRow()][betrachtetePos.getColumn()].setValue(option);
				SudokuCell[][] lsg = loeseFeld(arbeitsFeld);
				if (lsg != null) {
					return lsg;
				}
				arbeitsFeld[betrachtetePos.getRow()][betrachtetePos.getColumn()].setValue(0);					
			}
		}

		return null;

	}

	private static List<Integer> ermittleMoeglicheZiffernFuerFeld(FeldPosition betrachtetePos, SudokuCell[][] arbeitsFeld) {

		List <Integer> moeglicheWerte = new ArrayList<Integer>();

		for (int i=1; i < arbeitsFeld.length+1; i++) {
			moeglicheWerte.add(i);
		}

		moeglicheWerte = entferneZeilenKonfliktWerte(betrachtetePos, moeglicheWerte, arbeitsFeld);
		if (moeglicheWerte != null && !moeglicheWerte.isEmpty()) {

			moeglicheWerte = entferneSpaltenKonfliktWerte(betrachtetePos, moeglicheWerte, arbeitsFeld);			

			if (moeglicheWerte != null && !moeglicheWerte.isEmpty()) {
				moeglicheWerte = entferneQuadrantenKonfliktWerte(betrachtetePos, moeglicheWerte, arbeitsFeld);
			}
		}

		return moeglicheWerte != null && !moeglicheWerte.isEmpty() ? moeglicheWerte : null;

	}

	private static List<Integer> entferneQuadrantenKonfliktWerte(FeldPosition betrachtetePos,
			List<Integer> moeglicheWerte, SudokuCell[][] arbeitsFeld) {

		List<Integer> verbleibendeMoeglicheWerte = new ArrayList<Integer>(moeglicheWerte);

		int quadrantSize = (int) Math.sqrt(arbeitsFeld.length);
		int startRow = (betrachtetePos.getRow() / quadrantSize) * quadrantSize;
		int startColumn = (betrachtetePos.getColumn() / quadrantSize) * quadrantSize;

		for (int row = startRow; row < startRow + quadrantSize; row++) {
			for (int column = startColumn; column < startColumn + quadrantSize; column++) {
				verbleibendeMoeglicheWerte.remove((Integer) arbeitsFeld[row][column].getValue());
			}
		}
		return verbleibendeMoeglicheWerte;
	}

	private static List<Integer> entferneSpaltenKonfliktWerte(FeldPosition betrachtetePos,
			List<Integer> moeglicheWerte, SudokuCell[][] arbeitsFeld) {

		List<Integer> verbleibendeMoeglicheWerte = new ArrayList<Integer>(moeglicheWerte);
		for (int row = 0; row < arbeitsFeld.length; row++) {
			verbleibendeMoeglicheWerte.remove((Integer) arbeitsFeld[row][betrachtetePos.getColumn()].getValue());
		}

		return verbleibendeMoeglicheWerte;
	}

	private static List<Integer> entferneZeilenKonfliktWerte(FeldPosition betrachtetePos, List<Integer> moeglicheWerte,
			SudokuCell[][] arbeitsFeld) {

		List<Integer> verbleibendeMoeglicheWerte = new ArrayList<Integer>(moeglicheWerte);
		int maxColumn = arbeitsFeld[betrachtetePos.getRow()].length;
		for (int column = 0; column < maxColumn; column++) {
			verbleibendeMoeglicheWerte.remove((Integer) arbeitsFeld[betrachtetePos.getRow()][column].getValue());
		}

		return verbleibendeMoeglicheWerte;
	}

	private static void zeigeFeld(SudokuCell[][] arbeitsFeld) {

		drawLineOnConsole();
		
		int quadrantSize = (int) Math.sqrt(arbeitsFeld.length);

		for (int row=0; row < arbeitsFeld.length; row++) {
			for (int column=0; column < arbeitsFeld[row].length; column++) {
				System.out.print(arbeitsFeld[row][column].getValue() + " ");
				if ((column+1) %  quadrantSize == 0) {
					System.out.print("| ");
				}
			}
			if ((row % quadrantSize) == (quadrantSize - 1))
				drawLineOnConsole();
			else 
				System.out.println("\n");
		}

	}

	private static void drawLineOnConsole() {
		System.out.println();
		for (int i = 0; i < 2*(SudokuField.getSize() + (int) Math.sqrt(SudokuField.getSize()))-1; i++)
			System.out.print("-");
		System.out.println();
	}

	private static FeldPosition sucheNaechstesFreiFeld(SudokuCell[][] arbeitsFeld) {

		for (int row=0; row < arbeitsFeld.length; row++) {
			for (int column=0; column < arbeitsFeld[row].length; column++) {
				if (arbeitsFeld[row][column].getValue() == 0) {
					return new FeldPosition(row,column);
				}
			}
		}
		return null;
	}

	public static SudokuField kopiereFeld(SudokuField quelle) {

		SudokuField kopie = new SudokuField();

		for (int i=0; i < quelle.getCells().length; i++) {
			for (int j=0; j < quelle.getCells()[i].length; j++) {
				kopie.getCells()[i][j].setValue(quelle.getCells()[i][j].getValue());
				kopie.getCells()[i][j].setStartValue(quelle.getCells()[i][j].isStartValue());
			}
		}
		return kopie;

	}

}
