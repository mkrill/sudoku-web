package de.krillonline.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SudokuCell {
	
	private int value;
	private boolean startValue = false;

	public SudokuCell(int value, boolean startValue) {
		super();
		this.value = value;
		this.startValue = startValue;
	}

}
